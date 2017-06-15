package com.todo.todo.removenote.view;

import com.todo.todo.home.model.ToDoItemModel;

import java.util.List;


public interface TrashFragmentInterface {
    public void getHideToolBar(boolean flag);

    public void getCountIncreament(int position);

    public void displayTrashNotes(List<ToDoItemModel> todoItemModel);

    void getRefreshNotes();

    void getCountDecreament(Integer position);
}
