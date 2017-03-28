package com.todo.todo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.todo.todo.home.interactor.ToDoInteractor;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.util.Constants;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private  String TAG ="RegistrationDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ToDosManager";

    private ToDoInteractor todoInteractor;
    public  DatabaseHandler(Context context, ToDoInteractor todoInteractor) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
        this.todoInteractor=todoInteractor;
    }

    // Creating Tables  
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ToDoS_TABLE = "CREATE TABLE IF NOT EXISTS " + Constants.RequestParam.NOTES_TABLE_NAME + "("
                + Constants.RequestParam.KEY_ID + " INTEGER PRIMARY KEY," + Constants.RequestParam.KEY_TITLE + " TEXT," + Constants.RequestParam.KEY_NOTE + " TEXT,"
                + Constants.RequestParam.KEY_REMINDER + " TEXT" + ")";
        db.execSQL(CREATE_ToDoS_TABLE);
    }

    // Upgrading database  
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed  
        db.execSQL("DROP TABLE IF EXISTS " + Constants.RequestParam.NOTES_TABLE_NAME);

        // Create tables again  
        onCreate(db);
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
           // Inserting Row

           db.insert(Constants.RequestParam.NOTES_TABLE_NAME, null, values);
           Log.i(TAG, "addToDo: success");
           todoInteractor.getResponce(true);
           //2nd argument is String containing nullColumnHack

       }catch (Exception e){
           todoInteractor.getResponce(false);
           Log.i(TAG, "addToDo: "+e);
       }
       finally {
           db.close(); // Closing database connection
       }
    }

    // code to get the single ToDo  
    public ToDoItemModel getToDo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.RequestParam.NOTES_TABLE_NAME, new String[] { Constants.RequestParam.KEY_ID,
                        Constants.RequestParam.KEY_TITLE, Constants.RequestParam.KEY_NOTE,Constants.RequestParam.KEY_REMINDER}, Constants.RequestParam.KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ToDoItemModel toDoItemModel = new ToDoItemModel(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),cursor.getString(3));
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
                ToDo.set_note(cursor.getString(1));
                ToDo.set_title(cursor.getString(2));
                ToDo.set_reminder(cursor.getString(3));
                // Adding ToDo to list  
                ToDoList.add(ToDo);
            } while (cursor.moveToNext());
        }

        // return ToDo list  
        return ToDoList;
    }

    // code to update the single ToDo  
    public int updateToDo(ToDoItemModel toDoItemModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.RequestParam.KEY_TITLE, toDoItemModel.get_note());
        values.put(Constants.RequestParam.KEY_NOTE, toDoItemModel.get_title());
        values.put(Constants.RequestParam.KEY_REMINDER, toDoItemModel.get_reminder());
        // updating row  
        return db.update(Constants.RequestParam.NOTES_TABLE_NAME, values, Constants.RequestParam.KEY_ID + " = ?",
                new String[] { String.valueOf(toDoItemModel.get_id()) });
    }

    // Deleting single ToDo  
    public void deleteToDo(ToDoItemModel toDoItemModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.RequestParam.NOTES_TABLE_NAME, Constants.RequestParam.KEY_ID + " = ?",
                new String[] { String.valueOf(toDoItemModel.get_id()) });
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