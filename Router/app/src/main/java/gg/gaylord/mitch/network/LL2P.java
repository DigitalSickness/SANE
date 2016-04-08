package gg.gaylord.mitch.network;

import android.net.Network;

import gg.gaylord.mitch.support.Factory;
import gg.gaylord.mitch.support.NetworkConstants;
import gg.gaylord.mitch.support.UIManager;
import gg.gaylord.mitch.support.Utilities;

/**
 * Created by mitchell.gaylord on 1/28/2016.
 */
public class LL2P {

    private CRC16 CRC;
    private Integer srcAddress;
    private Integer destAddress;
    private byte[] payload;
    private Integer typeField;
    private UIManager uiManager;

    public LL2P (String _destAddress, String _srcAddress, String type, String _payload){
        destAddress = Integer.parseInt(_destAddress, 16);
        srcAddress = Integer.parseInt(_srcAddress,16);
        payload = Utilities.stringToByte(_payload);
        typeField = Integer.parseInt(type, 16);
        CRC = new CRC16();
        calculateCRC();
    }

    public LL2P (){
        this("0", "0", "0", "");
    }

    public LL2P(byte[] hexArray){

        fillInLL2PFrame(hexArray);
    }

    /* This method will initialize all objects that this class will use */
    public void createFields(){

        CRC.resetCRC();             // initializes CRC to 0
        setSrcAddress("000000");    // initializes source address to six bytes
        setDestAddress("000000");   // initializes dest address to six bytes
        setPayload("");             // initializes payload to empty, in order to have
        setTypeField("0000");       // initializes type to four bytes
        CRC = new CRC16();
        calculateCRC();
    }

    public void setSrcAddress(String source){
        srcAddress = Integer.parseInt(source, 16);
    }

    public void setDestAddress(String destination) {
        destAddress = Integer.parseInt(destination, 16);
    }

    public void setPayload(String pay){
        payload = Utilities.stringToByte(pay);
    }

    public void setTypeField(String type){
        typeField = Integer.parseInt(type, 16);
    }

    public void setSrcAddress(int source){
        srcAddress = source;
    }

    public void setDestAddress(int destination){
        destAddress = destination;
    }

    public void setTypeField(int type){
        typeField = type;
    }

    public String getSrcAddressHexString(){
        return Integer.toHexString(srcAddress);
    }

    public String getDestAddressHexString(){
        return Integer.toHexString(destAddress);
    }

    public String getPayloadHexString(){
        return new String (payload);
    }

    public String getTypeHexString(){
        return Integer.toHexString(typeField);
    }

    public String getCRCHexString(){
        return CRC.getCRCHexString();
    }

    public int getDestAddress() {
        return destAddress;
    }

    public int getSrcAddress(){
        return srcAddress;
    }

    public int getPayload(){
        int temp = 0;
        for (int i = 0; i < payload.length; i++)
        {
            temp = temp + payload[i];
        }

        return temp;
    }

    public int getTypeField() {
        return typeField;
    }

    public void setPayload(byte[] pay){
        payload = pay;
    }

    public byte[] getPayloadBytes(){
        return payload;
    }

    public void getLL2PObjectReference(Factory factory){
        uiManager = factory.getUiManager();
        CRC = factory.getCrcObject();
    }

    void calculateCRC(){
        byte[] crcBytes = new String((destAddress+ srcAddress + typeField + payload.toString())).getBytes();

        CRC.Update(crcBytes);
    }

    public byte[] getFrameBytes(){

        String s, d, t;
        s = getSrcAddressHexString();
        d = getDestAddressHexString();
        t = getTypeHexString();

        byte[] src = s.getBytes();
        byte[] dest = d.getBytes();
        byte[] type = t.getBytes();

        byte[] frame = new byte[src.length + dest.length + type.length + payload.length];

        System.arraycopy(src,0,frame,0,src.length);
        System.arraycopy(dest,0,frame,src.length,dest.length);
        System.arraycopy(type,0,frame,src.length+dest.length, type.length);
        System.arraycopy(payload, 0, frame, src.length + dest.length + type.length, payload.length);

        return frame;
    }

    public void fillInLL2PFrame(byte[] frame){
        String frameChars = new String(frame);
        setDestAddress(frameChars.substring(0, NetworkConstants.LL2P_ADDRESS_LENGTH));
        setSrcAddress(frameChars.substring(NetworkConstants.LL2P_ADDRESS_LENGTH, NetworkConstants.LL2P_ADDRESS_LENGTH * 2));
        setTypeField(frameChars.substring(NetworkConstants.LL2P_ADDRESS_LENGTH * 2,
                NetworkConstants.LL2P_ADDRESS_LENGTH * 2 + NetworkConstants.LL2P_TYPE_LENGTH));
        setPayload(frameChars.substring(NetworkConstants.LL2P_ADDRESS_LENGTH * 2 + NetworkConstants.LL2P_TYPE_LENGTH,
                frame.length - NetworkConstants.CRC_LENGTH));
        CRC = new CRC16();
        calculateCRC();
    }

    public String toString(){
        String temp = new String(this.getDestAddressHexString() + this.getSrcAddressHexString() + this.getTypeHexString() + payload.toString());
        calculateCRC();

        temp = temp + CRC.getCRCHexString();

        return temp;
    }

}
