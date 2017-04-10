package com.todo.todo.update.interactor;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.todo.todo.database.DatabaseHandler;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.update.presenter.UpdateNotePresenter;
import com.todo.todo.update.presenter.UpdateNotePresenterInterface;
import com.todo.todo.util.Connection;

/**
 * Created by bridgeit on 29/3/17.
 */

public class UpdateNoteInteractor implements  UpdateNoteInteractorInterface{
    private  String TAG ="UpdateNoteInteractor";
    UpdateNotePresenterInterface mUpdateNotePresenter;
    Context mContext;
    DatabaseReference mDatabase;
    public UpdateNoteInteractor( Context context,UpdateNotePresenterInterface updateNotePresenter) {
        this.mUpdateNotePresenter=updateNotePresenter;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.mContext=context;
    }

    @Override
    public void updateFirbaseData(String uid, String date, ToDoItemModel toDoItemModel) {
        Connection con=new Connection(mContext);
        if(con.isNetworkConnected()){
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
        else {

        }
    }

    @Override
    public void undoArchivedFirbaseData(String uid, String date, ToDoItemModel toDoItemModel) {
        Connection con=new Connection(mContext);

        if(con.isNetworkConnected()){
            // mUpdateNotePresenter.showProgress();
            try {
                Log.i(TAG, "getArchiveFirebaseData: ");
                toDoItemModel.set_Archive("false");
                mDatabase.child("usersdata").child(uid).child(date).child(String.valueOf(toDoItemModel.get_id())).setValue(toDoItemModel);
                //  mUpdateNotePresenter.getResponce(true);
                //  mUpdateNotePresenter.closeProgress();
            }catch (Exception e){
                //  mUpdateNotePresenter.getResponce(false);
                // mUpdateNotePresenter.closeProgress();
                Log.i(TAG, "updateFirbaseData: ");
            }
        }
        else {
        }
    }

    @Override
    public void updateLocal(ToDoItemModel toDoItemModel) {

    DatabaseHandler db=new DatabaseHandler(mContext);
        db.updateLocal(toDoItemModel);

    }

    @Override
    public void getArchiveFirebaseData(String uid, String date, ToDoItemModel toDoItemModel) {
        Connection con=new Connection(mContext);

        if(con.isNetworkConnected()){
           // mUpdateNotePresenter.showProgress();
            try {
                Log.i(TAG, "getArchiveFirebaseData: ");
                toDoItemModel.set_Archive("true");
                mDatabase.child("usersdata").child(uid).child(date).child(String.valueOf(toDoItemModel.get_id())).setValue(toDoItemModel);
              //  mUpdateNotePresenter.getResponce(true);
              //  mUpdateNotePresenter.closeProgress();

            }catch (Exception e){
              //  mUpdateNotePresenter.getResponce(false);
               // mUpdateNotePresenter.closeProgress();
                Log.i(TAG, "updateFirbaseData: ");
            }
        }
        else {

        }
    }
}
