package com.todo.todo.home.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.todo.todo.home.interactor.ToDoInteractor;
import com.todo.todo.home.model.ToDoModel;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private  String TAG ="RegistrationDatabaseModel";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ToDosManager";
    private static final String TABLE_ToDoS = "ToDos";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_NOTE = "notes";
    private static final String KEY_REMINDER = "reminder";
    private ToDoInteractor todoInteractor;
    public  DatabaseHandler(Context context, ToDoInteractor todoInteractor) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
        this.todoInteractor=todoInteractor;
    }

    // Creating Tables  
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ToDoS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ToDoS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT," + KEY_NOTE + " TEXT,"
                + KEY_REMINDER + " TEXT" + ")";
        db.execSQL(CREATE_ToDoS_TABLE);
    }

    // Upgrading database  
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed  
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ToDoS);

        // Create tables again  
        onCreate(db);
    }

    // code to add the new ToDo  
   public void addToDo(ToDoModel toDoModel) {
       SQLiteDatabase db = null;
       try {


         db = this.getWritableDatabase();
           Log.i(TAG, "addToDo: start");
           ContentValues values = new ContentValues();
           values.put(KEY_TITLE, toDoModel.get_title()); // ToDo Name
           values.put(KEY_NOTE, toDoModel.get_note()); // ToDo Phone
           values.put(KEY_REMINDER, toDoModel.get_reminder()); // ToDo REMINDER
           // Inserting Row

           db.insert(TABLE_ToDoS, null, values);
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
    public ToDoModel getToDo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ToDoS, new String[] { KEY_ID,
                        KEY_TITLE, KEY_NOTE,KEY_REMINDER}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ToDoModel toDoModel = new ToDoModel(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),cursor.getString(3));
        // return toDoModel
        return toDoModel;
    }

    // code to get all ToDos in a list view  
    public List<ToDoModel> getAllToDos() {
        Log.i(TAG, "getAllToDos: ");
        List<ToDoModel> ToDoList = new ArrayList<ToDoModel>();
        // Select All Query  
        String selectQuery = "SELECT  * FROM " + TABLE_ToDoS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list  
        if (cursor.moveToFirst()) {
            do {
                ToDoModel ToDo = new ToDoModel();
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
    public int updateToDo(ToDoModel toDoModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, toDoModel.get_note());
        values.put(KEY_NOTE, toDoModel.get_title());
        values.put(KEY_REMINDER, toDoModel.get_reminder());
        // updating row  
        return db.update(TABLE_ToDoS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(toDoModel.get_id()) });
    }

    // Deleting single ToDo  
    public void deleteToDo(ToDoModel toDoModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ToDoS, KEY_ID + " = ?",
                new String[] { String.valueOf(toDoModel.get_id()) });
        db.close();
    }

    // Getting ToDos Count  
    public int getToDosCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ToDoS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count  
        return cursor.getCount();
    }

}  