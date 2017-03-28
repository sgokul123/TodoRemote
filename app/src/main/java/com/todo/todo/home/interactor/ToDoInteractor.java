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
    private  String TAG ="ToDoInteractor";
    ToDoPresenter mToDoPresenter;
    DatabaseHandler db;
    DatabaseReference mRef;
    int size=0;
    ToDoItemModel todoitemModel;
    private  String uid,date;
    private DatabaseReference mDatabase;
    Context context;
    public ToDoInteractor(ToDoPresenter mToDoPresenter, Context mContext) {
        Log.i(TAG, "ToDoInteractor: ");
        this.mToDoPresenter = mToDoPresenter;
       // db = new DatabaseHandler(mContext,ToDoInteractor.this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void getCallToDatabase() {
        // call to he database to retrive data load into grid view
        //it will responce all notes back to presenter
        //  db = new RegistrationDatabase(context);

        Log.i(TAG, "getCallToDatabase: ");
        List<ToDoItemModel> toDos = db.getAllToDos();

        for (ToDoItemModel cn : toDos) {
            String log = "Id: " + cn.get_id() + " ,note: " + cn.get_note() + " ,title: " +
                    cn.get_title()+"reminder:  "+cn.get_reminder();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }
        mToDoPresenter.getCallBackNotes(toDos);
        //mToDoPresenter.getCallBackNotes();

    }

    @Override
    public void storeNote(ToDoItemModel toDoItemModel) {

        Log.i(TAG, "storeNote: s  tore");
        //Store Notes in Database
        // it will give boolean responce of result

      //  mToDoPresenter.showNoteProgressDialog();
        db.addToDo(toDoItemModel);
        //mToDoPresenter.closeNoteProgressDialog();
        //  mToDoPresenter.getResponce(true);

    }

    @Override
    public void uploadNotes(String uid, String date, String timestamp, final ToDoItemModel toDoItemModel) {
        mToDoPresenter.showNoteProgressDialog();
        date=date;
        uid=uid;
        todoitemModel=toDoItemModel;
        try{
            Log.i(TAG, "setSize: "+size);
           String str= mDatabase.child("usersdata").child(uid).child(date).push().getKey();
            Log.i(TAG, "uploadNotes: "+str);
            mDatabase.child("usersdata").child(uid).child(date).child().setValue(todoitemModel);
            mToDoPresenter.getResponce(true);
            mToDoPresenter.closeNoteProgressDialog();
        }catch (Exception f){
            Log.i(TAG, "uploadNotes: exceptoion");
            mToDoPresenter.closeNoteProgressDialog();
            Log.i(TAG, "registerData: "+f);
        }
    }


        public  void getSize(){
          
        }
    private void saveData() {
        Log.i(TAG, "setSize: "+size);
        mDatabase.child("usersdata").child(uid).child(date).child(String.valueOf(size)).setValue(todoitemModel);
        mToDoPresenter.getResponce(true);
        mToDoPresenter.closeNoteProgressDialog();

    }

    @Override
    public void getResponce(boolean flag) {
        mToDoPresenter.getResponce(flag);
    }




}
