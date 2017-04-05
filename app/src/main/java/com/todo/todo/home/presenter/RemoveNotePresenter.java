package com.todo.todo.home.presenter;

import android.content.Context;
import android.view.inputmethod.InputConnectionWrapper;

import com.todo.todo.database.DatabaseHandler;
import com.todo.todo.home.interactor.RemoveFirebaseData;
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
    RemoveFirebaseData removeFirebaseData;
    List<ToDoItemModel> newtoDoItemModels= new ArrayList<ToDoItemModel>();;
    String startdate;
    Context mContext;
    int index;
    public RemoveNotePresenter(Context applicationContext) {
        this.mContext=applicationContext;
        db=new DatabaseHandler(applicationContext);
        removeFirebaseData=new RemoveFirebaseData();

    }
    public void removeFirebaseData(List<ToDoItemModel> toDoItemModel, String mUserUID, int position){
        db.deleteLocaltodoNote(toDoItemModel.get(position));
        startdate=toDoItemModel.get(position).get_startdate();
        index=toDoItemModel.get(position).get_id();
        for (ToDoItemModel todo : toDoItemModel) {
            if(todo.get_startdate().equals(startdate)&&todo.get_id()>index){
                newtoDoItemModels.add(todo);
            }
        }

        if(newtoDoItemModels!=null){
            Connection con=new Connection(mContext);
            if(con.isNetworkConnected()){
                removeFirebaseData.removeData(newtoDoItemModels,mUserUID,startdate,index);
            }

        }

    }

    public void  getArchiveData(ToDoItemModel toDoItemModel, String mUserUID, int position){
        db.updateToDo(toDoItemModel);
        startdate=toDoItemModel.get_startdate();
        index=toDoItemModel.get_id();

            Connection con=new Connection(mContext);
            if(con.isNetworkConnected()){
                removeFirebaseData.updateFirebaseData(toDoItemModel,mUserUID,startdate,index);
            }


    }

}
