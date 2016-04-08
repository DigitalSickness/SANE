package gg.gaylord.mitch.support;

/**
 * Created by mitchell.gaylord on 1/21/2016.
 */
import android.util.Base64;

import java.util.Calendar;

public class Utilities {

    /**
     * This static variable is the value for the number of seconds in the current time since some time back in the 70's.
     * It's used to calculate the number of seconds since the program began by the method which follows.
     */
    public static long baseDateSeconds = Calendar.getInstance().getTimeInMillis()/1000;

    /**
     * This method returns the number of seconds since the program began.
     * @return
     */
    public static int getTimeInSeconds(){
        return (int) (Calendar.getInstance().getTimeInMillis()/1000 - baseDateSeconds);
    }

    public static String padHexString(String pad, int length){
        String temp = pad;
        int tempLength = pad.length();

        int iterations = length - tempLength;

        for(int i = 0; i < iterations; i++){
            temp = "0"+temp;
        }

        return temp;
    }

    public static String prependString(String pad, int length){
        String temp = pad;
        int tempLength = pad.length();

        int iterations = length - tempLength;

        for(int i = 0; i < iterations; i++){
            temp = " "+temp;
        }

        return temp;
    }

    public static String byteToString(byte[] bytes){
        return new String(Base64.decode(bytes, Base64.DEFAULT));
    }

    public static byte[] stringToByte(String string){
        return Base64.encode(string.getBytes(), Base64.DEFAULT);
    }
}