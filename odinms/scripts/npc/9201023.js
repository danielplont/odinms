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
======>OdinMS Thief Npc<======
Made by Yan of HeavenlyMS
*/
function start() {
	cm.sendSimple ("Hi i am Nana of HeavenlyMS do u want to buy something ?, please choose your category.\r\n#L0#Shoes#l\r\n#L1#Bottoms#l\r\n#L2#Tops#l\r\n#L3#Overalls#l\r\n#L4#Gloves#l\r\n#L5#Hats#l\r\n#L6#Shields#l\r\n#L7#Daggers#l\r\n#L8#Claws#l\r\n#L9#Throwing Stars#l");
}

function action(mode, type, selection) {
	cm.dispose();
	if (selection == 0) {
		cm.openShop (1993);
	} else if (selection == 1) {
		cm.openShop (1997);
	} else if (selection == 2) {
		cm.openShop (1998);
	} else if (selection == 3) {
		cm.openShop (1999);
	} else if (selection == 4) {
		cm.openShop (2000);
	} else if (selection == 5) {
		cm.openShop (2001);
	} else if (selection == 6) {
		cm.openShop (2002);
	} else if (selection == 7) {
		cm.openShop (2003);
	} else if (selection == 8) {
		cm.openShop (2004);
	} else if (selection == 9) {
		cm.openShop (2005);
	} else {
		cm.dispose();
	}
}