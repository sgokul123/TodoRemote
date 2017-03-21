package com.todo.todo.login.presenter;

import android.util.Log;

import com.todo.todo.login.interactor.LoginLoginInteractor;
import com.todo.todo.login.model.LoginModel;
import com.todo.todo.login.view.LoginActivity;


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
        LoginModel model=new LoginModel(email,password);
        interactor.getFirbaseLogin(model);
        Log.i(TAG, "getLogin:  call");
        
    }

    @Override
    public void getLoginAuthentication(Boolean flag) {
        if(flag) {
            loginActivity.loginSuccess();
        }
        Log.i(TAG, "getLoginAuthentication: ");
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
