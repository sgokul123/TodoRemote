package com.todo.todo.login.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.todo.todo.login.model.LoginModel;
import com.todo.todo.login.presenter.LoginPresenterInterface;
import com.todo.todo.registration.model.RegistrationModel;

import java.util.ArrayList;

/**
 * Created by bridgeit on 14/3/17.
 */

public class LoginLoginInteractor implements LoginInteractorInterface {
    FirebaseAuth firebaseAuth;
    DatabaseReference mRef;
    LoginModel loginModel;
    Context mContext;
    private String TAG = "LoginLoginInteractor";
    private Context context;
    ;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private LoginPresenterInterface mLoginPresenterInterface;

    public LoginLoginInteractor(LoginPresenterInterface loginPresenterInterface, Context context) {
        Log.i(TAG, "LoginLoginInteractor: ");
        mLoginPresenterInterface = loginPresenterInterface;
        this.mContext = context;
        mAuthListeners();

    }

    private void mAuthListeners() {

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

    @Override
    public void getFirbaseLogin(LoginModel loginModel) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(mAuthListener);
        mLoginPresenterInterface.showProgress();
        this.loginModel = loginModel;
        Log.i(TAG, "getFirbaseLogin: " + loginModel);


        firebaseAuth.signInWithEmailAndPassword(loginModel.getmEmail(), loginModel.getmPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Log.i(TAG, "getFirbaseLogin: call");
                    getProfile(task.getResult().getUser().getUid());

                } else {
                    Log.i(TAG, "closeDialog:  ");
                    mLoginPresenterInterface.loginFailuar();
                    mLoginPresenterInterface.closeProgress();
                }

            }
        });

    }


    private void getProfile(final String uid) {
        mRef = FirebaseDatabase.getInstance().getReference().child("userprofile").child(uid);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<RegistrationModel>> t = new GenericTypeIndicator<ArrayList<RegistrationModel>>() {
                };
                Log.i(TAG, "onDataChange: ");
                RegistrationModel userPreofile = new RegistrationModel();
                userPreofile = (RegistrationModel) dataSnapshot.getValue(RegistrationModel.class);

                mLoginPresenterInterface.getLoginAuthentication(userPreofile, uid);
                mLoginPresenterInterface.closeProgress();


            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.i(TAG, "onCancelled: ");

            }
        });

        //   mLoginPresenter.getLoginAuthentication();
        mLoginPresenterInterface.closeProgress();

    }


    public void handleFacebookAccessAuthToken(AccessToken accessToken) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(mAuthListener);
        String token = accessToken.getToken();
        AuthCredential credential = FacebookAuthProvider.getCredential(token);

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    mLoginPresenterInterface.facebookResponceUID(task.getResult().getUser().getUid());
                }

            }
        });
    }

    public void authenticationGoogle(final GoogleSignInAccount account) {

        firebaseAuth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                if (task.isSuccessful()) {
                    mLoginPresenterInterface.handleGoogleSignInResult(account, task.getResult().getUser().getUid());
                }
            }
        });
    }
}
