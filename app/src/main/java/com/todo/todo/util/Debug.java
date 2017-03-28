package com.todo.todo.util;

import android.util.Log;

/**
 * Created by bridgeit on 11/2/17.
 */
public class Debug {
    static boolean isDebugable = true;
    public static void showLog(String log,String message){
        if(isDebugable)
            Log.i("LOG", log+" "+message);
    }
}
