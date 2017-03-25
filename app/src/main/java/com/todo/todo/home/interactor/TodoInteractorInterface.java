package com.todo.todo.home.interactor;

import com.todo.todo.home.model.ToDoModel;

/**
 * Created by bridgeit on 20/3/17.
 */

public interface TodoInteractorInterface {
    public  void getCallToDatabase();
    public  void storeNote(ToDoModel toDoModel);
    public  void getResponce(boolean flag);
}
