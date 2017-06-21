package com.todo.todo.addnote.interactor;

import com.todo.todo.home.model.ToDoItemModel;

/**
 * Created by bridgeit on 20/4/17.
 */

public interface AddNoteInteractorInteface {
    public  void setData(int size);
    public  void getResponce(boolean flag);
    public  void storeNote(String date,ToDoItemModel toDoItemModel);
    public  void uploadNotes(String uid,String date,ToDoItemModel toDoItemModel);

    void setDataLocal(int size);
}
