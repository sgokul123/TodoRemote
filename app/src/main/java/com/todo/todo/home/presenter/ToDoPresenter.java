package com.todo.todo.home.presenter;

import android.content.Context;
import android.util.Log;


import com.todo.todo.home.interactor.ToDoInteractor;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.home.view.NewNoteActivity;
import com.todo.todo.home.view.ToDoActivity;

import java.util.List;

/**
 * Created by bridgeit on 20/3/17.
 */

public class ToDoPresenter implements  ToDoPresenterInteface {
    private  String TAG ="ToDoPresenter";
    ToDoActivity mToDoActivity;
    ToDoInteractor mToDoInteractor;
    Context  mContext;
    NewNoteActivity newNoteActivity;
    public ToDoPresenter(ToDoActivity mToDoActivity, Context applicationContext) {
        Log.i(TAG, "ToDoPresenter: ");
        this.mToDoActivity = mToDoActivity;
        this.mContext=applicationContext;
        mToDoInteractor =new ToDoInteractor(ToDoPresenter.this,mContext);
    }

    public ToDoPresenter(NewNoteActivity newNoteActivity, Context applicationContext) {
        this.newNoteActivity = newNoteActivity;
        this.mContext=applicationContext;
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
    public void getPresenterNotes() {
        Log.i(TAG, "getPresenterNotes: ");

        mToDoInteractor.getCallToDatabase();

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
    public void loadNotetoFirebase(String uid, String date, String timestamp, ToDoItemModel toDoItemModel) {
        mToDoInteractor.uploadNotes(uid,date,timestamp,toDoItemModel);
    }




}
