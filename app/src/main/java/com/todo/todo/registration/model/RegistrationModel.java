package com.todo.todo.registration.model;

/**
 * Created by bridgeit on 23/3/17.
 */

public class RegistrationModel {
    private  int mId;

    private  String mEmail;
    private  String mPassword;
    private  String mMobile;
    private  String mName;

    public RegistrationModel(int mId, String mEmail, String mPassword, String mMobile, String mName) {
        this.mId = mId;
        this.mEmail = mEmail;
        this.mPassword = mPassword;
        this.mMobile = mMobile;
        this.mName = mName;
    }

    public RegistrationModel() {
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
