package com.todo.todo.registration.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.todo.todo.registration.model.RegistrationModel;
import com.todo.todo.registration.presenter.RegistrationPresenterInterface;
import com.todo.todo.util.Connection;

public class RegistrationInteractor implements RegistrationInteractorInterface {
    private String TAG = "RegistrationInteractor";
    private Context mContext;
    private RegistrationPresenterInterface registrationPresenter;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public RegistrationInteractor(Context context, RegistrationPresenterInterface registrationPresenter) {
        this.registrationPresenter = registrationPresenter;
        this.mContext = context;
        Log.i(TAG, "RegistrationInteractor: ");
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    @Override
    public void saveUser(final RegistrationModel registrationModel) {

        registrationPresenter.showProgressDialog();
        try {
            Connection con=new Connection(mContext);
            if(con.isNetworkConnected()){
                mAuth.createUserWithEmailAndPassword(registrationModel.getMailid(), registrationModel.getUserPassword())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    registerData(task.getResult().getUser().getUid(), registrationModel);
                                } else {
                                    //registrationPresenter.getResponce(false);
                                    registrationPresenter.closeProgressDialog();
                                }
                            }
                        });
            }else {
                registrationPresenter.registrationFailuar();
            }

        } catch (Exception e) {
            registrationPresenter.closeProgressDialog();
            Log.i(TAG, "saveUser: " + e);

        }
        Log.i(TAG, "saveUser: ");
    }

    @Override
    public void getResponce(boolean flag) {


    }

    public void registerData(String uid, RegistrationModel registrationModel) {

        try {
            mDatabase.child("userprofile").child(uid).setValue(registrationModel);
            registrationPresenter.getResponce(uid, registrationModel);
            registrationPresenter.closeProgressDialog();

        } catch (Exception f) {
            Log.i(TAG, "registerData: " + f);
        }

    }
}
