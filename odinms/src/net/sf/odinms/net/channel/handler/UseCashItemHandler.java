package net.sf.odinms.net.channel.handler;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import net.sf.odinms.client.MapleCharacter;
import net.sf.odinms.client.MapleClient;
import net.sf.odinms.client.MapleInventoryType;
import net.sf.odinms.client.messages.ServernoticeMapleClientMessageCallback;
import net.sf.odinms.net.AbstractMaplePacketHandler;
import net.sf.odinms.net.MaplePacket;
import net.sf.odinms.net.channel.ChannelServer;
import net.sf.odinms.net.world.remote.WorldLocation;
import net.sf.odinms.server.MapleInventoryManipulator;
import net.sf.odinms.server.MaplePortal;
import net.sf.odinms.server.MapleTrade;
import net.sf.odinms.tools.MaplePacketCreator;
import net.sf.odinms.tools.data.input.SeekableLittleEndianAccessor;
import net.sf.odinms.server.MapleItemInformationProvider;
import net.sf.odinms.server.maps.MapleMap;

public class UseCashItemHandler extends AbstractMaplePacketHandler {
	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UseCashItemHandler.class);
	
	@Override
	public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
		@SuppressWarnings("unused")
		byte mode = slea.readByte();
		slea.readByte();
		int itemId = slea.readInt();
		int itemType = itemId/10000;
                
                ChannelServer cserv = c.getChannelServer();
                
		MapleInventoryManipulator.removeById(c, MapleInventoryType.CASH, itemId, 1, true, false);
		try {
			if (itemType == 507){ 
				int megaType = itemId / 1000 % 10;
				if (megaType == 2) {
					c.getChannelServer().getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(3, c.getChannel(), c.getPlayer().getName() +
						" : " + slea.readMapleAsciiString()).getBytes());
				}
			} else if (itemType == 539) {
				List<String> lines = new LinkedList<String>();
				for (int i = 0; i < 4; i++) {
					lines.add(slea.readMapleAsciiString());
				}
				c.getChannelServer().getWorldInterface().broadcastMessage(null, MaplePacketCreator.getAvatarMega(c.getPlayer(), c.getChannel(), itemId, lines).getBytes());
			} else if (itemType == 512) {
				MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();

				c.getPlayer().getMap().startMapEffect(ii.getMsg(itemId).replaceFirst("%s", c.getPlayer().getName()).replaceFirst("%s", slea.readMapleAsciiString()), itemId);
			} else if(itemType == 504){ // vip teleport rock
                                byte rocktype;
				rocktype = slea.readByte();

                                if (rocktype == 0x00) {
        				int mapId = slea.readInt();
                                        MapleMap target = cserv.getMapFactory().getMap(mapId);
                                        MaplePortal targetPortal = target.getPortal(0);
                                       	if (target.getId() != 180000000) {
                        			c.getPlayer().changeMap(target, targetPortal);
                			} else {
                        			MapleInventoryManipulator.addById(c, itemId, (short)1, "Teleport Rock Error (Not found)");
                                		new ServernoticeMapleClientMessageCallback(1, c).dropMessage("Player could not be found.");
                                        	c.getSession().write(MaplePacketCreator.enableActions());
                                        }
                                } else {
        				String name = slea.readMapleAsciiString();
                			MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(name);
                        		if (victim != null) {
                                		MapleMap target = victim.getMap();
                                        	WorldLocation loc = c.getChannelServer().getWorldInterface().getLocation(name);
        					if (loc.map != 180000000) {
                				if (!victim.isHidden()) {
                        			c.getPlayer().changeMap(target, target.findClosestSpawnpoint(victim.getPosition()));
        					}else{
                					MapleInventoryManipulator.addById(c, itemId, (short)1, "Teleport Rock Error (Not found)");
        						new ServernoticeMapleClientMessageCallback(1, c).dropMessage("Player could not be found.");
                					c.getSession().write(MaplePacketCreator.enableActions());
        					}
                				}else{
        						MapleInventoryManipulator.addById(c, itemId, (short)1, "Teleport Rock Error (Can't Teleport)");
                					new ServernoticeMapleClientMessageCallback(1, c).dropMessage("You cannot teleport to this map.");
        						c.getSession().write(MaplePacketCreator.enableActions());
        					}
                			} else {
                        			MapleInventoryManipulator.addById(c, itemId, (short)1, "Teleport Rock Error (Not found)");
                                		new ServernoticeMapleClientMessageCallback(1, c).dropMessage("Player could not be found.");
                                        	c.getSession().write(MaplePacketCreator.enableActions());
                                        }
                                }
			}
		} catch (RemoteException e) {
			c.getChannelServer().reconnectWorld();
			log.error("REOTE TRHOW", e);
		}
	}
}