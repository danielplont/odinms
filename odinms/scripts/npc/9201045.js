/* Amos the Strong
	Warp NPC

/* Made By: WaffeSS
	 
*/

var status = 0;
var maps = Array(800020130, 240020101, 800010100, 240060200, 240020401, 220080001, 100000005, 230040420, 105070002, 280030000);
var cost = Array(1000000, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000);
var costBeginner = Array(100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000);
var selectedMap = -1;
var job;

importPackage(net.sf.odinms.client);

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
			cm.sendNext("Hey I am Amos the strong the #bBoss Warper#k of Free Market!");
		} else if (status == 1) {
			cm.sendNextPrev("I can take you to many bosses for just a small fee.")
		} else if (status == 2) {
			var selStr = "Select your destination.#b";
			if (cm.getJob().equals(net.sf.odinms.client.MapleJob.BEGINNER)) {
				for (var i = 0; i < maps.length; i++) {
					selStr += "\r\n#L" + i + "##m" + maps[i] + "# (" + costBeginner[i] + " meso)#l";
				}
			} else {
				for (var i = 0; i < maps.length; i++) {
					selStr += "\r\n#L" + i + "##m" + maps[i] + "# (" + cost[i] + " meso)#l";
				}
			}
			cm.sendSimple(selStr);
		} else if (status == 3) {
			if (cm.getJob().equals(net.sf.odinms.client.MapleJob.BEGINNER)) {
				if (cm.getMeso() < costBeginner[selection]) {
					cm.sendOk("You do not have enough mesos.")
					cm.dispose();
				} else {
					cm.sendYesNo("So you have nothing left to do here? Do you want to go to #m" + maps[selection] + "#?");
					selectedMap = selection;
				}
			}
			else {
				if (cm.getMeso() < cost[selection]) {
					cm.sendOk("You do not have enough mesos.")
					cm.dispose();
				} else {
					cm.sendYesNo("So you have nothing left to do here? Do you want to go to #m" + maps[selection] + "#?");
					selectedMap = selection;
				}
			}		
		} else if (status == 4) {
			if (cm.getJob().equals(net.sf.odinms.client.MapleJob.BEGINNER)) {
				cm.gainMeso(-costBeginner[selectedMap]);
			}
			else {
				cm.gainMeso(-cost[selectedMap]);
			}
			cm.warp(maps[selectedMap], 0);
			cm.dispose();
		}
	}
}	

