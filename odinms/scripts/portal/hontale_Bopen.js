function enter(pi) {
	if (pi.getPlayer().getMapId() == 240050101) {
	pi.warp(240050102, "st00");	
	}
	else if (pi.getPlayer().getMapId() == 240050102) {
	pi.warp(240050103, "st00");
	}	else if (pi.getPlayer().getMapId() == 240050103) {
	pi.warp(240050104, "st00");
	}
	else if (pi.getPlayer().getMapId() == 240050104) {
	pi.warp(240050105, "st00");
	}
	else if (pi.getPlayer().getMapId() == 240050105) {
	pi.warp(240050400, "st00");
	}
	return true;
}
