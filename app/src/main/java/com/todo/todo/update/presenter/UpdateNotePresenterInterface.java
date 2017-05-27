package com.todo.todo.update.presenter;

import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.update.view.UpdateNoteActivityInterface;

public interface UpdateNotePresenterInterface extends UpdateNoteActivityInterface {
    public void updateNote(String uid, String date, ToDoItemModel toDoItemModel);

    public void getAchiveNote(String uid, String date, ToDoItemModel toDoItemModel);

    public void getUndoAchiveNote(String uid, String date, ToDoItemModel toDoItemModel);
        /*public  void getMoveNotes(String uid,ToDoItemModel fromNote,ToDoItemModel desinationNote);*/
}
