package com.todo.todo.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by bridgeit on 25/3/17.
 */

public class Connection {

    Context context;
    public Connection(Context context) {

        this.context=context;
    }

    public Connection() {

    }

    public boolean isNetworkConnected() {


        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

}
