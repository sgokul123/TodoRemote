package com.todo.todo.login.view;

/**
 * Created by bridgeit on 14/3/17.
 */

public interface LoginInterface {

    public void  loginSuccess(String userUid);

    public  void loginFailuar();
    public  void showProgress();
    public  void closeProgress();

}


