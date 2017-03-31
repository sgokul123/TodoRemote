package com.todo.todo.home.interactor;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.todo.todo.database.DatabaseHandler;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.home.presenter.ToDoPresenter;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by bridgeit on 20/3/17.
 */

public class ToDoInteractor implements  TodoInteractorInterface {
    ToDoPresenter mToDoPresenter;
    DatabaseHandler db;
    DatabaseReference mRef;
    int size=0;
    ToDoItemModel todoitemModel;
    Context mContext;
    private  String TAG ="ToDoInteractor";
    private  String uid,date;
    private DatabaseReference mDatabase;
    public ToDoInteractor(ToDoPresenter mToDoPresenter, Context context) {
        Log.i(TAG, "ToDoInteractor: ");
        this.mToDoPresenter = mToDoPresenter;
        this.mContext=context;
        // db = new DatabaseHandler(mContext,ToDoInteractor.this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void getCallToDatabase() {
        // call to he database to retrive data load into grid view
        //it will responce all notes back to presenter
         db = new DatabaseHandler(mContext,this);
        mToDoPresenter.showProgressDialog();
        Log.i(TAG, "getCallToDatabase: ");
        List<ToDoItemModel> toDos = db.getAllToDos();

        for (ToDoItemModel cn : toDos) {
            String log = "Id: " + cn.get_id() + " ,note: " + cn.get_note() + " ,title: " +
                    cn.get_title()+"reminder:  "+cn.get_reminder()+"startdate:  "+cn.get_startdate();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }
        mToDoPresenter.getCallBackNotes(toDos);
        mToDoPresenter.closeProgressDialog();
        //mToDoPresenter.getCallBackNotes();

    }

    @Override
    public void storeNote(String date, ToDoItemModel toDoItemModel) {
        Log.i(TAG, "storeNote: s  tore");
        mToDoPresenter.showNoteProgressDialog();
        db = new DatabaseHandler(mContext,this);
        db.addNoteToLocal(toDoItemModel);

    }


    @Override
    public void uploadNotes(String uid, String date, final ToDoItemModel toDoItemModel) {
        mToDoPresenter.showNoteProgressDialog();
        this.date=date;
        this.uid=uid;
        this.todoitemModel=toDoItemModel;
        Log.i(TAG, "uploadNotes: call firebase");

        FireBaseGetIndex fireBaseGetIndex=new FireBaseGetIndex(this);
        fireBaseGetIndex.getIndex(uid,date);

    }

    @Override
    public void getResponce(boolean flag) {
        mToDoPresenter.getResponce(flag);
        mToDoPresenter.closeNoteProgressDialog();
    }

    @Override
    public void setData(int size) {

        try{
            Log.i(TAG, "setSize: "+size);
            todoitemModel.set_id(size);
            mDatabase.child("usersdata").child(uid).child(date).child(String.valueOf(size)).setValue(todoitemModel);
            mToDoPresenter.getResponce(true);
            mToDoPresenter.closeNoteProgressDialog();

        }catch (Exception f){

            Log.i(TAG, "uploadNotes: exceptoion");
            mToDoPresenter.closeNoteProgressDialog();
            Log.i(TAG, "registerData: "+f);
        }

    }

    @Override
    public void getFireBaseDatabase(String uid) {
        mToDoPresenter.showProgressDialog();
        mRef = mDatabase.child("usersdata").child(uid);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<ToDoItemModel>> t = new GenericTypeIndicator<ArrayList<ToDoItemModel>>() {
                };

                Log.i(TAG, "onDataChange: ");
                ArrayList<ToDoItemModel> todoItemModel = new ArrayList<ToDoItemModel>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    todoItemModel.addAll(child.getValue(t));
                }
                mToDoPresenter.getCallBackNotes(todoItemModel);
                mToDoPresenter.closeProgressDialog();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.i(TAG, "onCancelled: ");
                mToDoPresenter.closeProgressDialog();
            }
        });

    }


}
