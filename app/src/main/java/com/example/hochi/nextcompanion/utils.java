package com.example.hochi.nextcompanion;

import android.util.Log;

import com.fasterxml.jackson.databind.JsonMappingException;

import java.net.SocketTimeoutException;

/**
 * Class for common used functions
 *
 */

public class utils {

    public static void HandelCallExeption(String TAG, Throwable t){
        if(t instanceof SocketTimeoutException){
            Log.v(TAG,"Connection to Server timed out", t);
            //TODO Show message to user.
        }else if(t instanceof JsonMappingException){
            Log.i(TAG,"Unable to parse response to POJO", t);
        }else{
            Log.e(TAG,"Unknown Error",t);
        }
    }

}
