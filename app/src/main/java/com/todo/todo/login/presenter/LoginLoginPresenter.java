package com.todo.todo.login.presenter;

import android.util.Log;
import android.widget.Toast;

import com.todo.todo.login.interactor.LoginLoginInteractor;
import com.todo.todo.login.model.LoginModel;
import com.todo.todo.login.view.LoginActivity;
import com.todo.todo.util.Connection;


/**
 * Created by bridgeit on 14/3/17.
 */

public class LoginLoginPresenter implements LoginPresenterInterface {
    private  String TAG ="LoginLoginPresenter";
    private LoginActivity loginActivity;
    private LoginLoginInteractor interactor;

    public LoginLoginPresenter(LoginActivity loginActivity) {
        Log.i(TAG, "LoginLoginPresenter: ");
               interactor =new LoginLoginInteractor(this);
                this.loginActivity=loginActivity;
    }

    @Override
    public void getLogin(String email, String password) {

        Connection connection=new Connection(loginActivity);
        if(connection.isNetworkConnected()){
            LoginModel model=new LoginModel(email,password);
            interactor.getFirbaseLogin(model);
            Log.i(TAG, "getLogin:  call");

        }else{
            loginActivity.closeProgress();
            Toast.makeText(loginActivity, "Connection not Present...", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void getLoginAuthentication(String uid) {

        if(uid!=null){
            Log.i(TAG, "getLoginAuthentication: ");
            loginActivity.loginSuccess(uid);
        }
       else {
            loginActivity.loginFailuar();
        }

    }


    @Override
    public void showProgress() {
        Log.i(TAG, "showProgress: ");
        loginActivity.showProgress();

    }

    @Override
    public void closeProgress() {
        Log.i(TAG, "closeProgress: ");
        loginActivity.closeProgress();

    }


}
