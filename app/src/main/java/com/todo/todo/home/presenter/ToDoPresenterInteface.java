package com.todo.todo.home.presenter;

import com.todo.todo.home.model.ToDoModel;
import com.todo.todo.home.view.NoteInterface;
import com.todo.todo.home.view.ToDoActivityInteface;

import java.util.List;

/**
 * Created by bridgeit on 20/3/17.
 */

public interface ToDoPresenterInteface  extends ToDoActivityInteface, NoteInterface {

    public  void getPresenterNotes();
    public  void getCallBackNotes(List<ToDoModel> toDoModels);
    public  void PutNote(ToDoModel toDoModel);


}
