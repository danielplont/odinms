/*
	This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
                       Matthias Butz <matze@odinms.de>
                       Jan Christian Meyer <vimes@odinms.de>

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
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package net.sf.odinms.server.life;

import net.sf.odinms.client.MapleClient;
import net.sf.odinms.server.MapleShop;
import net.sf.odinms.server.MapleShopFactory;
import net.sf.odinms.server.maps.MapleMapObjectType;
import net.sf.odinms.tools.MaplePacketCreator;

public class MapleNPC extends AbstractLoadedMapleLife {
	private MapleNPCStats stats;
	private boolean custom = false;
	
	public MapleNPC(int id, MapleNPCStats stats) {
		super(id);
		this.stats = stats;
	}
	
	public boolean hasShop() {
		return MapleShopFactory.getInstance().getShopForNPC(getId()) != null;
	}
	
	public void sendShop(MapleClient c) {
		MapleShop shop = MapleShopFactory.getInstance().getShopForNPC(getId());
		shop.sendShop(c);
	}
	
	@Override
	public void sendSpawnData(MapleClient client) {
	if(this.getId() == 9250045 || this.getId() == 9270000 || this.getId() == 9250023 || this.getId() == 9270007 || this.getId() == 9250024 || this.getId() == 9250044 || this.getId() == 9250025 || this.getId() == 9250043 || this.getId() == 9270004 || this.getId() == 9270005 || this.getId() == 9270001 || this.getId() == 9250026 || this.getId() == 9270006 || this.getId() == 9270012 || this.getId() == 9201066 || this.getId() == 9270003 || this.getId() == 9250046 || this.getId() == 9270013 || this.getId() == 9270002) {return;}
		client.getSession().write(MaplePacketCreator.spawnNPC(this, false));
		client.getSession().write(MaplePacketCreator.spawnNPC(this, true));//Show NPC Text?
	}
	
	@Override
	public void sendDestroyData(MapleClient client) {
		throw new UnsupportedOperationException();
	}

	@Override
	public MapleMapObjectType getType() {
		return MapleMapObjectType.NPC;
	}
	
	public String getName() {
		return stats.getName();
	}

	public boolean isCustom() {
		return custom;
	}

	public void setCustom(boolean custom) {
		this.custom = custom;
	}
}
