package gg.gaylord.mitch.network;

/**
 * Created by mitchell.gaylord on 1/28/2016.
 */
public class CRC16 {

    private int CRC;
    private int crcPattern = 0x11021;

    public CRC16 (){
        CRC = 0;
    }

    public int getCRC (){

        return CRC;
    }

    public String getCRCHexString(){

        return Integer.toHexString(CRC);
    }

    public int setCRC(int inputCRC){
        CRC = inputCRC;

        return CRC;
    }

    public void resetCRC (){
        CRC = 0;
    }

    public void update(byte aByte){
        int tempCRC;

        tempCRC = CRC^(aByte<<8);

        for(int i=0; i < 8; i++){
            if((tempCRC & 0x8000) != 0){
                tempCRC = (tempCRC<<1)^crcPattern;
            } else {
                tempCRC = tempCRC << 1;
            }
        }
        CRC = tempCRC;
    }

    public void Update(byte[] hexCRC){
       for (int i = 0; i < hexCRC.length; i++){
           update(hexCRC[i]);
       }
    }


    public String toString() {

        return new String (Integer.toString(CRC));
    }


}
