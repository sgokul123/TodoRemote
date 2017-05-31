package com.todo.todo.alarmmanager;

/**
 * Created by bridgeit on 30/5/17.
 */
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import com.todo.todo.R;

public class AlarmReceiver extends BroadcastReceiver {
    Bundle bundle;
    @Override
    public void onReceive(Context context, Intent intent) {
        notifyReminder(context);
        //bundle=intent.getExtras();
        Toast.makeText(context,
                "AlarmReceiver.onReceive()",
                Toast.LENGTH_LONG).show();

    }


    public void notifyReminder(Context context){
        NotificationManager nManager=            (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder=new            NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_wallet);    builder.setContentTitle("Sample Notification");
        builder.setContentText("Sample Notification for reminder...");
        builder.setSubText("Sample Notification for reminder...");
        Bitmap bmp= BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_wallet);
        builder.setLargeIcon(bmp);
        Intent i=new Intent();
        i.putExtras(bundle);
        i.setComponent(new ComponentName(context.getApplicationContext(), NotificationNote.class));

        PendingIntent pIntent= PendingIntent.getActivity(context.getApplicationContext(), 0,i,0);
        builder.setContentIntent(pIntent);
        nManager.notify(1,builder.build());}
}