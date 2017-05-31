package com.todo.todo.removenote.interactor;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.todo.todo.database.DatabaseHandler;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.removenote.presenter.TrashNotePresenter;
import com.todo.todo.util.Connection;
import com.todo.todo.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by bridgeit on 4/4/17.
 */

public class TrashFirebaseDataInteractor {
    private static String TAG = "TrashFirebaseDataInteractor";
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    Context mContext;
    int pos = 0;
    DatabaseHandler db;
    String startdate, userId;
    TrashNotePresenter mTrashNotePresenter;
    int index;
    String startDate;
    List<ToDoItemModel> newtoDoItemModels;
    ToDoItemModel mToDoItemModel;
    private ToDoItemModel mTrashItemModel;
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;

    public TrashFirebaseDataInteractor(Context context, TrashNotePresenter trashNotePresenter) {
        this.mContext = context;
        this.mTrashNotePresenter = trashNotePresenter;
        mDatabase = FirebaseDatabase.getInstance();
        db = new DatabaseHandler(mContext);
        newtoDoItemModels = new ArrayList<ToDoItemModel>();
    }

    public void removeData(List<ToDoItemModel> toDoItemModels, String mUserUID, String startdate, int index) {
        ToDoItemModel lastNote=new ToDoItemModel();
        mRef = mDatabase.getReference().child("usersdata");
        pos = index;
        if (toDoItemModels.size() == 1) {
            toDoItemModels.get(0).setId(pos);
            mRef.child(mUserUID).child(startdate).child(String.valueOf(pos)).setValue(toDoItemModels.get(0));
            pos = pos + 1;
            mRef.child(mUserUID).child(startdate).child(String.valueOf(pos)).setValue(null);
        } else if (toDoItemModels.size() == 0) {
            mRef.child(mUserUID).child(startdate).child(String.valueOf(pos)).setValue(null);

        } else {
            for (ToDoItemModel todoNote : toDoItemModels) {
                try {
                    lastNote=todoNote;
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

        if(startdate.equals(getCurrentDate())){
            pref = mContext.getSharedPreferences(Constants.ProfileeKey.SHAREDPREFERANCES_KEY,mContext.MODE_PRIVATE);
            editor=pref.edit();
            editor.putInt(Constants.Stringkeys.LAST_INDEX,pos);
            editor.commit();
        }
    }
    public String getCurrentDate() {
        String date = "";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(Constants.NotesType.DATE_FORMAT);
        date = df.format(c.getTime());
        date = date.trim();
        return date;
    }
    public void updateFirebaseData(ToDoItemModel toDoItemModel, String mUserUID, String startdate, int index) {
        mRef = mDatabase.getReference().child("usersdata");
        try {
            mRef.child(mUserUID).child(toDoItemModel.getStartdate()).child(String.valueOf(index)).setValue(toDoItemModel);
        } catch (Exception f) {
            Log.i(TAG, "setData: ");
        }
    }

    public void getIndexUpdateNotes(ToDoItemModel doItemModel, List<ToDoItemModel> toDoItemModel, String mUserUID) {
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

    public void getRestore(String mUserUID, final ToDoItemModel toDoItemModel) {
        mToDoItemModel = toDoItemModel;
        startDate = mToDoItemModel.getStartdate();
        index = mToDoItemModel.getId();
        userId = mUserUID;
        try {

            mRef = mDatabase.getReference().child(Constants.Stringkeys.FIREBASE_DATABASE_PARENT_CHILD);
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<ArrayList<ToDoItemModel>> t = new GenericTypeIndicator<ArrayList<ToDoItemModel>>() {
                    };

                    if (mToDoItemModel != null) {
                        ArrayList<ToDoItemModel> todoItemModel = new ArrayList<ToDoItemModel>();
                        if (dataSnapshot.child(userId).hasChild(startDate)) {
                            todoItemModel.addAll(dataSnapshot.child(userId).child(startDate).getValue(t));
                        }

                        todoItemModel.removeAll(Collections.singleton(null));
                        getupdateRestore(mToDoItemModel, todoItemModel.size());
                        mToDoItemModel = null;
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.i(TAG, "onCancelled: ");
                }
            });

        } catch (Exception e) {
            Log.i(TAG, "getFireBaseDatabase: " + e);
        }

    }


    private void getupdateRestore(ToDoItemModel toDoItemModel, int index) {
        mRef = mDatabase.getReference().child(Constants.Stringkeys.FIREBASE_DATABASE_PARENT_CHILD);
        ToDoItemModel todo = toDoItemModel;
        int newIndex = index;
        try {
            todo.setId(newIndex);
            mRef.child(userId).child(todo.getStartdate()).child(String.valueOf(newIndex)).setValue(todo);
        } catch (Exception f) {
            Log.i(TAG, "setData: ");
        }

    }

    public void getUndoDeleteNotes(ToDoItemModel toDoItemModel, List<ToDoItemModel> toDoAllItemModels, String mUserUID) {
        startDate = toDoItemModel.getStartdate();
        index = toDoItemModel.getId();
        userId = mUserUID;
        pos = toDoItemModel.getId();
        List<ToDoItemModel> toDoItemModels = toDoAllItemModels;
        mRef = mDatabase.getReference().child(Constants.Stringkeys.FIREBASE_DATABASE_PARENT_CHILD);
        for (ToDoItemModel toDoItem : toDoItemModels) {
            if (toDoItem.getStartdate().equals(startDate) && toDoItem.getId() >= index) {
                toDoItem.setId(pos);
                mRef.child(userId).child(toDoItem.getStartdate()).child(String.valueOf(toDoItem.getId())).setValue(toDoItem);
                pos = pos + 1;
            }
        }

    }

    public void addToFireBaseTrash(ToDoItemModel doItemModel, final String mUserUID) {
        final int[] id = {0};
        mTrashItemModel=doItemModel;
        mRef = mDatabase.getReference().child(Constants.Stringkeys.FIREBASE_DATABASE_TRASH);
        try {
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<ArrayList<ToDoItemModel>> t = new GenericTypeIndicator<ArrayList<ToDoItemModel>>() {
                    };
                    if (mTrashItemModel != null) {
                        List<ToDoItemModel> toDoList = new ArrayList<>();
                        toDoList =  dataSnapshot.child(mUserUID).getValue(t);
                        id[0]=toDoList.size();
                        mToDoItemModel = null;
                    }
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Log.i(TAG, "onCancelled: ");
                }
            });
        }catch(Exception e){
            Log.i(TAG, "addToFireBaseTrash: "+e);
        }
        mRef.child(mUserUID).child(String.valueOf(id[0])).setValue(doItemModel);
    }
}
