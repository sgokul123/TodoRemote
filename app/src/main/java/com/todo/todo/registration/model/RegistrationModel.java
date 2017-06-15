package com.todo.todo.registration.model;

/**
 * Created by bridgeit on 23/3/17.
 */

public class RegistrationModel {

    private String mailid;

    private String userFirstName;

    private String userLastName;

    private String userPassword;

    private String userProfileImgurl = "";

    private String mobileNo;

    public RegistrationModel(String userFirstName, String userLastName, String mobileNo, String mailid, String userPassword, String userProfileImgurl) {

        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.mobileNo = mobileNo;
        this.mailid = mailid;
        this.userPassword = userPassword;
        this.userProfileImgurl = userProfileImgurl;
    }

    public RegistrationModel() {    }

    public String getMailid() {
        return mailid;
    }

    public void setMailid(String mailid) {
        this.mailid = mailid;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserProfileImgurl() {
        return userProfileImgurl;
    }

    public void setUserProfileImgurl(String userProfileImgurl) {  this.userProfileImgurl = userProfileImgurl;    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

}

