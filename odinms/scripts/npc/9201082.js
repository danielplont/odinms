/*

Spindle NPC ID 9201082

All in one NX Shop

*/
function start() {
	cm.sendSimple ("All in one NX Shop, please choose your category.\r\n#L0#Capes#l\r\n#L1#Faces#l\r\n#L2#Eye Acc#l\r\n#L3#Face Acc#l\r\n#L4#Gloves#l\r\n#L5#Hats#l\r\n#L6#Hats 2#l\r\n#L7#Overall#l\r\n#L8#Tops#l\r\n#L9#Bottoms#l\r\n#L10#Rings#l\r\n#L11#Shields#l\r\n#L12#Shoes#l\r\n#L13#Weapons#l");
}

function action(mode, type, selection) {
	cm.dispose();
	if (selection == 0) {
		cm.openShop (100);
	} else if (selection == 1) {
		cm.openShop (101);
	} else if (selection == 2) {
		cm.openShop (102);
	} else if (selection == 3) {
		cm.openShop (103);
	} else if (selection == 4) {
		cm.openShop (104);
	} else if (selection == 5) {
		cm.openShop (105);
	} else if (selection == 6) {
		cm.openShop (106);
	} else if (selection == 7) {
		cm.openShop (107);
	} else if (selection == 8) {
		cm.openShop (108);
	} else if (selection == 9) {
		cm.openShop (109);
	} else if (selection == 10) {
		cm.openShop (110);
	} else if (selection == 11) {
		cm.openShop (111);
	} else if (selection == 12) {
		cm.openShop (112);
	} else if (selection == 13) {
		cm.openShop (113);
	} else {
		cm.dispose();
	}
}