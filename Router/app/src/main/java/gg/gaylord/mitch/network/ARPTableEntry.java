package gg.gaylord.mitch.network;

import java.util.Calendar;

/**
 * Created by mitchell.gaylord on 3/1/2016.
 */
public class ARPTableEntry implements Comparable<ARPTableEntry> {

    private Integer ll2pAddress;
    private Integer ll3pAddress;
    private int lastTimeTouched;

    public ARPTableEntry(Integer ll2p, Integer ll3p){
        ll2pAddress = ll2p;
        ll3pAddress = ll3p;

        //gets time in seconds
        lastTimeTouched = (int) Calendar.getInstance().getTimeInMillis()/1000;
    }

    public Integer getLL3PAddress() {
        return ll3pAddress;
    }

    public Integer getLL2PAddress() {
        return ll2pAddress;
    }

    public void updateTime(){
        lastTimeTouched = (int) Calendar.getInstance().getTimeInMillis()/1000;
    }

    public int getCurrentAgeInSeconds(){
        int temp = (int) Calendar.getInstance().getTimeInMillis()/1000;

        return temp - lastTimeTouched;
    }

    @Override
    public int compareTo(ARPTableEntry entry) {

        return this.ll2pAddress.compareTo(entry.getLL2PAddress());
    }
}
