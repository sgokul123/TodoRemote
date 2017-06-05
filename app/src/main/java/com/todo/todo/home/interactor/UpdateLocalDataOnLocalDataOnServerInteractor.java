package com.todo.todo.home.interactor;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.todo.todo.database.DatabaseHandler;
import com.todo.todo.home.model.ToDoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 30/3/17.
 */

public class UpdateLocalDataOnLocalDataOnServerInteractor implements UpdateLocalDataOnFirebaseInteractorInterface {
    ToDoActivityInteractor toDoActivityInteractor;
    Context mContext;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    List<ToDoItemModel> allLocalNotes;
    private String TAG = "UpdateLocalDataOnLocalDataOnServerInteractor";
    private String mUid;
    private int mModelSize = 0;

    public UpdateLocalDataOnLocalDataOnServerInteractor(Context context, ToDoActivityInteractor toDoActivityPresenter) {
        this.mContext = context;
        this.toDoActivityInteractor = toDoActivityPresenter;
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public void  updatetoFirebase(final String uid, final List<ToDoItemModel> localNotes) {
        final DatabaseHandler db = new DatabaseHandler(mContext);
        mUid = uid;
        allLocalNotes = localNotes;

        mRef = mDatabase.getReference().child("usersdata").child(mUid);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //  mUpdateOnServerInteractor.
                if (dataSnapshot.exists()) {

                    if (allLocalNotes.size() > 0) { //upto size greter  than 0

                        ToDoItemModel todoModel = allLocalNotes.get(0);
                        db.deleteToDo(todoModel);
                        allLocalNotes.remove(0);                //remove 0th dataitmemodel
                        Log.i(TAG, "onDataChange: " + allLocalNotes.size());

                        if (dataSnapshot.child(todoModel.getStartdate()).exists()) {
                            int size = (int) dataSnapshot.child(todoModel.getStartdate()).getChildrenCount();
                            Log.i(TAG, "onDataChange: " + size);
                            setData(size, todoModel);
                        } else {
                            //if child is not present in firebase then create it
                            setData(0, todoModel);
                        }
                    } else {
                        toDoActivityInteractor.callPresenterNotesAfterUpdateServer(mUid);
                    }
                } else {
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.i(TAG, "onCancelled: ");
            }
        });

    }

    @Override
    public void setData(int size, ToDoItemModel toDoItemModel) {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("usersdata");
        try {
            Log.i(TAG, "setSize: " + size);
            //toDoItemModel.setId(size);
            mRef.child(mUid).child(toDoItemModel.getStartdate()).child(String.valueOf(toDoItemModel.getId())).setValue(toDoItemModel);
        } catch (Exception f) {

            Log.i(TAG, "setData: ");
        }
    }


}
