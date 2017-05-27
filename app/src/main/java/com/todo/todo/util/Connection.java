package com.todo.todo.util;

import android.content.Context;
import android.net.ConnectivityManager;


public class Connection {

    Context context;

    public Connection(Context context) {

        this.context = context;
    }

    public boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

}
