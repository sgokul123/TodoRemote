package com.todo.todo.alarmmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.widget.Toast;

import com.todo.todo.R;

/**
 * Created by bridgeit on 28/4/17.
 */



public class MyBroadcastReceiver extends BroadcastReceiver {
    MediaPlayer mp;
    @Override
    public void onReceive(Context context, Intent intent) {
     /*   mp= MediaPlayer.create(context, R.values.alrm);
        mp.start();*/
        Toast.makeText(context, "Alarm received!", Toast.LENGTH_LONG).show();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        mp=MediaPlayer.create(context, R.raw.sun);
        mp.start();
        // Put here YOUR code.
        Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example

        wl.release();
    }
}
