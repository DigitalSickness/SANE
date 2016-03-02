package gg.gaylord.mitch.network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import gg.gaylord.mitch.support.LabException;

/**
 * Created by mitchell.gaylord on 2/4/2016.
 */
public class AdjacencyTable {

    Set<AdjacencyTableEntry> table;

    /*Constructor for Adjaceny Table
    Creates an empty table*/
    public AdjacencyTable(){
        table = new TreeSet<AdjacencyTableEntry>();
    }

    /* Adds an entry to the Adjacency Table */
    public AdjacencyTableEntry addEntry(Integer ll2pAddress, String ipAddress) {
        AdjacencyTableEntry entry = new AdjacencyTableEntry(ll2pAddress,ipAddress);

        try {
            table.add(entry);
        } catch (Exception e){
         e.printStackTrace();
        }
        return entry;
    }

    /*Removes the entry of the specified ll2p Address that is in the AdjacencyTable*/
    public AdjacencyTableEntry removeEntry(Integer ll2pAddress) throws LabException{
        boolean found = false;
        Iterator<AdjacencyTableEntry> remove = table.iterator();
        AdjacencyTableEntry tmp = null;

        while(remove.hasNext() && !found){
            tmp = remove.next();
            Integer tempLL2P = tmp.getLl2pAddress();
            if(tempLL2P == ll2pAddress){
                found = true;
                table.remove(tmp);
            }

            if(!found){
                throw new LabException("Entry Not Found");
            }
        }

        return tmp;
    }

    public InetAddress getIPAddressForMAC(Integer ll2pAddress) throws LabException{
        InetAddress tempInet = null;
        boolean found = false;
        AdjacencyTableEntry tmp = null;
        Iterator<AdjacencyTableEntry> findInet = table.iterator();

        while(findInet.hasNext() && !found)
        {
            tmp = findInet.next();
            if(tmp.getLl2pAddress().equals(ll2pAddress)){
                tempInet = tmp.getIpAddressInet();
                found = true;
                return tempInet;
            }
        }

        if(!found){
            throw new LabException("Entry was not found");
        }

       return tempInet;
    }

    public List<AdjacencyTableEntry> returnAdjacencyList(){
        List<AdjacencyTableEntry> returnList = new ArrayList<AdjacencyTableEntry>();
        returnList.addAll(table);

        return returnList;
    }

}
