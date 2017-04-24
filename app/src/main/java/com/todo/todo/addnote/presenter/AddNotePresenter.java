package com.todo.todo.addnote.presenter;

import android.content.Context;
import android.util.Log;

import com.todo.todo.addnote.interactor.AddNoteInteractore;
import com.todo.todo.addnote.view.NoteInterface;
import com.todo.todo.home.model.ToDoItemModel;


/**
 * Created by bridgeit on 20/4/17.
 */

public class AddNotePresenter  implements  AddNotePresenterInterface {

    private  Context mContext;
    AddNoteInteractore mAddNoteInteractore;
    NoteInterface mNoteInterface;
    private String TAG="AddNotePresenter";

    public AddNotePresenter(NoteInterface noteInteface, Context context) {
        this.mContext=context;
        this.mNoteInterface =noteInteface;
        mAddNoteInteractore=new AddNoteInteractore(this,mContext);
    }

    @Override
    public void loadNotetoFirebase(String uid, String date, ToDoItemModel toDoItemModel) {
        Log.i(TAG, "loadNotetoFirebase: firebase");
        mAddNoteInteractore .uploadNotes(uid,date,toDoItemModel);

    }

    @Override
    public void closeNoteProgressDialog() {
        Log.i(TAG, "closeProgressDialog: ");
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
}
