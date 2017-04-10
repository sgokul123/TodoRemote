package com.todo.todo.home.model;

import android.content.Intent;

/**
 * Created by bridgeit on 20/3/17.
 */

public class ToDoItemModel {

    int _id;
    String _title;
    String _reminder;
    String _note;
    String _startdate;
    String _Archive;


    public ToDoItemModel() {
    }

    public ToDoItemModel(int _id, String _title, String _reminder, String _note,String _Archive ) {
        this._id = _id;
        this._title = _title;
        this._reminder = _reminder;
        this._note = _note;
        this._Archive=_Archive;
    }
    public ToDoItemModel(String _title, String _note, String _reminder, String _startdate,String _Archive) {
        this._title = _title;
        this._reminder = _reminder;
        this._note = _note;
        this._startdate = _startdate;
        this._Archive=_Archive;
    }

    public ToDoItemModel(int _id, String _title, String _note,  String _reminder,String _startdate,String _Archive) {
        this._id = _id;
        this._title = _title;
        this._reminder = _reminder;
        this._note = _note;
        this._startdate = _startdate;
        this._Archive=_Archive;
    }

    public String get_Archive() {
        return _Archive;
    }

    public void set_Archive(String _Archive) {
        this._Archive = _Archive;
    }

    public String get_startdate() {
        return _startdate;
    }

    public void set_startdate(String _startdate) {
        this._startdate = _startdate;
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
