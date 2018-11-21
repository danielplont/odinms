package net.sf.odinms.server.maps;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;

import net.sf.odinms.server.MaplePortal;
import net.sf.odinms.server.life.MapleMonster;
import net.sf.odinms.net.channel.ChannelServer;

/**
 *
 * @author alex_soh
 */
public class SBossMapMonitor extends BossMapMonitor {
    
    protected int[] mobs;
    protected boolean deadCount[];
    protected ChannelServer cserv;
    protected boolean hasHappen;
    
    public SBossMapMonitor(MapleMap map,MapleMap pMap,MaplePortal portal,int[] mobs,ChannelServer cserv)
    {        
        super(map,pMap,portal);        
        this.mobs=mobs;
        this.deadCount=new boolean[mobs.length];       
        this.cserv=cserv;
        for(int i=0;i<deadCount.length;i++)
        {
            deadCount[i]=false;
        }
        hasHappen=false;
    }
      
    private boolean chkDeadCount()
    {
        boolean result=true;
        for(int i=0;i<deadCount.length;i++)
        {
            if(!deadCount[i])
            {
                result=false;
                break;
            }
        }
        return result;
    }
    
    private List<MapleMapObject> getAllMob()
    {
      return map.getMapObjectsInRange(new Point(0,0), Double.POSITIVE_INFINITY, Arrays
                    .asList(MapleMapObjectType.MONSTER));        
    }
    
    private boolean chkSpecialBoss()
    {
        List<MapleMapObject> list=getAllMob();                
        for(int j=0;j<deadCount.length;j++)
        {
            if(!deadCount[j])
            {
                for(int i=0;i<list.size();i++)
                {
                    MapleMonster monster = (MapleMonster) list.get(i);
                    if(monster.getId()==mobs[j])
                    {
                        deadCount[j]=true;
                        break;
                    }                    
                }
            }
        }
        return chkDeadCount();
    }
    
    @Override
    public void run()
    {
        while(map.playerCount()>0)
        {
            if(chkSpecialBoss() && !hasHappen)
            {
                //special event happen                
                map.killAllmonster();                
                cserv.broadcastPacket(net.sf.odinms.tools.MaplePacketCreator.serverNotice(6, cserv.getChannel(), "To the crew that have finally conquered Horned Tail after numerous attempts, I salute thee! You are the true heroes of Leafre!!"));                
                hasHappen=true;
            }
            try
            {
                Thread.sleep(3000);
            }
            catch(InterruptedException e)
            {
                //e.printStackTrace();
            }
        }
        while(map.mobCount()>0)
        {
            map.killAllmonster();
            try
            {
                Thread.sleep(5000);
            }
            catch(InterruptedException e)
            {
                //e.printStackTrace();
            }            
        }
        map.resetReactors();
        pMap.resetReactors();
        portal.setPortalState(MapleMapPortal.OPEN);
    }
}  
