package gg.gaylord.mitch.network;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gg.gaylord.mitch.support.Factory;
import gg.gaylord.mitch.support.LabException;
import gg.gaylord.mitch.support.UIManager;
import gg.gaylord.mitch.network.PacketInformation;


/**
 * Created by mitchell.gaylord on 2/4/2016.
 */
public class LL1Daemon {


    private final static int receivePort = 49999;
    private final static int portNumber = 49999;

    LL2P ll2pFrame;
    UIManager uiManager;
    LL2Daemon layer2Daemon;
    AdjacencyTable macAddressIPTable;
    List<AdjacencyTableEntry> macAddressIPList;

    DatagramSocket sendSocket;
    DatagramSocket receiveSocket;

    public LL1Daemon() {
        macAddressIPTable = new AdjacencyTable();
        openUDDPort();
    }

    public void getObjectReferences(Factory factory){
        ll2pFrame = factory.getLL2PObject();
        uiManager = factory.getUiManager();
        layer2Daemon = factory.getLl2Daemon();
        new listenForUDPPacket().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, receiveSocket);
    }

    public void openUDDPort(){
        sendSocket = null;

        try {
            sendSocket = new DatagramSocket();
        } catch (SocketException e){
            e.printStackTrace();
        }

        receiveSocket = null;

        try {
            receiveSocket = new DatagramSocket(receivePort);
        } catch (SocketException e){
            e.printStackTrace();
        }
    }

    /** Send UDP Packet
     * a private Async class to send packets out of the UI thread
     */

    private class sendUDDPacket extends  AsyncTask<PacketInformation, Void, Void>{

        @Override
        protected Void doInBackground(PacketInformation... arg0){
            PacketInformation pktInfo = arg0[0];

            try{
                pktInfo.getSendSocket().send(pktInfo.getSendPacket());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public List<AdjacencyTableEntry> getAdjacencyList(){
        return macAddressIPTable.returnAdjacencyList();
    }

    public void addAdjacency(Integer ll2p, String ip){
        macAddressIPTable.addEntry(ll2p, ip);
    }

    public void removeAdjacency (Integer mac) throws LabException {
        try {
            macAddressIPTable.removeEntry(mac);
        } catch (LabException e){
            e.printStackTrace();
        }
    }

    public void sendLL2PFrame() throws LabException{
        sendLL2PFrame(ll2pFrame);
    }

    public void sendLL2PFrame (LL2P frame) throws LabException{
        /*String frameToSend = new String(frame.getDestAddressHexString() + frame.getSrcAddressHexString()
                + frame.getTypeHexString() + frame.getPayloadHexString() + frame.getCRCHexString());*/
        boolean foundValidAddress = true;

        InetAddress ipAddress = null;

        try {
            ipAddress = macAddressIPTable.getIPAddressForMAC(frame.getDestAddress());
        } catch (LabException e) {
            foundValidAddress = false;
        }

        if (foundValidAddress){
            //create datagram for sending
            DatagramPacket sendPacket = new DatagramPacket(frame.toString().getBytes(), frame.toString().length(),ipAddress, portNumber);
            // send the packet to the remote system. use the async task private class for this
            new sendUDDPacket().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,new PacketInformation(sendSocket, sendPacket));
        } else {
            uiManager.raiseToast("Attempt to send unknown LL2P: " + frame.getDestAddressHexString(), Toast.LENGTH_LONG);
        }
    }


    private class listenForUDPPacket extends AsyncTask<DatagramSocket, Void, byte[]> {

        @Override
        protected byte[] doInBackground (DatagramSocket... socketList){
            byte[] receivedData = new byte[1024];
            DatagramSocket serverSocket=socketList[0];

            // create datagrame packet to receive the PDP data
            DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);

            /* attempt to read from the UDP port. Will hang while waiting for the data to arrive*/

            try {
                serverSocket.receive(receivePacket);
            } catch (IOException e){
                e.printStackTrace();
            }

            /* assign data from the packet to the return variable */
            int bytesReceived = receivePacket.getLength();
            byte[] rxData = new String (receivePacket.getData()).substring(0,bytesReceived).getBytes();
            return rxData;
        }

        @Override
        protected void onPostExecute(byte[] rxData){
            //later this will be showed and passed ot the layer 2 daemon
            // for now simple show it on the screen

            layer2Daemon.receiveLL2PFrame(rxData);        //add in lab 5
            //ll2pFrame.fillInLL2pFrame(rxData);            //used in lab 4, remove in lab 5
            //uiManager.updateLL2PDistplay();               // rmoved in lab 5

            String temp = new String(rxData);
            uiManager.raiseToast(temp);
            new listenForUDPPacket().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,receiveSocket);
        }
    }




}
