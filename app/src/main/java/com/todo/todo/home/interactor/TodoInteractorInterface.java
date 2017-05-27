package com.todo.todo.home.interactor;

import com.todo.todo.home.model.ToDoItemModel;

/**
 * Created by bridgeit on 20/3/17.
 */

public interface TodoInteractorInterface {
    public void getCallToDatabase();

    public void getResponce(boolean flag);

    public void getToDoData(String uid);

    public void getFireBaseDatabase(String uid);

    public void callPresenterNotesAfterUpdateServer(String uid);

    public void getMoveNotes(String uid, String curDate, ToDoItemModel fromNote, ToDoItemModel desinationNote);
}
