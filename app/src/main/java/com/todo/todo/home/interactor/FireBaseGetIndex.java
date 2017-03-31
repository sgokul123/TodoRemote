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
import java.util.List;

/**
 * Created by bridgeit on 29/3/17.
 */

public class FireBaseGetIndex  {
    private String TAG ="FireBaseGetIndex";
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    ToDoInteractor mToDoInteractor;
    UpdateOnServerInteractor mUpdateOnServerInteractor;
    public FireBaseGetIndex(ToDoInteractor toDoInteractor) {
        this.mToDoInteractor=toDoInteractor;

    }

    public FireBaseGetIndex(UpdateOnServerInteractor updateOnServerInteractor) {
        this.mUpdateOnServerInteractor=updateOnServerInteractor;
    }



    public void getIndex(String uid, final String date){
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("usersdata").child(uid).child(date);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (mToDoInteractor != null) {
                        int size= (int) dataSnapshot.getChildrenCount();
                        Log.i(TAG, "onDataChange: "+size);
                        mToDoInteractor.setData(size);
                        mToDoInteractor = null;
                    }
                }
                else {
                    mToDoInteractor.setData(0);
                    mToDoInteractor = null;
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.i(TAG, "onCancelled: ");

            }
        });

    }

    public void getIndexer(final String uid, final List<ToDoItemModel> LocalItemModels){
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("usersdata").child(uid);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //  mUpdateOnServerInteractor.
                if (dataSnapshot.exists()) {

                    if(LocalItemModels.size()>0){

                        ToDoItemModel todoModel=LocalItemModels.get(0);
                        LocalItemModels.remove(0);
                        Log.i(TAG, "onDataChange: "+LocalItemModels.size());

                        if (dataSnapshot.child(todoModel.get_startdate()).exists()) {
                            int size= (int) dataSnapshot.child(todoModel.get_startdate()).getChildrenCount();
                            Log.i(TAG, "onDataChange: "+size);
                            setData(uid,size,todoModel);


                        }else {
                            setData(uid,0,todoModel);
                        }
                    }else{

                    }
                }
                else {
                    // mUpdateOnServerInteractor
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.i(TAG, "onCancelled: ");

            }
        });

    }

    private void setData(String uid, int size, ToDoItemModel todoModel) {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("usersdata");
        try{
            Log.i(TAG, "setSize: "+size);
            todoModel.set_id(size);
            mRef.child(uid).child(todoModel.get_startdate()).child(String.valueOf(size)).setValue(todoModel);

        }catch (Exception f){

            Log.i(TAG, "setData: ");
        }
    }

}
