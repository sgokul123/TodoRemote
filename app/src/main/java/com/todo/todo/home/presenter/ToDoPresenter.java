package com.todo.todo.home.presenter;

import android.content.Context;
import android.util.Log;

import com.todo.todo.database.DatabaseHandler;
import com.todo.todo.home.interactor.ToDoInteractor;
import com.todo.todo.home.interactor.UpdateOnServerInteractor;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.home.view.NewNoteActivity;
import com.todo.todo.home.view.ToDoActivity;
import com.todo.todo.util.Connection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bridgeit on 20/3/17.
 */

public class ToDoPresenter implements  ToDoPresenterInteface {
    ToDoActivity mToDoActivity;
    ToDoInteractor mToDoInteractor;
    Context  mContext;
    NewNoteActivity newNoteActivity;
    List<ToDoItemModel> localNotes;
    private  String TAG ="ToDoPresenter";
    public ToDoPresenter(ToDoActivity mToDoActivity, Context context) {
        Log.i(TAG, "ToDoPresenter: ");
        this.mToDoActivity = mToDoActivity;
        this.mContext=context;
        mToDoInteractor =new ToDoInteractor(ToDoPresenter.this,mContext);
    }

    public ToDoPresenter(NewNoteActivity newNoteActivity, Context context) {
        this.newNoteActivity = newNoteActivity;
        this.mContext=context;
        mToDoInteractor =new ToDoInteractor(ToDoPresenter.this,mContext);
    }


    @Override
    public void closeProgressDialog() {
        Log.i(TAG, "closeProgressDialog: ");
        mToDoActivity.closeProgressDialog();
    }

    @Override
    public void showProgressDialog() {
        mToDoActivity.showProgressDialog();
    }

    @Override
    public void showDataInActivity(List<ToDoItemModel> toDoItemModels) {
        mToDoActivity.showDataInActivity(toDoItemModels);

    }

    @Override
    public void closeNoteProgressDialog() {
        newNoteActivity.closeNoteProgressDialog();
    }

    @Override
    public void showNoteProgressDialog() {
        newNoteActivity.showNoteProgressDialog();
    }

    @Override
    public void getResponce(boolean flag) {
        Log.i(TAG, "getResponce: ");
        newNoteActivity.getResponce(flag);
    }


    @Override
    public void callPresenterNotesAfterUpdateServer(String uid) {
        mToDoInteractor.getFireBaseDatabase(uid);
    }

    @Override
    public void getPresenterNotes(String uid) {
        Log.i(TAG, "getPresenterNotes: ");
        Connection con=new Connection(mContext);
        if(con.isNetworkConnected()){
            Log.i(TAG, "getPresenterNotes: server");
            localNotes = new ArrayList<ToDoItemModel>();
            DatabaseHandler db=new DatabaseHandler(mContext);
            localNotes=  db.getLocalData();
            if(localNotes.size()==0){
                mToDoInteractor.getFireBaseDatabase(uid);
            }
            else{
                UpdateOnServerInteractor updateOnServerInteractor=new UpdateOnServerInteractor(mContext,this);
                updateOnServerInteractor.updatetoFirebase(uid,localNotes);
            }

        }
        else{
            Log.i(TAG, "getPresenterNotes: local");
            mToDoInteractor.getCallToDatabase();
        }

    }

    @Override
    public void getPresenterNotesAfterUpdate(String uid) {
        DatabaseHandler db=new DatabaseHandler(mContext);
        db.deleteLocal(localNotes);
        mToDoInteractor.getFireBaseDatabase(uid);

    }
    @Override
    public void getCallBackNotes(List<ToDoItemModel> toDoItemModels) {
        Log.i(TAG, "getCallBackNotes: ");
        mToDoActivity.showDataInActivity(toDoItemModels);
    }

    @Override
    public void PutNote(String uid, ToDoItemModel toDoItemModel) {
        Log.i(TAG, "PutNote: ");
        //mToDoInteractor.getCallToDatabase();
        //   mToDoInteractor.uploadNotes(uid,toDoItemModel);
    }

    @Override
    public void loadNotetoFirebase(String uid, String date, ToDoItemModel toDoItemModel) {
        Connection con=new Connection(mContext);
        if(con.isNetworkConnected()){
            Log.i(TAG, "loadNotetoFirebase: firebase");
            mToDoInteractor.uploadNotes(uid,date,toDoItemModel);

        }else{
            Log.i(TAG, "loadNotetoFirebase: local");
            mToDoInteractor.storeNote(date,toDoItemModel);
        }


    }


}
