package gg.gaylord.mitch.network;

import java.util.Calendar;

import gg.gaylord.mitch.support.Factory;

/**
 * Created by mitchell.gaylord on 3/3/2016.
 */
public class RouteTableEntry implements Comparable<RouteTableEntry> {

    private Integer sourceLL3P;
    private NetworkDistancePair pair;
    private Integer lastTimeTouched;
    private Integer nextHop;

    public RouteTableEntry(){
        sourceLL3P = 0;
        pair = new NetworkDistancePair();
        lastTimeTouched = 0;
        nextHop = 0;
    }

    public RouteTableEntry(Integer ll3p, NetworkDistancePair netPair, Integer lastTouch, Integer next){
        sourceLL3P = ll3p;
        pair = netPair;
        nextHop = next;
        lastTimeTouched = lastTouch;
    }

    public Integer getSourceLL3P() {
        return sourceLL3P;
    }

    public void setSourceLL3P(Integer ll3p) {
        this.sourceLL3P = ll3p;
    }

    public NetworkDistancePair getNetworkDistancePair() {
        return pair;
    }

    public void setNetworkDistancePair(NetworkDistancePair p) {
        this.pair = p;
    }

    public Integer getLastTimeTouched() {
        return lastTimeTouched;
    }

    public void setLastTimeTouched(Integer lastTimeTouched) {
        this.lastTimeTouched = lastTimeTouched;
    }

    public Integer getNextHop() {
        return nextHop;
    }

    public void setNextHop(Integer next) {
        this.nextHop = next;
    }

    public void getObjectReferences(Factory factory){
        pair = factory.getNetworkDistancePair();

    }

    public int compareTo(RouteTableEntry route){

        return this.pair.getNetworkNumber().compareTo(route.getNetworkDistancePair().getNetworkNumber());
    }

    public void updateLastTimeTouched(){
        lastTimeTouched = (int) Calendar.getInstance().getTimeInMillis()/1000;
    }

    public int getCurrentAgeInSeconds(){
        int tempTime = (int) Calendar.getInstance().getTimeInMillis()/1000;

        return tempTime - lastTimeTouched;
    }

    public String toString(){

        return new String("Source LL3P: " + this.sourceLL3P + " Network Distance Pair: " + this.pair.toString() +
                " Last Time Touched: " + this.lastTimeTouched + " Next Hop: " + this.nextHop);
    }

    public boolean isNotExpired(){
        if (this.getCurrentAgeInSeconds() > 10){
            return true;
        } else {
            return false;
        }
    }
}
