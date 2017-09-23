package com.todo.todo.archive.view;

import com.todo.todo.home.model.ToDoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 18/8/17.
 */

public interface ArchiveFragmentInterface {
    public void displayArchivedNotes(List<ToDoItemModel> todoItemModel);

    void getRefreshNotes();
}
