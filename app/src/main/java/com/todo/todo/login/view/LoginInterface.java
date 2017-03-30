package com.todo.todo.login.view;

import com.todo.todo.registration.model.RegistrationModel;

/**
 * Created by bridgeit on 14/3/17.
 */

public interface LoginInterface {

    public void  loginSuccess(RegistrationModel registrationModel,String userUid);

    public  void loginFailuar();
    public  void showProgress();
    public  void closeProgress();

}


