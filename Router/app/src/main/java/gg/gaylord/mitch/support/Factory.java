package gg.gaylord.mitch.support;

import android.app.Activity;

import gg.gaylord.mitch.network.ARPDaemon;
import gg.gaylord.mitch.network.ARPTable;
import gg.gaylord.mitch.network.CRC16;
import gg.gaylord.mitch.network.LL1Daemon;
import gg.gaylord.mitch.network.LL2Daemon;
import gg.gaylord.mitch.network.LL2P;
import gg.gaylord.mitch.network.Scheduler;

/**
 * Created by mitchell.gaylord on 1/21/2016.
 */
public class Factory {

    Activity parentActivity;
    UIManager uiManager;
    NetworkConstants networkConstants;
    LL2P ll2pObject;
    LL1Daemon ll1Daemon;
    LL2Daemon ll2Daemon;
    CRC16 crcObject;
    ARPTable arpTableObject;
    ARPDaemon arpDaemon;
    Scheduler scheduler;

    public Factory (Activity activity){
        parentActivity = activity;
        createAllObjects();
        getAllObjectReferences();
    }

    private void createAllObjects(){
        uiManager = new UIManager();
        networkConstants = new NetworkConstants(parentActivity);
        ll2pObject = new LL2P();
        ll1Daemon = new LL1Daemon();
        ll2Daemon = new LL2Daemon();
        arpDaemon = new ARPDaemon();
        crcObject = new CRC16();
        arpTableObject = new ARPTable();
    }

    private void getAllObjectReferences(){
        uiManager.getOjbectReferences(this);
        ll2pObject.getLL2PObjectReference(this);
        ll1Daemon.getObjectReferences(this);
        ll2Daemon.getObjectReferences(this);
        arpDaemon.getObjectReferences(this);
    }

    public Activity getParentActivity(){
        return parentActivity;
    }

    public UIManager getUiManager(){
        return uiManager;
    }

    public LL2P getLL2PObject () { return ll2pObject; }

    public LL1Daemon getLl1Daemon() { return ll1Daemon; }

    public LL2Daemon getLl2Daemon() { return ll2Daemon; }

    public CRC16 getCrcObject() { return crcObject; }

    public ARPTable getArpTableObject() { return arpTableObject;}

    public ARPDaemon getArpDaemon() { return arpDaemon; }

}
