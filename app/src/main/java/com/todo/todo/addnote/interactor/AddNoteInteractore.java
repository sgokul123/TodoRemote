package com.todo.todo.addnote.interactor;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.todo.todo.addnote.presenter.AddNotePresenterInterface;
import com.todo.todo.database.DatabaseHandler;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.util.Connection;

/**
 * Created by bridgeit on 20/4/17.
 */

public class AddNoteInteractore  implements AddNoteInteractorInteface{
    Context mContext;
    private  String uid,date;
    ToDoItemModel todoitemModel;
    DatabaseHandler db;
    DatabaseReference mRef;
    private DatabaseReference mDatabase;
    AddNotePresenterInterface mAddNotePresenterInteface;
    private String TAG="AddNoteInteractore";

    public AddNoteInteractore(AddNotePresenterInterface addNotePresenterInteface, Context context) {
        this.mContext=context;
        this.mAddNotePresenterInteface=addNotePresenterInteface;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void setData(int size) {
        try{
            Log.i(TAG, "setSize: "+size);
            todoitemModel.setId(size);
            mDatabase.child("usersdata").child(uid).child(date).child(String.valueOf(size)).setValue(todoitemModel);
            mAddNotePresenterInteface.getResponce(true);
            mAddNotePresenterInteface.closeNoteProgressDialog();

        }catch (Exception f){
            mAddNotePresenterInteface.closeNoteProgressDialog();
    }
    }

    @Override
    public void getResponce(boolean flag) {
            mAddNotePresenterInteface.getResponce(flag);
            mAddNotePresenterInteface.closeNoteProgressDialog();
    }

    @Override
    public void storeNote(String date, ToDoItemModel toDoItemModel) {
        mAddNotePresenterInteface.showNoteProgressDialog();
        db = new DatabaseHandler(mContext,this);
        db.addNoteToLocal(toDoItemModel);
    }

    @Override
    public void uploadNotes(String uid, String date, ToDoItemModel toDoItemModel) {
        Connection con=new Connection(mContext);
        if(con.isNetworkConnected()){
            mAddNotePresenterInteface.showNoteProgressDialog();
            this.date=date;
            this.uid=uid;
            this.todoitemModel=toDoItemModel;

            FireBaseGetIndex fireBaseGetIndex=new FireBaseGetIndex(this);
            fireBaseGetIndex.getIndex(uid,date);
        }else{
            storeNote(date,toDoItemModel);
        }
    }
}
