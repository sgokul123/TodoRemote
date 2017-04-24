package com.todo.todo.login.view;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.todo.todo.registration.model.RegistrationModel;


public interface LoginInterface {

    public void  loginSuccess(RegistrationModel registrationModel,String userUid);
    public  void handleGoogleSignInResult(GoogleSignInAccount result, String uid);
    public  void loginFailuar();
    public  void showProgress();
    public  void closeProgress();
    public void facebookResponce(String uid);
}


