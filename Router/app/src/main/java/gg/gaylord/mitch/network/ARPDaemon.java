package gg.gaylord.mitch.network;

import gg.gaylord.mitch.support.Factory;

/**
 * Created by mitchell.gaylord on 3/1/2016.
 */
public class ARPDaemon {

    ARPTable arpTable;
    LL2Daemon ll2DaemonObject;

    public ARPDaemon(){

    }

    public void getObjectReferences(Factory factory){
        arpTable = factory.getArpTableObject();
        ll2DaemonObject = factory.getLl2Daemon();
    }

    public void addOrUpdate(Integer ll2p, Integer ll3p){
        arpTable.addOrUpdate(ll2p, ll3p);
    }
}
