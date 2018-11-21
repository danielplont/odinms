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

package net.sf.odinms.client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import net.sf.odinms.database.DatabaseConnection;

public class AutoRegister {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MapleClient.class);
    private static final int ACCOUNTS_PER_IP = 1; //change the value to the amount of accounts you want allowed for each ip
    public static final boolean autoRegister = true; //enable = true or disable = false

    public static boolean success = false; // DONT CHANGE

    public static boolean getAccountExists(String login) {
        boolean accountExists = false;
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT name FROM accounts WHERE name = ?");
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if(rs.first())
                accountExists = true;
        } catch (Exception ex) {
            log.error("ERROR", ex);
        }
        return accountExists;
    }

    public static void createAccount(String login, String pwd, String eip) {
        String sockAddr = eip;
        Connection con;

        //connect to database or halt
        try {
            con = DatabaseConnection.getConnection();
        } catch (Exception ex) {
            log.error("ERROR", ex);
            return;
        }

        try {
            PreparedStatement ipc = con.prepareStatement("SELECT lastknownip FROM accounts WHERE lastknownip = ?");
            ipc.setString(1, sockAddr.substring(1, sockAddr.lastIndexOf(':')));
            ResultSet rs = ipc.executeQuery();
            if (rs.first() == false || rs.last() == true && rs.getRow() < ACCOUNTS_PER_IP) {
                try {
                    PreparedStatement ps = con.prepareStatement("INSERT INTO accounts (name, password, email, birthday, macs, lastknownip) VALUES (?, ?, ?, ?, ?, ?)");
                    ps.setString(1, login);
                    ps.setString(2, LoginCrypto.hexSha1(pwd));
                    ps.setString(3, "no@email.provided");
                    ps.setString(4, "2008-04-07");
                    ps.setString(5, "00-00-00-00-00-00");
                    ps.setString(6, sockAddr.substring(1, sockAddr.lastIndexOf(':')));
                    ps.executeUpdate();

                    ps.close();

                    success = true;
                } catch (SQLException ex) {
                    log.error("ERROR", ex);
                    return;
                }
            }
            ipc.close();
            rs.close();
        } catch (SQLException ex) {
            log.error("ERROR", ex);
            return;
        }
    }
}