package com.todo.todo.registration.interactor;

import com.todo.todo.registration.model.RegistrationModel;

public interface RegistrationInteractorInterface {

    public void saveUser(RegistrationModel registrationModel);

    public void getResponce(RegistrationModel registrationModel);
}
