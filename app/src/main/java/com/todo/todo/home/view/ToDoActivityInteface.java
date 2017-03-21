package com.todo.todo.home.view;

import com.todo.todo.home.model.ToDoModel;

import java.util.List;

/**
 * Created by bridgeit on 20/3/17.
 */

public interface ToDoActivityInteface {

    public  void closeProgressDialog();
    public void  showProgressDialog();
    public  void showDataInActivity(List<ToDoModel> toDoModels);
}
