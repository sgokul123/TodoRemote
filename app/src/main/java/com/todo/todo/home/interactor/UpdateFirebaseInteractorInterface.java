package com.todo.todo.home.interactor;

import com.todo.todo.home.model.ToDoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 30/3/17.
 */

public interface UpdateFirebaseInteractorInterface {
    public  void updatetoFirebase( String uid, List<ToDoItemModel> localNotes);
    public  void setData(int size,ToDoItemModel toDoItemModel);

}
