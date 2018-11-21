package net.sf.odinms.server.maps;

import net.sf.odinms.server.MaplePortal;

public class BossMapMonitor extends MapleMapMonitor
{
    protected MapleMap pMap;
    protected MaplePortal portal;
    
    public BossMapMonitor(MapleMap map,MapleMap pMap,MaplePortal portal)
    {
        super(map);
        this.pMap=pMap;
        this.portal=portal;
    }

    public void run()
    {
        while(map.playerCount()>0)
        {
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
