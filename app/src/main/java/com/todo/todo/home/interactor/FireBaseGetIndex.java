package com.todo.todo.home.interactor;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.todo.todo.home.model.ToDoItemModel;

import java.util.ArrayList;

/**
 * Created by bridgeit on 29/3/17.
 */

public class FireBaseGetIndex  {
    private String TAG ="FireBaseGetIndex";
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    ToDoInteractor toDoInteractor;
    public FireBaseGetIndex(ToDoInteractor toDoInteractor) {
        this.toDoInteractor=toDoInteractor;

    }

    public void getIndex(String uid, final String date){
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("usersdata").child(uid).child(date);

           mRef.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   if (dataSnapshot.exists()) {
                       if (toDoInteractor != null) {
                           int size= (int) dataSnapshot.getChildrenCount();
                           Log.i(TAG, "onDataChange: "+size);
                           toDoInteractor.setData(size);
                           toDoInteractor = null;
                       }
                   }
                   else {
                       toDoInteractor.setData(0);
                       toDoInteractor = null;
                   }
                }
               @Override
               public void onCancelled(DatabaseError error) {
                   Log.i(TAG, "onCancelled: ");

               }
           });

    }

}
