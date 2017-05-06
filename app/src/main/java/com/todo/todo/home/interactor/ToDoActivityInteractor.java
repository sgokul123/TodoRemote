package com.todo.todo.home.interactor;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.todo.todo.database.DatabaseHandler;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.home.presenter.ToDoPresenterInteface;
import com.todo.todo.util.Connection;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by bridgeit on 20/3/17.
 */

public class ToDoActivityInteractor implements TodoInteractorInterface {
    ToDoPresenterInteface toDoPresenterInteface;
    DatabaseHandler db;
    DatabaseReference mRef;
    int size = 0;
    Context mContext;

    private String TAG = "ToDoActivityInteractor";
    private DatabaseReference mDatabase;

    public ToDoActivityInteractor(ToDoPresenterInteface toDoPresenterInteface, Context context) {
        Log.i(TAG, "ToDoActivityInteractor: ");
        this.toDoPresenterInteface = toDoPresenterInteface;
        this.mContext = context;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void getCallToDatabase() {

        // call to he database to retrive data load into grid view
        //it will responce all notes back to presenter

        db = new DatabaseHandler(mContext, this);
        toDoPresenterInteface.showProgressDialog();
        Log.i(TAG, "getCallToDatabase: ");
        List<ToDoItemModel> toDos = db.getAllToDos();
        toDoPresenterInteface.sendCallBackNotes(toDos);
        toDoPresenterInteface.closeProgressDialog();
    }

    @Override
    public void getResponce(boolean flag) {
        toDoPresenterInteface.getResponce(flag);
        toDoPresenterInteface.closeNoteProgressDialog();
    }

    @Override
    public void getToDoData(String uid) {

        Connection con = new Connection(mContext);
        if (con.isNetworkConnected()) {

            List<ToDoItemModel> localNotes;
            localNotes = new ArrayList<ToDoItemModel>();

            DatabaseHandler db = new DatabaseHandler(mContext);
            localNotes = db.getLocalData();
            if (localNotes.size() == 0) {

                Log.i(TAG, "getToDoData: ");
                getFireBaseDatabase(uid);

            } else {

                UpdateLocalDataOnLocalDataOnServerInteractor updateLocalDataOnServerInteractor = new UpdateLocalDataOnLocalDataOnServerInteractor(mContext, this);
                updateLocalDataOnServerInteractor.updatetoFirebase(uid, localNotes);
            }
        } else {
            Log.i(TAG, "getPresenterNotes: local");
            getCallToDatabase();
        }
    }

    @Override
    public void getFireBaseDatabase(final String uid) {

        toDoPresenterInteface.showProgressDialog();
        try {

            mRef = mDatabase.child("usersdata");
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<ArrayList<ToDoItemModel>> t = new GenericTypeIndicator<ArrayList<ToDoItemModel>>() {
                    };

                    if (dataSnapshot.hasChild(uid)) {
                        DataSnapshot dataSnapsho = dataSnapshot.child(uid);
                        Log.i(TAG, "onDataChange: ");
                        ArrayList<ToDoItemModel> todoItemModel = new ArrayList<ToDoItemModel>();
                        for (DataSnapshot child : dataSnapsho.getChildren()) {
                            Log.i(TAG, "onDataChange: ");
                            todoItemModel.addAll(child.getValue(t));
                        }
                        todoItemModel.removeAll(Collections.singleton(null));
                        toDoPresenterInteface.getCallBackNotes(todoItemModel);
                    }
                    toDoPresenterInteface.closeProgressDialog();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.i(TAG, "onCancelled: ");
                    toDoPresenterInteface.closeProgressDialog();
                }
            });

        } catch (Exception e) {
            Log.i(TAG, "getFireBaseDatabase: "+e);
            toDoPresenterInteface.closeNoteProgressDialog();
        }
    }

    @Override
    public void callPresenterNotesAfterUpdateServer(String uid) {
        getFireBaseDatabase(uid);
    }

    @Override
    public void getMoveNotes(String uid, String curDate, ToDoItemModel fromNote, ToDoItemModel desinationNote) {



    }
}
