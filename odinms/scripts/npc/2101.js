var status = 0;

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
			cm.sendSimple(" Well, Are you done with your training ?\r\n#L1#Yes,i do !#l\r\n\#L2#No,i would like to have a training again !#l");
			} else if (status == 1) {
			if (selection == 1) {
					cm.gainMeso(1000);				
					cm.warp(3, 0);
					} 
					cm.dispose();					
			} else {
                                        cm.sendOk("well, the training begin !");
					cm.warp(1, 0);
					cm.dispose();
		}
	}
}
