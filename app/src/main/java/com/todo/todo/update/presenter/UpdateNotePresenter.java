package com.todo.todo.update.presenter;

import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.update.interactor.UpdateNoteInteractor;
import com.todo.todo.update.view.UpdateNoteActivity;

/**
 * Created by bridgeit on 29/3/17.
 */

public class UpdateNotePresenter implements  UpdateNotePresenterInterface {
    UpdateNoteActivity mUpdateNoteActivity;
    UpdateNoteInteractor mUpdateNoteInteractor;

    public UpdateNotePresenter(UpdateNoteActivity updateNoteActivity) {
        this.mUpdateNoteActivity=updateNoteActivity;
        mUpdateNoteInteractor=new UpdateNoteInteractor(this);
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
        mUpdateNoteInteractor.updateFirbaseData(uid,date,toDoItemModel);

    }
}
