var mob;

function act() {
    rm.mapMessage(5, "Horntail has appeared from the depths of his cave.");
    rm.changeMusic("Bgm14/HonTale");
    mob=rm.spawnMonster(8810026, 76, 260);
    rm.killMonster(mob,false);
    rm.createMapMonitor(2,240050400,"sp","8810010,8810011,8810012,8810013,8810014,8810015,8810016,8810017");
    //the map id and portal name is choosen randomly here, so u guys change accordingly
}  