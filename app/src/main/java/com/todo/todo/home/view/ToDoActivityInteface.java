package com.todo.todo.home.view;

import com.todo.todo.home.model.ToDoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 20/3/17.
 */

public interface ToDoActivityInteface {

    public  void closeProgressDialog();
    public void  showProgressDialog();
    public  void showDataInActivity(List<ToDoItemModel> toDoItemModels);
    public  void getResponce(boolean flag);
    public  void getUndoArchivedNote(int position);
    public  void hideToolBar(boolean flag);
}
