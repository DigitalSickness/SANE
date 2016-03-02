package gg.gaylord.mitch.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by mitchell.gaylord on 2/9/2016.
 */
public class PacketInformation {

    private DatagramSocket sendSocket;
    private DatagramPacket sendPacket;

    public PacketInformation(DatagramSocket sendS, DatagramPacket sendP){
        sendSocket = sendS;
        sendPacket = sendP;
    }

    public DatagramSocket getSendSocket() {
        return sendSocket;
    }

    public void setSendSocket(DatagramSocket sendSocket) {
        sendSocket = sendSocket;
    }

    public DatagramPacket getSendPacket() {
        return sendPacket;
    }

    public void setSendPacket(DatagramPacket sendPacket) {
        sendPacket = sendPacket;
    }
}
