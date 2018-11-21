var status = 0;
var regcost = 499;
var vipcost = 999;
var goreg = false;
var govip = false;

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
                  cm.sendNext("Welcome. We're the Sleepywood Hotel. Our hotel works hard to serve you the best at all times. If you are tired and worn out from hunting, how about a relaxing stay at our hotel?");
             }
                if (status == 1) {
                    cm.sendSimple("We offer two kinds of rooms for our service. Please choose the one of your liking.\r\n#b#L0#Regular Sauna (" + regcost + " mesos per use)#l\r\n#L1#VIP Sauna (" + vipcost + "mesos per use)");
                    }
                if (status == 2) {
                    if (selection == 0) {
                    cm.sendYesNo("You have chosen the regular sauna. Your HP and MP will recover fast and you can even purchase some items there. Are you sure you want to go in?");
                    goreg = true;
                        }
                    if (selection == 1) {
                    cm.sendYesNo("You've chosen the VIP sauna. Your HP and MP will recover even faster than that of the regular sauna and you can even find a special item in there. Are you sure you want to go in?");
                    govip = true;
                      }
               }
               if (status == 3 && goreg == true) {
                    if (cm.getMeso() >= regcost) {
                        cm.warp(105040401);
                        cm.gainMeso(-regcost);
                        cm.dispose();
                        } else {
                        cm.sendNext("I'm sorry. It looks like you don't have enough mesos. It will cost you at least " + regcost + "mesos to stay at our hotel.");
                        cm.dispose();
                        }
                }
                if (status == 3 && govip == true) {
                      if (cm.getMeso() >= vipcost) {
                        cm.warp(105040402);
                        cm.gainMeso(-vipcost);
                        cm.dispose();
                        } else {
                        cm.sendNext("I'm sorry. It looks like you don't have enough mesos. It will cost you at least " + regcost + "mesos to stay at our hotel.");
                        cm.dispose();
                             }
                     }  
            }
        }  
