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
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.FP_WIZARD)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.IL_WIZARD))
                {
                    cm.sendOk("Hello, I'm sorry but you need to be lvl70 for your next job advance");
                    cm.dispose();
                }
                else
                {
                    cm.sendNext("Hello #b#h ##k, I'm Cody and I'm in charge of Job Advancing of #bOurMs#k.");
                }
            }
            else if (cm.getLevel() >= 70 && cm.getLevel() < 120)
            {
               if (cm.getJob().equals(net.sf.odinms.client.MapleJob.HERMIT)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.CHIEFBANDIT)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.RANGER)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.SNIPER)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.WHITEKNIGHT)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.CRUSADER)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.DRAGONKNIGHT)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.PRIEST)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.FP_MAGE)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.IL_MAGE))
               {
                    cm.sendOk("Hello, I'm sorry but you need to be lvl120 for your next job advance.");
                    cm.dispose();
               }
               else
               {
                    status = 60;
                    cm.sendNext("Hello #b#h ##k, I'm Cody and I'm in charge of Job Advancing of #bOurMs#k.");
               }
            }
            else if (cm.getLevel() >=120)
            {
                if (cm.getJob().equals(net.sf.odinms.client.MapleJob.NIGHTLORD)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.SHADOWER)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.BOWMASTER)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.CROSSBOWMASTER)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.PALADIN)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.HERO)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.DARKKNIGHT)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.BISHOP)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.FP_ARCHMAGE)
                    || cm.getJob().equals(net.sf.odinms.client.MapleJob.IL_ARCHMAGE))
                {
                    cm.sendOk("Hello #b#h ##k, \r\n#dYou are already at the highest job of MapleStory.#k");
                    cm.dispose();
                }
                else
                {
                    status = 101;
                    cm.sendNext("Hello #b#h ##k, I'm Cody and I'm in charge of Job Advancing on #bOurMs#k.");
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
        else if (status == 102)
        {
            if (cm.getJob().equals(net.sf.odinms.client.MapleJob.HERMIT))
            {
                status = 105;
                cm.sendYesNo("Congrats on reaching level 120 #g#h ##k Do you want to advance to a Nightlord now?");
            }
            else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.CHIEFBANDIT))
            {
                status = 108;
                cm.sendYesNo("Congrats on reaching level 120 #g#h ##k Do you want to advance to a Shadower now?");
            }
            else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.RANGER))
            {
                status = 111;
                cm.sendYesNo("Congrats on reaching level 120 #g#h ##k Do you want to advance to a Bowmaster now?");
            }
            else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.SNIPER))
            {
                status = 114;
                cm.sendYesNo("Congrats on reaching level 120 #g#h ##k Do you want to advance to a MarksMan now?");
            }
            else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.FP_MAGE))
            {
                status = 117;
                cm.sendYesNo("Congrats on reaching level 120 #g#h ##k Do you want to advance to a F/P Archmage now?");
            }
            else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.IL_MAGE))
            {
                status = 120;
                cm.sendYesNo("Congrats on reaching level 120 #g#h ##k Do you want to advance to an I/L Archmage now?");
            }
            else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.PRIEST))
            {
                status = 123;
                cm.sendYesNo("Congrats on reaching level 120 #g#h ##k Do you want to advance to a Bishop now?");
            }
            else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.CRUSADER))
            {
                status = 126;
                cm.sendYesNo("Congrats on reaching level 120 #g#h ##k Do you want to advance to a Hero now?");
            }
            else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.WHITEKNIGHT))
            {
                status = 129;
                cm.sendYesNo("Congrats on reaching level 120 #g#h ##k Do you want to advance to a Paladin now?");
            }
            else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.DRAGONKNIGHT))
            {
                status = 132;
                cm.sendYesNo("Congrats on reaching level 120 #g#h ##k Do you want to advance to a Dark Knight now?");
            }
            else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.BEGINNER))
            {
                cm.sendNext("Such a high level #bBeginner#k #g#h ##k Amazing! You must love being a SuperBeginner.");
                cm.dispose();
            }
        }
        else if (status == 106)
        {
            cm.changeJob(MapleJob.NIGHTLORD);
            //cm.teachSkill(4121000,0,30);//Maple Warrior
            cm.teachSkill(4120002,0,30);//Shadow Shifter
            cm.teachSkill(4121003,0,30);//Taunt
            //cm.teachSkill(4121004,0,30);//Ninja Ambush
            cm.teachSkill(4120005,0,30);//Venomous Star
            cm.teachSkill(4121006,0,30);//Shadow Claw
            cm.teachSkill(4121007,0,30);//Triple Throw
            //cm.teachSkill(4121008,0,30);//Ninja Storm
            cm.teachSkill(4121009,0,5);//Hero's Will
            cm.sendOk("You have successfully taken your 4th Job advance #b#h ##k. I provided you a full sets of skills aswell. Keep up the good work.");
            cm.dispose();
        }
        else if (status == 109)
        {
            cm.changeJob(MapleJob.SHADOWER);
            cm.teachSkill(4221000,0,30);//Maple Warrior
            cm.teachSkill(4221001,0,30);//Assassinate
            cm.teachSkill(4220002,0,30);//Shadow Shifter
            cm.teachSkill(4221003,0,30);//Taunt
            //cm.teachSkill(4221004,0,30);//Ninja Ambush
            cm.teachSkill(4220005,0,30);//Venomous Stab
            cm.teachSkill(4221006,0,30);//SmokeScreen
            cm.teachSkill(4221007,0,30);//Boomerang Step
            cm.teachSkill(4221008,0,5);//Hero's Will
            cm.sendOk("You have successfully taken your 4th Job advance #b#h ##k. I provided you a full sets of skills aswell. Keep up the good work.");
            cm.dispose();
        }
        else if (status == 112)
        {
            cm.changeJob(MapleJob.BOWMASTER);
            //cm.teachSkill(3121000,0,30);//Maple Warrior
            cm.teachSkill(3121002,0,30);//Sharp Eyes
            //cm.teachSkill(3121003,0,30);//Dragon's Breath
            cm.teachSkill(3121004,0,30);//Hurricane
            cm.teachSkill(3120005,0,30);//Bow Expert
            cm.teachSkill(3121006,0,30);//Pheonix
            cm.teachSkill(3121007,0,30);//HamString
            cm.teachSkill(3121008,0,30);//Concentrate
            cm.teachSkill(3121009,0,5);//Hero's Will
            cm.sendOk("You have successfully taken your 4th Job advance #b#h ##k. I provided you a full sets of skills aswell. Keep up the good work.");
            cm.dispose();
        }
        else if (status == 115)
        {
            cm.changeJob(MapleJob.CROSSBOWMASTER);
            cm.teachSkill(3221000,0,30);//Maple Warrior
            cm.teachSkill(3221001,0,30);//Piercing Arrow
            cm.teachSkill(3221002,0,30);//Sharp Eyes
            //cm.teachSkill(3221003,0,30);//Dragon's Breath
            cm.teachSkill(3220004,0,30);//Marksman Boost
            cm.teachSkill(3221005,0,30);//Frostprey
            cm.teachSkill(3221006,0,30);//Blind
            cm.teachSkill(3221007,0,30);//Snipe
            cm.teachSkill(3221008,0,5);//Hero's Will
            cm.sendOk("You have successfully taken your 4th Job advance #b#h ##k. I provided you a full sets of skills aswell. Keep up the good work.");
            cm.dispose();
        }
        else if (status == 118)
        {
            cm.changeJob(MapleJob.FP_ARCHMAGE);
            //cm.teachSkill(2121000,0,30);//Maple Warrior
            cm.teachSkill(2121001,0,30);//Big Bang
            cm.teachSkill(2121002,0,30);//Mana Reflection
            cm.teachSkill(2121003,0,30);//Fire Demon
            cm.teachSkill(2121004,0,30);//Infinity
            cm.teachSkill(2121005,0,30);//Elquines
            cm.teachSkill(2121006,0,30);//Paralyze
            //cm.teachSkill(2121007,0,30);//Meteor Shower
            cm.teachSkill(2121008,0,5);//Hero's Will
            cm.sendOk("You have successfully taken your 4th Job advance #b#h ##k. I provided you a full sets of skills aswell. Keep up the good work.");
            cm.dispose();
        }
        else if(status == 121)
        {
            cm.changeJob(MapleJob.IL_ARCHMAGE);
            //cm.teachSkill(2221000,0,30);//Maple Warrior
            cm.teachSkill(2221001,0,30);//Big Bang
            cm.teachSkill(2221002,0,30);//Mana Reflection
            cm.teachSkill(2221003,0,30);//Ice Demon
            cm.teachSkill(2221004,0,30);//Infinity
            cm.teachSkill(2221005,0,30);//Ifrit
            cm.teachSkill(2221006,0,30);//Chain Lightning
            //cm.teachSkill(2221007,0,30);//Blizard
            cm.teachSkill(2221008,0,5);//Hero's Will
            cm.sendOk("You have successfully taken your 4th Job advance #b#h ##k. I provided you a full sets of skills aswell. Keep up the good work.");
            cm.dispose();
        }
        else if (status == 124)
        {
            cm.changeJob(MapleJob.BISHOP);
            cm.teachSkill(2321000,0,30);//Maple Warrior
            cm.teachSkill(2321001,0,30);//Big Bang
            cm.teachSkill(2321002,0,30);//Mana Reflection
            cm.teachSkill(2321003,0,30);//Bahamut
            cm.teachSkill(2321004,0,30);//Infinity
            cm.teachSkill(2321005,0,30);//Holy Shield
            cm.teachSkill(2321006,0,10);//Ressurection
            cm.teachSkill(2321007,0,30);//Angel Ray
            //cm.teachSkill(2321008,0,30);//Genesis
            cm.teachSkill(2321009,0,5);//Hero's Will
            cm.sendOk("You have successfully taken your 4th Job advance #b#h ##k. I provided you a full sets of skills aswell. Keep up the good work.");
            cm.dispose();
        }
        else if (status == 127)
        {
            cm.changeJob(MapleJob.HERO);
            //cm.teachSkill(1121000,0,30);//Maple Warrior
            cm.teachSkill(1121001,0,30);//Monster Magnet
            cm.teachSkill(1121002,0,30);//Power Stance
            cm.teachSkill(1120003,0,30);//Adv Combo
            cm.teachSkill(1120004,0,30);//Achilles
            //cm.teachSkill(1120005,0,30);//Guardian
            //cm.teachSkill(1121006,0,30);//Rush
            cm.teachSkill(1121008,0,30);//Brandish
            cm.teachSkill(1121010,0,30);//Enrage
            cm.teachSkill(1121011,0,5);//Hero's Will

            cm.sendOk("You have successfully taken your 4th Job advance #b#h ##k. I provided you a full sets of skills aswell. Keep up the good work.");
            cm.dispose();
        }
        else if (status == 130)
        {
            cm.changeJob(MapleJob.PALADIN);
            cm.teachSkill(1221000,0,30);//Maple Warrior
            cm.teachSkill(1221001,0,30);//Monster Magnet
            cm.teachSkill(1221002,0,30);//Power Stance
            cm.teachSkill(1221003,0,20);//Holy Charge : Sword
            cm.teachSkill(1221004,0,20);//Divine Charge : BW
            cm.teachSkill(1220005,0,30);//Achilles
            //cm.teachSkill(1220006,0,30);//Guardian
            //cm.teachSkill(1221007,0,30);//Rush
            cm.teachSkill(1221009,0,30);//Blast
            cm.teachSkill(1220010,0,10);//Adv Charge
            cm.teachSkill(1221011,0,30);//Heaven's Hammer
            cm.teachSkill(1221012,0,5);//Hero's Will
            cm.sendOk("You have successfully taken your 4th Job advance #b#h ##k. I provided you a full sets of skills aswell. Keep up the good work.");
            cm.dispose();
        }
        else if (status == 133)
        {
            cm.changeJob(MapleJob.DARKKNIGHT);
            //cm.teachSkill(1321000,0,30);//Maple Warrior
            cm.teachSkill(1321001,0,30);//Monster Magnet
            cm.teachSkill(1321002,0,30);//Power Stance
            //cm.teachSkill(1321003,0,30);//Rush
            cm.teachSkill(1320005,0,30);//Achilles
            cm.teachSkill(1320006,0,30);//Berserk
            cm.teachSkill(1321007,0,10);//Beholder
            cm.teachSkill(1320008,0,25);//Aura of the Beholder
            cm.teachSkill(1320009,0,25);//Hex of the Beholder
            cm.teachSkill(1321010,0,5);//Hero's Will
            cm.sendOk("You have successfully taken your 4th Job advance #b#h ##k. I provided you a full sets of skills aswell. Keep up the good work.");
            cm.sendOk("Good luck on your training.");
            cm.dispose();
        }
    }
}

