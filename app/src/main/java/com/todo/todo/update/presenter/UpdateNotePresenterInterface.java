package com.todo.todo.update.presenter;

import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.update.view.UpdateNoteActivityInterface;

/**
 * Created by bridgeit on 29/3/17.
 */

public interface UpdateNotePresenterInterface extends UpdateNoteActivityInterface{
        public  void updateNote(String uid, String date, ToDoItemModel toDoItemModel);
        public  void getAchiveNote(String uid, String date, ToDoItemModel toDoItemModel);
        public  void getUndoAchiveNote(String uid, String date, ToDoItemModel toDoItemModel);
}
