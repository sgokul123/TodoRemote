package com.todo.todo.alarmmanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bridgeit on 28/4/17.
 */

public class AlarmStarter {


    public static void getStartalarm(String  startAfter) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = dateFormat.parse(startAfter);
        System.out.println(date.getTime());
    }
}
