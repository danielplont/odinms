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

/* KC ticket gate
	Warp to KC waiting platform (600010004)
	located in KC Subway Station (103000100)
*/

var status = 0;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (status >= 2 && mode == 0) {
			cm.sendOk("Alright, see you next time.");
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			cm.sendNext("Hi, I'm the Kerning city ticket gate.");
		} else if (status == 1) {
			cm.sendNextPrev("I can take you to the subway train to New Leaf City, if you have a ticket.\r\n(I think Bell sells them)")
		} else if (status == 2) {
			if (cm.haveItem(4031711)) {
				cm.sendNext("Great, you have a ticket. I will send you to the waiting area for the subway.\r\nHave a safe journey.")
				cm.gainItem(4031711,-1);
			} else {
				cm.sendOk("You do not have a ticket.\r\nYou can't sneak past me you know!!\r\nLeave my station now.......Begone!");
				status = 9;
			}
		} else if (status == 3) {
			cm.warp(600010004);
			cm.dispose();
		} else if (status == 10) {
			cm.warp(103000000);
			cm.dispose();
		}
	}
}