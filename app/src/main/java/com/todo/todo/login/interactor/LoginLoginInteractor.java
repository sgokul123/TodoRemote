package com.todo.todo.login.interactor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.todo.todo.login.model.LoginModel;
import com.todo.todo.login.presenter.LoginLoginPresenter;
import com.todo.todo.util.Connection;

/**
 * Created by bridgeit on 14/3/17.
 */

public class LoginLoginInteractor implements LoginInteractorInterface {
    private  String TAG ="LoginLoginInteractor";
    private  Context context;
    FirebaseAuth firebaseAuth;
    private LoginLoginPresenter mLoginPresenter;
    Connection connection;
    public LoginLoginInteractor(LoginLoginPresenter loginPresenter) {
        Log.i(TAG, "LoginLoginInteractor: ");
        mLoginPresenter=loginPresenter;
        connection=new Connection();
    }

    @Override
    public void getFirbaseLogin(LoginModel loginModel) {
        firebaseAuth=FirebaseAuth.getInstance();
        mLoginPresenter.showProgress();


            firebaseAuth.signInWithEmailAndPassword(loginModel.getmEmail(),loginModel.getmPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        Log.i(TAG, "getFirbaseLogin: call");
                        mLoginPresenter.closeProgress();
                        mLoginPresenter.getLoginAuthentication(true);

                    }
                    else {
                        Log.i(TAG, "closeDialog:  ");
                        mLoginPresenter.closeProgress();
                    }

                }
            });




    }



}
