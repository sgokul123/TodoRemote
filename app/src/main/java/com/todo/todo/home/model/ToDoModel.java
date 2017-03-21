package com.todo.todo.home.model;

/**
 * Created by bridgeit on 20/3/17.
 */

public class ToDoModel {

    int _id;
    String _title;
    String _reminder;
    String _note;

    public ToDoModel() {
    }

    public ToDoModel(String _title, String _reminder, String _note) {

        this._title = _title;
        this._reminder = _reminder;
        this._note = _note;
    }

    public ToDoModel(int _id, String _title, String _reminder, String _note) {

        this._id = _id;
        this._title = _title;
        this._reminder = _reminder;
        this._note = _note;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String get_reminder() {
        return _reminder;
    }

    public void set_reminder(String _reminder) {
        this._reminder = _reminder;
    }

    public String get_note() {
        return _note;
    }

    public void set_note(String _note) {
        this._note = _note;
    }
}
