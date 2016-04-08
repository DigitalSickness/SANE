package gg.gaylord.mitch.network;

import android.net.Network;

import gg.gaylord.mitch.support.Factory;
import gg.gaylord.mitch.support.LabException;
import gg.gaylord.mitch.support.NetworkConstants;
import gg.gaylord.mitch.support.UIManager;
import gg.gaylord.mitch.support.Utilities;

/**
 * Created by mitchell.gaylord on 2/18/2016.
 */
public class LL2Daemon {

    LL2P ll2pObject;
    LL1Daemon layer1Daemon;
    Integer localLL2P;
    UIManager uiManager;
    ARPDaemon arpDaemon;
    LRPDaemon lrpDaemon;

    public LL2Daemon(){
        ll2pObject = new LL2P();
    }

    public void getObjectReferences(Factory factory){
        uiManager = factory.getUiManager();
        ll2pObject = factory.getLL2PObject();
        layer1Daemon = factory.getLl1Daemon();
        arpDaemon = factory.getArpDaemon();
        lrpDaemon = factory.getLrpDaemon();
    }

    public void setLocalLL2PAddress(Integer ll2p){
        localLL2P = ll2p;
    }

    public void sendLL2PFrame(byte[] payload, Integer destinationLL2P, Integer ll2pType){
        ll2pObject = new LL2P(Utilities.padHexString(destinationLL2P.toString(), NetworkConstants.LL2P_ADDRESS_LENGTH), NetworkConstants.MY_LL2P_ADDRESS,
                ll2pType.toString(), payload.toString());

        uiManager.updateLL2PDisplay(ll2pObject);

        try{
            layer1Daemon.sendLL2PFrame(ll2pObject);
        } catch (LabException e) {
            e.printStackTrace();
        }
    }

    public void sendLL2PFrame(LL2P frame){
        try{
            layer1Daemon.sendLL2PFrame(frame);
        } catch (LabException e){
            e.printStackTrace();
        }
    }

    public void sendLL2PEchoRequest(String payload, Integer ll2pAddress){
        ll2pObject = new LL2P(Utilities.padHexString(ll2pAddress.toString(), NetworkConstants.LL2P_ADDRESS_LENGTH), NetworkConstants.MY_LL2P_ADDRESS,
                NetworkConstants.LL2P_ECHO_REQUEST_TYPE, payload);
        uiManager.updateLL2PDisplay(ll2pObject);
        uiManager.raiseToast("Sending Echo Request");

        sendLL2PFrame(ll2pObject);
    }

    private void replyToEchoRequest(LL2P echo){
        ll2pObject = new LL2P (Utilities.padHexString(echo.getDestAddressHexString(), NetworkConstants.LL2P_ADDRESS_LENGTH),NetworkConstants.MY_LL2P_ADDRESS,
                NetworkConstants.LL2P_ECHO_REPLY_TYPE, echo.getPayloadHexString());

        uiManager.updateLL2PDisplay(ll2pObject);
        uiManager.raiseToast("Sending Echo Reply");

       sendLL2PFrame(ll2pObject);
    }

    public void receiveLL2PFrame(LL2P frame){
        String frameCRC = frame.getCRCHexString();
        String routerCRC = ll2pObject.getCRCHexString();
        // added the toUpperCase, to always get into the following if statement, if the
        // frame destination address was lower case some out, found out this in debugging.
        String frameDestUpper = frame.getDestAddressHexString().toUpperCase();

    if(/*routerCRC.compareTo(frameCRC) == 0 &&*/ (frameDestUpper.equals(NetworkConstants.MY_LL2P_ADDRESS)))
        {
            String tempType = frame.getTypeHexString();
            byte[] payloadBytes = frame.getPayloadBytes();
            String payload = payloadBytes.toString();

            if (tempType.compareTo(NetworkConstants.ARP_UPDATE_TYPE)==0){
                //Send to the ARP Update
                uiManager.raiseToast("ARP Update Arrived");
            }

            if (tempType.compareTo(NetworkConstants.LL3P_PACKET_TYPE)==0){
                //Send to the LL3Daemon when it exists
                uiManager.raiseToast("LL3 Packet Arrived");
            }

            if (tempType.compareTo(NetworkConstants.LRP_TYPE)==0){
                //send to the LRP when it exists
                uiManager.raiseToast("LRP Update Arrived");
                lrpDaemon.receiveNewLRP(frame.getPayloadBytes(), Integer.parseInt(frame.getSrcAddressHexString(),16));
            }

            if (tempType.compareTo(NetworkConstants.LL2P_ECHO_REQUEST_TYPE)==0){
                //reply to the echo request
                ll2pObject= new LL2P(Utilities.padHexString(frame.getSrcAddressHexString(), NetworkConstants.LL2P_ADDRESS_LENGTH)
                        ,NetworkConstants.MY_LL2P_ADDRESS, tempType, payload);
                uiManager.updateLL2PDisplay(ll2pObject);
                uiManager.raiseToast("Echo Request Received");

                replyToEchoRequest(ll2pObject);
            }

            if (tempType.compareTo(NetworkConstants.LL2P_ARP_UPDATE)==0){
                uiManager.raiseToast("LL2P Arp Update Received");
                //adds or updates the frame in the arp table
                arpDaemon.addOrUpdate(frame.getSrcAddress(), Integer.parseInt(frame.getPayloadHexString(), 16));

                sendArpReply(frame.getSrcAddress());
            }

            if (tempType.compareTo(NetworkConstants.LL2P_ARP_REPLY)==0){
                uiManager.raiseToast("LL2P Arp Reply was Received");
                //add or updates the grame in the arp table
                arpDaemon.addOrUpdate(frame.getSrcAddress(), Integer.parseInt(frame.getPayloadHexString(), 16));
            }

        } else{
            uiManager.raiseToast("Invalid Frame Arrived");
        }
    }

    public void receiveLL2PFrame(byte[] frameBytes){
        LL2P newFrame = new LL2P(frameBytes);
        receiveLL2PFrame(newFrame);
    }

    public void sendArpUpdate(Integer ll2pNode){
        ll2pObject = new LL2P(Utilities.padHexString(Integer.toHexString(ll2pNode), NetworkConstants.LL2P_ADDRESS_LENGTH),
                NetworkConstants.MY_LL2P_ADDRESS, NetworkConstants.LL2P_ARP_UPDATE, NetworkConstants.MY_LL3P_ADDRESS);

        try {
            layer1Daemon.sendLL2PFrame(ll2pObject);
        } catch (LabException e){
            e.printStackTrace();
        }
    }

    public void sendArpReply(Integer ll2pNode){
        ll2pObject = new LL2P(Utilities.padHexString(Integer.toHexString(ll2pNode), NetworkConstants.LL2P_ADDRESS_LENGTH),
                NetworkConstants.MY_LL2P_ADDRESS, NetworkConstants.LL2P_ARP_REPLY, NetworkConstants.MY_LL3P_ADDRESS);

        try {
            layer1Daemon.sendLL2PFrame(ll2pObject);
        } catch (LabException e){
            e.printStackTrace();
        }
    }

}
