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
======>OdinMS NX Npc<======
by Yan of HeavenlyMS
*/
function start() {
	cm.sendSimple ("Hi i am Nana of HeavenlyMS do u want to buy something ?, please choose your category.\r\n#L0#shoes#l\r\n#L1#Overalls#l\r\n#L2#Gloves#l\r\n#L3#Hats#l\r\n#L4#Shields#l\r\n#L5#Wands#l\r\n#L6#Staffs#l");
}

function action(mode, type, selection) {
	cm.dispose();
	if (selection == 0) {
		cm.openShop (1996);
	} else if (selection == 1) {
		cm.openShop (2026);
	} else if (selection == 2) {
		cm.openShop (2027);
	} else if (selection == 3) {
		cm.openShop (2028);
	} else if (selection == 4) {
		cm.openShop (2029);
	} else if (selection == 5) {
		cm.openShop (2030);
	} else if (selection == 6) {
		cm.openShop (2031);
	} else {
		cm.dispose();
	}
}