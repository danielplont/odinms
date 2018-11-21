/* [NPC]
	Job Advancer
	Made by Tryst (wasdwasd) of Odinms Forums.
	Please don't release this anywhere else.
*/

importPackage(net.sf.odinms.client);

var status = 0;
var job;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			if (cm.getLevel() < 120) {
				cm.sendNext("Sorry, but you have to be at least level 120 to make 4th Job Advancement.");
				status = 98;
			} else if (cm.getLevel() >=120) {
				status = 101;
				cm.sendNext("Hello, So you want 4th Job Advancement eh?.");
			}
        	} else if (status == 102) {
			 if (cm.getJob().equals(net.sf.odinms.client.MapleJob.HERMIT)) {
        	        	status = 105;
       	         		cm.sendYesNo("Congrats on reaching level 120! Do you want to advance to a Nightlord?");
       	     		} else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.CHIEFBANDIT)) {
       	         		status = 108;
       	         		cm.sendYesNo("Congrats on reaching level 120! Do you want to advance to a Shadower?");
       	     		}		
        } else if (status == 106) {
            cm.changeJob(MapleJob.NIGHTLORD);
            cm.teachSkill(4121000,0,20);//Maple Warrior
            cm.teachSkill(4121004,0,30);//Ninja Ambush
            cm.teachSkill(4120005,0,30);//Venomous Star
            cm.teachSkill(4121006,0,30);//Shadow Claw
            cm.teachSkill(4121008,0,30);//Ninja Storm
            cm.teachSkill(4121009,0,1);//Hero's Will
            cm.sendOk("Here you go! Hope you enjoy it. I've given you a full set of skills as well, aren't you lucky!");
	    cm.dispose();
        }
        else if (status == 109)
	{
            cm.changeJob(MapleJob.SHADOWER);
            cm.teachSkill(4221000,0,20);//Maple Warrior
            cm.teachSkill(4221001,0,30);//Assassinate
            cm.teachSkill(4221004,0,30);//Ninja Ambush
            cm.teachSkill(4220005,0,30);//Venomous Stab
            cm.teachSkill(4221006,0,30);//SmokeScreen
            cm.teachSkill(4221007,0,30);//Boomerang Step
            cm.teachSkill(4221008,0,1);//Hero's Will
            cm.sendOk("Here you go! Hope you enjoy it. I've given you a full set of skills as well, aren't you lucky!");
	    cm.dispose();
		}
	}
}