/* [NPC]
	All-In-One Job Advancer Cody
 */
importPackage(net.sf.odinms.client);


var status = 0;
var job;

function start() 
{
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection)
{
    if (mode == -1)
    {
        cm.dispose();
    }
    else
    {
        if (mode == 1)
        {
            status++;
        }
        else
        {
            status--;
        }
        if (status == 0)
        {
            if (cm.getLevel() < 30)
            {
                status = 98;
                cm.sendNext("Sorry, but you have to be at least level 30 to get a 2nd job advance.");
            	cm.dispose();
	        }
            else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.WARRIOR)
                || cm.getJob().equals(net.sf.odinms.client.MapleJob.MAGICIAN)
                ||cm.getJob().equals(net.sf.odinms.client.MapleJob.BOWMAN)
                ||cm.getJob().equals(net.sf.odinms.client.MapleJob.THIEF))
            {
                cm.sendNext("Hello #b#h ##k, I'm in charge of Job Advancing of #bodinms#k.");

		    }
            else if (cm.getLevel() >= 30 && cm.getLevel() < 70)
            {
                if (cm.getJob().equals(net.sf.odinms.client.MapleJob.ASSASSIN)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.BANDIT)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.HUNTER)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.CROSSBOWMAN)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.PAGE)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.FIGHTER)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.SPEARMAN)
                    ||cm.getJob().equals(net.sf.odinms.client.MapleJob.CLERIC)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob. FP_WIZARD)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob. IL_WIZARD))
                {
                    cm.sendOk("Hello, I'm sorry but you need to be lvl70 for your next job advance");
                    cm.dispose();
                }
                else
                {
                    cm.sendNext("Hello #b#h ##k, I'm Cody and I'm in charge of Job Advancing of #bOurMs#k.");
                }
            }
            else if (cm.getLevel() >= 70)
            {
               if (cm.getJob().equals(net.sf.odinms.client.MapleJob.HERMIT)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.CHIEFBANDIT)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.RANGER)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.SNIPER)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.WHITEKNIGHT)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.CRUSADER)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.DRAGONKNIGHT)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.PRIEST)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob. FP_MAGE)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob. IL_MAGE))
               {
                    cm.sendOk("Hello #b#h ##k, \r\n#dYou are already at the highest job in this version of MapleStory.");
                    cm.dispose();
               }
               else
               {
                    status = 60;
                    cm.sendNext("Hello #b#h ##k, I'm Cody and I'm in charge of Job Advancing of #bOurMs#k.");
               }
            }
        }
        else if (status == 1)
        {
            if (cm.getJob().equals(net.sf.odinms.client.MapleJob.THIEF))
            {
                cm.sendSimple("Congrats on reaching level 30! Which would you like to be? #b\r\n#L0#Assassin#l\r\n#L1#Bandit#l#k");
            }
            if (cm.getJob().equals(net.sf.odinms.client.MapleJob.WARRIOR))
            {
                cm.sendSimple("Congrats on reaching level 30! Which would you like to be? #b\r\n#L2#Fighter#l\r\n#L3#Page#l\r\n#L4#Spearman#l#k");
            }
            if (cm.getJob().equals(net.sf.odinms.client.MapleJob.MAGICIAN))
            {
                cm.sendSimple("Congrats on reaching level 30! Which would you like to be? #b\r\n#L5#Ice Lightning Wizard#l\r\n#L6#Fire Poison Wizard#l\r\n#L7#Cleric#l#k");
            }
            if (cm.getJob().equals(net.sf.odinms.client.MapleJob.BOWMAN))
            {
                cm.sendSimple("Congrats on reaching level 30! Which would you like to be? #b\r\n#L8#Hunter#l\r\n#L9#Crossbowman#l#k");
            }
            if (cm.getJob().equals(net.sf.odinms.client.MapleJob.BEGINNER))
            {
                cm.sendNext("Congrats on reaching level 30! However, you must've already undergone the First Job Advancement before you can use my services.");
                cm.dispose();
            }
        }
        else if (status == 2)
        {
            var jobName;
            if (selection == 0)
            {
                jobName = "Assassin";
                job = net.sf.odinms.client.MapleJob.ASSASSIN;
            }
            if (selection == 1)
            {
                jobName = "Bandit";
                job = net.sf.odinms.client.MapleJob.BANDIT;
            }
            if (selection == 2)
            {
                jobName = "Fighter";
                job = net.sf.odinms.client.MapleJob.FIGHTER;
            }
            if (selection == 3)
            {
                jobName = "Page";
                job = net.sf.odinms.client.MapleJob.PAGE;
            }
            if (selection == 4)
            {
                jobName = "Spearman";
                job = net.sf.odinms.client.MapleJob.SPEARMAN;
            }
            if (selection == 5)
            {
                jobName = "Ice Lightning Wizard";
                job = net.sf.odinms.client.MapleJob.IL_WIZARD;
            }
            if (selection == 6)
            {
                jobName = "Fire Poison Wizard";
                job = net.sf.odinms.client.MapleJob.FP_WIZARD;
            }
            if (selection == 7)
            {
                jobName = "Cleric";
                job = net.sf.odinms.client.MapleJob.CLERIC;
            }
            if (selection == 8)
            {
                jobName = "Hunter";
                job = net.sf.odinms.client.MapleJob.HUNTER;
            }
            if (selection == 9)
            {
                jobName = "Crossbowman";
                job = net.sf.odinms.client.MapleJob.CROSSBOWMAN;
            }
            cm.sendYesNo("Do you want to become a #r" + jobName + "#k?");
        }
        else if (status == 3)
        {
            cm.changeJob(job);
            cm.sendOk("You have successfully taken you're job advancement. Hope to see you again in the future.");
            cm.dispose();
        }
        else if (status == 61)
        {
            if (cm.getJob().equals(net.sf.odinms.client.MapleJob.ASSASSIN))
            {
                status = 63;
                cm.sendYesNo("Congrats on reaching level 70 #b#h ##k! Do you want to advance to a Hermit?");
            }
            else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.BANDIT))
            {
                status = 66;
                cm.sendYesNo("Congrats on reaching level 70 #b#h ##k! Do you want to advance to a Chief Bandit?");
            }
            else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.HUNTER))
            {
                status = 69;
                cm.sendYesNo("Congrats on reaching level 70 #b#h ##k! Do you want to advance to a Ranger?");
            }
            else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.CROSSBOWMAN))
            {
                status = 72;
                cm.sendYesNo("Congrats on reaching level 70 #b#h ##k! Do you want to advance to a Sniper?");
            }
            else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.FP_WIZARD))
            {
                status = 75;
                cm.sendYesNo("Congrats on reaching level 70 #b#h ##k! Do you want to advance to a F/P Mage?");
            }
            else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.IL_WIZARD))
            {
                status = 78;
                cm.sendYesNo("Congrats on reaching level 70 #b#h ##k! Do you want to advance to a I/L Mage?");
            }
            else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.CLERIC))
            {
                status = 81;
                cm.sendYesNo("Congrats on reaching level 70 #b#h ##k! Do you want to advance to a Priest?");
            }
            else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.FIGHTER))
            {
                status = 84;
                cm.sendYesNo("Congrats on reaching level 70 #b#h ##k! Do you want to advance to a Crusader?");
            }
            else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.PAGE))
            {
                status = 87;
                cm.sendYesNo("Congrats on reaching level 70 #b#h ##k! Do you want to advance to a White Knight?");
            }
            else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.SPEARMAN))
            {
                status = 90;
                cm.sendYesNo("Congrats on reaching level 70 #b#h ##k! Do you want to advance to a Dragon Knight?");
            }
            else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.BEGINNER))
            {
                cm.sendNext("Such a high level #bBeginner#k #b#h ##k! Amazing! You must love being a SuperBeginner.");
            }
        }
        else if (status == 64)
        {
            cm.changeJob(MapleJob.HERMIT);
            cm.sendOk("You have successfully taken your 3rd job advance.");
            cm.dispose();
        }
        else if (status == 67)
        {
            cm.changeJob(MapleJob.CHIEFBANDIT);
            cm.sendOk("You have successfully taken your 3rd job advance.");
            cm.dispose();
        }
        else if (status == 70)
        {
            cm.changeJob(MapleJob.RANGER);
            cm.sendOk("You have successfully taken your 3rd job advance.");
            cm.dispose();
        }
        else if (status == 73)
        {
            cm.changeJob(MapleJob.SNIPER);
            cm.sendOk("You have successfully taken your 3rd job advance.");
            cm.dispose();
        }
        else if (status == 76)
        {
            cm.changeJob(MapleJob.FP_MAGE);
            cm.sendOk("You have successfully taken your 3rd job advance.");
            cm.dispose();
        }
        else if (status == 79)
        {
            cm.changeJob(MapleJob.IL_MAGE);
            cm.sendOk("You have successfully taken your 3rd job advance.");
            cm.dispose();
        }
        else if (status == 82)
        {
            cm.changeJob(MapleJob.PRIEST);
            cm.sendOk("You have successfully taken your 3rd job advance.");
            cm.dispose();
        }
        else if (status == 85)
        {
            cm.changeJob(MapleJob.CRUSADER);
            cm.sendOk("You have successfully taken your 3rd job advance.");
            cm.dispose();
        }
        else if (status == 88)
        {
            cm.changeJob(MapleJob.WHITEKNIGHT);
            cm.sendOk("You have successfully taken your 3rd job advance.");
            cm.dispose();
        }
        else if (status == 91)
        {
            cm.changeJob(MapleJob.DRAGONKNIGHT);
            cm.sendOk("You have successfully taken your 3rd job advance.");
            cm.dispose();
        }
        else if (status == 99)
        {
            cm.sendOk("Good luck on your training.");
            cm.dispose();
        }
    }
}

