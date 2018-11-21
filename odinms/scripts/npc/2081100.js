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
			 if (cm.getJob().equals(net.sf.odinms.client.MapleJob.CRUSADER)) {
				status = 126;
				cm.sendYesNo("Conratulations on reaching such a high level. Do you want to Job Advance now?");
			} else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.WHITEKNIGHT)) {
				status = 129;
				cm.sendYesNo("Conratulations on reaching such a high level. Do you want to Job Advance now?");
			} else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.DRAGONKNIGHT)) {
				status = 132;
				cm.sendYesNo("Conratulations on reaching such a high level. Do you want to Job Advance now?");
			}		
		} else if (status == 127) {
            cm.changeJob(MapleJob.HERO);
            cm.teachSkill(1121000,0,20); //Maple Warrior
            cm.teachSkill(1121001,0,30); //Monster Magnet
            cm.teachSkill(1121002,0,30); //Power Stance
            cm.teachSkill(1120004,0,30); //Achilles
            cm.teachSkill(1121008,0,30); //Brandish
            cm.teachSkill(1121010,0,30); //Enrage
            cm.teachSkill(1121011,0,1); //Hero's Will	
            cm.sendOk("Here you go! Hope you enjoy it. I've given you a full set of skills as well, aren't you lucky!");
            cm.dispose();
        } 
        else if (status == 130)
        {
            cm.changeJob(MapleJob.PALADIN);
            cm.teachSkill(1221000,0,20);//Maple Warrior
            cm.teachSkill(1221001,0,30); //Monster Magnet
            cm.teachSkill(1221002,0,30); //Power Stance
            cm.teachSkill(1221003,0,19);//Holy Charge : Sword
            cm.teachSkill(1221004,0,19);//Divine Charge : BW
            cm.teachSkill(1220005,0,30); //Achilles
            cm.teachSkill(1221009,0,30); //Blast
            cm.teachSkill(1221011,0,30); //Heaven's Hammer
            cm.teachSkill(1221012,0,1); //Hero's Will
            cm.sendOk("Here you go! Hope you enjoy it. I've given you a full set of skills as well, aren't you lucky!");
            cm.dispose();
        } 
        else if (status == 133)
        {
            cm.changeJob(MapleJob.DARKKNIGHT);
            cm.teachSkill(1321000,0,20);//Maple Warrior
            cm.teachSkill(1321001,0,30); //Monster Magnet
            cm.teachSkill(1321002,0,30); //Power Stance
            cm.teachSkill(1320005,0,30); //Achilles
            cm.teachSkill(1320006,0,30); //Berserk
            cm.teachSkill(1321007,0,10);//Beholder
            cm.teachSkill(1320008,0,30);//Aura of the Beholder
            cm.teachSkill(1320009,0,30);//Hex of the Beholder
            cm.teachSkill(1321010,0,1);  //Hero's Will
            cm.sendOk("Here you go! Hope you enjoy it. I've given you a full set of skills as well, aren't you lucky!");
            cm.dispose();
		}
	}
}