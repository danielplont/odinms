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
			 if (cm.getJob().equals(net.sf.odinms.client.MapleJob.FP_MAGE)) {
				status = 117;
				cm.sendYesNo("Conratulations on reaching such a high level. Do you want to Job Advance now?");
			} else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.IL_MAGE)) {
				status = 120;
				cm.sendYesNo("Conratulations on reaching such a high level. Do you want to Job Advance now?");
			} else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.PRIEST)) {
				status = 123;
				cm.sendYesNo("Conratulations on reaching such a high level. Do you want to Job Advance now?");
			}		
		} else if (status == 118) {
            cm.changeJob(MapleJob.FP_ARCHMAGE);
            cm.teachSkill(2121000,0,20);//Maple Warrior
            //cm.teachSkill(2121001,0,30);//Big Bang
            cm.teachSkill(2121002,0,30);//Mana Reflection
            cm.teachSkill(2121003,0,30);//Fire Demon
            cm.teachSkill(2121005,0,30);//Elquines
            cm.teachSkill(2121006,0,30);//Paralyze
            cm.teachSkill(2121007,0,30);//Meteor Shower
            cm.teachSkill(2121008,0,1);//Hero's Will
            cm.sendOk("Here you go! Hope you enjoy it. I've given you a full set of skills as well, aren't you lucky!");
            cm.dispose();
        }
        else if(status == 121) 
        {
            cm.changeJob(MapleJob.IL_ARCHMAGE);
            cm.teachSkill(2221000,0,20);//Maple Warrior
            //cm.teachSkill(2221001,0,30);//Big Bang
            cm.teachSkill(2221002,0,30);//Mana Reflection
            cm.teachSkill(2221003,0,30);//Ice Demon
            cm.teachSkill(2221005,0,30);//Ifrit
            cm.teachSkill(2221006,0,30);//Chain Lightning
            cm.teachSkill(2221007,0,30);//Blizard
            cm.teachSkill(2221008,0,1);//Hero's Will
            cm.sendOk("Here you go! Hope you enjoy it. I've given you a full set of skills as well, aren't you lucky!");
            cm.dispose();
        }
        else if (status == 124) 
        {
            cm.changeJob(MapleJob.BISHOP);
            cm.teachSkill(2321000,0,20);//Maple Warrior
            //cm.teachSkill(2321001,0,30);//Big Bang
            cm.teachSkill(2321002,0,30);//Mana Reflection
            cm.teachSkill(2321003,0,30);//Bahamut
            cm.teachSkill(2321005,0,30);//Holy Shield
            cm.teachSkill(2321006,0,10);//Ressurection
            cm.teachSkill(2321008,0,30);//Genesis
            cm.teachSkill(2321009,0,1);//Hero's Will
            cm.sendOk("Here you go! Hope you enjoy it. I've given you a full set of skills as well, aren't you lucky!");
            cm.dispose();
		}
	}
}