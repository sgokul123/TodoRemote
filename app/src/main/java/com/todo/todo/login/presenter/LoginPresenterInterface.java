package com.todo.todo.login.presenter;

import com.todo.todo.registration.model.RegistrationModel;

/**
 * Created by bridgeit on 14/3/17.
 */

public interface LoginPresenterInterface {
        public  void getLogin(String email, String password);
        public  void getLoginAuthentication(RegistrationModel registrationModel,String uid);

        public  void showProgress();
        public  void closeProgress();

}
