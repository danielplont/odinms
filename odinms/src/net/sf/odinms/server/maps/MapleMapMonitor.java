package net.sf.odinms.server.maps;

/**
 * @author alex_soh
 */
public abstract class MapleMapMonitor extends Thread
{
    public static final int BOSS_MAP=1;
    public static final int SBOSS_MAP=2;
    protected MapleMap map;

    protected MapleMapMonitor(MapleMap map)
    {
        this.map=map;
    }

    protected void killAllMonster()
    {
        map.killAllmonster();
    }

    @Override
    public abstract void run();
}
