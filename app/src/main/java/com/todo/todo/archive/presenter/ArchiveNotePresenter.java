package com.todo.todo.archive.presenter;

import android.content.Context;

import com.todo.todo.database.DatabaseHandler;
import com.todo.todo.archive.interactor.ArchiveNoteInteractor;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.archive.view.ArchiveFragment;

import java.util.List;

/**
 * Created by bridgeit on 15/5/17.
 */

public class ArchiveNotePresenter  {
    ArchiveFragment archiveFragment;
    Context mContext;
    DatabaseHandler databaseHandler;
    ArchiveNoteInteractor archiveNoteInteractor;
    public ArchiveNotePresenter(Context baseContext, ArchiveFragment archiveFragment) {
        this.mContext=baseContext;
        this.archiveFragment=archiveFragment;
        archiveNoteInteractor =new ArchiveNoteInteractor(mContext,this);
        databaseHandler=new DatabaseHandler(mContext);
    }

    public void getRestoreArchiveNote(String mUserUID, ToDoItemModel toDoItemModel) {
        archiveNoteInteractor.undoArchivedFirbaseData(mUserUID,toDoItemModel.getStartdate(),toDoItemModel);
    }
    public void removeFirebaseData(ToDoItemModel doItemModel, List<ToDoItemModel> toDoItemModels, String mUserUID){
        databaseHandler.addNoteToTrash(doItemModel);
        archiveNoteInteractor.getIndexUpdateNotes(doItemModel,toDoItemModels,mUserUID);
    }


}
