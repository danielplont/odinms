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

/*

*/
function start() {
	cm.sendSimple ("Hi i am Nana of odinms do u want to buy something ?, please choose your category.\r\n#L0#Shoes#l\r\n#L1#Bottom#l\r\n#L2#Top#l\r\n#L3#Overalls#l\r\n#L4#Gloves#l\r\n#L5#Hats#l\r\n#L6#Shields#l\r\n#L7#One-Handed Axe#l\r\n#L8#Two-Handed Axe#l\r\n#L9#One-Handed BW#l\r\n#L10#Two-Handed BW#l\r\n#L11#One-Handed Sword#l\r\n#L12#Two-Handed Sword#l\r\n#L13#Spear#l\r\n#L14#Pole Arm#l");
}

function action(mode, type, selection) {
	cm.dispose();
	if (selection == 0) {
		cm.openShop (1995);
	} else if (selection == 1) {
		cm.openShop (2006);
	} else if (selection == 2) {
		cm.openShop (2007);
	} else if (selection == 3) {
		cm.openShop (2008);
	} else if (selection == 4) {
		cm.openShop (2009);
	} else if (selection == 5) {
		cm.openShop (2010);
	} else if (selection == 6) {
		cm.openShop (2011);
	} else if (selection == 7) {
		cm.openShop (2012);
	} else if (selection == 8) {
		cm.openShop (2013);
	} else if (selection == 9) {
		cm.openShop (2014);
	} else if (selection == 10) {
		cm.openShop (2015);
	} else if (selection == 11) {
		cm.openShop (2016);
	} else if (selection == 12) {
		cm.openShop (2017);
	} else if (selection == 13) {
		cm.openShop (2018);
	} else if (selection == 14) {
		cm.openShop (2019);
	} else {
		cm.dispose();
	}
}