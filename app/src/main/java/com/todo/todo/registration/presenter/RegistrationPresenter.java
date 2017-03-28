package com.todo.todo.registration.presenter;

import android.content.Context;
import android.util.Log;

import com.todo.todo.registration.interactor.RegistrationInteractor;
import com.todo.todo.registration.model.RegistrationModel;
import com.todo.todo.registration.view.RegistrationFragment;

/**
 * Created by bridgeit on 23/3/17.
 */

public class RegistrationPresenter  implements  RegistrationPresenterInterface{
    private  String TAG ="RegistrationPresenter";
    private  RegistrationInteractor registrationInteractor;
    private  Context mContext;
    RegistrationFragment registrationFragmentMain;
    public RegistrationPresenter(Context context, RegistrationFragment registrationFragment) {
        Log.i(TAG, "RegistrationPresenter: ");
        this.mContext=context;
        this.registrationFragmentMain = registrationFragment;
    }

    @Override
    public void showProgressDialog() {
        registrationFragmentMain.showProgressDialog();
    }

    @Override
    public void closeProgressDialog() {
        registrationFragmentMain.closeProgressDialog();
    }

    @Override
    public void getResponce(boolean flag) {
        registrationFragmentMain.getResponce(flag);
    }

    @Override
    public void setNewUser(RegistrationModel registrationModel) {
        Log.i(TAG, "setNewUser: ");
        registrationInteractor=new RegistrationInteractor(mContext,RegistrationPresenter.this);

        registrationInteractor.saveUser(registrationModel);
    }
}
