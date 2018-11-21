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

importPackage(net.sf.odinms.tools);
importPackage(net.sf.odinms.client);

/* Adobis
	Zakum entrance
*/
var status = 0;
var price = 2000000;
var map = Array(240010501);

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 0 && status == 0) {
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			cm.sendSimple("Welcome to Entracnce to Zakum Altar ! Would you like to go inside and fight #bZakum#k ? If you want to fight him you need #b#t4001017##k, so you can summon Zakum .\r\n#L1#I would like to buy a #b#t4001017##k for 20,000,000 Mesos !#l\r\n\#L2#I already have a #b#t4001017##k , Let me in!#l");
			} else if (status == 1) {
			if (selection == 1) {
				if(cm.getMeso() >= 20000000) {
					cm.gainMeso(-20000000);
					cm.gainItem(4001017, 1);
					cm.sendOk("Enjoy!");
				} else {
					cm.sendOk("You don't have enough mesos to buy eye of fire!");
				}
				cm.dispose();
			} else if (status == 2) {
			} else if (selection == 2) {
                                            cm.getLevel() >= 50 
					cm.warp(280030000, 0);
					cm.dispose();
}
else{
cm.sendOk("You are not in the right levels to get inside (Level 50+) !");

			}
		}
	}
}
