package gg.gaylord.mitch.network;

import gg.gaylord.mitch.support.NetworkConstants;
import gg.gaylord.mitch.support.Utilities;

/**
 * Created by mitchell.gaylord on 3/3/2016.
 */
public class NetworkDistancePair {

    private Integer networkNumber;
    private Integer distance;

    public NetworkDistancePair(){
        networkNumber = 0;
        distance = 0;
    }

    public Integer getNetworkNumber() {
        return networkNumber;
    }

    public void setNetworkNumber(Integer networkNum) {
        this.networkNumber = networkNum;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer dist) {
        this.distance = dist;
    }

    public NetworkDistancePair(Integer netNum, Integer dist){
        networkNumber = netNum;
        distance = dist;

    }

    public String toString(){
        return new String(Utilities.padHexString(Integer.toHexString(networkNumber), 2) +
                Utilities.padHexString(Integer.toHexString(distance), 3));
    }
}
