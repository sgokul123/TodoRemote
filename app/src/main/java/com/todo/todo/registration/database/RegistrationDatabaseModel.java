package com.todo.todo.registration.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.todo.todo.registration.interactor.RegistrationInteractor;
import com.todo.todo.registration.model.RegistrationModel;

import java.util.ArrayList;
import java.util.List;


public class RegistrationDatabaseModel extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ToDosUserManager";
    private static final String TABLE_USERS = "UserData";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMAIL = "email";
    private  String TAG ="RegistrationDatabaseModel";
    SQLiteDatabase db;
    RegistrationInteractor registrationInteractor;

    public RegistrationDatabaseModel(Context context, RegistrationInteractor registrationInteractor) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
      this.  registrationInteractor =registrationInteractor;
    }

    // Creating Tables  
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate: db");
        String CREATE_ToDoS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_PHONE + " TEXT,"
                + KEY_EMAIL + " TEXT," + KEY_PASSWORD + " TEXT" +")";
        db.execSQL(CREATE_ToDoS_TABLE);
    }

    // Upgrading database  
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed  
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Create tables again  
        onCreate(db);
    }
    // code to add the new ToDo
    public void addUser(RegistrationModel registrationModel) {
        try {

           db = this.getWritableDatabase();
            //Log.i(TAG, "addToDo: start");
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, registrationModel.getmName()); // ToDo Name
            values.put(KEY_PHONE, registrationModel.getmMobile()); // ToDo Phone
            values.put(KEY_EMAIL, registrationModel.getmEmail()); // ToDo REMINDER
            values.put(KEY_PASSWORD, registrationModel.getmPassword()); // ToDo REMINDER
            // Inserting Row
            Log.i(TAG, "addUser: ");
            db.insert(TABLE_USERS, null, values);
            registrationInteractor.getResponce(true);
            Log.i(TAG, "addUser: added");
            //  Log.i(TAG, "addToDo: success");
            //2nd argument is String containing nullColumnHack

        }catch(Exception e){
            Log.i(TAG, "addUser: "+e);
            registrationInteractor.getResponce(false);
        }
        finally {
            db.close(); // Closing database connection
        }
    }

    // code to get the single ToDo  
        public RegistrationModel getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_ID,
                        KEY_NAME, KEY_PHONE,KEY_EMAIL,KEY_PASSWORD}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        RegistrationModel registrationModel = new RegistrationModel(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4));
        // return toDoModel
        return registrationModel;
    }

    // code to get all ToDos in a list view  
    public List<RegistrationModel> getAllUsers() {
        Log.i(TAG, "getAllToDos: ");
        List<RegistrationModel> registrationModelList = new ArrayList<RegistrationModel>();
        // Select All Query  
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list  
        if (cursor.moveToFirst()) {
            do {
                RegistrationModel registrationModel = new RegistrationModel();
                registrationModel.setmId(Integer.parseInt(cursor.getString(0)));
                registrationModel.setmName(cursor.getString(1));
                registrationModel.setmEmail(cursor.getString(2));
                registrationModel.setmMobile(cursor.getString(3));
                registrationModel.setmPassword(cursor.getString(4));
                // Adding ToDo to list  
                registrationModelList.add(registrationModel);
            } while (cursor.moveToNext());
        }

        // return ToDo list  
        return registrationModelList;
    }

    // code to update the single ToDo  
    public int updateUser(RegistrationModel registrationModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, registrationModel.getmId());
        values.put(KEY_EMAIL, registrationModel.getmEmail());
        values.put(KEY_NAME, registrationModel.getmName());
        values.put(KEY_PASSWORD, registrationModel.getmPassword());
        values.put(KEY_PHONE, registrationModel.getmMobile());

        // updating row  
        return db.update(TABLE_USERS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(registrationModel.getmId()) });
    }

    // Deleting single ToDo  
    public void deleteUser(RegistrationModel registrationModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_ID + " = ?",
                new String[] { String.valueOf(registrationModel.getmId()) });
        db.close();
    }

    // Getting ToDos Count  
    public int getUserCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count  
        return cursor.getCount();
    }

}  