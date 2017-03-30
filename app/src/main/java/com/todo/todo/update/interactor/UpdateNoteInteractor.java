package com.todo.todo.update.interactor;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.todo.todo.database.DatabaseHandler;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.update.presenter.UpdateNotePresenter;

/**
 * Created by bridgeit on 29/3/17.
 */

public class UpdateNoteInteractor implements  UpdateNoteInteractorInterface{
    private  String TAG ="UpdateNoteInteractor";
    UpdateNotePresenter mUpdateNotePresenter;

    DatabaseReference mDatabase;
    public UpdateNoteInteractor(UpdateNotePresenter updateNotePresenter) {
        this.mUpdateNotePresenter=updateNotePresenter;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void updateFirbaseData(String uid, String date, ToDoItemModel toDoItemModel) {
        mUpdateNotePresenter.showProgress();
        try {

            mDatabase.child("usersdata").child(uid).child(date).child(String.valueOf(toDoItemModel.get_id())).setValue(toDoItemModel);
            mUpdateNotePresenter.getResponce(true);
            mUpdateNotePresenter.closeProgress();
        }catch (Exception e){
            mUpdateNotePresenter.getResponce(false);
            mUpdateNotePresenter.closeProgress();
            Log.i(TAG, "updateFirbaseData: ");
        }

    }
}
