package gg.gaylord.mitch.network;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import gg.gaylord.mitch.support.Factory;
import gg.gaylord.mitch.support.NetworkConstants;
import gg.gaylord.mitch.support.UIManager;

/**
 * Created by mitchell.gaylord on 3/1/2016.
 */
public class Scheduler {

    ScheduledThreadPoolExecutor threadPoolManager;
    ARPTable arpTable;
    LRPDaemon lrpDaemon;
    RouteTable routeTable;


    public Scheduler() {
        threadPoolManager = new ScheduledThreadPoolExecutor(3);
    }

    public void getObjectReferences(Factory factory){
        arpTable = factory.getArpTableObject();
        lrpDaemon = factory.getLrpDaemon();
        routeTable = factory.getRouteTable();

        threadPoolManager.scheduleAtFixedRate(arpTable, 10, 80, TimeUnit.SECONDS);
        //threadPoolManager.scheduleAtFixedRate(routeTable, NetworkConstants.ROUTER_BOOT_TIME, NetworkConstants.ROUTE_UPDATE_VALUE, TimeUnit.SECONDS);
        //threadPoolManager.scheduleAtFixedRate(lrpDaemon, NetworkConstants.ROUTER_BOOT_TIME, NetworkConstants.ROUTE_UPDATE_VALUE, TimeUnit.SECONDS);

    }

}
