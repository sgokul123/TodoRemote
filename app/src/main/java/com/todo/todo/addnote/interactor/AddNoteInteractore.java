package com.todo.todo.addnote.interactor;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.todo.todo.addnote.presenter.AddNotePresenterInterface;
import com.todo.todo.database.DatabaseHandler;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.util.Connection;
import com.todo.todo.util.Constants;

/**
 * Created by bridgeit on 20/4/17.
 */

public class AddNoteInteractore  implements AddNoteInteractorInteface{
    private final SharedPreferences pref;
    private SharedPreferences.Editor editor;
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
        pref = mContext.getSharedPreferences(Constants.ProfileeKey.SHAREDPREFERANCES_KEY,mContext.MODE_PRIVATE);
    }

    @Override
    public void setData(int size) {
        try{
           todoitemModel.setId(size);
            int id=pref.getInt(Constants.Stringkeys.LAST_SRNO,0);
            todoitemModel.setSrid(id);
            mDatabase.child("usersdata").child(uid).child(date).child(String.valueOf(size)).setValue(todoitemModel);
            editor = pref.edit();
            editor.putInt(Constants.Stringkeys.LAST_INDEX,size+1);
            editor.putInt(Constants.Stringkeys.LAST_SRNO,id+1);
            editor.commit();
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
     //        Connection con=new Connection(mContext);
       // if(con.isNetworkConnected()){
        if(pref.getBoolean(Constants.BundleKey.USER__LOCAL_REGISTER,false)){
            mAddNotePresenterInteface.showNoteProgressDialog();
            this.date=date;
            this.uid=uid;
            this.todoitemModel=toDoItemModel;
            FireBaseGetIndex fireBaseGetIndex=new FireBaseGetIndex(this);
            fireBaseGetIndex.getIndexLocal(pref.getString(Constants.BundleKey.USER_USER_UID_LOCAL,"local"),date);
        }else {
            mAddNotePresenterInteface.showNoteProgressDialog();
            this.date=date;
            this.uid=uid;
            this.todoitemModel=toDoItemModel;
            FireBaseGetIndex fireBaseGetIndex=new FireBaseGetIndex(this);
            fireBaseGetIndex.getIndex(uid,date);
        }

      /*  }else{
            storeNote(date,toDoItemModel);
        }*/
    }

    @Override
    public void setDataLocal(int size) {
        try{
            todoitemModel.setId(size);
            mDatabase.child("localdata").child(pref.getString(Constants.BundleKey.USER_USER_UID_LOCAL,"")).child(date).child(String.valueOf(size)).setValue(todoitemModel);
            editor = pref.edit();
            editor.putInt(Constants.Stringkeys.LAST_INDEX,size+1);
            editor.commit();
            mAddNotePresenterInteface.getResponce(true);
            mAddNotePresenterInteface.closeNoteProgressDialog();
        }catch (Exception f){
            mAddNotePresenterInteface.closeNoteProgressDialog();
        }
    }
}
