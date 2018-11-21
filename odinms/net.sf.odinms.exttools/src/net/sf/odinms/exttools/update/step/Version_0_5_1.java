/*
	This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
                       Matthias Butz <matze@odinms.de>
                       Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation. You may not use, modify
    or distribute this program under any other version of the
    GNU Affero General Public License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package net.sf.odinms.exttools.update.step;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import net.sf.odinms.database.DatabaseConnection;

/**
 *
 * @author Matze
 */
public class Version_0_5_1 implements UpdateStep {

	private boolean haveForfeited = false;
	private boolean haveRanks = false;
	private boolean needCharIdChange = false;
	private boolean haveFredrick = false;
	private boolean haveSalt = false;
	private boolean haveGuilds = false;
	private boolean haveTempBan = false;
	
	private boolean haveColumn(Statement s, String tableName, String name) throws SQLException {
		boolean ret = false;
		ResultSet rs = s.executeQuery("DESCRIBE " + tableName);
		while (rs.next()) {
			if (rs.getString(1).equals(name)) {
				ret = true;
			}
		}
		rs.close();
		return ret;
	}
	
	@Override
	public boolean isRequired() throws SQLException {
		Connection con = DatabaseConnection.getConnection();
		Statement s = con.createStatement();
		haveForfeited = haveColumn(s, "queststatus", "forfeited");
		haveRanks = haveColumn(s, "characters", "rank");
		haveSalt = haveColumn(s, "accounts", "salt");
		haveGuilds = haveColumn(s, "characters", "guildid");
		haveTempBan = haveColumn(s, "accounts", "tempban");
		ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM characters WHERE id < 21000");
		if (rs.next()) {
			needCharIdChange = rs.getInt(1) > 0;
		}
		rs = s.executeQuery("SELECT shopid FROM shops WHERE npcid = '9030000'");
		if (rs.next()) {
			haveFredrick = true;
		}
		rs.close();
		s.close();
		return !haveForfeited || !haveRanks || needCharIdChange || !haveFredrick || !haveSalt || !haveGuilds || !haveTempBan;
	}

	@Override
	public void apply() throws SQLException {
		Connection con = DatabaseConnection.getConnection();
		Statement s = con.createStatement();
		if (!haveForfeited) {
			s.executeUpdate("ALTER TABLE queststatus ADD forfeited INT NOT NULL DEFAULT '0'");
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 2007);
			PreparedStatement ps = con.prepareStatement("UPDATE queststatus SET time = ?");
			ps.setInt(1, (int) (cal.getTimeInMillis() / 1000));
			ps.executeUpdate();
			ps.close();
		}
		if (!haveRanks) {
			s.executeUpdate("ALTER TABLE characters ADD rank INT UNSIGNED NOT NULL DEFAULT '1', " +
				"ADD rankMove INT NOT NULL DEFAULT '0'," +
				"ADD jobRank INT UNSIGNED NOT NULL DEFAULT '1'," +
				"ADD jobRankMove INT NOT NULL DEFAULT '0'");
			s.executeUpdate("INSERT INTO gmlog (id, cid, command, `when`) VALUES (DEFAULT, 0, '!recalcstats', NOW())");
		}
		if (!haveSalt) {
			s.executeUpdate("ALTER TABLE `accounts` ADD salt VARCHAR(32) NULL AFTER password");
			s.executeUpdate("ALTER TABLE `accounts` CHANGE `password` `password` VARCHAR( 128 ) NOT NULL");  
		}
		if (!haveGuilds) {
			s.executeUpdate("ALTER TABLE `characters` ADD COLUMN `guildid` INTEGER UNSIGNED NOT NULL DEFAULT 0 AFTER `jobRankMove`," +
					" ADD COLUMN `guildrank` INTEGER UNSIGNED NOT NULL DEFAULT 5 AFTER `guildid`;");			
		}
		if (!haveTempBan) {
			s.executeUpdate("ALTER TABLE accounts ADD tempban TIMESTAMP;");
			s.executeUpdate("ALTER TABLE accounts ADD greason TINYINT(4);");
		}
		if (needCharIdChange) {
			s.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
			s.executeUpdate("ALTER TABLE characters AUTO_INCREMENT = 30001");
			s.executeUpdate("UPDATE `characters` SET id = id + 30000");
			s.executeUpdate("UPDATE `buddies` SET characterid = characterid + 30000, buddyid = buddyid + 30000");
			s.executeUpdate("UPDATE `cheatlog` SET cid = cid + 30000");
			s.executeUpdate("UPDATE `eventstats` SET characterid = characterid + 30000");
			s.executeUpdate("UPDATE `gmlog` SET cid = cid + 30000");
			s.executeUpdate("UPDATE `famelog` SET characterid = characterid + 30000, characterid_to = characterid_to + 30000");
			s.executeUpdate("UPDATE `inventoryitems` SET characterid = characterid + 30000");
			s.executeUpdate("UPDATE `keymap` SET characterid = characterid + 30000");
			s.executeUpdate("UPDATE `queststatus` SET characterid = characterid + 30000");
			s.executeUpdate("UPDATE `savedlocations` SET characterid = characterid + 30000");
			s.executeUpdate("UPDATE `skills` SET characterid = characterid + 30000");
			s.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
		}
		if (!haveFredrick) {
			s.executeUpdate("UPDATE shops SET npcid = '9030000' WHERE npcid = '1052002'");
		}
		s.close();
	}

	@Override
	public String getVersion() {
		return "0.5.1-dev";
	}

}
