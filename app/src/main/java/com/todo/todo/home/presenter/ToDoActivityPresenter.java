package com.todo.todo.home.presenter;

import android.content.Context;
import android.util.Log;

import com.todo.todo.database.DatabaseHandler;
import com.todo.todo.home.interactor.ToDoActivityInteractor;
import com.todo.todo.home.interactor.UpdateOnServerInteractor;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.home.view.NoteInterface;
import com.todo.todo.home.view.ToDoActivityInteface;
import com.todo.todo.util.Connection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bridgeit on 20/3/17.
 */

public class ToDoActivityPresenter implements  ToDoPresenterInteface {
    private  static String TAG="ToDoActivityPresenter";
    ToDoActivityInteface mToDoActivityInteface;
    ToDoActivityInteractor mToDoActivityInteractor;
    Context  mContext;
    NoteInterface mNoteInterface;
    List<ToDoItemModel> localNotes;

    public ToDoActivityPresenter(ToDoActivityInteface toDoActivityInteface, Context context) {
        Log.i(TAG, "ToDoActivityPresenter: ");
        this.mToDoActivityInteface = toDoActivityInteface;
        this.mContext=context;
        mToDoActivityInteractor =new ToDoActivityInteractor(this,mContext);
    }

    public ToDoActivityPresenter(NoteInterface noteInterface, Context context) {
        this.mNoteInterface = noteInterface;
        this.mContext=context;
        mToDoActivityInteractor =new ToDoActivityInteractor(this,mContext);
    }


    @Override
    public void closeProgressDialog() {
        Log.i(TAG, "closeProgressDialog: ");
        mToDoActivityInteface.closeProgressDialog();
    }

    @Override
    public void showProgressDialog() {
        mToDoActivityInteface.showProgressDialog();
    }

    @Override
    public void showDataInActivity(List<ToDoItemModel> toDoItemModels) {
        mToDoActivityInteface.showDataInActivity(toDoItemModels);

    }

    @Override
    public void closeNoteProgressDialog() {
        mNoteInterface.closeNoteProgressDialog();
    }

    @Override
    public void showNoteProgressDialog() {
        mNoteInterface.showNoteProgressDialog();
    }

    @Override
    public void getResponce(boolean flag) {
        Log.i(TAG, "getResponce: ");
        mNoteInterface.getResponce(flag);
    }



    @Override
    public void getPresenterNotes(String uid) {
        Log.i(TAG, "getPresenterNotes: ");

        mToDoActivityInteractor.getToDoData(uid);
    }

    @Override
    public void getPresenterNotesAfterUpdate(String uid) {
        DatabaseHandler db=new DatabaseHandler(mContext);
        db.deleteLocal(localNotes);
        mToDoActivityInteractor.getFireBaseDatabase(uid);


    }
    @Override
    public void getCallBackNotes(List<ToDoItemModel> toDoItemModels) {
        Log.i(TAG, "getCallBackNotes: ");
        //store into local database
        DatabaseHandler db=new DatabaseHandler(mContext,this);
        db.addAllNotesToLocal(toDoItemModels);
               //
    }

    @Override
    public void sendCallBackNotes(List<ToDoItemModel> toDoItemModels) {
        Log.i(TAG, "sendCallBackNotes: ");
        mToDoActivityInteface.showDataInActivity(toDoItemModels);
    }

    @Override
    public void PutNote(String uid, ToDoItemModel toDoItemModel) {
        Log.i(TAG, "PutNote: ");
        //mToDoActivityInteractor.getCallToDatabase();
        //   mToDoActivityInteractor.uploadNotes(uid,toDoItemModel);
    }

    @Override
    public void loadNotetoFirebase(String uid, String date, ToDoItemModel toDoItemModel) {

            Log.i(TAG, "loadNotetoFirebase: firebase");
            mToDoActivityInteractor.uploadNotes(uid,date,toDoItemModel);




    }


}
