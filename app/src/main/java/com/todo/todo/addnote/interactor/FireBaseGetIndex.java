package com.todo.todo.addnote.interactor;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.todo.todo.home.interactor.UpdateLocalDataOnLocalDataOnServerInteractor;
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
    AddNoteInteractorInteface mAddNoteInteractorInteface;
    UpdateLocalDataOnLocalDataOnServerInteractor mUpdateLocalDataOnServerInteractor;
    public FireBaseGetIndex(AddNoteInteractorInteface addNoteInteractorInteface) {
        this.mAddNoteInteractorInteface = addNoteInteractorInteface;
    }

    public FireBaseGetIndex(UpdateLocalDataOnLocalDataOnServerInteractor updateLocalDataOnServerInteractor) {
        this.mUpdateLocalDataOnServerInteractor = updateLocalDataOnServerInteractor;
    }

    public void getIndex(final String uid, final String date){
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("usersdata");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(uid).child(date).exists()) {
                    if (mAddNoteInteractorInteface != null) {

                        int size= (int) dataSnapshot.child(uid).child(date).getChildrenCount();
                        mAddNoteInteractorInteface.setData(size);
                        mAddNoteInteractorInteface = null;
                    }
                }
                else {
                    mAddNoteInteractorInteface.setData(0);
                    mAddNoteInteractorInteface = null;
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
                if (dataSnapshot.exists()) {

                    if(LocalItemModels.size()>0){

                        ToDoItemModel todoModel=LocalItemModels.get(0);
                        LocalItemModels.remove(0);
                        if (dataSnapshot.child(todoModel.getStartdate()).exists()) {
                            int size= (int) dataSnapshot.child(todoModel.getStartdate()).getChildrenCount();
                            setData(uid,size,todoModel);
                        }else {
                            setData(uid,0,todoModel);
                        }
                    }else{

                    }
                }
                else {
                    // mUpdateLocalDataOnServerInteractor
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
            todoModel.setId(size);
            mRef.child(uid).child(todoModel.getStartdate()).child(String.valueOf(size)).setValue(todoModel);
        }catch (Exception f){
            Log.i(TAG, "setData: ");
        }
    }

}
