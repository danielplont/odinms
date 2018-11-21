var status = 0;

function start() {
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (status == 0) {
		cm.sendYesNo("Do you wish to enter to #bHorntail's Cave#k?");
		status++;
	} else {
		if ((status == 1 && type == 1 && selection == -1 && mode == 0) || mode == -1) {
			cm.dispose();
		} else {
			if (status == 1) {
					cm.sendOk ("Goodluck on your battle with the #rHorntail#k!");
					status++
			} else if (status == 2) {
					cm.warp(240060200, 0);
					cm.dispose();
			}
		}
	}
}