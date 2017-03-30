package com.todo.todo.home.presenter;

import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.home.view.NoteInterface;
import com.todo.todo.home.view.ToDoActivityInteface;

import java.util.List;

/**
 * Created by bridgeit on 20/3/17.
 */

public interface ToDoPresenterInteface  extends ToDoActivityInteface, NoteInterface {

    public  void getPresenterNotes(String uid);
    public  void getCallBackNotes(List<ToDoItemModel> toDoItemModels);
    public  void PutNote(String uid,ToDoItemModel toDoItemModel);
    public  void loadNotetoFirebase(String uid,String date,ToDoItemModel toDoItemModel);


}
