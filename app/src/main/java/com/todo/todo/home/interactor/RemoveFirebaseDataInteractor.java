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
import com.todo.todo.home.presenter.RemoveNotePresenter;
import com.todo.todo.util.Connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by bridgeit on 4/4/17.
 */

public class RemoveFirebaseDataInteractor {
    private static String TAG = "RemoveFirebaseDataInteractor";
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    Context mContext;
    ToDoActivityInteractor mToDoActivityInteractor;
    int pos = 0;
    DatabaseHandler db;
    String startdate,userId;
    RemoveNotePresenter mRemoveNotePresenter;
    int index;
    String startDate;
    List<ToDoItemModel> newtoDoItemModels;
    ToDoItemModel mToDoItemModel;
    public RemoveFirebaseDataInteractor(Context context, RemoveNotePresenter removeNotePresenter) {
        this.mContext = context;
        this.mRemoveNotePresenter = removeNotePresenter;
        mDatabase = FirebaseDatabase.getInstance();
        db = new DatabaseHandler(mContext);
        newtoDoItemModels = new ArrayList<ToDoItemModel>();
    }

    public void removeData(List<ToDoItemModel> toDoItemModels, String mUserUID, String startdate, int index) {
        mRef = mDatabase.getReference().child("usersdata");
        pos = index;
        if (toDoItemModels.size() == 1) {
            toDoItemModels.get(0).setId(pos);
            mRef.child(mUserUID).child(startdate).child(String.valueOf(pos)).setValue(toDoItemModels.get(0));
            mRef.child(mUserUID).child(startdate).child(String.valueOf(pos + 1)).setValue(null);
        } else if (toDoItemModels.size() == 0) {
            mRef.child(mUserUID).child(startdate).child(String.valueOf(pos)).setValue(null);

        } else {
            for (ToDoItemModel todoNote : toDoItemModels) {
                try {
                    Log.i(TAG, "setSize: " + pos);
                    todoNote.setId(pos);
                    mRef.child(mUserUID).child(todoNote.getStartdate()).child(String.valueOf(pos)).setValue(todoNote);
                    pos = pos + 1;
                } catch (Exception f) {
                    Log.i(TAG, "setData: ");
                }
            }
            mRef.child(mUserUID).child(startdate).child(String.valueOf(pos)).setValue(null);
        }

    }

    public void updateFirebaseData(ToDoItemModel toDoItemModel, String mUserUID, String startdate, int index) {
        mRef = mDatabase.getReference().child("usersdata");
        try {
            mRef.child(mUserUID).child(toDoItemModel.getStartdate()).child(String.valueOf(index)).setValue(toDoItemModel);

        } catch (Exception f) {
            Log.i(TAG, "setData: ");
        }
    }

    public void getIndexUpdateNotes(ToDoItemModel doItemModel, List<ToDoItemModel> toDoItemModel, String mUserUID, int position) {
        List<ToDoItemModel> toDoItemModels = new ArrayList<>();
        db.deleteLocaltodoNote(doItemModel);
        startdate = doItemModel.getStartdate();
        index = doItemModel.getId();
        for (ToDoItemModel todo : toDoItemModel) {
            if (todo.getStartdate().equals(startdate) && todo.getId() > index) {
                toDoItemModels.add(todo);
            }
        }
        if (toDoItemModels != null) {
            Connection con = new Connection(mContext);
            if (con.isNetworkConnected()) {
                removeData(toDoItemModels, mUserUID, startdate, index);
            }

        }
    }
    public void getRestore(String mUserUID, ToDoItemModel toDoItemModel) {
        mToDoItemModel=toDoItemModel;
        startDate=mToDoItemModel.getStartdate();
        index=mToDoItemModel.getId();
        userId=mUserUID;
        try {

            mRef = mDatabase.getReference().child("usersdata");
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<ArrayList<ToDoItemModel>> t = new GenericTypeIndicator<ArrayList<ToDoItemModel>>() {
                    };
                    if(mToDoItemModel!=null){
                        ArrayList<ToDoItemModel> todoItemModel = new ArrayList<ToDoItemModel>();
                        if (dataSnapshot.hasChild(userId)) {
                            todoItemModel.addAll(dataSnapshot.child(userId).child(startDate).getValue(t));
                        }
                        todoItemModel.removeAll(Collections.singleton(null));
                        getUpdateRestoreNote(todoItemModel,mToDoItemModel);
                        mToDoItemModel=null;
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.i(TAG, "onCancelled: ");
                }
            });

        } catch (Exception e) {
            Log.i(TAG, "getFireBaseDatabase: "+e);
        }

    }

    private void getUpdateRestoreNote(ArrayList<ToDoItemModel> todoItemModels, ToDoItemModel mToDoItemModel) {
        List<ToDoItemModel> toDoItemModels=new ArrayList<>();
        toDoItemModels.add(mToDoItemModel);
        for(ToDoItemModel toDoItemModel: todoItemModels){
           if(toDoItemModel.getStartdate().equals(startDate)&&toDoItemModel.getId()>=index){
               toDoItemModels.add(toDoItemModel);
           }
        }

        getupdateRestore(toDoItemModels,index);
    }

    private void getupdateRestore(List<ToDoItemModel> newtoDoItemModels, int index) {
        mRef = mDatabase.getReference().child("usersdata");
        int newIndex=index;
        try {
            for(ToDoItemModel todoNote:newtoDoItemModels){
                todoNote.setId(newIndex);
                mRef.child(userId).child(todoNote.getStartdate()).child(String.valueOf(newIndex)).setValue(todoNote);
                newIndex=newIndex+1;
            }

        } catch (Exception f) {
            Log.i(TAG, "setData: ");
        }

    }
}
