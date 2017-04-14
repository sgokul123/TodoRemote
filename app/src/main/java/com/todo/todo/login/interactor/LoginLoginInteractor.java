package com.todo.todo.login.interactor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.login.model.LoginModel;
import com.todo.todo.login.presenter.LoginLoginPresenter;
import com.todo.todo.registration.model.RegistrationModel;
import com.todo.todo.util.Connection;

import java.util.ArrayList;

/**
 * Created by bridgeit on 14/3/17.
 */

public class LoginLoginInteractor implements LoginInteractorInterface {
    private  String TAG ="LoginLoginInteractor";
    private  Context context;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mRef;
    private LoginLoginPresenter mLoginPresenter;
LoginModel loginModel;
    public LoginLoginInteractor(LoginLoginPresenter loginPresenter) {
        Log.i(TAG, "LoginLoginInteractor: ");
        mLoginPresenter=loginPresenter;

    }

    @Override
    public void getFirbaseLogin(LoginModel loginModel) {
        firebaseAuth=FirebaseAuth.getInstance();
        mLoginPresenter.showProgress();
        this.loginModel = loginModel;
        Log.i(TAG, "getFirbaseLogin: "+loginModel);


            firebaseAuth.signInWithEmailAndPassword(loginModel.getmEmail(),loginModel.getmPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        Log.i(TAG, "getFirbaseLogin: call");
                            getProfile(task.getResult().getUser().getUid());

                    }
                    else {
                        Log.i(TAG, "closeDialog:  ");
                        mLoginPresenter.closeProgress();
                    }

                }
            });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


    }


    private void getProfile(final String uid) {
        mRef= FirebaseDatabase.getInstance().getReference().child("userprofile").child(uid);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<RegistrationModel>> t = new GenericTypeIndicator<ArrayList<RegistrationModel>>() {
                };
                Log.i(TAG, "onDataChange: ");
               RegistrationModel userPreofile = new RegistrationModel();
                userPreofile= (RegistrationModel) dataSnapshot.getValue(RegistrationModel.class);

                mLoginPresenter.getLoginAuthentication(userPreofile,uid);
                mLoginPresenter.closeProgress();


            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.i(TAG, "onCancelled: ");

            }
        });

     //   mLoginPresenter.getLoginAuthentication();
        mLoginPresenter.closeProgress();

    }


}
