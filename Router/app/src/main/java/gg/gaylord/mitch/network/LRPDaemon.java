package gg.gaylord.mitch.network;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import gg.gaylord.mitch.support.Factory;
import gg.gaylord.mitch.support.NetworkConstants;
import gg.gaylord.mitch.support.UIManager;

/**
 * Created by mitchell.gaylord on 3/24/2016.
 */
public class LRPDaemon implements Runnable {

    ARPTable arpTable;
    RouteTable routeTable;
    ForwardingTable forwardingTable;
    UIManager uiManager;
    Activity parentActivity;
    LL2Daemon ll2Daemon;

    public LRPDaemon(){

    }

    public void getObjectReferences(Factory factory){
        routeTable = factory.getRouteTable();
        forwardingTable = factory.getForwardingTable();

        routeTable.addEntry(Integer.parseInt(NetworkConstants.MY_LL2P_ADDRESS, 16),
                new NetworkDistancePair(Integer.parseInt(NetworkConstants.MY_LL3P_ADDRESS.substring(0, 2),16), 0), 0, 0);

        arpTable = factory.getArpTableObject();
        uiManager = factory.getUiManager();
        parentActivity = factory.getParentActivity();
        ll2Daemon = factory.getLl2Daemon();
    }

    public RouteTable getRouteTable(){
        return routeTable;
    }

    public List<RouteTableEntry> getRoutingTableAsList(){
        return routeTable.getRouteList();
    }

    public ForwardingTable getForwardingTable(){
        return forwardingTable;
    }

    public List<RouteTableEntry> getForwardingTableAsList(){
        return forwardingTable.getForwardingList();
    }

    public void receiveNewLRP(byte[] lrpPacket, Integer ll2pSource){
        //TODO
        LRPClass newLRP = new LRPClass(lrpPacket);

        //Part 1 - Touch the ARP Entry
        if (arpTable.ll2pIsInTable(ll2pSource)){
            //touch the arp entry
            arpTable.addOrUpdate(ll2pSource, newLRP.getSourceAdd());
        }


        //Part 2 - Unpack LRP and Update Route Table
        List<NetworkDistancePair> listOfNetworks = newLRP.getPairList();

        Iterator<NetworkDistancePair> iterateNewLRP = listOfNetworks.iterator();

        while (iterateNewLRP.hasNext()){
            NetworkDistancePair tmp = null;
            tmp = iterateNewLRP.next();

            routeTable.addEntry(newLRP.getSourceAdd(), tmp, tmp.getDistance(), newLRP.getSourceAdd());
        }

        //updates forward table if need be, since more routes have been added
        forwardingTable.addRouteList(routeTable.getRouteList());

        //updates the forward and route tables to be displayed on the screen
        uiManager.forwardingTableToDisplay();
        uiManager.routeTableToDisplay();
    }

    @Override
    public void run(){
        List<ARPTableEntry> listOfAdjacentRouters = new ArrayList<ARPTableEntry>();
        listOfAdjacentRouters = arpTable.getARPTableList();

        Iterator<ARPTableEntry> arpTableEntryIterator = listOfAdjacentRouters.iterator();

        while (arpTableEntryIterator.hasNext()){
            ARPTableEntry tmp = arpTableEntryIterator.next();

            //setting the network num and also setting the distance to 1
            NetworkDistancePair networkDistancePair = new NetworkDistancePair(0, 1);

            long currentTime = (int) Calendar.getInstance().getTimeInMillis()/1000;

            routeTable.addEntry(Integer.parseInt(NetworkConstants.MY_LL2P_ADDRESS, 16), networkDistancePair , 1, tmp.getLL3PAddress());
        }



    }

}
