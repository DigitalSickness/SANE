package gg.gaylord.mitch.network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import gg.gaylord.mitch.support.LabException;

/**
 * Created by mitchell.gaylord on 3/1/2016.
 */
public class ARPTable implements Runnable{

    Set<ARPTableEntry> arpTable;

    public ARPTable() {
        arpTable = new TreeSet<ARPTableEntry>();
    }

    public ARPTableEntry addEntry (Integer ll2p, Integer ll3p){
        ARPTableEntry newEntry = new ARPTableEntry(ll2p, ll3p);

        arpTable.add(newEntry);

        return newEntry;
    }

    public Integer getLL2PAddressFor (Integer ll3p) throws LabException{

        Integer tempLL2P = null;
        boolean found = false;
        ARPTableEntry tmp = null;
        Iterator<ARPTableEntry> findLL2P = arpTable.iterator();

        while(findLL2P.hasNext() && !found)
        {
            tmp = findLL2P.next();
            if(tmp.getLL3PAddress().equals(ll3p)){
                tempLL2P = tmp.getLL2PAddress();
                found = true;
                return tempLL2P;
            }
        }

        if (!found){
            throw new LabException("LL3P Address Was Not Found");
        }

        return tempLL2P;
    }

    public ARPTableEntry removeLL2P (Integer ll2p) throws LabException{
        boolean found = false;
        Iterator<ARPTableEntry> remove = arpTable.iterator();
        ARPTableEntry tmp = null;

        while(remove.hasNext() && !found){
            tmp = remove.next();
            if(tmp.getLL2PAddress().equals(ll2p)){
                found = true;
                arpTable.remove(tmp);
            }

            if(!found){
                throw new LabException("Entry Not Found");
            }
        }

        return tmp;
    }

    public ARPTableEntry removeLL3P (Integer ll3p) throws LabException{

        boolean found = false;
        Iterator<ARPTableEntry> remove = arpTable.iterator();
        ARPTableEntry tmp = null;

        while(remove.hasNext() && !found){
            tmp = remove.next();
            if(tmp.getLL2PAddress().equals(ll3p)){
                found = true;
                arpTable.remove(tmp);
            }

            if(!found){
                throw new LabException("Entry Not Found");
            }
        }

        return tmp;
    }

    public List<ARPTableEntry> getARPTableList() {
        List<ARPTableEntry> newList = new ArrayList<ARPTableEntry>();

        newList.addAll(arpTable);

        return newList;
    }

    public boolean ll2pIsInTable (Integer ll2p){
        boolean found = false;
        Iterator<ARPTableEntry> find = arpTable.iterator();
        ARPTableEntry tmp = null;

        while(find.hasNext() && !found) {
            tmp = find.next();
            if (tmp.getLL2PAddress().equals(ll2p)) {
                found = true;
            }
        }

        return found;
    }

    public boolean ll3pIsInTable (Integer ll3p){
        boolean found = false;
        Iterator<ARPTableEntry> find = arpTable.iterator();
        ARPTableEntry tmp = null;

        while(find.hasNext() && !found) {
            tmp = find.next();
            if (tmp.getLL3PAddress().equals(ll3p)) {
                found = true;
            }
        }

        return found;
    }

    public void expireAndRemove(){
        Iterator<ARPTableEntry> entries = arpTable.iterator();
        ARPTableEntry tmp = null;

        while (entries.hasNext()){
            tmp = entries.next();
            if(tmp.getCurrentAgeInSeconds() > 60) {
                arpTable.remove(tmp);
            }

        }
    }

    public void addOrUpdate(Integer ll2p, Integer ll3p){
        Iterator<ARPTableEntry> entries = arpTable.iterator();
        ARPTableEntry tmp = null;
        boolean found = false;

        while (entries.hasNext()){
            tmp = entries.next();

            if(tmp.getLL2PAddress().equals(ll2p) && tmp.getLL3PAddress().equals(ll3p)) {
                tmp.updateTime();
                found = true;
            }
        }

        if (!found){
            this.addEntry(ll2p, ll3p);
        }
    }

    @Override
    public void run(){
        this.expireAndRemove();
    }
}
