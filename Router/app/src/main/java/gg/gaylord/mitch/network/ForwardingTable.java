package gg.gaylord.mitch.network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by mitchell.gaylord on 3/3/2016.
 */
public class ForwardingTable extends RouteTable{

    Set<RouteTableEntry> forwardingTable = new TreeSet<RouteTableEntry>();

    public ForwardingTable(){
        super();
    }

    public RouteTableEntry addFibEntry(RouteTableEntry entry){
        Iterator<RouteTableEntry> routeTableEntryIterator = forwardingTable.iterator();
        RouteTableEntry tmp = null;
        boolean found = false;

        while(routeTableEntryIterator.hasNext()){
            tmp = routeTableEntryIterator.next();

            if(tmp.getNetworkDistancePair().getNetworkNumber().equals(entry.getNetworkDistancePair().getNetworkNumber())
                    && tmp.getNetworkDistancePair().getDistance() > entry.getNetworkDistancePair().getDistance()){

                forwardingTable.remove(tmp);
                forwardingTable.add(entry);
                found = true;
            }
        }

        if (!found){
            forwardingTable.add(entry);
        }

        return tmp;
    }

    public void addRouteList(ArrayList<RouteTableEntry> routeList){

        for (int i = 0; i < routeList.size(); i++){
            addFibEntry(routeList.get(i));
        }
    }

    @Override
    protected Set getTable() {
        return forwardingTable;
    }

    public Integer getNextHopAddress(Integer ll3pNetworkAddress){
        Integer nextHop = null;

        Iterator<RouteTableEntry> routeTableEntryIterator = forwardingTable.iterator();
        RouteTableEntry tmp = null;

        while (routeTableEntryIterator.hasNext()){
            tmp = routeTableEntryIterator.next();

            if (tmp.getNetworkDistancePair().getNetworkNumber().equals(ll3pNetworkAddress)){
                nextHop = tmp.getNextHop();
            }

        }

        return nextHop;
    }

    public List<RouteTableEntry> getForwardingList() {
        List<RouteTableEntry> newList = new ArrayList<RouteTableEntry>();

        newList.addAll(forwardingTable);

        return newList;
    }

    public List<RouteTableEntry> getFibExcludingLl3pAddress(Integer ll3p){
        Iterator<RouteTableEntry> routeTableEntryIterator = forwardingTable.iterator();
        RouteTableEntry tmp;
        List<RouteTableEntry> tempForwardingTable = new ArrayList<RouteTableEntry>();

        while(routeTableEntryIterator.hasNext()){
            tmp = routeTableEntryIterator.next();

            if (!tmp.getNetworkDistancePair().getNetworkNumber().equals(ll3p)){
                tempForwardingTable.add(tmp);
            }
        }

        return tempForwardingTable;
    }
}
