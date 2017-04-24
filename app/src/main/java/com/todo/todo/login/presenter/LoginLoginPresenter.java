package com.todo.todo.login.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.todo.todo.login.interactor.LoginLoginInteractor;
import com.todo.todo.login.model.LoginModel;
import com.todo.todo.login.view.LoginActivity;
import com.todo.todo.login.view.LoginInterface;
import com.todo.todo.registration.model.RegistrationModel;
import com.todo.todo.util.Connection;


/**
 * Created by bridgeit on 14/3/17.
 */

public class LoginLoginPresenter implements LoginPresenterInterface {
    private  String TAG ="LoginLoginPresenter";
    private LoginInterface mLoginInterface;
    private LoginLoginInteractor interactor;
    Context  mContext;

    public LoginLoginPresenter(LoginInterface loginInterface, Context context) {
        Log.i(TAG, "LoginLoginPresenter: ");
               interactor =new LoginLoginInteractor(this,mContext);
                this.mLoginInterface=loginInterface;
        this.mContext=context;
    }

    @Override
    public void getLogin(String email, String password) {

        Connection connection=new Connection(mContext);
        if(connection.isNetworkConnected()){
            LoginModel model=new LoginModel(email,password);
            interactor.getFirbaseLogin(model);
            Log.i(TAG, "getLogin:  call");

        }else{
            mLoginInterface.closeProgress();
            Toast.makeText(mContext, "Connection not Present...", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void getLoginAuthentication(RegistrationModel registrationModel, String uid) {
        if(uid!=null){
            Log.i(TAG, "getLoginAuthentication: ");

            mLoginInterface.loginSuccess(registrationModel,uid);
        }
        else {
            mLoginInterface.loginFailuar();
        }
    }



    @Override
    public void showProgress() {
        Log.i(TAG, "showProgress: ");
        mLoginInterface.showProgress();

    }

    @Override
    public void closeProgress() {
        Log.i(TAG, "closeProgress: ");
        mLoginInterface.closeProgress();

    }

    @Override
    public void handleGoogleSignInResult(GoogleSignInAccount account, String uid) {
        mLoginInterface.handleGoogleSignInResult(account,uid);

    }

    @Override
    public void facebookResponceUID(String uid) {
        mLoginInterface.facebookResponce(uid);
    }


    public void handleFacebookAccessToken(AccessToken accessToken) {
        interactor.handleFacebookAccessAuthToken(accessToken);
    }


    public void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        interactor.authenticationGoogle(account);
    }

}
