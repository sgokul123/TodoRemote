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
    ToDoInteractor mToDoInteractor;
    int pos=0;
    public RemoveFirebaseData() {

    }

    public  void removeData(List<ToDoItemModel> toDoItemModels, String startdate, int index){
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("usersdata");
        pos=index;
        for (ToDoItemModel todoNote :toDoItemModels) {
            try{
                Log.i(TAG, "setSize: "+pos);
                todoNote.set_id(pos);
                mRef.child(uid).child(todoModel.get_startdate()).child(String.valueOf(size)).setValue(todoModel);

            }catch (Exception f){

                Log.i(TAG, "setData: ");
            }
        }

    }
    private void setData(String uid, int size, ToDoItemModel todoModel) {


    }
}
