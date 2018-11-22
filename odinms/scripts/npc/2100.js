/*
	This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
                       Matthias Butz <matze@odinms.de>
                       Jan Christian Meyer <vimes@odinms.de>
            edit by Peter Kwon <mokey_raperz231@hotmail.com>

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

/* Sera
	First NPC on Map 0
*/

var wui = 0;

function start() {
	cm.sendOk ("Welcome to the test server, you will be teleported to Henesys and given a few items to start out. Cody does job advancements. Fairy vendors are located in the main towns selling weapons/armor along with other customer vendors.");
}

function action(mode, type, selection) {
	if (mode == 0 || wui == 1) {
		cm.dispose();
		cm.gainItem(1082149, 1);
		cm.gainItem(2040807, 7);
		cm.gainItem(1050018, 1);
		cm.gainItem(1051017, 1);
		cm.gainItem(2000005, 5000);
		cm.gainItem(1122001, 1);
        cm.warp(100000000, 0);
        cm.gainMeso(15000000);
		var statup = new java.util.ArrayList();
		var p = cm.c.getPlayer();
		var totAp = p.getRemainingAp() + p.getStr() + p.getDex() + p.getInt() + p.getLuk();
		p.setStr(10);
		p.setDex(10);
		p.setInt(10);
		p.setLuk(10);
		p.setRemainingAp (totAp - 20);
		statup.add (new net.sf.odinms.tools.Pair(net.sf.odinms.client.MapleStat.STR, java.lang.Integer.valueOf(4)));
		statup.add (new net.sf.odinms.tools.Pair(net.sf.odinms.client.MapleStat.DEX, java.lang.Integer.valueOf(4)));
		statup.add (new net.sf.odinms.tools.Pair(net.sf.odinms.client.MapleStat.LUK, java.lang.Integer.valueOf(4)));
		statup.add (new net.sf.odinms.tools.Pair(net.sf.odinms.client.MapleStat.INT, java.lang.Integer.valueOf(4)));
		statup.add (new net.sf.odinms.tools.Pair(net.sf.odinms.client.MapleStat.AVAILABLEAP, java.lang.Integer.valueOf(p.getRemainingAp())));

		p.getClient().getSession().write (net.sf.odinms.tools.MaplePacketCreator.updatePlayerStats(statup));
		cm.sendOk ("Goodbye!");
	}
}