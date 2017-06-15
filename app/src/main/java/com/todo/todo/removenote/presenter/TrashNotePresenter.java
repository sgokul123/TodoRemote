package com.todo.todo.removenote.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.todo.todo.database.DatabaseHandler;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.home.view.ToDoNotesFragment;
import com.todo.todo.removenote.interactor.TrashFirebaseDataInteractor;
import com.todo.todo.removenote.view.TrashFragment;
import com.todo.todo.util.Connection;
import com.todo.todo.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bridgeit on 4/4/17.
 */

public class TrashNotePresenter {
    private static String TAG = "TrashNotePresenter";
    private  SharedPreferences pref;
    DatabaseHandler db;
    TrashFirebaseDataInteractor trashFirebaseDataInteractor;
    List<ToDoItemModel> newtoDoItemModels = new ArrayList<ToDoItemModel>();
    String startdate;
    Context mContext;
    DatabaseHandler databaseHandler;
    ToDoNotesFragment toDoNotesFragment;
    TrashFragment trashFragment;
    int index;

    public TrashNotePresenter(Context applicationContext) {
        this.mContext = applicationContext;
        trashFirebaseDataInteractor = new TrashFirebaseDataInteractor(mContext, this);
        this.databaseHandler = new DatabaseHandler(mContext);
        pref = mContext.getSharedPreferences(Constants.ProfileeKey.SHAREDPREFERANCES_KEY, mContext.MODE_PRIVATE);

    }

    public TrashNotePresenter(Context baseContext, TrashFragment trashFragment) {
        this.mContext = baseContext;
        this.trashFragment = trashFragment;
        trashFirebaseDataInteractor = new TrashFirebaseDataInteractor(mContext, this);
        this.databaseHandler = new DatabaseHandler(mContext);
    }

    public void removeFirebaseData(ToDoItemModel doItemModel, List<ToDoItemModel> toDoItemModel, String mUserUID) {
        databaseHandler.addNoteToTrash(doItemModel);
        trashFirebaseDataInteractor.getIndexUpdateNotes(doItemModel, toDoItemModel, mUserUID);
       // trashFirebaseDataInteractor.addToFireBaseTrash(doItemModel,mUserUID);
    }

    public void getArchiveData(ToDoItemModel toDoItemModel, String mUserUID, int position) {
        db.updateToDo(toDoItemModel);
        startdate = toDoItemModel.getStartdate();
        index = toDoItemModel.getId();

        Connection con = new Connection(mContext);
        if (con.isNetworkConnected()) {
            trashFirebaseDataInteractor.updateFirebaseData(toDoItemModel, mUserUID, startdate, index);
        }
    }

    public void getTrashNotes() {
        ArrayList<ToDoItemModel> todoItemModels = (ArrayList<ToDoItemModel>) databaseHandler.getAllTrashToDos();
        trashFragment.displayTrashNotes(todoItemModels);
    }

    public void getDeleteTrashNote(ToDoItemModel toDoItemModel) {
        databaseHandler.deleteTrashToDos(toDoItemModel);
    }

    public void getRestoreNote(String mUserUID, ToDoItemModel toDoItemModel) {
        trashFirebaseDataInteractor.getRestore(mUserUID, toDoItemModel);
        databaseHandler.deleteTrashToDos(toDoItemModel);
    }

    public void undoRemoveFirebaseData(ToDoItemModel toDoItemModel, List<ToDoItemModel> toDoAllItemModels, String mUserUID) {
        databaseHandler.deleteTrashToDos(toDoItemModel);
        trashFirebaseDataInteractor.getUndoDeleteNotes(toDoItemModel, toDoAllItemModels, mUserUID);
    }

    public void getDeleteMultipleTrashNotes(List<ToDoItemModel> trashNotes) {
        databaseHandler.deleteMultipleTrash(trashNotes);
        getTrashNotes();
    }
}
