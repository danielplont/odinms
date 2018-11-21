importPackage(net.sf.odinms.client);

var wui = 0;

function start() {
    cm.sendYesNo("Do you wish to go out from here ?");
}

function action(mode, type, selection) {
    if (mode == 0 || wui == 1) {
        cm.dispose();
    } else {
        wui = 1;                
                cm.sendOk ("Alright see you next time.");
        cm.warp(220080000,"sp");
    }
}