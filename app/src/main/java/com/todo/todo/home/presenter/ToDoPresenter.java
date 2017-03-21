package com.todo.todo.home.presenter;

import android.content.Context;
import android.util.Log;


import com.todo.todo.home.interactor.ToDoInteractor;
import com.todo.todo.home.model.ToDoModel;
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
    public ToDoPresenter(ToDoActivity mToDoActivity, Context applicationContext) {
        Log.i(TAG, "ToDoPresenter: ");
        this.mToDoActivity = mToDoActivity;
        this.mContext=applicationContext;
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

    }


    @Override
    public void getPresenterNotes() {
        Log.i(TAG, "getPresenterNotes: ");
        mToDoInteractor =new ToDoInteractor(ToDoPresenter.this,mContext);
        mToDoInteractor.getCallToDatabase();

    }

    @Override
    public void getCallBackNotes(List<ToDoModel> toDoModels) {

        mToDoActivity.showDataInActivity(toDoModels);
    }

}
