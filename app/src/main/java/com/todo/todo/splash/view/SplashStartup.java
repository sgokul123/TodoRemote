package com.todo.todo.splash.view;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by bridgeit on 22/6/17.
 */

public class SplashStartup  extends android.app.Application{
    private DatabaseReference databaseReference;
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference=FirebaseDatabase.getInstance().getReferenceFromUrl("https://todo-165105.firebaseio.com/").child("usersdata");
        databaseReference.keepSynced(true);
    }
}
