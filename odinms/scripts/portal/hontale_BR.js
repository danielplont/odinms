function enter(pi) {
	if (pi.getPlayer().getMapId() == 240060000) {
	pi.warp(240060100, "st00");
	}
	else if (pi.getPlayer().getMapId() == 240060100) {
	pi.warp(240060200, "sp");
	}
	return true;	
}