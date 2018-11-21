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

package net.sf.odinms.exttools.database;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import net.sf.odinms.database.DatabaseConnection;

public class ResetLoggedin {
	public static void main(String args[]) throws SQLException, FileNotFoundException, IOException {
		Properties dbProp = new Properties();
		dbProp.load(new FileReader("db.properties"));
		DatabaseConnection.setProps(dbProp);
		Connection con = DatabaseConnection.getConnection();
		PreparedStatement ps = con.prepareStatement("UPDATE accounts SET loggedin = 0");
		int status = ps.executeUpdate();
		System.out.println("Successfully resetted " + status + " accounts to loggedout");
	}
}
