package gg.gaylord.mitch.network;

import android.net.Network;
import android.util.Base64;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gg.gaylord.mitch.support.Factory;
import gg.gaylord.mitch.support.NetworkConstants;
import gg.gaylord.mitch.support.Utilities;

/**
 * Created by mitchell.gaylord on 3/23/2016.
 */
public class LRPClass {

    private Integer sourceAdd;
    private Integer lrpCounter;
    private Integer sequenceNumber;
    private Integer routeCount;

    private List<NetworkDistancePair> pairList;

    public LRPClass(){
        sourceAdd = 0;
        lrpCounter = 0;
        sequenceNumber = 0;
        routeCount = 0;
        pairList = new ArrayList<NetworkDistancePair>();
    }

    public LRPClass(Integer myLL3P, ForwardingTable myForwardingTable, Integer ll3pAddressThatWillReceiveThisLRP){
        sourceAdd = myLL3P;

        //creates an iterator that loops through the forwarding table
        Iterator<RouteTableEntry> forwardIterator = myForwardingTable.getTable().iterator();
        RouteTableEntry tmp = null;
        Integer tempRouteCount = 0;

        while (forwardIterator.hasNext())
        {
            tmp = forwardIterator.next();
            tempRouteCount++;
            pairList.add(tmp.getNetworkDistancePair());

        }

        routeCount = tempRouteCount;

        lrpCounter++;
        sequenceNumber++;
    }

    public LRPClass(byte[] receivedLRPPacket){
        fillInFromBytes(receivedLRPPacket);
    }

    public List<NetworkDistancePair> getPairList() {
        return pairList;
    }

    public void setPairList(ArrayList<NetworkDistancePair> pairList) {
        this.pairList = pairList;
    }

    public Integer getSourceAdd() {
        return sourceAdd;
    }

    public void setSourceAdd(Integer sourceAdd) {
        this.sourceAdd = sourceAdd;
    }

    public void fillInFromBytes(byte[] receivedLRPPacket){
        String stringLRPBytes = Utilities.byteToString(receivedLRPPacket);
        pairList = new ArrayList<NetworkDistancePair>();

        sourceAdd = Integer.parseInt(stringLRPBytes.substring(0, NetworkConstants.LL3P_ADDRESS_LENGTH), 16);

        sequenceNumber = Integer.parseInt(stringLRPBytes.substring(NetworkConstants.LL3P_ADDRESS_LENGTH,
                NetworkConstants.LL3P_ADDRESS_LENGTH + NetworkConstants.SEQUENCE_NUMBER_LENGTH), 16);

        routeCount = Integer.parseInt(stringLRPBytes.substring(NetworkConstants.LL3P_ADDRESS_LENGTH
                +NetworkConstants.SEQUENCE_NUMBER_LENGTH,
                NetworkConstants.LL3P_ADDRESS_LENGTH+NetworkConstants.SEQUENCE_NUMBER_LENGTH+NetworkConstants.ROUTE_COUNT_LENGTH),16);


        // this is a temporary counter in iterate through the rest of the string
        // in order to properly
        int tempCount = NetworkConstants.LL3P_ADDRESS_LENGTH + NetworkConstants.SEQUENCE_NUMBER_LENGTH + NetworkConstants.ROUTE_COUNT_LENGTH;

        //iterates through the rest of the byte array using a temp count for the substring
        for (int i = 0; i < routeCount; i++){
            int tempNetNum, tempDistNum;

            tempNetNum = Integer.parseInt(stringLRPBytes.substring(tempCount, tempCount + NetworkConstants.NETWORK_NUMBER_LENGTH), 16);
            tempCount+=NetworkConstants.NETWORK_NUMBER_LENGTH;
            tempDistNum = Integer.parseInt(stringLRPBytes.substring(tempCount, tempCount + NetworkConstants.NETWORK_DISTANCE_LENGTH), 16);
            tempCount+=NetworkConstants.NETWORK_DISTANCE_LENGTH;

            pairList.add(new NetworkDistancePair(tempNetNum, tempDistNum));
        }
    }

    public int getRouteCount(){
        return routeCount;
    }

    public byte[] getBytes(){
        return this.toString().getBytes();
    }

    public void setRoutes(ForwardingTable forwardingTable, Integer sourceLL3PAdd){
        //need to get all routes except the ones that are from the source LL3p
        List<RouteTableEntry> newForwardList = forwardingTable.getFibExcludingLl3pAddress(sourceLL3PAdd);

        // new list to store the new route list
        // Note sure if this is needed
        List<NetworkDistancePair> newNetDistPairList = new ArrayList<NetworkDistancePair>();

        Iterator<RouteTableEntry> forwardTableIterator = newForwardList.iterator();

        while (forwardTableIterator.hasNext()){
            RouteTableEntry tmp = forwardTableIterator.next();

           newNetDistPairList.add(tmp.getNetworkDistancePair());
        }

        this.pairList = (ArrayList<NetworkDistancePair>)newNetDistPairList;
    }

    public String toString(){
        String tmp = new String("Source LL3P Address: " + sourceAdd + " Sequence #: " + sequenceNumber +
               " Num Routes In LRP: " + routeCount);

        for (int i = 0; i < pairList.size(); i++){
            tmp += pairList.get(i).toString();
        }
        return tmp;
    }
}
