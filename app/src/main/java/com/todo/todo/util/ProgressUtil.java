package com.todo.todo.util;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressUtil {
    ProgressDialog mProgressDialog;
    private static String TAG ="ProgressUtil";
    public ProgressUtil(Context context){
        mProgressDialog= new ProgressDialog(context);
    }

    public void showProgress(String message){

        if(mProgressDialog!=null && !mProgressDialog.isShowing()  ) {

            mProgressDialog.setMessage(message);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.show();
            mProgressDialog.setCancelable(false);

        }
    }

    public void dismissProgress(){
        mProgressDialog.dismiss();
    }

}
