package com.todo.todo.database;

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


public class RegistrationDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ToDoManager";
    private static final String TABLE_USERS = "UserNData";
    private static final String KEY_NAME = "name";
    private static final String KEY_LNAME = "lname";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_URL = "imageurl";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMAIL = "email";
    SQLiteDatabase db;
    RegistrationInteractor registrationInteractor;
    private  String TAG ="RegistrationDatabase";

    public RegistrationDatabase(Context context, RegistrationInteractor registrationInteractor) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
        this.  registrationInteractor =registrationInteractor;
    }

    // Creating Tables  
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate: db");

        String CREATE_ToDoS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + "("
                + KEY_NAME + " TEXT,"+ KEY_LNAME+ " TEXT," + KEY_PHONE + " TEXT,"
                + KEY_EMAIL + " TEXT," + KEY_PASSWORD + " TEXT,"+ KEY_URL  + " TEXT" +")";

        db.execSQL(CREATE_ToDoS_TABLE);
    }

    // Upgrading database  
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed  
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);  // Create tables again
        onCreate(db);
    }

    // code to add the new ToDo
    public void addUser(RegistrationModel registrationModel) {
        try {

            db = this.getWritableDatabase();
            //Log.i(TAG, "addToDo: start");
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, registrationModel.getUserFirstName()); // ToDo Name
            values.put(KEY_LNAME, registrationModel.getUserLastName()); // ToDo Name
            values.put(KEY_PHONE, registrationModel.getMobileNo()); // ToDo Phone
            values.put(KEY_EMAIL, registrationModel.getMailid()); // ToDo REMINDER
            values.put(KEY_PASSWORD, registrationModel.getUserPassword()); // ToDo REMINDER
            values.put(KEY_URL, registrationModel.getUserProfileImgurl()); // ToDo REMINDER
            // Inserting Row
            Log.i(TAG, "addUser: ");
            db.insert(TABLE_USERS, null, values);

            registrationInteractor.getResponce(registrationModel);

            Log.i(TAG, "addUser: added");
            //  Log.i(TAG, "addToDo: success");
            //2nd argument is String containing nullColumnHack

        }catch(Exception e){
            Log.i(TAG, "addUser: "+e);
           // registrationInteractor.getResponce(false);
        }
        finally {
            db.close(); // Closing database connection
        }
    }

    // code to get the single ToDo  
    public RegistrationModel getUser(String  id) {
        SQLiteDatabase db = this.getReadableDatabase();
        RegistrationModel registrationModel = null;
        Cursor cursor = db.query(TABLE_USERS, new String[] {
                        KEY_NAME,KEY_LNAME, KEY_PHONE,KEY_EMAIL,KEY_PASSWORD,KEY_URL}, KEY_EMAIL + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();

            registrationModel = new RegistrationModel(
                    cursor.getString(1),cursor.getString(2), cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6));

        }
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

                registrationModel.setUserFirstName(cursor.getString(1));
                registrationModel.setUserLastName(cursor.getString(2));
                registrationModel.setMailid(cursor.getString(4));
                registrationModel.setMobileNo(cursor.getString(3));
                registrationModel.setUserPassword(cursor.getString(5));
                registrationModel.setUserProfileImgurl(cursor.getString(6));
                // Adding ToDo to list  
                registrationModelList.add(registrationModel);
            } while (cursor.moveToNext());
        }

        // return ToDo list  
        return registrationModelList;
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