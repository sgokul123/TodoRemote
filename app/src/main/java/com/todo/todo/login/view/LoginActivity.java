package com.todo.todo.login.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.todo.todo.R;
import com.todo.todo.home.view.ToDoActivity;
import com.todo.todo.login.presenter.LoginLoginPresenter;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,LoginInterface  {

    private  String TAG ="LoginActivity";
    AppCompatButton mButtonLogin;//,mButtonfbLogin;
    AppCompatEditText mEditTextEmail,mEditTextPassword;
    AppCompatTextView textview1;
    private String mStrPass,mStrEmail;
    private Pattern mPattern;
    private LoginLoginPresenter login;
    private Matcher mMatcher;
    ProgressDialog progressDialog;
    private static String APP_ID = "308180782571605"; // Replace your App ID here
    LoginButton loginButton;
    // Instance of Facebook Class


    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       // mButtonfbLogin=(AppCompatButton) findViewById(R.id.button_fb_login);

        mButtonLogin =(AppCompatButton) findViewById(R.id.button_signin);
        mEditTextEmail=(AppCompatEditText) findViewById(R.id.edittext_email);
        mEditTextPassword=(AppCompatEditText) findViewById(R.id.edittext_password);
        textview1=(AppCompatTextView) findViewById(R.id.textview_forgot);
        mButtonLogin.setOnClickListener(this);
        Log.i(TAG, "onCreate: ");
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait while login ...");
        login = new LoginLoginPresenter(LoginActivity.this);
       // mButtonfbLogin.setOnClickListener(this);
//fb login

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i(TAG, "onSuccess: ");
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel: ");
            }

            @Override
            public void onError(FacebookException error) {
                Log.i(TAG, "onError: ");
            }});
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.setOnClickListener(this);
        // If using in a fragment
      //  loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
       /* loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.i(TAG, "onSuccess: "+loginResult);
            }

            @Override
            public void onCancel() {
                // App code
                Log.i(TAG, "onCancel: does not loged in");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code

                Log.i(TAG, "onError: ");
            }
        });
*/

      /*  Animation animationToRight = new TranslateAnimation(-400,400, 0, 0);
        animationToRight.setDuration(12000);
        animationToRight.setRepeatMode(Animation.RESTART);
        animationToRight.setRepeatCount(Animation.INFINITE);
        textview1.setAnimation(animationToRight);*/
    }

    @Override
    public void onClick(View v) {

        Toast.makeText(this, "login...", Toast.LENGTH_SHORT).show();
        switch (v.getId()){
            case R.id.login_button:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
                break;
            case R.id.button_signin:
                doAuthentication();
                break;
            default:
                break;
        }
        // LoginPresenterInterface presenter=new LoginLoginPresenter();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void doAuthentication() {
        mPattern = Pattern.compile(EMAIL_PATTERN);
        mStrEmail = mEditTextEmail.getText().toString();
        mStrPass = mEditTextPassword.getText().toString();
        Log.i(TAG, "doAuthentication: "+mStrEmail);
        if (!(mStrEmail.equals("") && mStrPass.equals(""))) {
            mMatcher = mPattern.matcher(mStrEmail);
            if (mMatcher.matches()) {
                login.getLogin(mStrEmail,mStrPass);


            } else {
                Toast.makeText(getApplicationContext(), "Please Enter Valid Email ...", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(getApplicationContext(), "Please Enter The User Name  And Password ... ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void loginSuccess() {
        Toast.makeText(this, "Success....", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "loginSuccess: ");

        Intent intent=new Intent(LoginActivity.this,ToDoActivity.class);
        intent.putExtra("user_id",mEditTextEmail.getText().toString());
        startActivity(intent);
        finish();

    }

    @Override
    public void loginFailuar() {
        progressDialog.dismiss();
        Log.i(TAG, "loginFailuar: ");
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void closeProgress() {
        progressDialog.dismiss();
    }


}
