package com.todo.todo.home.presenter;

import android.content.Context;

import com.todo.todo.database.DatabaseHandler;
import com.todo.todo.home.interactor.RemoveFirebaseData;
import com.todo.todo.home.model.ToDoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 4/4/17.
 */

public class RemoveNotePresenter  {
    private  static  String TAG="RemoveNotePresenter";
    DatabaseHandler db;
    RemoveFirebaseData removeFirebaseData;
    List<ToDoItemModel> newtoDoItemModels;
    String startdate;
    int index;
    public RemoveNotePresenter(Context applicationContext) {
        removeFirebaseData=new RemoveFirebaseData();

    }
    public void removeData( List<ToDoItemModel> toDoItemModel,int position){
        startdate=toDoItemModel.get(position).get_startdate();
        index=toDoItemModel.get(position).get_id();
        for (ToDoItemModel todo : toDoItemModel) {
            if(todo.get_startdate().equals(startdate)&&todo.get_id()>index){
                newtoDoItemModels.add(todo);
            }
        }
        removeFirebaseData.removeData(newtoDoItemModels,startdate,index);
    }

}
