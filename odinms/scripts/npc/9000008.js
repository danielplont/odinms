/*
@    Author : Snow
@
@    NPC = NAME
@    Map =  MAP
@    NPC MapId = MAPID
@    Function = Rebirth Player
@
*/

var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

         
         if (mode == -1) {//ExitChat
        cm.dispose();
    
    }else if (mode == 0){//No
        cm.sendOk("Ok, talk to me when you're sure you want to be #bReborn#k.");
        cm.dispose();

    }else{            //Regular Talk
        if (mode == 1)
            status++;
        else
            status--;
        
                 if (status == 0) {
        cm.sendYesNo("Welcome, great hero. You have been through a long and challenging road, and you have become immensely strong. I can increase your power even further, and surpass your limits! You will become a level 1 #bBeginner#k again, but you will keep your stats the same and all the skills in your #bhot keys#k. Do you wish to be #rReborn#k?");
        }else if (status == 1) {
        if(cm.getChar().getLevel() < 200){
        cm.sendOk("Sorry, You have to be level 200 to rebirth.");
        cm.dispose();
        }else{
        cm.sendOk("#bCongratulations!#k, you have been qualified for a #eRebirth#n. Make sure you have #renough space#k to unequip your items or else I'll just level you to 201.");
        }
         }else if (status == 2) {
		wui = 1;
		var statup = new java.util.ArrayList();
		var p = cm.c.getPlayer();
        cm.getChar().levelUp();
        cm.unequipEverything()
        cm.changeJob(net.sf.odinms.client.MapleJob.BEGINNER);
        cm.sendNext("You have bee reborned! Good luck on your next journey.");
	cm.setLevel(2);
		statup.add (new net.sf.odinms.tools.Pair(net.sf.odinms.client.MapleStat.LEVEL, java.lang.Integer.valueOf(1)));
		p.getClient().getSession().write (net.sf.odinms.tools.MaplePacketCreator.updatePlayerStats(statup));
        cm.dispose();
        }            
          }
     }