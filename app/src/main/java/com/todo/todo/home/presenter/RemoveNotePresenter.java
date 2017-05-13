package com.todo.todo.home.presenter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.todo.todo.database.DatabaseHandler;
import com.todo.todo.home.interactor.RemoveFirebaseDataInteractor;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.home.view.ToDoActivity;
import com.todo.todo.home.view.TrashFragment;
import com.todo.todo.util.Connection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bridgeit on 4/4/17.
 */

public class RemoveNotePresenter  {
    private  static  String TAG="RemoveNotePresenter";
    DatabaseHandler db;
    RemoveFirebaseDataInteractor removeFirebaseDataInteractor;
    List<ToDoItemModel> newtoDoItemModels= new ArrayList<ToDoItemModel>();
    String startdate;
    Context mContext;
    DatabaseHandler databaseHandler;
    ToDoActivity mToDoActivity;
    TrashFragment trashFragment;
    int index;
    public RemoveNotePresenter(Context applicationContext, ToDoActivity toDoActivity) {
        this.mContext=applicationContext;
        this.mToDoActivity=toDoActivity;
        removeFirebaseDataInteractor =new RemoveFirebaseDataInteractor(mContext,this);
        this.databaseHandler=new DatabaseHandler(mContext);
    }

    public RemoveNotePresenter(Context baseContext, TrashFragment trashFragment) {
        this.mContext=baseContext;
        this.trashFragment=trashFragment;
        removeFirebaseDataInteractor =new RemoveFirebaseDataInteractor(mContext,this);
        this.databaseHandler=new DatabaseHandler(mContext);
    }


    public void removeFirebaseData(ToDoItemModel doItemModel, List<ToDoItemModel> toDoItemModel, String mUserUID, int position){
        databaseHandler.addNoteToTrash(doItemModel);
        removeFirebaseDataInteractor.getIndexUpdateNotes(doItemModel,toDoItemModel,mUserUID,position);
    }

    public void  getArchiveData(ToDoItemModel toDoItemModel, String mUserUID, int position){
        db.updateToDo(toDoItemModel);
        startdate=toDoItemModel.getStartdate();
        index=toDoItemModel.getId();

            Connection con=new Connection(mContext);
            if(con.isNetworkConnected()){
                removeFirebaseDataInteractor.updateFirebaseData(toDoItemModel,mUserUID,startdate,index);
            }
    }

    public void getTrashNotes() {
       ArrayList<ToDoItemModel> todoItemModels= (ArrayList<ToDoItemModel>) databaseHandler.getAllTrashToDos();
        trashFragment.displayTrashNotes(todoItemModels);
    }

    public void getDeleteTrashNote(ToDoItemModel toDoItemModel) {
        databaseHandler.deleteTrashToDos(toDoItemModel);
    }

    public void getRestoreNote(String mUserUID, ToDoItemModel toDoItemModel) {

        removeFirebaseDataInteractor.getRestore(mUserUID,toDoItemModel);
        databaseHandler.deleteTrashToDos(toDoItemModel);
    }
}
