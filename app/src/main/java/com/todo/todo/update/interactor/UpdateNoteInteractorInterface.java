package com.todo.todo.update.interactor;

import com.todo.todo.home.model.ToDoItemModel;


public interface UpdateNoteInteractorInterface {
    public void updateFirbaseData(String uid, String date, ToDoItemModel toDoItemModel);

    public void undoArchivedFirbaseData(String uid, String date, ToDoItemModel toDoItemModel);

    public void updateLocal(ToDoItemModel toDoItemModel);

    public void getArchiveFirebaseData(String uid, String date, ToDoItemModel toDoItemModel);
    /*public  void getMoveNotes(String uid,ToDoItemModel fromNote,ToDoItemModel desinationNote);*/
}
