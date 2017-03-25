package com.todo.todo.home.presenter;

import android.content.Context;
import android.util.Log;


import com.todo.todo.home.interactor.ToDoInteractor;
import com.todo.todo.home.model.ToDoModel;
import com.todo.todo.home.view.NewNote;
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
    NewNote newNote;
    public ToDoPresenter(ToDoActivity mToDoActivity, Context applicationContext) {
        Log.i(TAG, "ToDoPresenter: ");
        this.mToDoActivity = mToDoActivity;
        this.mContext=applicationContext;
        mToDoInteractor =new ToDoInteractor(ToDoPresenter.this,mContext);
    }

    public ToDoPresenter(NewNote newNote, Context applicationContext) {
        this.newNote = newNote;
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
    public void showDataInActivity(List<ToDoModel> toDoModels) {
        mToDoActivity.showDataInActivity(toDoModels);

    }

    @Override
    public void closeNoteProgressDialog() {
        newNote.closeNoteProgressDialog();
    }

    @Override
    public void showNoteProgressDialog() {
        newNote.showNoteProgressDialog();
    }

    @Override
    public void getResponce(boolean flag) {
        Log.i(TAG, "getResponce: ");
        newNote.getResponce(flag);
    }


    @Override
    public void getPresenterNotes() {
        Log.i(TAG, "getPresenterNotes: ");

        mToDoInteractor.getCallToDatabase();

    }

    @Override
    public void getCallBackNotes(List<ToDoModel> toDoModels) {
        Log.i(TAG, "getCallBackNotes: ");
        mToDoActivity.showDataInActivity(toDoModels);
    }

    @Override
    public void PutNote(ToDoModel toDoModel) {
        Log.i(TAG, "PutNote: ");
        //mToDoInteractor.getCallToDatabase();
        mToDoInteractor.storeNote(toDoModel);

    }



}
