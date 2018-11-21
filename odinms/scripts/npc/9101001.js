var status = 0;

function start() {
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (status == 0) {
		cm.sendNext("You have finished all your trainings. Good job. You seem to be ready to start with the journey right away ! Good , I will let you on to the next place.");
		status++;
	} else {
		if ((status == 1 && type == 1 && selection == -1 && mode == 0) || mode == -1) {
			cm.dispose();
		} else {
			if (status == 1) {
					cm.sendOk ("But remember, once you get out of here, you will enter a village full with monsters. Well them, good bye!");
					status++
			} else if (status == 2) {
					cm.warp(40000, 0);
					cm.dispose();
			}
		}
	}
}