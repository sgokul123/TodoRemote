package com.todo.todo.registration.presenter;

import android.content.Context;
import android.util.Log;

import com.todo.todo.registration.interactor.RegistrationInteractor;
import com.todo.todo.registration.model.RegistrationModel;
import com.todo.todo.registration.view.Registration;

import java.util.List;

/**
 * Created by bridgeit on 23/3/17.
 */

public class RegistrationPresenter  implements  RegistrationPresenterInterface{
    private  String TAG ="RegistrationPresenter";
    private  RegistrationInteractor registrationInteractor;
    private  Context mContext;
    Registration registrationMain;
    public RegistrationPresenter(Context context, Registration registration) {
        Log.i(TAG, "RegistrationPresenter: ");
        this.mContext=context;
        this.registrationMain=registration;
    }

    @Override
    public void showProgressDialog() {
        registrationMain.showProgressDialog();
    }

    @Override
    public void closeProgressDialog() {
        registrationMain.closeProgressDialog();
    }

    @Override
    public void getResponce(boolean flag) {
        registrationMain.getResponce(flag);
    }

    @Override
    public void setNewUser(RegistrationModel registrationModel) {
        Log.i(TAG, "setNewUser: ");
        registrationInteractor=new RegistrationInteractor(mContext,RegistrationPresenter.this);

        registrationInteractor.saveUser(registrationModel);
    }
}
