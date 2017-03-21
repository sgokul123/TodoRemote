package com.todo.todo.home.interactor;

import android.content.Context;
import android.util.Log;


import com.todo.todo.home.database.DatabaseHandler;
import com.todo.todo.home.model.ToDoModel;
import com.todo.todo.home.presenter.ToDoPresenter;

import java.util.List;

/**
 * Created by bridgeit on 20/3/17.
 */

public class ToDoInteractor implements  TodoInteractorInterface {
    private  String TAG ="ToDoInteractor";
    ToDoPresenter mToDoPresenter;
    DatabaseHandler db;
    Context context;
    public ToDoInteractor(ToDoPresenter mToDoPresenter, Context mContext) {
        Log.i(TAG, "ToDoInteractor: ");
        this.mToDoPresenter = mToDoPresenter;
        db = new DatabaseHandler(mContext);

    }

    @Override
    public void getCallToDatabase() {
        Log.i(TAG, "getCallToDatabase: ");
        List<ToDoModel> toDos = db.getAllToDos();

        for (ToDoModel cn : toDos) {
            String log = "Id: " + cn.get_id() + " ,note: " + cn.get_note() + " ,title: " +
                    cn.get_title()+"reminder:  "+cn.get_reminder();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }
        mToDoPresenter.getCallBackNotes(toDos);
        //mToDoPresenter.getCallBackNotes();
    }
}
