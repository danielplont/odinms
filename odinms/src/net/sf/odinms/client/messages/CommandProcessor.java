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

package net.sf.odinms.client.messages;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Calendar;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import net.sf.odinms.client.IItem;
import net.sf.odinms.client.Item;
import net.sf.odinms.client.MapleCharacter;
import net.sf.odinms.client.MapleCharacterUtil;
import net.sf.odinms.client.MapleClient;
import net.sf.odinms.client.MapleInventoryType;
import net.sf.odinms.client.MapleJob;
import net.sf.odinms.client.MapleStat;
import net.sf.odinms.client.MapleInventory;
import net.sf.odinms.client.SkillFactory;
import net.sf.odinms.client.anticheat.CheatingOffense;
import net.sf.odinms.database.DatabaseConnection;
import net.sf.odinms.net.ExternalCodeTableGetter;
import net.sf.odinms.net.MaplePacket;
import net.sf.odinms.net.PacketProcessor;
import net.sf.odinms.scripting.event.EventInstanceManager;
import net.sf.odinms.net.RecvPacketOpcode;
import net.sf.odinms.net.SendPacketOpcode;
import net.sf.odinms.net.channel.ChannelServer;
import net.sf.odinms.net.channel.handler.GeneralchatHandler;
import net.sf.odinms.net.world.remote.CheaterData;
import net.sf.odinms.net.world.remote.WorldLocation;
import net.sf.odinms.scripting.portal.PortalScriptManager;
import net.sf.odinms.scripting.reactor.ReactorScriptManager;
import net.sf.odinms.server.MapleInventoryManipulator;
import net.sf.odinms.server.MapleItemInformationProvider;
import net.sf.odinms.server.MaplePortal;
import net.sf.odinms.server.MapleShop;
import net.sf.odinms.server.MapleShopFactory;
import net.sf.odinms.server.MapleTrade;
import net.sf.odinms.server.ShutdownServer;
import net.sf.odinms.server.TimerManager;
import net.sf.odinms.server.life.MapleLifeFactory;
import net.sf.odinms.server.life.MapleMonster;
import net.sf.odinms.server.life.MapleMonsterInformationProvider;
import net.sf.odinms.server.life.MapleMonsterStats;
import net.sf.odinms.server.life.MapleNPC;
import net.sf.odinms.server.maps.MapleDoor;
import net.sf.odinms.server.maps.MapleMap;
import net.sf.odinms.server.maps.MapleMapObject;
import net.sf.odinms.server.maps.MapleMapObjectType;
import net.sf.odinms.server.quest.MapleQuest;
import net.sf.odinms.tools.HexTool;
import net.sf.odinms.tools.MaplePacketCreator;
import net.sf.odinms.tools.MockIOSession;
import net.sf.odinms.tools.Pair;
import net.sf.odinms.tools.StringUtil;
import net.sf.odinms.tools.data.output.MaplePacketLittleEndianWriter;
import net.sf.odinms.server.life.SpawnPoint;
import net.sf.odinms.server.maps.MapleMapFactory;
import net.sf.odinms.server.maps.MapleMapItem;
import net.sf.odinms.server.maps.MapleReactor;
import net.sf.odinms.server.maps.MapleReactorFactory;
import net.sf.odinms.server.maps.MapleReactorStats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandProcessor implements CommandProcessorMBean {
	private static CommandProcessor instance = new CommandProcessor();
	private static final Logger log = LoggerFactory.getLogger(GeneralchatHandler.class);
	private static List<Pair<MapleCharacter,String>> gmlog = new LinkedList<Pair<MapleCharacter,String>>();
	private static Runnable persister;
	final private static String[] reasons = {"Hacking", "Botting", "Scamming", "Fake GM", "Harassment", "Advertising"};

	static {
		persister = new PersistingTask();
		TimerManager.getInstance().register(persister, 62000);
	}
	
	private CommandProcessor() {
		// hidden singleton so we can become managable
	}
	
	public static class PersistingTask implements Runnable {
		@Override
		public void run() {
			synchronized (gmlog) {
				Connection con = DatabaseConnection.getConnection();
				try {
					PreparedStatement ps = con.prepareStatement("INSERT INTO gmlog (cid, command) VALUES (?, ?)");
					for (Pair<MapleCharacter,String> logentry : gmlog) {
						ps.setInt(1, logentry.getLeft().getId());
						ps.setString(2, logentry.getRight());
						ps.executeUpdate();
					}
					ps.close();
				} catch (SQLException e) {
					log.error("error persisting cheatlog", e);
				}
				gmlog.clear();
			}
		}
	}
	
	public static void registerMBean() {
		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		try {
			mBeanServer.registerMBean(instance, new ObjectName("net.sf.odinms.client.messages:name=CommandProcessor"));
		} catch (Exception e) {
			log.error("Error registering CommandProcessor MBean");
		}
	}
	
	private static int getNoticeType(String typestring) {
		if (typestring.equals("n")) {
			return 0;
		} else if (typestring.equals("p")) {
			return 1;
		} else if (typestring.equals("l")) {
			return 2;
		} else if (typestring.equals("nv")) {
			return 5;
		} else if (typestring.equals("v")) {
			return 5;
		} else if (typestring.equals("b")) {
			return 6;
		}
		return -1;
	}

              	private static String joinAfterString(String splitted[], String str) {
		for (int i = 1; i < splitted.length; i++) {
			if (splitted[i].equalsIgnoreCase(str) && i + 1 < splitted.length) {
                            return StringUtil.joinStringFrom(splitted, i+1);
			}
		}
		return null;
	}
        
	private static int getOptionalIntArg(String splitted[], int position, int def) {
		if (splitted.length > position) {
			try {
				return Integer.parseInt(splitted[position]);
			} catch (NumberFormatException nfe) {
				return def;
			}
		}
		return def;
	}

	private static String getNamedArg(String splitted[], int startpos, String name) {
		for (int i = startpos; i < splitted.length; i++) {
			if (splitted[i].equalsIgnoreCase(name) && i + 1 < splitted.length) {
				return splitted[i + 1];
			}
		}
		return null;
	}

	private static Integer getNamedIntArg(String splitted[], int startpos, String name) {
		String arg = getNamedArg(splitted, startpos, name);
		if (arg != null) {
			try {
				return Integer.parseInt(arg);
			} catch (NumberFormatException nfe) {
				// swallow - we don't really care
			}
		}
		return null;
	}
	
	private static int getNamedIntArg(String splitted[], int startpos, String name, int def) {
		Integer ret = getNamedIntArg(splitted, startpos, name);
		if (ret == null) {
			return def;
		}
		return ret.intValue();
	}

	private static Double getNamedDoubleArg(String splitted[], int startpos, String name) {
		String arg = getNamedArg(splitted, startpos, name);
		if (arg != null) {
			try {
				return Double.parseDouble(arg);
			} catch (NumberFormatException nfe) {
				// swallow - we don't really care
			}
		}
		return null;
	}

	public static boolean processCommand(MapleClient c, String line) {
		return processCommandInternal(c, new ServernoticeMapleClientMessageCallback(c), c.getPlayer().isGM(), line);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.odinms.client.messages.CommandProcessorMBean#processCommandJMX(int, int, java.lang.String)
	 */
	public String processCommandJMX(int cserver, int mapid, String command) {
		ChannelServer cserv = ChannelServer.getInstance(cserver);
		if (cserv == null) {
			return "The specified channel Server does not exist in this serverprocess";
		}
		MapleClient c = new MapleClient(null, null, new MockIOSession());
		MapleCharacter chr = MapleCharacter.getDefault(c, 26023);
		c.setPlayer(chr);
		chr.setName("/---------jmxuser-------------\\"); // (name longer than maxmimum length)
		MapleMap map = cserv.getMapFactory().getMap(mapid);
		if (map != null) {
			chr.setMap(map);
			SkillFactory.getSkill(5101004).getEffect(1).applyTo(chr);
			map.addPlayer(chr);
		}
		cserv.addPlayer(chr);
		MessageCallback mc = new StringMessageCallback();
		try {
			processCommandInternal(c, mc, true, command);
		} finally {
			if (map != null) {
				map.removePlayer(chr);
			}
			cserv.removePlayer(chr);
		}
		return mc.toString();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.odinms.client.messages.CommandProcessorMBean#processCommandInstance(net.sf.odinms.client.MapleClient, java.lang.String)
	 */
	private static boolean processCommandInternal(MapleClient c, MessageCallback mc, boolean isGM, String line) {
		MapleCharacter player = c.getPlayer();
		ChannelServer cserv = c.getChannelServer();
		if (line.charAt(0) == '!' && isGM) {
			synchronized (gmlog) {
				gmlog.add(new Pair<MapleCharacter, String>(player, line));
			}
			log.warn("{} used a GM command: {}", c.getPlayer().getName(), line);
			String[] splitted = line.split(" ");
			if (splitted[0].equals("!map")) {
				int mapid = Integer.parseInt(splitted[1]);
				MapleMap target = cserv.getMapFactory().getMap(mapid);
				MaplePortal targetPortal = null;
				if (splitted.length > 2) {
					try {
						targetPortal = target.getPortal(Integer.parseInt(splitted[2]));
					} catch (IndexOutOfBoundsException ioobe) {
						// noop, assume the gm didn't know how many portals there are
					} catch (NumberFormatException nfe) {
						// noop, assume that the gm is drunk
					}
				}
				if (targetPortal == null) {
					targetPortal = target.getPortal(0);
				}
				player.changeMap(target, targetPortal);
			} else if (splitted[0].equals("!jail")) {
				MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
				int mapid = 200090300; // mulung ride
				if (splitted.length > 2 && splitted[1].equals("2")) {
					mapid = 980000404; // exit for CPQ; not used
					victim = cserv.getPlayerStorage().getCharacterByName(splitted[2]);
				}
				if (victim != null) {
					MapleMap target = cserv.getMapFactory().getMap(mapid);
					MaplePortal targetPortal = target.getPortal(0);
					victim.changeMap(target, targetPortal);
					mc.dropMessage(victim.getName() + " was jailed!");
				} else {
					mc.dropMessage(splitted[1] + " not found!");
				}
			} else if (splitted[0].equals("!lolcastle")) {
				if (splitted.length != 2) {
					mc.dropMessage("Syntax: !lolcastle level (level = 1-5)");
				}
				MapleMap target = c.getChannelServer().getEventSM().getEventManager("lolcastle").getInstance("lolcastle" + splitted[1]).getMapFactory().getMap(990000300, false, false);
				player.changeMap(target, target.getPortal(0));
			} else if (splitted[0].equals("!warp")) {
				MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
				if (victim != null) {
					if (splitted.length == 2) {
						MapleMap target = victim.getMap();
						c.getPlayer().changeMap(target, target.findClosestSpawnpoint(victim.getPosition()));
					} else {
						int mapid = Integer.parseInt(splitted[2]);
						MapleMap target = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(mapid);
						victim.changeMap(target, target.getPortal(0));
					}
				} else {
					try {
						victim = c.getPlayer();
						WorldLocation loc = c.getChannelServer().getWorldInterface().getLocation(splitted[1]);
						if (loc != null) {
							mc.dropMessage("You will be cross-channel warped. This may take a few seconds.");
							//WorldLocation loc = new WorldLocation(40000, 2);
							MapleMap target = c.getChannelServer().getMapFactory().getMap(loc.map);
							c.getPlayer().cancelAllBuffs();
							String ip = c.getChannelServer().getIP(loc.channel);
							c.getPlayer().getMap().removePlayer(c.getPlayer());
							victim.setMap(target);
							String[] socket = ip.split(":");
							if (c.getPlayer().getTrade() != null) {
								MapleTrade.cancelTrade(c.getPlayer());
							}
							c.getPlayer().saveToDB(true);
							if (c.getPlayer().getCheatTracker() != null)
								c.getPlayer().getCheatTracker().dispose();
							ChannelServer.getInstance(c.getChannel()).removePlayer(c.getPlayer());
							c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION);
							try {
								MaplePacket packet = MaplePacketCreator.getChannelChange(
									InetAddress.getByName(socket[0]), Integer.parseInt(socket[1]));
								c.getSession().write(packet);
							} catch (Exception e) {
								throw new RuntimeException(e);
							}
						} else {
							int map = Integer.parseInt(splitted[1]);
							MapleMap target = cserv.getMapFactory().getMap(map);
							player.changeMap(target, target.getPortal(0));
						}
					} catch (/*Remote*/Exception e) {
						mc.dropMessage("Something went wrong " + e.getMessage());
					}
				}
			}  else if (splitted[0].equals("!toggleoffense")) {
				try {
					CheatingOffense co = CheatingOffense.valueOf(splitted[1]);
					co.setEnabled(!co.isEnabled());
				} catch (IllegalArgumentException iae) {
					mc.dropMessage("Offense " + splitted[1] + " not found");
				}
	
				} else if (splitted[0].equals("!warphere")) {
				MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
				victim.changeMap(c.getPlayer().getMap(), c.getPlayer().getMap().findClosestSpawnpoint(
					c.getPlayer().getPosition()));
			} else if (splitted[0].equals("!spawn")) {
				int mid = Integer.parseInt(splitted[1]);
				int num = Math.min(getOptionalIntArg(splitted, 2, 1), 500);

				if (mid == 9400203) {
					log.info(MapleClient.getLogMessage(player, "Trying to spawn a silver slime"));
					return true;
				}

				Integer hp = getNamedIntArg(splitted, 1, "hp");
				Integer exp = getNamedIntArg(splitted, 1, "exp");
				Double php = getNamedDoubleArg(splitted, 1, "php");
				Double pexp = getNamedDoubleArg(splitted, 1, "pexp");

				MapleMonster onemob = MapleLifeFactory.getMonster(mid);

				int newhp = 0;
				int newexp = 0;

				double oldExpRatio = ((double) onemob.getHp() / onemob.getExp());

				if (hp != null) {
					newhp = hp.intValue();
				} else if (php != null) {
					newhp = (int) (onemob.getMaxHp() * (php.doubleValue() / 100));
				} else {
					newhp = onemob.getMaxHp();
				}
				if (exp != null) {
					newexp = exp.intValue();
				} else if (pexp != null) {
					newexp = (int) (onemob.getExp() * (pexp.doubleValue() / 100));
				} else {
					newexp = onemob.getExp();
				}

				if (newhp < 1) {
					newhp = 1;
				}
				double newExpRatio = ((double) newhp / newexp);
				if (newExpRatio < oldExpRatio && newexp > 0) {
					mc.dropMessage("The new hp/exp ratio is better than the old one. (" + newExpRatio + " < " +
						oldExpRatio + ") Please don't do this");
					return true;
				}
				
				MapleMonsterStats overrideStats = new MapleMonsterStats();
				overrideStats.setHp(newhp);
				overrideStats.setExp(newexp);
				overrideStats.setMp(onemob.getMaxMp());
				
				for (int i = 0; i < num; i++) {
					MapleMonster mob = MapleLifeFactory.getMonster(mid);
					mob.setHp(newhp);
					mob.setOverrideStats(overrideStats);
					c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob, c.getPlayer().getPosition());
				}
			} else if (splitted[0].equals("!servermessage")) {
				ChannelServer.getInstance(c.getChannel()).setServerMessage(StringUtil.joinStringFrom(splitted, 1));
			} else if (splitted[0].equals("!array")) {
				mc.dropMessage("Array");
			} else if (splitted[0].equals("!notice")) {
				int joinmod = 1;

				int range = -1;
				if (splitted[1].equals("m")) {
					range = 0;
				} else if (splitted[1].equals("c")) {
					range = 1;
				} else if (splitted[1].equals("w")) {
					range = 2;
				}

				int tfrom = 2;
				if (range == -1) {
					range = 2;
					tfrom = 1;
				}
				int type = getNoticeType(splitted[tfrom]);
				if (type == -1) {
					type = 0;
					joinmod = 0;
				}
				String prefix = "";
				if (splitted[tfrom].equals("nv")) {
					prefix = "[Notice] ";
				}
				joinmod += tfrom;
				MaplePacket packet = MaplePacketCreator.serverNotice(type, prefix +
					StringUtil.joinStringFrom(splitted, joinmod));
				if (range == 0) {
					c.getPlayer().getMap().broadcastMessage(packet);
				} else if (range == 1) {
					ChannelServer.getInstance(c.getChannel()).broadcastPacket(packet);
				} else if (range == 2) {
					try {
						ChannelServer.getInstance(c.getChannel()).getWorldInterface().broadcastMessage(
							c.getPlayer().getName(), packet.getBytes());
					} catch (RemoteException e) {
						c.getChannelServer().reconnectWorld();
					}
				}
			} else if (splitted[0].equals("!say")) {
                            if (splitted.length > 1) { 
                                MaplePacket packet = MaplePacketCreator.serverNotice(6, "[" + c.getPlayer().getName() + "] " + StringUtil.joinStringFrom(splitted, 1));
                                try {
                                    ChannelServer.getInstance(c.getChannel()).getWorldInterface().broadcastMessage(
                                    c.getPlayer().getName(), packet.getBytes());
				} catch (RemoteException e) {
                                    c.getChannelServer().reconnectWorld();
				}
                            } else {
                                mc.dropMessage("Syntax: !say <message>");
                            }
			} else if (splitted[0].equals("!job")) {
				c.getPlayer().changeJob(MapleJob.getById(Integer.parseInt(splitted[1])));
			} else if (splitted[0].equals("!clock")) {
				player.getMap().broadcastMessage(MaplePacketCreator.getClock(getOptionalIntArg(splitted, 1, 60)));
			} else if (splitted[0].equals("!pill")) {
				MapleInventoryManipulator.addById(c, 2002009, (short) 5, c.getPlayer().getName() + " used !pill", player.getName());
			} else if (splitted[0].equals("!item")) {
				short quantity = (short) getOptionalIntArg(splitted, 2, 1);
				MapleInventoryManipulator.addById(c, Integer.parseInt(splitted[1]), quantity, c.getPlayer().getName() +
					"used !item with quantity " + quantity, player.getName());
			} else if (splitted[0].equals("!drop")) {
				MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
				int itemId = Integer.parseInt(splitted[1]);
				short quantity = (short) (short) getOptionalIntArg(splitted, 2, 1);
				IItem toDrop;
				if (ii.getInventoryType(itemId) == MapleInventoryType.EQUIP)
					toDrop = ii.getEquipById(itemId);
				else
					toDrop = new Item(itemId, (byte) 0, (short) quantity);
				StringBuilder logMsg = new StringBuilder("Created by ");
				logMsg.append(c.getPlayer().getName());
				logMsg.append(" using !drop. Quantity: ");
				logMsg.append(quantity);
				toDrop.log(logMsg.toString(), false);
				toDrop.setOwner(player.getName());
				c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), toDrop, c.getPlayer().getPosition(), true,true);
			} else if (splitted[0].equals("!shop")) {
				MapleShopFactory sfact = MapleShopFactory.getInstance();
				MapleShop shop = sfact.getShop(1);
				shop.sendShop(c);
			} else if (splitted[0].equals("!equip")) {
				MapleShopFactory sfact = MapleShopFactory.getInstance();
				MapleShop shop = sfact.getShop(2);
				shop.sendShop(c);
			} else if (splitted[0].equals("!gmshop")) {
				MapleShopFactory sfact = MapleShopFactory.getInstance();
				MapleShop shop = sfact.getShop(1337);
				shop.sendShop(c);
                        } else if (splitted[0].equals("!cleardrops")){
                            MapleMap map = c.getPlayer().getMap();
                            double range = Double.POSITIVE_INFINITY;
                            List<MapleMapObject> items = map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(MapleMapObjectType.ITEM));
                            for (MapleMapObject itemmo : items) {
                            map.removeMapObject(itemmo);
                            map.broadcastMessage(MaplePacketCreator.removeItemFromMap(itemmo.getObjectId(), 0, c.getPlayer().getId()));
                                }
                            mc.dropMessage("You have destroyed " + items.size() + " items on the ground.");  
                        } else if (splitted[0].equals("!clearReactorDrops")) {
				ReactorScriptManager.getInstance().clearDrops();
			} else if (splitted[0].equals("!clearshops")) {
				MapleShopFactory.getInstance().clear(); 
} else if (splitted[0].equals("!kill")) {
                                MapleCharacter victim1 = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
    victim1.setHp(0);
    victim1.setMp(0);
    victim1.updateSingleStat(MapleStat.HP, 0);
    victim1.updateSingleStat(MapleStat.MP, 0);
                                MapleCharacter victim2 = cserv.getPlayerStorage().getCharacterByName(splitted[2]);
    victim2.setHp(0);
    victim2.setMp(0);
    victim2.updateSingleStat(MapleStat.HP, 0);
    victim2.updateSingleStat(MapleStat.MP, 0);
                                MapleCharacter victim3 = cserv.getPlayerStorage().getCharacterByName(splitted[3]);
    victim3.setHp(0);
    victim3.setMp(0);
    victim3.updateSingleStat(MapleStat.HP, 0);
    victim3.updateSingleStat(MapleStat.MP, 0);
                                MapleCharacter victim4 = cserv.getPlayerStorage().getCharacterByName(splitted[4]);
    victim4.setHp(0);
    victim4.setMp(0);
    victim4.updateSingleStat(MapleStat.HP, 0);
    victim4.updateSingleStat(MapleStat.MP, 0);
                                MapleCharacter victim5 = cserv.getPlayerStorage().getCharacterByName(splitted[5]);
    victim5.setHp(0);
    victim5.setMp(0);
    victim5.updateSingleStat(MapleStat.HP, 0);
    victim5.updateSingleStat(MapleStat.MP, 0);
                                MapleCharacter victim6 = cserv.getPlayerStorage().getCharacterByName(splitted[6]);
    victim6.setHp(0);
    victim6.setMp(0);
    victim6.updateSingleStat(MapleStat.HP, 0);
    victim6.updateSingleStat(MapleStat.MP, 0);
   }
     else if (splitted[0].equals("!levelperson")) {
    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);  
                             victim.setLevel(getOptionalIntArg(splitted, 2, 2));
    victim.levelUp();
     int newexp = victim.getExp();
     if (newexp < 0) {
      victim.gainExp(-newexp, false, false);
	}
	} else if (splitted[0].equals("!listreports")) {
                            ResultSet rs = getReports();
                            try {
                                while(rs.next()){
                                    mc.dropMessage("id: " + rs.getInt("id") + " | time reported: " + rs.getTimestamp("reporttime").toString() + " | reason: " + reasons[rs.getByte("reason")]);
                                }
                            } catch(Exception ex) {}
                        } else if (splitted[0].equals("!getreport")) {
                            if(splitted.length != 2) return true;
                            int reportid = Integer.parseInt(splitted[1]);
                            ResultSet rs = getReport(reportid);
                            try {
                                while(rs.next()){
                                    mc.dropMessage("id: " + rs.getInt("id") + " | time reported: " + rs.getTimestamp("reporttime").toString() + " | reason: " + reasons[rs.getByte("reason")]);
                                    mc.dropMessage("reporter charid: " + rs.getInt("reporterid"));
                                    mc.dropMessage("victim charid: " + rs.getInt("victimid"));
                                    mc.dropMessage("chatlog: ");
                                    mc.dropMessage(rs.getString("chatlog"));
                                    mc.dropMessage("Status: " + rs.getString("status"));
                                    
                                }
                            } catch(Exception ex){}
                        } else if (splitted[0].equals("!delreport")) {   
                            if(splitted.length != 2) return true;
                            int reportid = Integer.parseInt(splitted[1]);
                            deleteReport(reportid);
                        } else if (splitted[0].equals("!setreportstatus")) {  
                             if(splitted.length < 3) return true;
                            int reportid = Integer.parseInt(splitted[1]);
                            String status = StringUtil.joinStringFrom(splitted, 2);
                            setReportStatus(reportid, status);
                        } else if (splitted[0].equals("!getnamebyid")) {
                            if(splitted.length != 2) return true;
                            int cid = Integer.parseInt(splitted[1]);
                            mc.dropMessage(getCharInfoById(cid));
                        } else if (splitted[0].equals("!getnamebyid")) {
                            if(splitted.length != 2) return true;
                            int cid = Integer.parseInt(splitted[1]);
                            mc.dropMessage(getCharInfoById(cid));
		 }else if (splitted[0].equals("!online")) { // testing
				MessageCallback callback = new ServernoticeMapleClientMessageCallback(c);
				StringBuilder builder = new StringBuilder("Characters online: ");
				for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
					if (builder.length() > 150) { 
						builder.setLength(builder.length() - 2);
						callback.dropMessage(builder.toString());
						builder = new StringBuilder();
					}
					builder.append(MapleCharacterUtil.makeMapleReadable(chr.getName()));
					builder.append(", ");
				}
				builder.setLength(builder.length() - 2);
				c.getSession().write(MaplePacketCreator.serverNotice(6, builder.toString()));
} else if (splitted[0].equals("!clearevents")) {
				for (ChannelServer instance : ChannelServer.getAllInstances()) {
					instance.reloadEvents();
				}
                        }else if(splitted[0].equalsIgnoreCase("!showMonsterID"))
        {
            MapleMap map = player.getMap();
            double range = Double.POSITIVE_INFINITY;
            List<MapleMapObject> monsters = map.getMapObjectsInRange(player.getPosition(), range, Arrays
                .asList(MapleMapObjectType.MONSTER));
            for (MapleMapObject monstermo : monsters) {
                MapleMonster monster = (MapleMonster) monstermo;
                String alive="false";
                if(monster.isAlive())alive="true";
                mc.dropMessage("name="+monster.getName()+" ID="+monster.getId()+" isAlive="+alive);
            }
        }
	else if (splitted[0].equals("!showPortalName"))
        {
            final MaplePortal portal = player.getMap().findClosestSpawnpoint(player.getPosition());
            mc.dropMessage(portal.getName() + " id: " + portal.getId() + " script: " + portal.getScriptName()+" name: "+portal.getName());
        } else if (splitted[0].equals("!level")) {  //By PurpleMadness
                            c.getPlayer().setLevel(getOptionalIntArg(splitted, 1, 1));
				c.getPlayer().levelUp();
				int newexp = c.getPlayer().getExp();
				if (newexp < 0) {
					c.getPlayer().gainExp(-newexp, false, false);
                             	}
                        } else if (splitted[0].equals("!getnamebyid")) {
                            if(splitted.length != 2) return true;
                            int cid = Integer.parseInt(splitted[1]);
                            mc.dropMessage(getCharInfoById(cid));

				}else if (splitted[0].equals("!exprate")) { // by Redline/2azn4u
				if (splitted.length > 1) {
					int exp = Integer.parseInt(splitted[1]);
					cserv.setExpRate(exp);
					MaplePacket packet = MaplePacketCreator.serverNotice(6, "Exp Rate has been changed to " + exp + "x");
					ChannelServer.getInstance(c.getChannel()).broadcastPacket(packet);
				} else 
					mc.dropMessage("Syntax: !exprate <number>");
                                
                        } 
				else if (splitted[0].equals("!music")) {
                                if (splitted[1].equals("names") && splitted.length == 2) {
                                        mc.dropMessage("Names are case sensitive!");
                                        mc.dropMessage("Use !song names [Folder Name] to get list of songs.");
                                        mc.dropMessage("Folder Names:");
                                        mc.dropMessage("Bgm00 | Bgm01 | Bgm02 | Bgm03 | Bgm04");
                                        mc.dropMessage("Bgm05 | Bgm06 | Bgm07 | Bgm08 | Bgm09");
                                        mc.dropMessage("Bgm10 | Bgm11 | Bgm12 | Bgm13 | Bgm14");
                                        mc.dropMessage("    Bgm15 | BgmEvent | BgmGL | BgmJp");
                                } else if (splitted[1].equals("names") && splitted.length == 3) {
                                        if (splitted[2].equals("Bgm00")) {
                                            mc.dropMessage("Bgm00/SleepyWood");
                                            mc.dropMessage("Bgm00/FloralLife");
                                            mc.dropMessage("Bgm00/GoPicnic");
                                            mc.dropMessage("Bgm00/Nightmare");
                                            mc.dropMessage("Bgm00/RestNPeace");
                                        } else if (splitted[2].equals("Bgm01")) {
                                            mc.dropMessage("Bgm01/AncientMove");
                                            mc.dropMessage("Bgm01/MoonlightShadow");
                                            mc.dropMessage("Bgm01/WhereTheBarlogFrom");
                                            mc.dropMessage("Bgm01/CavaBien");
                                            mc.dropMessage("Bgm01/HighlandStar");
                                            mc.dropMessage("Bgm01/BadGuys");
                                        } else if (splitted[2].equals("Bgm02")) {
                                            mc.dropMessage("Bgm02/MissingYou");
                                            mc.dropMessage("Bgm02/WhenTheMorningComes");
                                            mc.dropMessage("Bgm02/EvilEyes");
                                            mc.dropMessage("Bgm02/JungleBook");
                                            mc.dropMessage("Bgm02/AboveTheTreetops");
                                        } else if (splitted[2].equals("Bgm03")) {
                                            mc.dropMessage("Bgm03/Subway");
                                            mc.dropMessage("Bgm03/Elfwood");
                                            mc.dropMessage("Bgm03/BlueSky");
                                            mc.dropMessage("Bgm03/Beachway");
                                            mc.dropMessage("Bgm03/SnowyVillage");
                                        } else if (splitted[2].equals("Bgm04")) {
                                            mc.dropMessage("Bgm04/PlayWithMe");
                                            mc.dropMessage("Bgm04/WhiteChristmas");
                                            mc.dropMessage("Bgm04/UponTheSky");
                                            mc.dropMessage("Bgm04/ArabPirate");
                                            mc.dropMessage("Bgm04/Shinin'Harbor");
                                            mc.dropMessage("Bgm04/WarmRegard");
                                        } else if (splitted[2].equals("Bgm05")) {
                                            mc.dropMessage("Bgm05/WolfWood");
                                            mc.dropMessage("Bgm05/DownToTheCave");
                                            mc.dropMessage("Bgm05/AbandonedMine");
                                            mc.dropMessage("Bgm05/MineQuest");
                                            mc.dropMessage("Bgm05/HellGate");
                                        } else if (splitted[2].equals("Bgm06")) {
                                            mc.dropMessage("Bgm06/FinalFight");
                                            mc.dropMessage("Bgm06/WelcomeToTheHell");
                                            mc.dropMessage("Bgm06/ComeWithMe");
                                            mc.dropMessage("Bgm06/FlyingInABlueDream");
                                            mc.dropMessage("Bgm06/FantasticThinking");
                                        } else if (splitted[2].equals("Bgm07")) {
                                            mc.dropMessage("Bgm07/WaltzForWork");
                                            mc.dropMessage("Bgm07/WhereverYouAre");
                                            mc.dropMessage("Bgm07/FunnyTimeMaker");
                                            mc.dropMessage("Bgm07/HighEnough");
                                            mc.dropMessage("Bgm07/Fantasia");
                                        } else if (splitted[2].equals("Bgm08")) {
                                            mc.dropMessage("Bgm08/LetsMarch");
                                            mc.dropMessage("Bgm08/ForTheGlory");
                                            mc.dropMessage("Bgm08/FindingForest");
                                            mc.dropMessage("Bgm08/LetsHuntAliens");
                                            mc.dropMessage("Bgm08/PlotOfPixie");
                                        } else if (splitted[2].equals("Bgm09")) {
                                            mc.dropMessage("Bgm09/DarkShadow");
                                            mc.dropMessage("Bgm09/TheyMenacingYou");
                                            mc.dropMessage("Bgm09/FairyTale");
                                            mc.dropMessage("Bgm09/FairyTalediffvers");
                                            mc.dropMessage("Bgm09/TimeAttack");
                                        } else if (splitted[2].equals("Bgm10")) {
                                            mc.dropMessage("Bgm10/Timeless");
                                            mc.dropMessage("Bgm10/TimelessB");
                                            mc.dropMessage("Bgm10/BizarreTales");
                                            mc.dropMessage("Bgm10/TheWayGrotesque");
                                            mc.dropMessage("Bgm10/Eregos");
                                        } else if (splitted[2].equals("Bgm11")) {
                                            mc.dropMessage("Bgm11/BlueWorld");
                                            mc.dropMessage("Bgm11/Aquarium");
                                            mc.dropMessage("Bgm11/ShiningSea");
                                            mc.dropMessage("Bgm11/DownTown");
                                            mc.dropMessage("Bgm11/DarkMountain");
                                        } else if (splitted[2].equals("Bgm12")) {
                                            mc.dropMessage("Bgm12/AquaCave");
                                            mc.dropMessage("Bgm12/DeepSee");
                                            mc.dropMessage("Bgm12/WaterWay");
                                            mc.dropMessage("Bgm12/AcientRemain");
                                            mc.dropMessage("Bgm12/RuinCastle");
                                            mc.dropMessage("Bgm12/Dispute");
                                        } else if (splitted[2].equals("Bgm13")) {
                                            mc.dropMessage("Bgm13/CokeTown");
                                            mc.dropMessage("Bgm13/Leafre");
                                            mc.dropMessage("Bgm13/Minar'sDream");
                                            mc.dropMessage("Bgm13/AcientForest");
                                            mc.dropMessage("Bgm13/TowerOfGoddess");
                                        } else if (splitted[2].equals("Bgm14")) {
                                            mc.dropMessage("Bgm14/DragonLoad");
                                            mc.dropMessage("Bgm14/HonTale");
                                            mc.dropMessage("Bgm14/CaveOfHontale");
                                            mc.dropMessage("Bgm14/DragonNest");
                                            mc.dropMessage("Bgm14/Ariant");
                                            mc.dropMessage("Bgm14/HotDesert");
                                        } else if (splitted[2].equals("Bgm15")) {
                                            mc.dropMessage("Bgm15/MureungHill");
                                            mc.dropMessage("Bgm15/MureungForest");
                                            mc.dropMessage("Bgm15/WhiteHerb");
                                            mc.dropMessage("Bgm15/Pirate");
                                            mc.dropMessage("Bgm15/SunsetDesert");
                                        } else if (splitted[2].equals("BgmEvent")) {
                                            mc.dropMessage("BgmEvent/FunnyRabbit");
                                            mc.dropMessage("BgmEvent/FunnyRabbitFaster");
                                        } else if (splitted[2].equals("BgmGL")) {
                                            mc.dropMessage("BgmGL/amoria");
                                            mc.dropMessage("BgmGL/chapel");
                                            mc.dropMessage("BgmGL/cathedral");
                                            mc.dropMessage("BgmGL/Amorianchallenge");
                                        } else if (splitted[2].equals("BgmJp")) {
                                            mc.dropMessage("BgmJp/Feeling");
                                            mc.dropMessage("BgmJp/BizarreForest");
                                            mc.dropMessage("BgmJp/Hana");
                                            mc.dropMessage("BgmJp/Yume");
                                            mc.dropMessage("BgmJp/Bathroom");
                                            mc.dropMessage("BgmJp/BattleField");
                                            mc.dropMessage("BgmJp/FirstStepMaster");
                                }
                                } else {
                                        String songName =  splitted[1];
                                        c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.musicChange(songName)); 
                                        
                                } 
                        } else if (splitted[0].equals("!mesorate")) { // by Redline/2azn4u
				if (splitted.length > 1) {
					int meso = Integer.parseInt(splitted[1]);
					cserv.setMesoRate(meso);
					MaplePacket packet = MaplePacketCreator.serverNotice(6, "Meso Rate has been changed to " + meso + "x");
					ChannelServer.getInstance(c.getChannel()).broadcastPacket(packet);
				} else 
					mc.dropMessage("Syntax: !mesorate <number>");
                        } else if (splitted[0].equals("!droprate")) { // by doncare aka voice123

                if (splitted.length > 1) {
                    int drop = Integer.parseInt(splitted[1]);
                    cserv.setDropRate(drop);
                    MaplePacket packet = MaplePacketCreator.serverNotice(6, "Drop Rate has been changed to " + drop + "x");
                    ChannelServer.getInstance(c.getChannel()).broadcastPacket(packet);
                } else 
                    mc.dropMessage("Syntax: !droprate <number>");
                
                        }        else if (splitted[0].equals("!bossdroprate")) { // by doncare aka voice123

                if (splitted.length > 1) {
                    int bossdrop = Integer.parseInt(splitted[1]);
                    cserv.setBossDropRate(bossdrop);
                    MaplePacket packet = MaplePacketCreator.serverNotice(6, "Boss Drop Rate has been changed to " + bossdrop + "x");
                    ChannelServer.getInstance(c.getChannel()).broadcastPacket(packet);
                } else 
                    mc.dropMessage("Syntax: !bossdroprate <number>");
 } 			else if (splitted[0].equals("!mesos")){
                            c.getPlayer().gainMeso(Integer.parseInt(splitted[1]), true);    
                   }	else if (splitted[0].equals("!warpallhere")) {
                	for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters())
                    	if (mch.getMapId() != c.getPlayer().getMapId())
                        mch.changeMap(c.getPlayer().getMap(), c.getPlayer().getPosition()); 
			} else if (splitted[0].equalsIgnoreCase("!killmonster")) {
            if(splitted.length ==2)
            {
                MapleMap map = c.getPlayer().getMap();
                double range = Double.POSITIVE_INFINITY;
                int targetId=Integer.parseInt(splitted[1]);
        
                List<MapleMapObject> monsters = map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays
                    .asList(MapleMapObjectType.MONSTER));

                for (MapleMapObject monstermo : monsters) {
                    MapleMonster monster = (MapleMonster) monstermo;
                    if (monster.getId()==targetId) {
                        map.killMonster(monster, player, false);
                        break;
                    }
                }
            }

			} else if (splitted[0].equals("!resetquest")) {
				MapleQuest.getInstance(Integer.parseInt(splitted[1])).forfeit(c.getPlayer());
			} else if (splitted[0].equals("!gps")) {
				// c.getSession().write(MaplePacketCreator.getPlayerShop(c.getPlayer(), 0, null));
			} else if (splitted[0].equals("!nearestPortal")) {
				final MaplePortal portal = player.getMap().findClosestSpawnpoint(player.getPosition());
				mc.dropMessage(portal.getName() + " id: " + portal.getId() + " script: " + portal.getScriptName());
			} else if (splitted[0].equals("!sp")) {
				player.setRemainingSp(getOptionalIntArg(splitted, 1, 1));
				player.updateSingleStat(MapleStat.AVAILABLESP, player.getRemainingSp());
			} else if (splitted[0].equals("!fakerelog")) {
				c.getSession().write(MaplePacketCreator.getCharInfo(player));
				player.getMap().removePlayer(player);
				player.getMap().addPlayer(player);
			} else if (splitted[0].equals("!test")) {
				// faeks id is 30000 (30 75 00 00)
				// MapleCharacter faek = ((MapleCharacter) c.getPlayer().getMap().getMapObject(30000));
				
				//List<BuddylistEntry> buddylist = Arrays.asList(new BuddylistEntry("derGuteBuddy", 30000, 1, true));
//				c.getSession().write(MaplePacketCreator.updateBuddylist(buddylist));
				// c.getSession().write(MaplePacketCreator.updateBuddyChannel(30000, 1));
				// c.getSession().write(MaplePacketCreator.updateBuddyChannel(30000, 0));
				//c.getSession().write(MaplePacketCreator.requestBuddylistAdd(30000, "FaekChar"));
				//c.getSession().write(MaplePacketCreator.requestBuddylistAdd(30001, "FaekChar2"));
				//c.getSession().write(MaplePacketCreator.multiChat("lulu", line, 0));
				// c.getSession().write(MaplePacketCreator.showOwnBuffEffect(1311008, 5));
				// c.getSession().write(MaplePacketCreator.showBuffeffect(30000, 1311008, 5));
				//c.getSession().write(MaplePacketCreator.getPacketFromHexString("2B 00 07 22 64 1F 23 00 57 69 6E 64 53 63 61 72 73 00 FF FF 2C 02 56 0A 35 B7 34 A9 17 00 78 4D 41 55 53 49 78 00 73 00 FF FF 2C 00 FF FF FF FF 6A 3A 0D 00 6F 31 56 69 45 54 78 47 69 52 4C 00 2C 02 56 0A 35 B7 7D 3C 05 00 69 74 7A 78 65 6D 69 6C 79 79 00 00 2C 02 56 0A 35 B7 00 ED 19 00 31 39 39 52 61 6E 64 6F 6D 67 75 79 00 02 56 0A 35 B7 69 7D 00 00 64 61 76 74 73 61 69 00 6D 67 75 79 00 02 56 0A 35 B7 46 85 17 00 44 72 61 6B 65 58 6B 69 6C 6C 65 72 00 00 FF FF FF FF AD 78 00 00 42 61 74 6F 73 69 61 00 6C 6C 65 72 00 02 56 0A 35 B7 A7 B1 02 00 53 65 63 6E 69 6E 00 00 6C 6C 65 72 00 00 FF FF FF FF 05 50 00 00 48 61 6E 64 4F 66 47 6F 64 00 65 72 00 02 56 0A 35 B7 29 21 41 00 53 61 65 61 00 66 47 6F 64 00 65 72 00 00 FF FF FF FF 79 00 01 00 62 75 74 74 77 61 78 00 64 00 65 72 00 02 56 0A 35 B7 B9 01 02 00 48 65 72 6F 53 6F 50 72 6F 00 65 72 00 02 56 0A 35 B7 63 0F 23 00 4D 53 43 42 00 6F 50 72 6F 00 65 72 00 02 56 0A 35 B7 63 40 0F 00 44 65 6D 30 6E 7A 61 62 75 7A 61 00 00 02 56 0A 35 B7 B2 C8 00 00 41 73 69 61 6E 4D 49 63 6B 65 79 00 00 00 FF FF FF FF E1 6D 13 00 54 52 44 52 6F 6C 6C 61 00 65 79 00 00 00 FF FF FF FF 0D 35 00 00 53 65 63 72 61 6E 6F 00 00 65 79 00 00 00 FF FF FF FF DF E3 01 00 62 69 7A 7A 00 6E 6F 00 00 65 79 00 00 00 FF FF FF FF 56 93 2F 00 54 65 72 70 65 00 6F 00 00 65 79 00 00 00 FF FF FF FF 69 EB 14 00 53 6B 79 64 72 65 61 6D 00 65 79 00 00 00 FF FF FF FF 1B 04 02 00 4E 61 67 6C 66 61 72 00 00 65 79 00 00 00 FF FF FF FF FA 6F 00 00 53 68 6D 75 66 66 00 67 6F 6E 00 00 00 00 FF FF FF FF 09 E2 00 00 44 65 70 74 69 63 00 67 6F 6E 00 00 00 00 FF FF FF FF 85 49 15 00 54 79 73 74 6F 00 00 67 6F 6E 00 00 00 02 56 0A 35 B7 F8 9A 17 00 46 6F 68 6E 7A 00 00 67 6F 6E 00 00 00 02 56 0A 35 B7 86 B2 0F 00 41 62 79 73 61 6C 43 6C 65 72 69 63 00 02 56 0A 35 B7 1A 88 1D 00 78 73 63 72 69 62 62 6C 65 73 7A 00 00 00 FF FF FF FF D5 5C 1E 00 46 6A 6F 65 72 67 79 6E 6E 00 7A 00 00 00 FF FF FF FF 4B CE 03 00 41 72 72 6F 77 68 65 61 64 31 33 35 00 02 56 0A 35 B7 8F 2F 20 00 4E 61 77 75 74 6F 00 61 64 31 33 35 00 00 FF FF FF FF D5 8E 1E 00 4C 61 72 69 6C 79 00 61 64 31 33 35 00 00 FF FF FF FF 9B 85 0F 00 53 68 65 65 70 68 65 72 64 00 33 35 00 00 FF FF FF FF 30 C0 23 00 46 6A 6F 65 72 00 6E 61 6C 20 66 61 69 00 09 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00"));
				c.getSession().write(MaplePacketCreator.getPacketFromHexString("2B 00 14 30 C0 23 00 00 11 00 00 00"));
			} else if (splitted[0].equals("!dc")) {
				int level = 0;
				MapleCharacter victim;
				if (splitted[1].charAt(0) == '-') {
					level = StringUtil.countCharacters(splitted[1], 'f');
					victim = cserv.getPlayerStorage().getCharacterByName(splitted[2]);
				} else {
					victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
				}
				victim.getClient().getSession().close();
				if (level >= 1) {
					victim.getClient().disconnect();
				}
				if (level >= 2) {
					victim.saveToDB(true);
					cserv.removePlayer(victim);
				}
			} else if (splitted[0].equals("!coke")){
                            MapleMonster mob0 = MapleLifeFactory.getMonster(9500144);
                            MapleMonster mob1 = MapleLifeFactory.getMonster(9500151);
                            MapleMonster mob2 = MapleLifeFactory.getMonster(9500152);
                            MapleMonster mob3 = MapleLifeFactory.getMonster(9500153);
                            MapleMonster mob4 = MapleLifeFactory.getMonster(9500154);
                            MapleMonster mob5 = MapleLifeFactory.getMonster(9500143);
                            MapleMonster mob6 = MapleLifeFactory.getMonster(9500145);
                            MapleMonster mob7 = MapleLifeFactory.getMonster(9500149);
                            MapleMonster mob8 = MapleLifeFactory.getMonster(9500147);
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob1, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob2, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob3, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob4, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob5, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob6, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob7, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob8, c.getPlayer().getPosition());
                        }else if (splitted[0].equals("!papu")){
                            MapleMonster mob0 = MapleLifeFactory.getMonster(8500001);
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
                        } else if (splitted[0].equals("!zakum")){
			MapleMonster mob0 = MapleLifeFactory.getMonster(8800003);
			MapleMonster mob1 = MapleLifeFactory.getMonster(8800004);
			MapleMonster mob2 = MapleLifeFactory.getMonster(8800005);
			MapleMonster mob3 = MapleLifeFactory.getMonster(8800006);
			MapleMonster mob4 = MapleLifeFactory.getMonster(8800007);
			MapleMonster mob5 = MapleLifeFactory.getMonster(8800008);
			MapleMonster mob6 = MapleLifeFactory.getMonster(8800009);
			MapleMonster mob7 = MapleLifeFactory.getMonster(8800010);
			MapleMonster mob8 = MapleLifeFactory.getMonster(8800000);
			c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
			c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob1, c.getPlayer().getPosition());
			c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob2, c.getPlayer().getPosition());
			c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob3, c.getPlayer().getPosition());
			c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob4, c.getPlayer().getPosition());
			c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob5, c.getPlayer().getPosition());
			c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob6, c.getPlayer().getPosition());
			c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob7, c.getPlayer().getPosition());
			c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob8, c.getPlayer().getPosition());
			c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.serverNotice(0, "The almighty Zakum has awakened!"));
			}else if (splitted[0].equals("!horntail")){
			MapleMonster mob0 = MapleLifeFactory.getMonster(8810002);
			MapleMonster mob1 = MapleLifeFactory.getMonster(8810003);
			MapleMonster mob2 = MapleLifeFactory.getMonster(8810004);
			MapleMonster mob3 = MapleLifeFactory.getMonster(8810005);
			MapleMonster mob4 = MapleLifeFactory.getMonster(8810006);
			MapleMonster mob5 = MapleLifeFactory.getMonster(8810007);
			MapleMonster mob6 = MapleLifeFactory.getMonster(8810008);
			MapleMonster mob7 = MapleLifeFactory.getMonster(8810009);
			c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
			c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob1, c.getPlayer().getPosition());
			c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob2, c.getPlayer().getPosition());
			c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob3, c.getPlayer().getPosition());
			c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob4, c.getPlayer().getPosition());
			c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob5, c.getPlayer().getPosition());
			c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob6, c.getPlayer().getPosition());
			c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob7, c.getPlayer().getPosition());
			c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.serverNotice(0, "As the cave shakes and rattles, here comes Horntail.")); 
			}else if (splitted[0].equals("!ergoth")){
                            MapleMonster mob0 = MapleLifeFactory.getMonster(9300028);
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
                        }else if (splitted[0].equals("!ludimini")){
                            MapleMonster mob0 = MapleLifeFactory.getMonster(8160000);
                            MapleMonster mob1 = MapleLifeFactory.getMonster(8170000);
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob1, c.getPlayer().getPosition());
                        }else if (splitted[0].equals("!cornian")){
                            MapleMonster mob0 = MapleLifeFactory.getMonster(8150201);
                            MapleMonster mob1 = MapleLifeFactory.getMonster(8150200);
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob1, c.getPlayer().getPosition());
                        }else if (splitted[0].equals("!balrog")){
                            MapleMonster mob0 = MapleLifeFactory.getMonster(8130100);
                            MapleMonster mob1 = MapleLifeFactory.getMonster(8150000);
                            MapleMonster mob2 = MapleLifeFactory.getMonster(9400536);
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob1, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob2, c.getPlayer().getPosition());
                        }else if (splitted[0].equals("!mushmom")){
                            MapleMonster mob0 = MapleLifeFactory.getMonster(6130101);
                            MapleMonster mob1 = MapleLifeFactory.getMonster(6300005);
                            MapleMonster mob2 = MapleLifeFactory.getMonster(9400205);
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob1, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob2, c.getPlayer().getPosition());
                        }else if (splitted[0].equals("!wyvern")){
                            MapleMonster mob0 = MapleLifeFactory.getMonster(8150300);
                            MapleMonster mob1 = MapleLifeFactory.getMonster(8150301);
                            MapleMonster mob2 = MapleLifeFactory.getMonster(8150302);
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob1, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob2, c.getPlayer().getPosition());
                        }else if (splitted[0].equals("!pirate")){
                            MapleMonster mob0 = MapleLifeFactory.getMonster(9300119);
                            MapleMonster mob1 = MapleLifeFactory.getMonster(9300107);
                            MapleMonster mob2 = MapleLifeFactory.getMonster(9300105);
                            MapleMonster mob3 = MapleLifeFactory.getMonster(9300106);
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob1, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob2, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob3, c.getPlayer().getPosition());
                        }else if (splitted[0].equals("!clone")){
                            MapleMonster mob0 = MapleLifeFactory.getMonster(9001002);
                            MapleMonster mob1 = MapleLifeFactory.getMonster(9001000);
                            MapleMonster mob2 = MapleLifeFactory.getMonster(9001003);
                            MapleMonster mob3 = MapleLifeFactory.getMonster(9001001);
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob1, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob2, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob3, c.getPlayer().getPosition());
                        }else if (splitted[0].equals("!anego")){
                            MapleMonster mob0 = MapleLifeFactory.getMonster(9400121);
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
                        }else if (splitted[0].equals("!theboss")){
                            MapleMonster mob0 = MapleLifeFactory.getMonster(9400300);
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
                        }else if (splitted[0].equals("!snackbar")){
                            MapleMonster mob0 = MapleLifeFactory.getMonster(9500179);
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
                        }else if (splitted[0].equals("!papapixie")){
                            MapleMonster mob0 = MapleLifeFactory.getMonster(9300039);
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
                        }else if (splitted[0].equals("!horseman")){
                            MapleMonster mob0 = MapleLifeFactory.getMonster(9400549);
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
                        }else if (splitted[0].equals("!blackcrow")){
                            MapleMonster mob0 = MapleLifeFactory.getMonster(9400014);
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
                        }else if (splitted[0].equals("!leafreboss")){
                            MapleMonster mob0 = MapleLifeFactory.getMonster(9400014);
                            MapleMonster mob1 = MapleLifeFactory.getMonster(8180001);
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob1, c.getPlayer().getPosition());
                        }else if (splitted[0].equals("!shark")){
                            MapleMonster mob0 = MapleLifeFactory.getMonster(8150101);
                            MapleMonster mob1 = MapleLifeFactory.getMonster(8150100);
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob1, c.getPlayer().getPosition());
                        }else if (splitted[0].equals("!franken")){
                            MapleMonster mob0 = MapleLifeFactory.getMonster(9300139);
                            MapleMonster mob1 = MapleLifeFactory.getMonster(9300140);
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob1, c.getPlayer().getPosition());
                        }else if (splitted[0].equals("!bird")){
                            MapleMonster mob0 = MapleLifeFactory.getMonster(9300090);
                            MapleMonster mob1 = MapleLifeFactory.getMonster(9300089);
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob1, c.getPlayer().getPosition());
                        }else if (splitted[0].equals("!pianus")){
                            MapleMonster mob0 = MapleLifeFactory.getMonster(8510000);
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
                        }else if (splitted[0].equals("!centipede")){
                            MapleMonster mob0 = MapleLifeFactory.getMonster(9500177);
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
                        } else if (splitted[0].equals("!horntail")){
                            MapleMonster mob0 = MapleLifeFactory.getMonster(8810026);
                            c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob0, c.getPlayer().getPosition());
                            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.serverNotice(0, "As the cave shakes and rattles, here comes Horntail.")); 
                            MapleMap map = c.getPlayer().getMap();
				double range = Double.POSITIVE_INFINITY;				
				List<MapleMapObject> monsters = map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays
					.asList(MapleMapObjectType.MONSTER));
					for (MapleMapObject monstermo : monsters) {
					MapleMonster monster = (MapleMonster) monstermo;					
						map.killMonster(monster, c.getPlayer(), false);
									
				}
			} else if (splitted[0].equals("!goto")) {
                    //Map name array
                    int[] gotomapid = { 
                        180000000, //GmMap
                        60000, //Southperry
                        1010000, //Amherst
                        100000000, //henesys 
                        101000000, //ellinia 
                        102000000, //perion
                        103000000, //kerning
                        104000000, //lith
                        105040300, //sleepywood
                        110000000, //florina
                        200000000, //orbis
                        209000000, //happy
                        211000000, //elnath
                        220000000, //ludi
                        230000000, //aqua
                        240000000, //leafre
                        250000000, //mulung
                        251000000, //herb
                        221000000, //omega
                        222000000, //korean (Folk Town)
                        600000000, //nlc (New Leaf City)
                        990000000, //excavation (Sharenian/Excavation Site)
                        230040420, //Pianus cave
                        240060200, //Horned Tail's cave
                        100000005, //Mushmom
                        240020101, //Griffey
                        240020401, //Manon
                        682000001, //Headless Horseman
                        105090900, //Jr.Balrog
			280030000, //Zakum's Altar
			220080001, //Papulatus map
			801000000, //showa Town
			200000301, //Guild HeadQuarters
			800000000, //Shrine (Mushroom Shrine)
			910000000, //Free Market Entrance
			240040511, //Skelegon map (Leafre)
                    };
                    String[] gotomapname = { 
                        "gmmap",
                        "southperry",
                        "amherst",
                        "henesys", 
                        "ellinia", 
                        "perion", 
                        "kerning", 
                        "lith", 
                        "sleepywood", 
                        "florina",
                        "orbis", 
                        "happy", 
                        "elnath", 
                        "ludi", 
                        "aqua", 
                        "leafre", 
                        "mulung", 
                        "herb", 
                        "omega", 
                        "korean", 
                        "nlc",
                        "excavation",
                        "pianus",
                        "horntail",
                        "mushmom",
                        "griffey",
                        "manon",
                        "horseman",
                        "balrog",
			"zakum",
			"papu",
			"showa",
			"guild",
			"shrine",
			"fm",
			"skelegon",
                    };
                    //Function
                    if (splitted.length < 2) { //If no arguments, list options.
                        mc.dropMessage("Syntax: !goto <mapname> <optional_target>, where target is char name and mapname is one of:");
                        mc.dropMessage("gmmap, southperry, amherst, henesys, ellinia, perion, kerning, lith, sleepywood, florina,");
                        mc.dropMessage("orbis, happy, elnath, ludi, aqua, leafre, mulung, herb, omega, korean, nlc, excavation, pianus");
                        mc.dropMessage("horntail, mushmom, griffey, manon, horseman, balrog, zakum, papu, showa, guild, shrine, fm, skelegon");
                    } else {
                        for (int i = 0; gotomapid[i] != 0 && gotomapname[i] != null; ++i) { //for every array which isn't empty
  if (splitted[1].equals(gotomapname[i])) { //If argument equals name
    MapleMap target = cserv.getMapFactory().getMap(gotomapid[i]);
    MaplePortal targetPortal = target.getPortal(0);
    if (splitted.length < 3) { //If no target name, continue
      player.changeMap(target, targetPortal);
    } else if (splitted.length > 2) { //If target name, new target
      MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[2]);
      victim.changeMap(target, targetPortal);
                }
             }
        }
 }
                        }  else if (splitted[0].equals("!heal")) {
      			player.setHp(player.getMaxHp());
       			player.updateSingleStat(MapleStat.HP, player.getMaxHp());
       			player.setMp(player.getMaxMp());
       			player.updateSingleStat(MapleStat.MP, player.getMaxMp());
			} else if (splitted[0].equals("!cheaters")) {
				try {
					List<CheaterData> cheaters = c.getChannelServer().getWorldInterface().getCheaters();
					for (int x = cheaters.size() - 1; x >= 0; x--) {
						CheaterData cheater = cheaters.get(x);
						mc.dropMessage(cheater.getInfo());
					}
				} catch (RemoteException e) {
					c.getChannelServer().reconnectWorld();
				} 
			 } else if (splitted[0].equals("!charinfo")) {
				StringBuilder builder = new StringBuilder();
				MapleCharacter other = cserv.getPlayerStorage().getCharacterByName(splitted[1]);

				builder.append(MapleClient.getLogMessage(other, ""));
				builder.append(" at ");
				builder.append(other.getPosition().x);
				builder.append("/");
				builder.append(other.getPosition().y);
				builder.append(" ");
				builder.append(other.getHp());
				builder.append("/");
				builder.append(other.getCurrentMaxHp());
				builder.append("hp ");
				builder.append(other.getMp());
				builder.append("/");
				builder.append(other.getCurrentMaxMp());
				builder.append("mp ");
				builder.append(other.getExp());
				builder.append("exp hasParty: ");
				builder.append(other.getParty() != null);
				builder.append(" hasTrade: ");
				builder.append(other.getTrade() != null);
				builder.append(" remoteAddress: ");
				builder.append(other.getClient().getSession().getRemoteAddress());
				mc.dropMessage(builder.toString());
				other.getClient().dropDebugMessage(mc);
			} else if (splitted[0].equals("!ban")) {
				if (splitted.length < 3) {
					new ServernoticeMapleClientMessageCallback(2, c).dropMessage("Syntaxhelper : Syntax: !ban charname reason");
					return true;
				}
				String originalReason = StringUtil.joinStringFrom(splitted, 2);
				String reason = c.getPlayer().getName() + " banned " + splitted[1] + ": " +
				originalReason;
				MapleCharacter target = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
				if (target != null) {
					String readableTargetName = MapleCharacterUtil.makeMapleReadable(target.getName());
					String ip = target.getClient().getSession().getRemoteAddress().toString().split(":")[0];
					reason += " (IP: " + ip + ")";
					target.ban(reason);
					mc.dropMessage("Banned " + readableTargetName + " ipban for " + ip + " reason: " + originalReason);
				} else {
					if (MapleCharacter.ban(splitted[1], reason, false)) {
						mc.dropMessage("Offline Banned " + splitted[1]);
					} else {
						mc.dropMessage("Failed to ban " + splitted[1]);
					}
                                }
			} else if (splitted[0].equals("!tempban")) {
				Calendar tempB = Calendar.getInstance();
				String originalReason = joinAfterString(splitted, ":");

				if (splitted.length < 4 || originalReason == null) {
					mc.dropMessage("Syntax helper: !tempban <name> [i / m / w / d / h] <amount> [r  [reason id] : Text Reason");
					return true;
				}

				int yChange = getNamedIntArg(splitted, 1, "y", 0);
				int mChange = getNamedIntArg(splitted, 1, "m", 0);
				int wChange = getNamedIntArg(splitted, 1, "w", 0);
				int dChange = getNamedIntArg(splitted, 1, "d", 0);
				int hChange = getNamedIntArg(splitted, 1, "h", 0);
				int iChange = getNamedIntArg(splitted, 1, "i", 0);
				int gReason = getNamedIntArg(splitted, 1, "r", 7);

				String reason = c.getPlayer().getName() + " tempbanned " + splitted[1] + ": " + originalReason;


				if (gReason > 14) {
					mc.dropMessage("You have entered an incorrect ban reason ID, please try again.");
					return true;
				}

				DateFormat df = DateFormat.getInstance();
				tempB.set(tempB.get(Calendar.YEAR) + yChange, tempB.get(Calendar.MONTH) + mChange, tempB.get(Calendar.DATE) +
					(wChange * 7) + dChange, tempB.get(Calendar.HOUR_OF_DAY) + hChange, tempB.get(Calendar.MINUTE) +
					iChange);

				MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);

				if (victim == null) {
					int accId = MapleClient.findAccIdForCharacterName(splitted[1]);
					if (accId >= 0 && MapleCharacter.tempban(reason, tempB, gReason, accId)) {
						mc.dropMessage("The character " + splitted[1] + " has been successfully offline-tempbanned till " + df.format(tempB.getTime()) + ".");
					} else {
						mc.dropMessage("There was a problem offline banning character " + splitted[1] + ".");
					}
				} else {
					victim.tempban(reason, tempB, gReason);
					mc.dropMessage("The character " + splitted[1] + " has been successfully tempbanned till " + df.format(tempB.getTime()));
				}
                        }
                	else if (splitted[0].equals("!levelup")) {
				c.getPlayer().levelUp();
				int newexp = c.getPlayer().getExp();
				if (newexp < 0) {
					c.getPlayer().gainExp(-newexp, false, false);
				}
			} else if (splitted[0].equals("!whereami")) {
				new ServernoticeMapleClientMessageCallback(c).dropMessage("You are on map " +
					c.getPlayer().getMap().getId());
			} else if (splitted[0].equals("!version")) {
				new ServernoticeMapleClientMessageCallback(c)
					.dropMessage("Revision: Revision 988 SeanPack V.5.8 Beta");
			} else if (splitted[0].equals("!connected")) {
				try {
					Map<Integer, Integer> connected = cserv.getWorldInterface().getConnected();
					StringBuilder conStr = new StringBuilder("Connected Clients: ");
					boolean first = true;
					for (int i : connected.keySet()) {
						if (!first) {
							conStr.append(", ");
						} else {
							first = false;
						}
						if (i == 0) {
							conStr.append("Total: ");
							conStr.append(connected.get(i));
						} else {
							conStr.append("Ch");
							conStr.append(i);
							conStr.append(": ");
							conStr.append(connected.get(i));
						}
					}
					new ServernoticeMapleClientMessageCallback(c).dropMessage(conStr.toString());
				} catch (RemoteException e) {
					c.getChannelServer().reconnectWorld();
				}
			} else if (splitted[0].equals("!whosthere")) {
				MessageCallback callback = new ServernoticeMapleClientMessageCallback(c);
				StringBuilder builder = new StringBuilder("Players on Map: ");
				for (MapleCharacter chr : c.getPlayer().getMap().getCharacters()) {
					if (builder.length() > 150) { // wild guess :o
						builder.setLength(builder.length() - 2);
						callback.dropMessage(builder.toString());
						builder = new StringBuilder();
					}
					builder.append(MapleCharacterUtil.makeMapleReadable(chr.getName()));
					builder.append(", ");
				}
				builder.setLength(builder.length() - 2);
				c.getSession().write(MaplePacketCreator.serverNotice(6, builder.toString()));
			} else if (splitted[0].equals("!shutdown")) {
				int time = 60000;
				if (splitted.length > 1) {
					time = Integer.parseInt(splitted[1]) * 60000;
				}
				persister.run();
				c.getChannelServer().shutdown(time);
			} else if (splitted[0].equals("!shutdownworld")) {
				int time = 60000;
				if (splitted.length > 1) {
					time = Integer.parseInt(splitted[1]) * 60000;
				}
				persister.run();
				c.getChannelServer().shutdownWorld(time);
				// shutdown
			} else if (splitted[0].equals("!shutdownnow")) {
				persister.run();
				new ShutdownServer(c.getChannel()).run();
			} else if (splitted[0].equals("!timerdebug")) {
				TimerManager.getInstance().dropDebugInfo(mc);
			} else if (splitted[0].equals("!threads")) {
				Thread[] threads = new Thread[Thread.activeCount()];
				Thread.enumerate(threads);
				String filter = "";
				if (splitted.length > 1) {
					filter = splitted[1];
				}
				for (int i = 0; i < threads.length; i++) {
					String tstring = threads[i].toString();
					if (tstring.toLowerCase().indexOf(filter.toLowerCase()) > -1) {
						mc.dropMessage(i + ": " + tstring);
					}
				}
			} else if (splitted[0].equals("!showtrace")) {
				if (splitted.length < 2) {
					return true;
				}
				Thread[] threads = new Thread[Thread.activeCount()];
				Thread.enumerate(threads);
				Thread t = threads[Integer.parseInt(splitted[1])];
				mc.dropMessage(t.toString() + ":");
				for (StackTraceElement elem : t.getStackTrace()) {
					mc.dropMessage(elem.toString());
				}
			} else if (splitted[0].equals("!dumpthreads")) {
				Map<Thread, StackTraceElement[]> traces = Thread.getAllStackTraces();
				try {
					PrintWriter pw = new PrintWriter(new File("threaddump.txt"));
					for (Entry<Thread, StackTraceElement[]> t : traces.entrySet()) {
						pw.println(t.getKey().toString());
						for (StackTraceElement elem : t.getValue()) {
							pw.println(elem.toString());
						}
						pw.println();
					}
					pw.close();
				} catch (FileNotFoundException e) {
					log.error("ERROR", e);
				}
			} else if (splitted[0].equals("!reloadops")) {
				try {
					ExternalCodeTableGetter.populateValues(SendPacketOpcode.getDefaultProperties(), SendPacketOpcode.values());
					ExternalCodeTableGetter.populateValues(RecvPacketOpcode.getDefaultProperties(), RecvPacketOpcode.values());
				} catch (Exception e) {
					log.error("Failed to reload props", e);
				}
				PacketProcessor.getProcessor(PacketProcessor.Mode.CHANNELSERVER).reset(PacketProcessor.Mode.CHANNELSERVER);
				PacketProcessor.getProcessor(PacketProcessor.Mode.CHANNELSERVER).reset(PacketProcessor.Mode.CHANNELSERVER);
			} else if (splitted[0].equals("!clearPortalScripts")) {
				PortalScriptManager.getInstance().clearScripts();
			} else if (splitted[0].equals("!killall") || splitted[0].equals("!monsterdebug")) {
				MapleMap map = c.getPlayer().getMap();
				double range = Double.POSITIVE_INFINITY;
				if (splitted.length > 1) {
					int irange = Integer.parseInt(splitted[1]);
					range = irange * irange;
				}
				List<MapleMapObject> monsters = map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays
					.asList(MapleMapObjectType.MONSTER));
				boolean kill = splitted[0].equals("!killall");
				for (MapleMapObject monstermo : monsters) {
					MapleMonster monster = (MapleMonster) monstermo;
					if (kill) {
						map.killMonster(monster, c.getPlayer(), false);
					} else {
						mc.dropMessage("Monster " + monster.toString());
					}
				}
				if (kill) {
					mc.dropMessage("You have killed " + monsters.size() + " monsters in your map");
				}
			} else if (splitted[0].equals("!skill")) {
				int skill = Integer.parseInt(splitted[1]);
				int level = getOptionalIntArg(splitted, 2, 1);
				int masterlevel = getOptionalIntArg(splitted, 3, 1);
				c.getPlayer().changeSkillLevel(SkillFactory.getSkill(skill), level, masterlevel);
			} else if (splitted[0].equals("!spawndebug")) {
				c.getPlayer().getMap().spawnDebug(mc);
			} else if (splitted[0].equals("!door")) {
				Point doorPos = new Point(player.getPosition());
				doorPos.y -= 270;
				MapleDoor door = new MapleDoor(c.getPlayer(), doorPos);
				door.getTarget().addMapObject(door);
				//c.getSession().write(MaplePacketCreator.spawnDoor(/*c.getPlayer().getId()*/ 0x1E47, door.getPosition(), false));
				/*c.getSession().write(MaplePacketCreator.saveSpawnPosition(door.getPosition()));*/
				MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
				mplew.write(HexTool.getByteArrayFromHexString("B9 00 00 47 1E 00 00 0A 04 76 FF"));
				c.getSession().write(mplew.getPacket());
				mplew = new MaplePacketLittleEndianWriter();
				mplew.write(HexTool.getByteArrayFromHexString("36 00 00 EF 1C 0D 4C 3E 1D 0D 0A 04 76 FF"));
				c.getSession().write(mplew.getPacket());
				c.getSession().write(MaplePacketCreator.enableActions());
				door = new MapleDoor(door);
				door.getTown().addMapObject(door);
			} else if (splitted[0].equals("!tdrops")) {
				player.getMap().toggleDrops();
			} else if (splitted[0].equals("!lowhp")) {
				player.setHp(1);
				player.setMp(500);
				player.updateSingleStat(MapleStat.HP, 1);
				player.updateSingleStat(MapleStat.MP, 500);
			}  else if (splitted[0].equals("!fullhp")) {
				player.setHp(player.getMaxHp());
				player.updateSingleStat(MapleStat.HP, player.getMaxHp());
			} else if (splitted[0].equals("!cheaters")) {
				try {
					List<CheaterData> cheaters = c.getChannelServer().getWorldInterface().getCheaters();
					for (int x = cheaters.size() - 1; x >= 0; x--) {
						CheaterData cheater = cheaters.get(x);
						mc.dropMessage(cheater.getInfo());
					}
				} catch (RemoteException e) {
					c.getChannelServer().reconnectWorld();
				}
			} else if (splitted[0].equals("!clearguilds")) {
				try
				{
					mc.dropMessage("Attempting to reload all guilds... this may take a while...");
					cserv.getWorldInterface().clearGuilds();
					mc.dropMessage("Completed.");
				}
				catch (RemoteException re)
				{
					mc.dropMessage("RemoteException occurred while attempting to reload guilds.");
					log.error("RemoteException occurred while attempting to reload guilds.", re);
				}
			
			} else if (splitted[0].equals("!npc")) {
				int npcId = Integer.parseInt(splitted[1]);
				MapleNPC npc = MapleLifeFactory.getNPC(npcId);
				if (npc != null && !npc.getName().equals("MISSINGNO")) {
					npc.setPosition(c.getPlayer().getPosition());
					npc.setCy(c.getPlayer().getPosition().y);
					npc.setRx0(c.getPlayer().getPosition().x + 50);
					npc.setRx1(c.getPlayer().getPosition().x - 50);
					npc.setFh(c.getPlayer().getMap().getFootholds().findBelow(c.getPlayer().getPosition()).getId());
					npc.setCustom(true);
					c.getPlayer().getMap().addMapObject(npc);
					c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.spawnNPC(npc, false));
					// c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.spawnNPC(npc, true));
				} else {
					mc.dropMessage("You have entered an invalid Npc-Id");
				}
			}  else if (splitted[0].equals("!removenpcs")) {
				List<MapleMapObject> npcs = player.getMap().getMapObjectsInRange(c.getPlayer().getPosition(), Double.POSITIVE_INFINITY, Arrays
					.asList(MapleMapObjectType.NPC));
				for (MapleMapObject npcmo : npcs) {
					MapleNPC npc = (MapleNPC) npcmo;
					if (npc.isCustom()) {
						player.getMap().removeMapObject(npc.getObjectId());
					}
				}
			} else {
				mc.dropMessage("GM Command " + splitted[0] + " does not exist");
			}
			return true;
		}
		return false;
	}

	 private static ResultSet getReports(){
            try {
            Connection dcon = DatabaseConnection.getConnection();
            PreparedStatement ps = dcon.prepareStatement("SELECT * FROM reports");
            return ps.executeQuery();
            
            } catch(Exception ex) {}
            return null;
            
        }
        private static void deleteReport(int id){
            try {
            Connection dcon = DatabaseConnection.getConnection();
            PreparedStatement ps = dcon.prepareStatement("DELETE FROM reports WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();       
            
            
            } catch(Exception ex) {}
            
            
        }
        private static ResultSet getReport(int id){
            try {
            Connection dcon = DatabaseConnection.getConnection();
            PreparedStatement ps = dcon.prepareStatement("SELECT * FROM reports where id = ?");
            ps.setInt(1, id);
            return ps.executeQuery();
            
            } catch(Exception ex) {}
            return null;
        }
        private static void setReportStatus(int id, String status){
                        try {
            Connection dcon = DatabaseConnection.getConnection();
            PreparedStatement ps = dcon.prepareStatement("UPDATE reports SET status = ? WHERE id = ?");
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
            ps.close();       
            
            
            } catch(Exception ex) {}
            
        }
        
        private static String getCharInfoById(int id){
                        try {
            Connection dcon = DatabaseConnection.getConnection();
            PreparedStatement ps = dcon.prepareStatement("SELECT * FROM characters where id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            return rs.getString("name");
            
            } catch(Exception ex) {}
            return "error while trying to get name";
            
        }
       
        
}


        
