package com.todo.todo.update.presenter;

import android.content.Context;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.update.interactor.UpdateNoteInteractor;
import com.todo.todo.update.view.UpdateNoteActivityInterface;
import com.todo.todo.util.Connection;

/**
 * Created by bridgeit on 29/3/17.
 */

public class UpdateNotePresenter implements  UpdateNotePresenterInterface {
    private  static String TAG ="UpdateNotePresenter";
    UpdateNoteActivityInterface updateNoteActivityInterface;
    UpdateNoteInteractor mUpdateNoteInteractor;
    ItemTouchHelper.SimpleCallback simpleCallback1;
    Context mContext;
    public UpdateNotePresenter(Context context,UpdateNoteActivityInterface updateNoteActivity) {
        this.updateNoteActivityInterface =updateNoteActivity;
        mUpdateNoteInteractor=new UpdateNoteInteractor(context,this);
        this.mContext=context;
    }

    public UpdateNotePresenter(Context context, ItemTouchHelper.SimpleCallback simpleCallback1) {
        this.mContext=context;
        this.simpleCallback1=simpleCallback1;
        mUpdateNoteInteractor=new UpdateNoteInteractor(context,this);
    }

    @Override
    public void closeProgress() {
        updateNoteActivityInterface.closeProgress();
    }

    @Override
    public void showProgress() {
        updateNoteActivityInterface.showProgress();
    }

    @Override
    public void getResponce(boolean flag) {
        updateNoteActivityInterface.getResponce(flag);
    }

    @Override
    public void updateNote(String uid, String date, ToDoItemModel toDoItemModel) {
            mUpdateNoteInteractor.updateFirbaseData(uid,date,toDoItemModel);
    }

    @Override
    public void getAchiveNote(String uid, String date, ToDoItemModel toDoItemModel) {
        Log.i(TAG, "getAchiveNote: ");
        mUpdateNoteInteractor.getArchiveFirebaseData(uid,date,toDoItemModel);
    }

    @Override
    public void getUndoAchiveNote(String uid, String date, ToDoItemModel toDoItemModel) {

        mUpdateNoteInteractor.undoArchivedFirbaseData(uid,date,toDoItemModel);
    }
}
