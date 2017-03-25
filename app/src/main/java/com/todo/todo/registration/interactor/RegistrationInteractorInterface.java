package com.todo.todo.registration.interactor;

import com.todo.todo.registration.model.RegistrationModel;

/**
 * Created by bridgeit on 23/3/17.
 */

public interface RegistrationInteractorInterface {

    public  void saveUser(RegistrationModel registrationModel);
    public  void getResponce(boolean flag);
}
