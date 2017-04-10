package com.todo.todo.update.interactor;

import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.update.view.UpdateNoteActivity;

/**
 * Created by bridgeit on 29/3/17.
 */

public interface UpdateNoteInteractorInterface {
    public void updateFirbaseData(String uid, String date, ToDoItemModel toDoItemModel);
    public void undoArchivedFirbaseData(String uid, String date, ToDoItemModel toDoItemModel);
    public  void updateLocal(ToDoItemModel toDoItemModel);
    public void getArchiveFirebaseData(String uid, String date, ToDoItemModel toDoItemModel);
}
