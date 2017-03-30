package com.todo.todo.home.interactor;

import com.todo.todo.home.model.ToDoItemModel;

/**
 * Created by bridgeit on 20/3/17.
 */

public interface TodoInteractorInterface {
    public  void getCallToDatabase();
    public  void storeNote(String date,ToDoItemModel toDoItemModel);
    public  void uploadNotes(String uid,String date,ToDoItemModel toDoItemModel);
    public  void getResponce(boolean flag);
    public  void setData(int size);

    public  void getFireBaseDatabase(String uid);
}
