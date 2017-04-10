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
import com.todo.todo.home.presenter.ToDoPresenterInteface;
import com.todo.todo.util.Connection;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by bridgeit on 20/3/17.
 */

public class ToDoActivityInteractor implements  TodoInteractorInterface {
    ToDoPresenterInteface toDoPresenterInteface;
    DatabaseHandler db;
    DatabaseReference mRef;
    int size=0;
    ToDoItemModel todoitemModel;
    Context mContext;
    private  String TAG ="ToDoActivityInteractor";
    private  String uid,date;
    private DatabaseReference mDatabase;
    public ToDoActivityInteractor(ToDoPresenterInteface toDoPresenterInteface, Context context) {
        Log.i(TAG, "ToDoActivityInteractor: ");
        this.toDoPresenterInteface = toDoPresenterInteface;
        this.mContext=context;
        // db = new DatabaseHandler(mContext,ToDoActivityInteractor.this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void getCallToDatabase() {
        // call to he database to retrive data load into grid view
        //it will responce all notes back to presenter
         db = new DatabaseHandler(mContext,this);
        toDoPresenterInteface.showProgressDialog();
        Log.i(TAG, "getCallToDatabase: ");
        List<ToDoItemModel> toDos = db.getAllToDos();

        for (ToDoItemModel cn : toDos) {
            String log = "Id: " + cn.get_id() + " ,note: " + cn.get_note() + " ,title: " +
                    cn.get_title()+"reminder:  "+cn.get_reminder()+"startdate:  "+cn.get_startdate();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }
        toDoPresenterInteface.sendCallBackNotes(toDos);
        toDoPresenterInteface.closeProgressDialog();
        //toDoPresenterInteface.getCallBackNotes();

    }

    @Override
    public void storeNote(String date, ToDoItemModel toDoItemModel) {
        Log.i(TAG, "storeNote: s  tore");
        toDoPresenterInteface.showNoteProgressDialog();
        db = new DatabaseHandler(mContext,this);
        db.addNoteToLocal(toDoItemModel);

    }


    @Override
    public void uploadNotes(String uid, String date, final ToDoItemModel toDoItemModel) {
        Connection con=new Connection(mContext);
        if(con.isNetworkConnected()){
        toDoPresenterInteface.showNoteProgressDialog();
        this.date=date;
        this.uid=uid;
        this.todoitemModel=toDoItemModel;
        Log.i(TAG, "uploadNotes: call firebase");

        FireBaseGetIndex fireBaseGetIndex=new FireBaseGetIndex(this);
        fireBaseGetIndex.getIndex(uid,date);
        }else{
            Log.i(TAG, "loadNotetoFirebase: local");
            storeNote(date,toDoItemModel);
        }
    }

    @Override
    public void getResponce(boolean flag) {
        toDoPresenterInteface.getResponce(flag);
        toDoPresenterInteface.closeNoteProgressDialog();
    }

    @Override
    public void setData(int size) {

        try{
            Log.i(TAG, "setSize: "+size);
            todoitemModel.set_id(size);
            mDatabase.child("usersdata").child(uid).child(date).child(String.valueOf(size)).setValue(todoitemModel);
            toDoPresenterInteface.getResponce(true);
            toDoPresenterInteface.closeNoteProgressDialog();

        }catch (Exception f){

            Log.i(TAG, "uploadNotes: exceptoion");
            toDoPresenterInteface.closeNoteProgressDialog();
            Log.i(TAG, "registerData: "+f);
        }

    }

    @Override
    public void getToDoData(String uid) {
        Connection con=new Connection(mContext);
        if(con.isNetworkConnected()){
            List<ToDoItemModel> localNotes;
            localNotes = new ArrayList<ToDoItemModel>();
            DatabaseHandler db=new DatabaseHandler(mContext);
            localNotes=  db.getLocalData();
            if(localNotes.size()==0){
                Log.i(TAG, "getToDoData: ");
                getFireBaseDatabase(uid);
            }
            else{
                UpdateOnServerInteractor updateOnServerInteractor=new UpdateOnServerInteractor(mContext,this);
                updateOnServerInteractor.updatetoFirebase(uid,localNotes);
            }

        }
        else{
            Log.i(TAG, "getPresenterNotes: local");
            getCallToDatabase();
        }
    }

    @Override
    public void getFireBaseDatabase(String uid) {


        toDoPresenterInteface.showProgressDialog();
        mRef = mDatabase.child("usersdata").child(uid);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<ToDoItemModel>> t = new GenericTypeIndicator<ArrayList<ToDoItemModel>>() {
                };

                Log.i(TAG, "onDataChange: ");
                ArrayList<ToDoItemModel> todoItemModel = new ArrayList<ToDoItemModel>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.i(TAG, "onDataChange: ");
                    todoItemModel.addAll(child.getValue(t));
                }
                toDoPresenterInteface.getCallBackNotes(todoItemModel);
                toDoPresenterInteface.closeProgressDialog();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.i(TAG, "onCancelled: ");
                toDoPresenterInteface.closeProgressDialog();
            }
        });

    }

    @Override
    public void callPresenterNotesAfterUpdateServer(String uid) {
        getFireBaseDatabase(uid);
    }


}
