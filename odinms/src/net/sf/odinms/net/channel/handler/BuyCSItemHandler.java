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

package net.sf.odinms.net.channel.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.odinms.client.MapleClient;
import net.sf.odinms.net.AbstractMaplePacketHandler;
import net.sf.odinms.net.MapleServerHandler;
import net.sf.odinms.server.MapleInventoryManipulator;
import net.sf.odinms.tools.MaplePacketCreator;
import net.sf.odinms.tools.data.input.SeekableLittleEndianAccessor;
import net.sf.odinms.server.CashItemFactory;
import net.sf.odinms.server.CashItemInfo;

/**
*
* @author Penguins (Acrylic)
*/
public class BuyCSItemHandler extends AbstractMaplePacketHandler {
	private final static Logger log = LoggerFactory.getLogger(MapleServerHandler.class);
	@Override
	public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
		slea.skip(2);
		int snCS = slea.readInt();
		CashItemInfo item = CashItemFactory.getItem(snCS);
		MapleInventoryManipulator.addById(c, item.getId(), (short) item.getCount(), "Cash Item was purchased.");
		c.getSession().write(MaplePacketCreator.showBoughtCSItem(item.getId()));
		c.getPlayer().modifyCSPoints(0, -item.getPrice());
		c.getSession().write(MaplePacketCreator.showNXMapleTokens(c.getPlayer()));
		c.getSession().write(MaplePacketCreator.enableCSUse0());
		c.getSession().write(MaplePacketCreator.enableCSUse1());
		c.getSession().write(MaplePacketCreator.enableCSUse2());
		c.getSession().write(MaplePacketCreator.enableCSUse3());
		log.info(slea.toString());
	}
}