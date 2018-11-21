/*
This file is part of the OdinMS Maple Story Server
Copyright (C) 2008 Patrick Huy
Matthias Butz <matze@odinms.de>
Jan Christian Meyer <vimes@odinms.de>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License version 3
as published by the Free Software Foundation. You may not use, modify
or distribute this program under any other version of the
GNU Affero General Public License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

/* NimaKIN-> King321123 from RageZone's forums.
First NPC on Map 180000000
*/
if (cm.getJob().equals(net.sf.odinms.client.MapleJob. SUPERGM)) {
cm.sendNext("Hi! I am #bNimaKIN#k. I can help you thought your jounrey as a #bGameMaster#k.");
} else {
cm.sendOk("I am sorry, But you are not a #bGameMaster#k.")
cm.dispose();
}
} else if (status == 0) {
cm.sendNextPrev("Okay, You gotta know somethings on #bRageZone#k. Please choose your category! #b\r\n#L0#What is a GameMaster?#l\r\n#L1#What can I do?#l\r\n#L2#What is this Map?#l \r\n#L3#Which commands?#l\r\n#L4#Max my stats!##l");
} else if (status == 1) {
if (selection == 0) {
cm.sendOk("A #bGame Master#k is an inmportant thing. It can do everything it just want! I will give you an ex. You can warp people to your map, You can level someone to any level you just want! But, You gotta take care of #bBugs, Problems#k.");
if (selection == 1) {
cm.sendOk("You can do everything you just want! Ex. You can #bwarp#k everyone to any map you want. You can #blevel#k yourself to 200, and higher!, You can do anything you just want!");
if (selection == 2) {
cm.sendOk("This is the #bGameMaster's Map#k. You can do here what ever you want, And nobody can't enter here. (not inculding GM's). You can use #bKIN#k, to change your hair. You can use me to know about commands, and to give you some GM Items!);
if (selection == 3) {
cm.sendOk("The command are : #b!map <mapid> !goto <mapname> !warp <charname> <mapid> !warpallhere <warps everyone here> !warphere <charname> !warp <charname> !job <jobid> !level <level>#k. And much more .. Ask your developer for them!);
if (selection == 4) {
cm.sendOk("Here, I can max your stats, to #b32000#k. I will give you some #bGM Items#k too, Have fun!");
cm.dispose();
cm.gainItem(1002140, 1);
cm.gainItem(1042003, 1);
cm.gainItem(1062007, 1);
cm.gainItem(1322013, 1);
cm.gainItem(1072235, 1);
} else {
var statup = new java.util.ArrayList();
var p = cm.c.getPlayer();
var totAp = p.getRemainingAp() + p.getStr() + p.getDex() + p.getInt() + p.getLuk();
p.setStr(32000);
p.setDex(32000);
p.setInt(32000);
p.setLuk(32000);
p.setRemainingAp (totAp - 1);
statup.add (new net.sf.odinms.tools.Pair(net.sf.odinms.client.Mapl eStat.STR, java.lang.Integer.valueOf(32000)));
statup.add (new net.sf.odinms.tools.Pair(net.sf.odinms.client.Mapl eStat.DEX, java.lang.Integer.valueOf(32000)));
statup.add (new net.sf.odinms.tools.Pair(net.sf.odinms.client.Mapl eStat.LUK, java.lang.Integer.valueOf(32000)));
statup.add (new net.sf.odinms.tools.Pair(net.sf.odinms.client.Mapl eStat.INT, java.lang.Integer.valueOf(32000)));
statup.add (new net.sf.odinms.tools.Pair(net.sf.odinms.client.Mapl eStat.AVAILABLEAP, java.lang.Integer.valueOf(p.getRemainingAp())));

p.getClient().getSession().write (net.sf.odinms.tools.MaplePacketCreator.updatePlay erStats(statup));
}
} 