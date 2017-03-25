package com.todo.todo.registration.presenter;

import com.todo.todo.registration.model.RegistrationModel;
import com.todo.todo.registration.view.RegistrationInterface;

import java.util.List;

/**
 * Created by bridgeit on 23/3/17.
 */

public interface RegistrationPresenterInterface extends RegistrationInterface {

    public  void setNewUser(RegistrationModel registrationModel);


}
