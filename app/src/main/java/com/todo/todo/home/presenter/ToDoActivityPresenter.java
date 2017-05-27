package com.todo.todo.home.presenter;

import android.content.Context;

import com.todo.todo.addnote.view.NoteInterface;
import com.todo.todo.database.DatabaseHandler;
import com.todo.todo.home.interactor.ToDoActivityInteractor;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.home.view.ToDoActivityInteface;

import java.util.List;

/**
 * Created by bridgeit on 20/3/17.
 */

public class ToDoActivityPresenter implements ToDoPresenterInteface {
    private static String TAG = "ToDoActivityPresenter";
    ToDoActivityInteface mToDoActivityInteface;
    ToDoActivityInteractor mToDoActivityInteractor;
    Context mContext;
    NoteInterface mNoteInterface;
    List<ToDoItemModel> localNotes;

    public ToDoActivityPresenter(ToDoActivityInteface toDoActivityInteface, Context context) {
        this.mToDoActivityInteface = toDoActivityInteface;
        this.mContext = context;
        mToDoActivityInteractor = new ToDoActivityInteractor(this, mContext);
    }

    @Override
    public void closeProgressDialog() {
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
        mNoteInterface.getResponce(flag);
    }

    @Override
    public void getUndoArchivedNote(int position) {

    }

    @Override
    public void hideToolBar(boolean flag) {

    }


    @Override
    public void getPresenterNotes(String uid) {
        mToDoActivityInteractor.getToDoData(uid);
    }

    @Override
    public void getPresenterNotesAfterUpdate(String uid) {
        DatabaseHandler db = new DatabaseHandler(mContext);
        db.deleteLocal(localNotes);
        mToDoActivityInteractor.getFireBaseDatabase(uid);


    }

    @Override
    public void getCallBackNotes(List<ToDoItemModel> toDoItemModels) {

        DatabaseHandler db = new DatabaseHandler(mContext, this);
        db.addAllNotesToLocal(toDoItemModels);
    }

    @Override
    public void sendCallBackNotes(List<ToDoItemModel> toDoItemModels) {
        mToDoActivityInteface.showDataInActivity(toDoItemModels);
    }


}
