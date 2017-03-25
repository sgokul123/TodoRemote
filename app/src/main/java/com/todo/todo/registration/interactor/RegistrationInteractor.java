package com.todo.todo.registration.interactor;

import android.content.Context;
import android.util.Log;

import com.todo.todo.home.database.DatabaseHandler;
import com.todo.todo.registration.database.RegistrationDatabaseModel;
import com.todo.todo.registration.model.RegistrationModel;
import com.todo.todo.registration.presenter.RegistrationPresenter;

/**
 * Created by bridgeit on 23/3/17.
 */

public class RegistrationInteractor implements  RegistrationInteractorInterface{
    private  String TAG ="RegistrationInteractor";
    private  Context mContext;
    private  RegistrationPresenter registrationPresenter;
    RegistrationDatabaseModel db;
    public RegistrationInteractor(Context context , RegistrationPresenter registrationPresenter) {
       this. registrationPresenter=registrationPresenter;
        this.mContext=context;
        Log.i(TAG, "RegistrationInteractor: ");
        db = new RegistrationDatabaseModel(mContext,RegistrationInteractor.this);
    }


    @Override
    public void saveUser(RegistrationModel registrationModel) {
       // registrationPresenter.showProgressDialog();
            db.addUser(registrationModel);
        Log.i(TAG, "saveUser: ");

    }

    @Override
    public void getResponce(boolean flag) {
            if(flag){
              registrationPresenter.getResponce(flag);
               // registrationPresenter.closeProgressDialog();
            }
            else {
               // registrationPresenter.closeProgressDialog();
            }
    }
}
