package com.todo.todo.home.interactor;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.todo.todo.database.DatabaseHandler;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.util.Connection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bridgeit on 4/4/17.
 */

public class RemoveFirebaseDataInteractor {
    private  static  String TAG ="RemoveFirebaseDataInteractor";
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    Context mContext;
    ToDoActivityInteractor mToDoActivityInteractor;
    int pos=0;
    DatabaseHandler db;
    String startdate;
    int index;
    List<ToDoItemModel>  newtoDoItemModels;
    public RemoveFirebaseDataInteractor(Context context) {
        this.mContext=context;
        mDatabase = FirebaseDatabase.getInstance();
        db=new DatabaseHandler(mContext);
        newtoDoItemModels= new ArrayList<ToDoItemModel>();
    }

    public  void removeData(List<ToDoItemModel> toDoItemModels, String mUserUID, String startdate, int index){
        mRef = mDatabase.getReference().child("usersdata");
        pos=index;
        if(toDoItemModels.size()==1){
            toDoItemModels.get(0).setId(pos);
            mRef.child(mUserUID).child(startdate).child(String.valueOf(pos)).setValue(toDoItemModels.get(0));
            mRef.child(mUserUID).child(startdate).child(String.valueOf(pos+1)).setValue(null);
        }else{
            for (ToDoItemModel todoNote :toDoItemModels) {
                try{
                    Log.i(TAG, "setSize: "+pos);
                    todoNote.setId(pos);
                    mRef.child(mUserUID).child(todoNote.getStartdate()).child(String.valueOf(pos)).setValue(todoNote);
                    pos=pos+1;
                }catch (Exception f){
                    Log.i(TAG, "setData: ");
                }
            }
            mRef.child(mUserUID).child(startdate).child(String.valueOf(pos)).setValue(null);
        }

    }

    public void updateFirebaseData(ToDoItemModel toDoItemModel, String mUserUID, String startdate, int index) {
        mRef = mDatabase.getReference().child("usersdata");
        try{
            mRef.child(mUserUID).child(toDoItemModel.getStartdate()).child(String.valueOf(index)).setValue(toDoItemModel);

        }catch (Exception f){
            Log.i(TAG, "setData: ");
        }
    }

    public void getIndexUpdateNotes(List<ToDoItemModel> toDoItemModel, String mUserUID, int position) {

        db.deleteLocaltodoNote(toDoItemModel.get(position));
        startdate=toDoItemModel.get(position).getStartdate();
        index=toDoItemModel.get(position).getId();
        for (ToDoItemModel todo : toDoItemModel) {
            if(todo.getStartdate().equals(startdate) && todo.getId()>index){
                newtoDoItemModels.add(todo);
            }
        }
        if(newtoDoItemModels!=null){
            Connection con=new Connection(mContext);
            if(con.isNetworkConnected()){
                removeData(newtoDoItemModels,mUserUID,startdate,index);
            }

        }
    }
}
