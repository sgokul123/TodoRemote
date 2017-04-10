package com.todo.todo.home.interactor;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.todo.todo.home.model.ToDoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 4/4/17.
 */

public class RemoveFirebaseData {
    private  static  String TAG ="RemoveFirebaseData";
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    ToDoActivityInteractor mToDoActivityInteractor;
    int pos=0;
    public RemoveFirebaseData() {
        mDatabase = FirebaseDatabase.getInstance();
    }

    public  void removeData(List<ToDoItemModel> toDoItemModels, String mUserUID, String startdate, int index){
        mRef = mDatabase.getReference().child("usersdata");
        pos=index;

        for (ToDoItemModel todoNote :toDoItemModels) {
            try{
                Log.i(TAG, "setSize: "+pos);
                todoNote.set_id(pos);
                mRef.child(mUserUID).child(todoNote.get_startdate()).child(String.valueOf(pos)).setValue(todoNote);
                pos=pos+1;
            }catch (Exception f){

                Log.i(TAG, "setData: ");
            }
        }
        mRef.child(mUserUID).child(startdate).child(String.valueOf(pos)).setValue(null);

    }
    private void setData(String uid, int size, ToDoItemModel todoModel) {


    }

    public void updateFirebaseData(ToDoItemModel toDoItemModel, String mUserUID, String startdate, int index) {
        mRef = mDatabase.getReference().child("usersdata");
        try{
            mRef.child(mUserUID).child(toDoItemModel.get_startdate()).child(String.valueOf(index)).setValue(toDoItemModel);

        }catch (Exception f){
            Log.i(TAG, "setData: ");
        }
    }
}
