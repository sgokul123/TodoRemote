package com.todo.todo.addnote.presenter;

import com.todo.todo.addnote.view.NoteInterface;
import com.todo.todo.home.model.ToDoItemModel;

/**
 * Created by bridgeit on 20/4/17.
 */

public interface AddNotePresenterInterface extends NoteInterface {

    public  void loadNotetoFirebase(String uid,String date,ToDoItemModel toDoItemModel);
}
