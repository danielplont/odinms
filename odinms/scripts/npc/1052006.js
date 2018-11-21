/*
	This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
					   Matthias Butz <matze@odinms.de>
					   Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation version 3 as published by
    the Free Software Foundation. You may not use, modify or distribute
    this program under any other version of the GNU Affero General Public
    License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/* Jake
	Subway Worker
	- Subway Ticketing Booth (103000100)
*/

importPackage(net.sf.odinms.client);

var status = 0;
var valid = 0;
var check = 0;
var itemid = Array(4031036, 4031037, 4031038);
var name = Array("Construction Site B1", "Construction Site B2", "Construction Site B3");
var cost = Array(500, 1200, 2000);

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 0) {
			cm.sendOk("Alright, see you next time.");
			cm.dispose();
			return;
		}
		if (mode == 1) {
			status++;
		}
		else {
			status--;
		}
		if (status == 0) {
			if (cm.getLevel() < 15) {
				cm.sendOk("You must be a higher level to enter the construction sites.");
				cm.dispose();
				check = 1;
			}
			else {
				var selStr = "You must purchase a ticket to enter. Once you have made the purchase, you may enter via #bThe Ticket Gate#k on the right. Which ticket would you like to buy?";
				if (cm.getLevel() >= 40) {
					valid = 3;
				}
				else if (cm.getLevel() >= 30) {
					valid = 2;
				}
				else if (cm.getLevel() >= 20) {
					valid = 1;
				}
				for (var i = 0; i < valid; i++) {
						selStr += "\r\n#L" + i + "##b" + name[i] + " (" + cost[i] + " meso)#l";
				}
				cm.sendSimple(selStr);
			}
		} else if (status == 1) {
			if (check != 1) {
				if (cm.getMeso() < cost[selection]) {
					cm.sendOk("You do not have enough mesos to buy a ticket!")
					cm.dispose();
				} else {
					cm.sendOk("You can now insert your ticket into #bThe Ticket Gate#k on the right.");
					cm.gainItem(itemid[selection], 1);
					cm.gainMeso(-cost[selection]);
				}
				cm.dispose();	
			}
		}
	}
}	
