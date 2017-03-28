package com.todo.todo.login.model;

/**
 * Created by bridgeit on 14/3/17.
 */

public class LoginModel {
    private  int mId;

    private  String mEmail;
    private  String mPassword;
    private  String mMobile;
    private  String mName;



    public LoginModel(String email, String password) {
        this.mEmail = email;
        this.mPassword = password;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

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

    public String getmMobile() {
        return mMobile;
    }

    public void setmMobile(String mMobile) {
        this.mMobile = mMobile;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
}
