package gg.gaylord.mitch.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

import gg.gaylord.mitch.support.NetworkConstants;
import gg.gaylord.mitch.support.Utilities;

/**
 * Created by mitchell.gaylord on 2/4/2016.
 */
public class AdjacencyTableEntry implements Comparable <AdjacencyTableEntry>{

    private Integer ll2pAddress;
    private InetAddress ipAddress_Inet;
    private String ipAddress_String;

    public AdjacencyTableEntry()
    {
        ll2pAddress = 0;
        ipAddress_String = "";

        try {
            ipAddress_Inet = InetAddress.getByName(ipAddress_String);
        } catch(UnknownHostException e){

        }
    }

    public AdjacencyTableEntry(Integer ll2p, String ip){
        ll2pAddress = ll2p;
        ipAddress_String = ip;
        try{
            ipAddress_Inet = InetAddress.getByName(ipAddress_String);
        } catch (UnknownHostException e){

        }
    }

    public String getIpAddressString() {
        return ipAddress_String;
    }

    public InetAddress getIpAddressInet() {
        return ipAddress_Inet;
    }

    public void setIpAddressString(String ip) {
        ipAddress_String = ip;

        try {
            ipAddress_Inet = InetAddress.getByName(ip);
        }catch (UnknownHostException e){

        }
    }

    public Integer getLl2pAddress() {
        return ll2pAddress;
    }

    public void setLl2pAddress(Integer ll2p) {
        ll2pAddress = ll2p;
    }

    public String toString(){

        String temp = new String("LL2P Address: " + Utilities.padHexString(Integer.toHexString(ll2pAddress), NetworkConstants.LL2P_ADDRESS_LENGTH)
                + " Ip Address: " + ipAddress_Inet.toString());

        return temp;
    }

    public int compareTo(AdjacencyTableEntry entry){

        return this.ll2pAddress.compareTo(entry.getLl2pAddress());

    }

}
