package com.todo.todo.update.presenter;

import android.content.Context;

import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.update.interactor.UpdateNoteInteractor;
import com.todo.todo.update.view.UpdateNoteActivity;
import com.todo.todo.util.Connection;

/**
 * Created by bridgeit on 29/3/17.
 */

public class UpdateNotePresenter implements  UpdateNotePresenterInterface {
    UpdateNoteActivity mUpdateNoteActivity;
    UpdateNoteInteractor mUpdateNoteInteractor;
    Context mContext;
    public UpdateNotePresenter(Context context,UpdateNoteActivity updateNoteActivity) {
        this.mUpdateNoteActivity=updateNoteActivity;
        mUpdateNoteInteractor=new UpdateNoteInteractor(context,this);
        this.mContext=context;
    }

    @Override
    public void closeProgress() {
        mUpdateNoteActivity.closeProgress();
    }

    @Override
    public void showProgress() {
        mUpdateNoteActivity.showProgress();
    }

    @Override
    public void getResponce(boolean flag) {
        mUpdateNoteActivity.getResponce(flag);
    }

    @Override
    public void updateNote(String uid, String date, ToDoItemModel toDoItemModel) {
        Connection con=new Connection(mContext);
        if(con.isNetworkConnected()){
            mUpdateNoteInteractor.updateFirbaseData(uid,date,toDoItemModel);
        }
       else {

        }

    }
}
