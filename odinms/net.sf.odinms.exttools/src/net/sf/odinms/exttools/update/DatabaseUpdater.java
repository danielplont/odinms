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

package net.sf.odinms.exttools.update;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.odinms.database.DatabaseConnection;
import net.sf.odinms.exttools.update.step.UpdateStep;
import net.sf.odinms.exttools.update.step.Version_0_5_1;

/**
 *
 * @author Matze
 */
public class DatabaseUpdater {
	
	private static List<UpdateStep> availableUpdates = new LinkedList<UpdateStep>();

	private static void registerUpdates() {
		 availableUpdates.add(new Version_0_5_1());
	}
	
	public static void main(String[] args) {
		registerUpdates();
		InputStreamReader is = null;
		try {
			Properties dbProp = new Properties();
			is = new FileReader("db.properties");
			dbProp.load(is);
			DatabaseConnection.setProps(dbProp);
		} catch (IOException ex) {
			Logger.getLogger(DatabaseUpdater.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				is.close();
			} catch (IOException ex) {
				Logger.getLogger(DatabaseUpdater.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Connection con = DatabaseConnection.getConnection();
		try {
			con.setAutoCommit(false);
			System.out.println("Your database is now being analyzed and updated if necessary...");
			boolean appliedAny = false;
			for (UpdateStep step : availableUpdates) {
				if (step.isRequired()) {
					System.out.print("Updating database to version " + step.getVersion() + "...");
					step.apply();
					System.out.println("done");
					appliedAny = true;
				}
			}
			con.commit();
			if (!appliedAny) {
				System.out.println("Your database is already up-to-date.");
			} else {
				System.out.println("Your database has been successfully updated.");
			}
		} catch (SQLException ex) {
			try {
				con.rollback();
				Logger.getLogger(DatabaseUpdater.class.getName()).log(Level.SEVERE, "Unable to update database, transaction has been rollbacked", ex);
			} catch (SQLException ex2) {
				Logger.getLogger(DatabaseUpdater.class.getName()).log(Level.SEVERE, "Unable to rollback transaction", ex2);
			}
		}
	}
	
}
