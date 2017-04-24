package com.todo.todo.home.presenter;

import android.content.Context;

import com.todo.todo.database.DatabaseHandler;
import com.todo.todo.home.interactor.RemoveFirebaseDataInteractor;
import com.todo.todo.home.model.ToDoItemModel;
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
    int index;
    public RemoveNotePresenter(Context applicationContext) {
        this.mContext=applicationContext;
        removeFirebaseDataInteractor =new RemoveFirebaseDataInteractor(mContext);

    }
    public void removeFirebaseData(List<ToDoItemModel> toDoItemModel, String mUserUID, int position){
        removeFirebaseDataInteractor.getIndexUpdateNotes(toDoItemModel,mUserUID,position);
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

}
