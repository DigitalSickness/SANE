package gg.gaylord.mitch.network;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import gg.gaylord.mitch.support.Factory;
import gg.gaylord.mitch.support.UIManager;

/**
 * Created by mitchell.gaylord on 3/3/2016.
 */
public class RouteTable {

    Set<RouteTableEntry> routeTable;
    UIManager uiManager;
    Activity activity;

    public RouteTable(){
        routeTable = new TreeSet<RouteTableEntry>();
    }

    public void getObjectReferences(Factory factory){
        uiManager = factory.getUiManager();
    }

    public List<RouteTableEntry> getRouteList() {
        List<RouteTableEntry> newList = new ArrayList<RouteTableEntry>();

        newList.addAll(routeTable);

        return newList;
    }

    public RouteTableEntry addEntry(Integer source, NetworkDistancePair net, Integer dist, Integer nextHop){
        RouteTableEntry newEntry = new RouteTableEntry(source, net, dist, nextHop);

        routeTable.add(newEntry);

        return newEntry;
    }

    public void removeOldRoutes(){
        Iterator<RouteTableEntry> routeTableEntryIterator = this.getTable().iterator();
        RouteTableEntry tmp = null;
        boolean resetIterator = false;

        while (routeTableEntryIterator.hasNext()){
            tmp = routeTableEntryIterator.next();

            if(tmp.isNotExpired()){
                getTable().remove(tmp);
                resetIterator = true;
            }

            if(resetIterator){
                routeTableEntryIterator = getTable().iterator();
                resetIterator = false;
            }
        }
    }

    protected Set getTable() {
        return routeTable;
    }

    public RouteTableEntry removeEntry(Integer network, Integer sourceRouterLL3P){
        Iterator<RouteTableEntry> routeTableEntryIterator = this.getTable().iterator();
        RouteTableEntry tmp = null;
        boolean resetIterator = false;



        while (routeTableEntryIterator.hasNext()){
            tmp = routeTableEntryIterator.next();

            if(tmp.getNetworkDistancePair().getNetworkNumber().equals(network)
                    && tmp.getSourceLL3P().equals(sourceRouterLL3P)){
                getTable().remove(tmp);
                resetIterator = true;
            }

            if(resetIterator){
                routeTableEntryIterator = getTable().iterator();
                resetIterator = false;
            }
        }

        return tmp;
    }
}
