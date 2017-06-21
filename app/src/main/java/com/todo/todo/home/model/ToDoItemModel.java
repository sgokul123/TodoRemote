package com.todo.todo.home.model;

/**
 * Created by bridgeit on 20/3/17.
 */

public class ToDoItemModel {

    private int id;
    private String title;
    private  int srid;
    private String reminder;
    private String note;
    private String startdate;
    private String archive;
    private String settime;
    private  String color;
    private  boolean isPin;
    public ToDoItemModel() {

    }

    public ToDoItemModel(int id, String title, String reminder, String note, String archive, String Settime,String color,boolean isPin,int srid) {
        this.id = id;
        this.title = title;
        this.reminder = reminder;
        this.note = note;
        this.archive = archive;
        this.settime = Settime;
        this.color=color;
        this.isPin=isPin;
        this.srid=srid;
    }

    public ToDoItemModel(String title, String note, String reminder, String startdate, String archive, String Settime,String color,boolean isPin,int srid) {
        this.title = title;
        this.reminder = reminder;
        this.note = note;
        this.startdate = startdate;
        this.archive = archive;
        this.settime = Settime;
        this.color=color;
        this.isPin=isPin;
        this.srid=srid;
    }

    public ToDoItemModel(int id, String title, String note, String reminder, String startdate, String archive, String Settime,String color,boolean isPin,int srid) {
        this.id = id;
        this.title = title;
        this.reminder = reminder;
        this.note = note;
        this.startdate = startdate;
        this.archive = archive;
        this.settime = Settime;
        this.color=color;
        this.isPin=isPin;
        this.srid=srid;
    }

    public String getSettime() {
        return settime;
    }

    public void setSettime(String settime) {
        this.settime = settime;
    }

    public String getArchive() {
        return archive;
    }

    public void setArchive(String archive) {
        this.archive = archive;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isPin() {
        return isPin;
    }

    public void setPin(boolean pin) {
        isPin = pin;
    }

    public int getSrid() {  return srid;    }

    public void setSrid(int srid) {  this.srid = srid;    }
}
