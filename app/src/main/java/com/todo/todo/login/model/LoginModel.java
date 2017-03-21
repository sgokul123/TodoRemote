package com.todo.todo.login.model;

/**
 * Created by bridgeit on 14/3/17.
 */

public class LoginModel {

    private  String mEmail;

    public LoginModel(String mEmail, String mPassword) {
        this.mEmail = mEmail;
        this.mPassword = mPassword;
    }

    private  String mPassword;

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }



}
