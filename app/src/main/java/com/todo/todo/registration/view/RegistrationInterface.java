package com.todo.todo.registration.view;

import com.todo.todo.registration.model.RegistrationModel;

public interface RegistrationInterface {

    public  void showProgressDialog();
    public  void closeProgressDialog();
    public void getResponce(String uid, RegistrationModel model);
}
