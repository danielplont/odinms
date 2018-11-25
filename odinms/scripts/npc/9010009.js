/* Duey
   Removed Skill maxer
*/


var status = 0;

function start() {
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
			status++;
		else
			status--;
		if (status == 0)
		{
			cm.sendYesNo("Hello I am Duey");
		}
		else if (status == 1)
		{
            cm.sendOk("I'm useless but full of potential!");
            cm.dispose();
	    }
	}
}

/*
function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			cm.sendYesNo("Hello I am Duey and I can max all you're skills with no fees. Do wish to have all your skills maxed now?");
		} else if (status == 1) {
			cm.teachSkill(5001000,1,1); //Start of max-level "1" skills
			cm.teachSkill(5001001,1,1);
			cm.teachSkill(5001002,1,1);
			cm.teachSkill(5101000,1,1);
			cm.teachSkill(5101001,1,1);
			cm.teachSkill(5101002,1,1);
			cm.teachSkill(5101003,1,1);
			cm.teachSkill(5101004,1,1);
			cm.teachSkill(5101005,1,1);
			cm.teachSkill(1003,1,1);
			cm.teachSkill(1004,1,1);
			cm.teachSkill(1121011,1,1);
			cm.teachSkill(1221012,1,1);
			cm.teachSkill(1321010,1,1);
			cm.teachSkill(2121008,1,1);
			cm.teachSkill(2221008,1,1);
			cm.teachSkill(2321009,1,1);
			cm.teachSkill(3121009,1,1);
			cm.teachSkill(3221008,1,1);
			cm.teachSkill(4121009,1,1);
			cm.teachSkill(4221008,1,1); //End of max-level "1" skills
			cm.teachSkill(1000002,8,8); //Start of max-level "8" skills
			cm.teachSkill(3000002,8,8);
			cm.teachSkill(4000001,8,8); //End of max-level "8" skills
			cm.teachSkill(1000001,10,10); //Start of max-level "10" skills
			cm.teachSkill(2000001,10,10); //End of max-level "10" skills
			cm.teachSkill(1000000,16,16); //Start of max-level "16" skills
			cm.teachSkill(2000000,16,16);
			cm.teachSkill(3000000,16,16); //End of max-level "16" skills
			cm.teachSkill(1001003,20,20); //Start of max-level "20" skills
			cm.teachSkill(1001004,20,20);
			cm.teachSkill(1001005,20,20);
			cm.teachSkill(2001002,20,20);
			cm.teachSkill(2001003,20,20);
			cm.teachSkill(2001004,20,20);
			cm.teachSkill(2001005,20,20);
			cm.teachSkill(3000001,20,20);
			cm.teachSkill(3001003,20,20);
			cm.teachSkill(3001004,20,20);
			cm.teachSkill(3001005,20,20);
			cm.teachSkill(4000000,20,20);
			cm.teachSkill(4001344,20,20);
			cm.teachSkill(4001334,20,20);
			cm.teachSkill(4001002,20,20);
			cm.teachSkill(4001003,20,20);
			cm.teachSkill(1101005,20,20);
			cm.teachSkill(1100001,20,20); //Start of mastery's
			cm.teachSkill(1100000,20,20);
			cm.teachSkill(1200001,20,20);
			cm.teachSkill(1200000,20,20);
			cm.teachSkill(1300000,20,20);
			cm.teachSkill(1300001,20,20);
			cm.teachSkill(3100000,20,20);
			cm.teachSkill(3200000,20,20);
			cm.teachSkill(4100000,20,20);
			cm.teachSkill(4200000,20,20); //End of mastery's
			cm.teachSkill(4201002,20,20);
			cm.teachSkill(4101003,20,20);
			cm.teachSkill(3201002,20,20);
			cm.teachSkill(3101002,20,20);
			cm.teachSkill(1301004,20,20);
			cm.teachSkill(1301005,20,20);
			cm.teachSkill(1201004,20,20);
			cm.teachSkill(1201005,20,20);
			cm.teachSkill(1101004,20,20); //End of boosters
			cm.teachSkill(1101006,20,20);
			cm.teachSkill(1201006,20,20);
			cm.teachSkill(1301006,20,20);
			cm.teachSkill(2101001,20,20);
			cm.teachSkill(2100000,20,20);
			cm.teachSkill(2101003,20,20);
			cm.teachSkill(2101002,20,20);
			cm.teachSkill(2201001,20,20);
			cm.teachSkill(2200000,20,20);
			cm.teachSkill(2201003,20,20);
			cm.teachSkill(2201002,20,20);
			cm.teachSkill(2301004,20,20);
			cm.teachSkill(2301003,20,20);
			cm.teachSkill(2300000,20,20);
			cm.teachSkill(2301001,20,20);
			cm.teachSkill(3101003,20,20);
			cm.teachSkill(3101004,20,20);
			cm.teachSkill(3201003,20,20);
			cm.teachSkill(3201004,20,20);
			cm.teachSkill(4100002,20,20);
			cm.teachSkill(4101004,20,20);
			cm.teachSkill(4200001,20,20);
			cm.teachSkill(4201003,20,20); //End of second-job skills and first-job
			cm.teachSkill(4211005,20,20);
			cm.teachSkill(4211003,20,20);
			cm.teachSkill(4210000,20,20);
			cm.teachSkill(4110000,20,20);
			cm.teachSkill(4111001,20,20);
			cm.teachSkill(4111003,20,20);
			cm.teachSkill(3210000,20,20);
			cm.teachSkill(3110000,20,20);
			cm.teachSkill(3211002,20,20);
			cm.teachSkill(3111002,20,20);
			cm.teachSkill(2210000,20,20);
			cm.teachSkill(2211004,20,20);
			cm.teachSkill(2211005,20,20);
			cm.teachSkill(2111005,20,20);
			cm.teachSkill(2111004,20,20);
			cm.teachSkill(2110000,20,20);
			cm.teachSkill(2311001,20,20);
			cm.teachSkill(2311005,20,20);
			cm.teachSkill(2310000,20,20);
			cm.teachSkill(1311007,20,20);
			cm.teachSkill(1310000,20,20);
			cm.teachSkill(1311008,20,20);
			cm.teachSkill(1210001,20,20);
			cm.teachSkill(1211009,20,20);
			cm.teachSkill(1210000,20,20);
			cm.teachSkill(1110001,20,20);
			cm.teachSkill(1111007,20,20);
			cm.teachSkill(1110000,20,20); //End of 3rd job skills (Removed the 4th job skills)
			cm.teachSkill(1101007,30,30); //Start of Custom                     //Warrior-Hero //Power Guard
			cm.teachSkill(1100003,30,30); //Final Attack Axe
			cm.teachSkill(1100002,30,30); //Final Attack Sword
			cm.teachSkill(1111002,30,30); //Combo Attack
			cm.teachSkill(1111008,30,30); //Shout
			cm.teachSkill(1120004,30,30); //Achilles
			cm.teachSkill(1120003,30,30); //Advanced Combo 1200003
			cm.teachSkill(1121011,30,30); //Awakening
			cm.teachSkill(1120005,30,30); //Blocking
			cm.teachSkill(1121008,30,30); //Brandish	
			cm.teachSkill(1121000,20,20); //Maple Warrior
			cm.teachSkill(1111006,30,30); //Coma Axe
			cm.teachSkill(1111005,30,30); //Coma Sword
			cm.teachSkill(1111004,30,30); //Panic Axe
			cm.teachSkill(1111003,30,30); //Panic Sword
			cm.teachSkill(1121006,30,30); //Rush //Power Stance and Enrage Deleted!
			cm.teachSkill(1200003,30,30); //Final Attack BW                   //Warrior-Paladin
			cm.teachSkill(1200002,30,30); //Final Attack Sword
			cm.teachSkill(1201007,30,30); //Power Guard
			cm.teachSkill(1211002,30,30); //Charged Blow
			cm.teachSkill(1211004,30,30); //Fire Charge BW
			cm.teachSkill(1211003,30,30); //Fire Charge Sword
			cm.teachSkill(1211005,30,30); //Ice Charge Sword
			cm.teachSkill(1211008,30,30); //Lightning Charge BW
			cm.teachSkill(1211007,30,30); //Thunder Charge Sword
			cm.teachSkill(1211006,30,30); //Blizzard Charge BW
			cm.teachSkill(1220005,30,30); //Achilles
			cm.teachSkill(1220010,10,10); //Advanced Charge
			cm.teachSkill(1221012,1,1); //Awakening
			cm.teachSkill(1221009,30,30); //Blast
			cm.teachSkill(1220006,30,30); //Blocking
			cm.teachSkill(1221004,20,20); //Divine Charge Mace
			cm.teachSkill(1221003,20,20); //Holy Charge Sword
			cm.teachSkill(1221000,20,20); //Maple Warrior
			cm.teachSkill(1221007,30,30); //Rush
			cm.teachSkill(1221011,30,30); //Sanctuary
			cm.teachSkill(1221002,30,30); //Stance
			cm.teachSkill(1300003,30,30); //Final Attack Pole Arm                   //Warrior-Dark Knight
			cm.teachSkill(1300002,30,30); //FInal Attack Spear
			cm.teachSkill(1301007,30,30); //Hyper Body
			cm.teachSkill(1311005,30,30); //Sacrifice
			cm.teachSkill(1311004,30,30); //Dragon Fury: Pole Arm
			cm.teachSkill(1311003,30,30); //Dragon Fury: Spear
			cm.teachSkill(1311002,30,30); //Pole Arm Crusher
			cm.teachSkill(1311001,30,30); //Spear Crusher
			cm.teachSkill(1320005,30,30); //Achilles
			cm.teachSkill(1321010,1,1); //Awakening
			cm.teachSkill(1321007,10,10); //Beholder
			cm.teachSkill(1320009,25,25); //Beholders buff
			cm.teachSkill(1320008,25,25); //Beholders Healing
			cm.teachSkill(1320006,30,30); //Berserk
			cm.teachSkill(1321000,20,20); //Maple Warrior
			cm.teachSkill(1321003,30,30); //Rush
			cm.teachSkill(1321002,30,30); //Stance
			cm.teachSkill(2101004,30,30); //Fire Arrow                  //Magican-Arch Mage Fire Poison
			cm.teachSkill(2101005,30,30); //Poison Breath
			cm.teachSkill(2110001,30,30); //Element Amplification
			cm.teachSkill(2111006,30,30); //Element Composition
			cm.teachSkill(2111002,30,30); //Explosion
			cm.teachSkill(2111003,30,30); //Poison Mist
			cm.teachSkill(2121001,30,30); //Big Bang
			cm.teachSkill(2121005,30,30); //Elquines
			cm.teachSkill(2121003,30,30); //Fire Demon
			cm.teachSkill(2121004,30,30); //Infinity
			cm.teachSkill(2121002,30,30); //Mana Reflection
			cm.teachSkill(2121000,20,20); //Maple Warrior
			cm.teachSkill(2121007,30,30); //Meteo
			cm.teachSkill(2121006,30,30); //Paralyze
			cm.teachSkill(2201004,30,30); //Cold Beam                   //Magican-Arch Mage Ice Lightning
			cm.teachSkill(2201005,30,30); //Thunder Bolt
			cm.teachSkill(2210001,30,30); //Element Amplification
			cm.teachSkill(2211006,30,30); //Element Composition
			cm.teachSkill(2211002,30,30); //Ice Strike
			cm.teachSkill(2211003,30,30); //Thunder Spear
			cm.teachSkill(2221001,30,30); //Big Bang
			cm.teachSkill(2221007,30,30); //Blizzard
			cm.teachSkill(2221006,30,30); //Chain Lightning
			cm.teachSkill(2221003,30,30); //Ice Demon
			cm.teachSkill(2221005,30,30); //Ifrit
			cm.teachSkill(2221004,30,30); //Infinity
			cm.teachSkill(2221002,30,30); //Mana Reflection
			cm.teachSkill(2221000,20,20); //Maple Warrior
			cm.teachSkill(2301002,30,30); //Heal //Magican-Bishop
			cm.teachSkill(2301005,30,30); //Holy Arrow
			cm.teachSkill(2311002,20,20); //Mystic Door
			cm.teachSkill(2311005,30,30); //Doom
			cm.teachSkill(2311003,30,30); //Holy Symbol
			cm.teachSkill(2311004,30,30); //Shining Ray
			cm.teachSkill(2311006,30,30); //Summon Dragon
			cm.teachSkill(2321007,30,30); //Angels Ray
			cm.teachSkill(2321003,30,30); //Bahamut
			cm.teachSkill(2321001,30,30); //Big Bang
			cm.teachSkill(2321008,30,30); //Genesis
			cm.teachSkill(2321005,30,30); //Holy Shield
			cm.teachSkill(2321004,30,30); //Infinity
			cm.teachSkill(2321002,30,30); //Mana Reflection
			cm.teachSkill(2321006,10,10); //Resurrection
			cm.teachSkill(3101005,30,30); //Arrow Bomb                //Bowman
			cm.teachSkill(3111004,30,30); //Arrow Rain
			cm.teachSkill(3111003,30,30); //Inferno
			cm.teachSkill(3111005,30,30); //Silver Hawk
			cm.teachSkill(3111006,30,30); //Strafe
			cm.teachSkill(3120005,30,30); //Bow Expert
			cm.teachSkill(3121008,30,30); //Concentration
			cm.teachSkill(3121003,30,30); //Dragon Pulse
			cm.teachSkill(3121007,30,30); //Hamstring
			cm.teachSkill(3121006,30,30); //Phoenix
			cm.teachSkill(3121002,30,30); //Sharp Eyes
			cm.teachSkill(3121004,30,30); //Storm Arrow
			cm.teachSkill(3201005,30,30); //Iron Arrow            //XBowman
			cm.teachSkill(3211004,30,30); //Arrow Eruption
			cm.teachSkill(3211003,30,30); //Blizzard
			cm.teachSkill(3211005,30,30); //Golden Eagle
			cm.teachSkill(3211006,30,30); //Strafe   
			cm.teachSkill(3221006,30,30); //Blind
			cm.teachSkill(3220004,30,30); //Crossbow Expert
			cm.teachSkill(3221003,30,30); //Dragon Pulse
			cm.teachSkill(3221005,30,30); //Freezer
			cm.teachSkill(3221001,30,30); //Piercing
			cm.teachSkill(3221000,20,20); //Maple Warrior
			cm.teachSkill(3221002,30,30); //Sharp Eyes   
			cm.teachSkill(4100001,30,30); //Critical Throw           //Assasin
			cm.teachSkill(4101005,30,30); //Drain
			cm.teachSkill(4111005,30,30); //Avenger
			cm.teachSkill(4111004,30,30); //Shadow Meso
			cm.teachSkill(4111002,30,30); //Shadow Partner    
			cm.teachSkill(4120002,30,30); //Fake
			cm.teachSkill(4121000,20,20); //Maple Warrior
			cm.teachSkill(4121004,30,30); //Ninja Ambush
			cm.teachSkill(4121008,30,30); //Ninja Storm
			cm.teachSkill(4121003,30,30); //Showdown
			cm.teachSkill(4121006,30,30); //Spirit Claw
			cm.teachSkill(4121007,30,30); //Triple Throw
			cm.teachSkill(4120005,30,30); //Venom   
			cm.teachSkill(4111006,20,20); //Flash Jump
			cm.teachSkill(4201005,30,30); //Savage Blow              //Bandit
			cm.teachSkill(4201004,30,30); //Steal
			cm.teachSkill(4211002,30,30); //Assaulter
			cm.teachSkill(4211004,30,30); //Band of Thieves
			cm.teachSkill(4211001,30,30); //Chakra
			cm.teachSkill(4211006,30,30); //Meso Explosion
			cm.teachSkill(4221001,30,30); //Assasinate
			cm.teachSkill(4221007,30,30); //Boomerang Step
			cm.teachSkill(4220002,30,30); //Fake
			cm.teachSkill(4221000,30,30); //Maple Warrior
			cm.teachSkill(4221004,30,30); //Ninja Ambush
			cm.teachSkill(4221003,30,30); //Showdown
			cm.teachSkill(4221006,30,30); //Smoke Shell
			cm.teachSkill(4220005,30,30); //Venom
			}
	    }
}

*/