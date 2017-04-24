package com.todo.todo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.todo.todo.home.adapter.ItemAdapter;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.update.presenter.UpdateNotePresenter;

import java.io.ByteArrayOutputStream;

/**
 * Created by bridgeit on 14/4/17.
 */

public class Utility {
    private  static  String TAG="Utility";
    ProgressUtil mProgressDialog;
    Context mContext;
    public Utility(Context context) {
        this.mContext=context;
        mProgressDialog=new ProgressUtil(mContext);
    }

    public void uploadFile(Bitmap bitmap, String mEmail_id){
        //if there is a file to upload
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bitmap, "Title", null);

        if (Uri.parse(path) != null) {
            //displaying a progress dialog while upload is going on
            mProgressDialog.showProgress("Uploading");
            StorageReference riversRef = FirebaseStorage.getInstance().getReference();
            StorageReference ref=riversRef.child("myProfiles/"+mEmail_id.substring(0,mEmail_id.indexOf("@"))+".jpg");
            ref.putFile(Uri.parse(path))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //hiding the progress dialog
                            mProgressDialog.dismissProgress();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //hiding the progress dialog
                            mProgressDialog.dismissProgress();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //displaying percentage in progress dialog
                            mProgressDialog.showProgress("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            Log.i(TAG, "uploadFile: ");  //you can display an error toast
        }
    }


}
