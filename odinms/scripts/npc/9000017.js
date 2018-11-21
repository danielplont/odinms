/*
	This file is part of the DuckStory Maple Story Server
    Copyright (C) 2008 Dhaniram Heera  
                       Zirak Chowdhry 
                       Waqar Ahmed 

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation. You may not use, modify
    or distribute this program under any other version of the
    GNU Affero General Public License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see .
*/

/* Coco
	Fame Seller (10mil mesos per fame)
*/

var wui = 0;
var price = 10000000;
var fame = 1

function start() {
if (cm.getMeso() >= price) 
{
	cm.sendYesNo ("I sell #fUI/UIWindow.img/QuestIcon/6/0# Do you want to buy some #fUI/UIWindow.img/QuestIcon/6/0# to look as sexy as me you will need to give up 10mil mesos in order to get fame.");
	cm.gainFame(+fame);
	cm.gainMeso(-price);
	} else {
			cm.sendOk("Sorry you don't have enough mesos to buy a fame.");
			cm.dispose();
}
}