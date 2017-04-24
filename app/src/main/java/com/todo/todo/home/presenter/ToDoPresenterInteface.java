package com.todo.todo.home.presenter;

import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.addnote.view.NoteInterface;
import com.todo.todo.home.view.ToDoActivityInteface;

import java.util.List;


public interface ToDoPresenterInteface  extends ToDoActivityInteface, NoteInterface {

    public  void getPresenterNotes(String uid);
    public  void getCallBackNotes(List<ToDoItemModel> toDoItemModels);
    public  void sendCallBackNotes(List<ToDoItemModel> toDoItemModels);
     public  void getPresenterNotesAfterUpdate(String uid);

}
