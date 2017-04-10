package com.todo.todo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.todo.todo.home.interactor.ToDoActivityInteractor;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.home.presenter.ToDoActivityPresenter;
import com.todo.todo.util.Constants;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private String TAG = "RegistrationDatabase";
    private ToDoActivityInteractor todoActivityInteractor;

    private ToDoActivityPresenter toDoActivityPresenter;

    public DatabaseHandler(Context context, ToDoActivityInteractor todoActivityInteractor) {
        super(context, Constants.RequestParam.DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
        this.todoActivityInteractor = todoActivityInteractor;
    }

    public DatabaseHandler(Context context) {
        super(context, Constants.RequestParam.DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    public DatabaseHandler(Context context, ToDoActivityPresenter toDoActivityPresenter) {
        super(context, Constants.RequestParam.DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
        this.toDoActivityPresenter = toDoActivityPresenter;
    }

    // Creating Tables  
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ToDoS_TABLE = "CREATE TABLE IF NOT EXISTS " + Constants.RequestParam.NOTES_TABLE_NAME + "("
                + Constants.RequestParam.KEY_ID + " INTEGER PRIMARY KEY," + Constants.RequestParam.KEY_TITLE + " TEXT," + Constants.RequestParam.KEY_NOTE + " TEXT,"
                + Constants.RequestParam.KEY_REMINDER + " TEXT," + Constants.RequestParam.KEY_STARTDATE + " TEXT," + Constants.RequestParam.KEY_ARCHIVE + " TEXT" + ")";
        db.execSQL(CREATE_ToDoS_TABLE);
        String CREATE_LOCAL_ToDoS_TABLE = "CREATE TABLE IF NOT EXISTS " + Constants.RequestParam.LOCAL_NOTES_TABLE_NAME + "("
                + Constants.RequestParam.KEY_ID + " INTEGER PRIMARY KEY," + Constants.RequestParam.KEY_TITLE + " TEXT," + Constants.RequestParam.KEY_NOTE + " TEXT,"
                + Constants.RequestParam.KEY_REMINDER + " TEXT," + Constants.RequestParam.KEY_STARTDATE + " TEXT," + Constants.RequestParam.KEY_ARCHIVE + " TEXT" + ")";
        db.execSQL(CREATE_LOCAL_ToDoS_TABLE);
    }

    // Upgrading database  
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed  
        db.execSQL("DROP TABLE IF EXISTS " + Constants.RequestParam.NOTES_TABLE_NAME);
        //drop local database
        db.execSQL("DROP TABLE IF EXISTS " + Constants.RequestParam.LOCAL_NOTES_TABLE_NAME);
        // Create tables again  
        onCreate(db);
    }

    public void dropLocaltable(SQLiteDatabase db) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Constants.RequestParam.NOTES_TABLE_NAME);
        String CREATE_ToDoS_TABLE = "CREATE TABLE IF NOT EXISTS " + Constants.RequestParam.NOTES_TABLE_NAME + "("
                + Constants.RequestParam.KEY_ID + " INTEGER PRIMARY KEY," + Constants.RequestParam.KEY_TITLE + " TEXT," + Constants.RequestParam.KEY_NOTE + " TEXT,"
                + Constants.RequestParam.KEY_REMINDER + " TEXT," + Constants.RequestParam.KEY_STARTDATE + " TEXT," + Constants.RequestParam.KEY_ARCHIVE + " TEXT" + ")";
        db.execSQL(CREATE_ToDoS_TABLE);

    }

    // code to add the new ToDo  
    public void addToDo(ToDoItemModel toDoItemModel) {
        SQLiteDatabase db = null;
        try {

            db = this.getWritableDatabase();

            Log.i(TAG, "addToDo: start");
            ContentValues values = new ContentValues();
            values.put(Constants.RequestParam.KEY_TITLE, toDoItemModel.get_title()); // ToDo Name
            values.put(Constants.RequestParam.KEY_NOTE, toDoItemModel.get_note()); // ToDo Phone
            values.put(Constants.RequestParam.KEY_REMINDER, toDoItemModel.get_reminder()); // ToDo REMINDER
            values.put(Constants.RequestParam.KEY_STARTDATE, toDoItemModel.get_startdate()); // ToDo REMINDER
            values.put(Constants.RequestParam.KEY_ARCHIVE, toDoItemModel.get_Archive());
            // Inserting Row

            db.insert(Constants.RequestParam.NOTES_TABLE_NAME, null, values);
            Log.i(TAG, "addToDo: success");
            todoActivityInteractor.getResponce(true);
            //2nd argument is String containing nullColumnHack

        } catch (Exception e) {
            todoActivityInteractor.getResponce(false);
            Log.i(TAG, "addToDo: " + e);
        } finally {
            db.close(); // Closing database connection
        }
    }


    //save to local database when network is not present
    public void addNoteToLocal(ToDoItemModel toDoItemModel) {
        SQLiteDatabase db = null;
        try {

            db = this.getWritableDatabase();
            Log.i(TAG, "addToDo: start");
            ContentValues values = new ContentValues();
            values.put(Constants.RequestParam.KEY_TITLE, toDoItemModel.get_title()); // ToDo Name
            values.put(Constants.RequestParam.KEY_NOTE, toDoItemModel.get_note()); // ToDo Phone
            values.put(Constants.RequestParam.KEY_REMINDER, toDoItemModel.get_reminder()); // ToDo REMINDER
            values.put(Constants.RequestParam.KEY_STARTDATE, toDoItemModel.get_startdate()); // ToDo REMINDER
            values.put(Constants.RequestParam.KEY_ARCHIVE, toDoItemModel.get_Archive());
            // Inserting Row

            db.insert(Constants.RequestParam.LOCAL_NOTES_TABLE_NAME, null, values);
            Log.i(TAG, "addToDo: success");
            todoActivityInteractor.getResponce(true);
            //2nd argument is String containing nullColumnHack

        } catch (Exception e) {
            todoActivityInteractor.getResponce(false);
            Log.i(TAG, "addToDo: " + e);
        } finally {
            db.close(); // Closing database connection
        }
    }

    //add all models to local database
    public void addAllNotesToLocal(List<ToDoItemModel> toDoItemModels) {
        SQLiteDatabase db = null;
        try {

            db = this.getWritableDatabase();
            dropLocaltable(db);
            Log.i(TAG, "addToDo: start");
            for (ToDoItemModel toDoItemModel : toDoItemModels) {
                ContentValues values = new ContentValues();
                values.put(Constants.RequestParam.KEY_TITLE, toDoItemModel.get_title()); // ToDo Name
                values.put(Constants.RequestParam.KEY_NOTE, toDoItemModel.get_note()); // ToDo Phone
                values.put(Constants.RequestParam.KEY_REMINDER, toDoItemModel.get_reminder()); // ToDo REMINDER
                values.put(Constants.RequestParam.KEY_STARTDATE, toDoItemModel.get_startdate()); // ToDo REMINDER
                values.put(Constants.RequestParam.KEY_ARCHIVE, toDoItemModel.get_Archive()); // ToDo REMINDER

                db.insert(Constants.RequestParam.NOTES_TABLE_NAME, null, values);
            }

            toDoActivityPresenter.sendCallBackNotes(toDoItemModels);

        } catch (Exception e) {

            Log.i(TAG, "addToDo: " + e);
        } finally {
            db.close(); // Closing database connection
        }
    }

    // code to get the single ToDo  
    public ToDoItemModel getToDo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.RequestParam.NOTES_TABLE_NAME, new String[]{Constants.RequestParam.KEY_ID,
                        Constants.RequestParam.KEY_TITLE, Constants.RequestParam.KEY_NOTE, Constants.RequestParam.KEY_REMINDER, Constants.RequestParam.KEY_STARTDATE, Constants.RequestParam.KEY_ARCHIVE}, Constants.RequestParam.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ToDoItemModel toDoItemModel = new ToDoItemModel(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        // return toDoItemModel
        return toDoItemModel;

    }

    // code to get all ToDos in a list view
    public List<ToDoItemModel> getAllToDos() {
        Log.i(TAG, "getAllToDos: ");
        List<ToDoItemModel> ToDoList = new ArrayList<ToDoItemModel>();
        // Select All Query  
        String selectQuery = "SELECT  * FROM " + Constants.RequestParam.NOTES_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list  
        if (cursor.moveToFirst()) {
            do {
                ToDoItemModel ToDo = new ToDoItemModel();
                ToDo.set_id(Integer.parseInt(cursor.getString(0)));
                ToDo.set_title(cursor.getString(1));
                ToDo.set_note(cursor.getString(2));
                ToDo.set_reminder(cursor.getString(3));
                ToDo.set_startdate(cursor.getString(4));
                ToDo.set_Archive(cursor.getString(5));
                // Adding ToDo to list  
                ToDoList.add(ToDo);
            } while (cursor.moveToNext());
        }

        String selectQuerylocal = "SELECT  * FROM " + Constants.RequestParam.LOCAL_NOTES_TABLE_NAME;


        Cursor cursor2 = db.rawQuery(selectQuerylocal, null);

        // looping through all rows and adding to list
        if (cursor2.moveToFirst()) {

            do {
                Log.i(TAG, "getAllToDos: " + cursor2.getColumnNames());
                ToDoItemModel ToDo = new ToDoItemModel();
                ToDo.set_id(Integer.parseInt(cursor2.getString(0)));
                ToDo.set_title(cursor2.getString(1));
                ToDo.set_note(cursor2.getString(2));
                ToDo.set_reminder(cursor2.getString(3));
                ToDo.set_startdate(cursor2.getString(4));
                ToDo.set_Archive(cursor2.getString(5));
                // Adding ToDo to list
                ToDoList.add(ToDo);
            } while (cursor2.moveToNext());
        }
        // return ToDo list
        return ToDoList;
    }


    //get all local data to upload

    public List<ToDoItemModel> getLocalData() {
        List<ToDoItemModel> localNotes = new ArrayList<ToDoItemModel>();
        String selectQuerylocal = "SELECT  * FROM " + Constants.RequestParam.LOCAL_NOTES_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor2 = db.rawQuery(selectQuerylocal, null);

        // looping through all rows and adding to list
        if (cursor2.moveToFirst()) {

            do {
                Log.i(TAG, "getAllToDos: " + cursor2.getColumnNames());
                ToDoItemModel localNote = new ToDoItemModel();
                localNote.set_id(Integer.parseInt(cursor2.getString(0)));
                localNote.set_title(cursor2.getString(1));
                localNote.set_note(cursor2.getString(2));
                localNote.set_reminder(cursor2.getString(3));
                localNote.set_startdate(cursor2.getString(4));
                localNote.set_Archive(cursor2.getString(5));
                // Adding ToDo to list
                localNotes.add(localNote);
            } while (cursor2.moveToNext());
        }
        return localNotes;
    }


    // code to update the single ToDo  
    public int updateToDo(ToDoItemModel toDoItemModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.RequestParam.KEY_TITLE, toDoItemModel.get_note());
        values.put(Constants.RequestParam.KEY_NOTE, toDoItemModel.get_title());
        values.put(Constants.RequestParam.KEY_REMINDER, toDoItemModel.get_reminder());
        values.put(Constants.RequestParam.KEY_STARTDATE, toDoItemModel.get_startdate());
        values.put(Constants.RequestParam.KEY_ARCHIVE, toDoItemModel.get_Archive());
        // updating row  
        return db.update(Constants.RequestParam.NOTES_TABLE_NAME, values, Constants.RequestParam.KEY_ID + " = ?",
                new String[]{String.valueOf(toDoItemModel.get_id())});
    }

    //update Local data
    // code to update the single ToDo
    public int updateLocal(ToDoItemModel toDoItemModel) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.RequestParam.KEY_TITLE, toDoItemModel.get_note());
        values.put(Constants.RequestParam.KEY_NOTE, toDoItemModel.get_title());
        values.put(Constants.RequestParam.KEY_REMINDER, toDoItemModel.get_reminder());
        values.put(Constants.RequestParam.KEY_STARTDATE, toDoItemModel.get_startdate());
        values.put(Constants.RequestParam.KEY_ARCHIVE, toDoItemModel.get_Archive());
        // updating row
        return db.update(Constants.RequestParam.LOCAL_NOTES_TABLE_NAME, values, Constants.RequestParam.KEY_ID + " = ?",
                new String[]{String.valueOf(toDoItemModel.get_id())});
    }
    //delete local data

    // Deleting single ToDo
    public void deleteLocal(List<ToDoItemModel> toDoItemModels) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (ToDoItemModel todo : toDoItemModels) {
            db.delete(Constants.RequestParam.LOCAL_NOTES_TABLE_NAME, Constants.RequestParam.KEY_ID + " = ?",
                    new String[]{String.valueOf(todo.get_id())});
        }
        db.close();
    }

    // Deleting single ToDo  
    public void deleteToDo(ToDoItemModel toDoItemModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i(TAG, "deleteToDo: delete");
        db.delete(Constants.RequestParam.LOCAL_NOTES_TABLE_NAME, Constants.RequestParam.KEY_ID + " = ?",
                new String[]{String.valueOf(toDoItemModel.get_id())});
        db.close();
    }

    // Deleting single ToDo
    public void deleteLocaltodoNote(ToDoItemModel toDoItemModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i(TAG, "deleteToDo: delete");
        db.delete(Constants.RequestParam.NOTES_TABLE_NAME, Constants.RequestParam.KEY_ID + " = ?",
                new String[]{String.valueOf(toDoItemModel.get_id())});
        db.delete(Constants.RequestParam.LOCAL_NOTES_TABLE_NAME, Constants.RequestParam.KEY_ID + " = ?",
                new String[]{String.valueOf(toDoItemModel.get_id())});
        db.close();
    }

    // Getting ToDos Count  
    public int getToDosCount() {
        String countQuery = "SELECT  * FROM " + Constants.RequestParam.NOTES_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count  
        return cursor.getCount();
    }

}  