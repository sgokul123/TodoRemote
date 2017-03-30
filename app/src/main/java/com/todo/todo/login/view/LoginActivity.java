package com.todo.todo.login.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.todo.todo.R;
import com.todo.todo.base.BaseActivity;
import com.todo.todo.home.view.ToDoActivity;
import com.todo.todo.login.presenter.LoginLoginPresenter;
import com.todo.todo.registration.model.RegistrationModel;
import com.todo.todo.registration.view.RegistrationFragment;
import com.todo.todo.util.Constants;
import com.todo.todo.util.ProgressUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends BaseActivity implements View.OnClickListener,LoginInterface, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static String APP_ID = "308180782571605"; // Replace your App ID here
    AppCompatButton mButtonLogin;//,mButtonfbLogin;
    AppCompatEditText mEditTextEmail,mEditTextPassword;
    AppCompatTextView textview1,textViewSignUp;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ProgressUtil progressDialog;
    LoginButton loginButton;
    SignInButton signgoogleInButton;
    private  String TAG ="LoginActivity";
    private String mStrPass;
    private String mStrEmail="";
    private String mStrName="Sonawane Gokul";
    private Uri mImageUrl= Uri.parse("");
    private Pattern mPattern;
    private LoginLoginPresenter login;
    private Matcher mMatcher;
    // Instance of Facebook Class
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager callbackManager;

    @Override
    public void initialise() {
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        // mButtonfbLogin=(AppCompatButton) findViewById(R.id.button_fb_login);
        signgoogleInButton = (SignInButton) findViewById(R.id.gsign_in_button);
        signgoogleInButton.setSize(SignInButton.SIZE_STANDARD);
        signgoogleInButton.setOnClickListener(this);
        mButtonLogin =(AppCompatButton) findViewById(R.id.button_signin);
        mEditTextEmail=(AppCompatEditText) findViewById(R.id.edittext_email);
        mEditTextPassword=(AppCompatEditText) findViewById(R.id.edittext_password);
        textview1=(AppCompatTextView) findViewById(R.id.textview_forgot);
        textViewSignUp=(AppCompatTextView) findViewById(R.id.registation);
        mButtonLogin.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
        Log.i(TAG, "onCreate: ");

        progressDialog=new ProgressUtil(this);
        login = new LoginLoginPresenter(LoginActivity.this );
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        pref = getSharedPreferences("testapp", MODE_PRIVATE);
        editor = pref.edit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialise();
        facebookLogin();
           /*  Animation animationToRight = new TranslateAnimation(-400,400, 0, 0);
        animationToRight.setDuration(12000);
        animationToRight.setRepeatMode(Animation.RESTART);
        animationToRight.setRepeatCount(Animation.INFINITE);
        textview1.setAnimation(animationToRight);*/
    }
    //check is User  Login

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.login_button:
               // LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

                break;
            case R.id.button_signin:

                doAuthentication();
                break;
            case R.id.gsign_in_button:

                googleLogin();
                signIn();
                break;
            case R.id.registation:
                getSupportFragmentManager().beginTransaction().replace(R.id.login_layout, RegistrationFragment.newInstance("","")).addToBackStack(null).commit();
                Log.i(TAG, "onClick: ");

                break;
            default:

                break;
        }

    }

    //Facebook Social Login
    public  void facebookLogin(){

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginActivity.this, "User ID: "
                        + loginResult.getAccessToken().getUserId()
                        + "\n" +
                        "Auth Token: "
                        + loginResult.getAccessToken().getToken(), Toast.LENGTH_SHORT).show();
                //Facebbok Login
                Log.i(TAG, "onSuccess: ");
                editor.putString(Constants.BundleKey.USER_REGISTER,"true");
                editor.putString(Constants.BundleKey.USER_EMAIL,mStrEmail);
                editor.putString(Constants.BundleKey.USER_NAME,mStrName);
                editor.putString(Constants.BundleKey.FACEBOOK_LOGIN,"true");
                editor.commit();

             /*   Profile profile = Profile.getCurrentProfile();
                mStrEmail=profile.getId();
                mStrName=profile.getFirstName()+" "+profile.getLastName();
               */// mImageUrl=profile.getProfilePictureUri(20,20);
               // Log.i(TAG, "onSuccess: "+mImageUrl);
                Intent intent=new Intent(LoginActivity.this,ToDoActivity.class);
                intent.putExtra(Constants.BundleKey.USER_EMAIL,mStrEmail);
                intent.putExtra(Constants.BundleKey.USER_NAME,mStrName);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login attempt canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //Google Social Login
    public  void googleLogin(){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    //connection check
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            // mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            //   updateUI(true);
            //Google Login

            editor.putString(Constants.BundleKey.USER_REGISTER,"true");
            editor.putString(Constants.BundleKey.USER_EMAIL,mStrEmail);
            editor.putString(Constants.BundleKey.USER_NAME,mStrName);
            editor.putString(Constants.BundleKey.GOOGLE_LOGIN,"true");
            editor.commit();

            Intent intent=new Intent(LoginActivity.this,ToDoActivity.class);
            intent.putExtra(Constants.BundleKey.USER_EMAIL,mStrEmail);
            intent.putExtra(Constants.BundleKey.USER_NAME,mStrName);
            startActivity(intent);
            finish();

            Toast.makeText(this, "Signin", Toast.LENGTH_SHORT).show();
        } else {
            // Signed out, show unauthenticated UI.
            //   updateUI(false);

            Toast.makeText(this, "Signout", Toast.LENGTH_SHORT).show();
        }
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
    public void loginSuccess(RegistrationModel registrationModel, String userUid) {
        Toast.makeText(this, "Success...."+userUid, Toast.LENGTH_SHORT).show();
        Log.i(TAG, "loginSuccess: "+userUid);

        editor.putString(Constants.BundleKey.USER_REGISTER,"true");
        editor.putString(Constants.BundleKey.USER_EMAIL,registrationModel.getMailid());
        editor.putString(Constants.BundleKey.USER_USER_UID,userUid);
        editor.putString(Constants.ProfileeKey.FIRST_NAME,registrationModel.getUserFirstName());
        editor.putString(Constants.ProfileeKey.LAST_NAME,registrationModel.getUserLastName());
        editor.putString(Constants.ProfileeKey.MOBILE_NO,registrationModel.getMobileNo());
        editor.putString(Constants.ProfileeKey.PROFILE_IMAGE_URL,registrationModel.getUserProfileImgurl());

        editor.putString(Constants.BundleKey.USER_NAME,mStrName);
        editor.commit();
        ///  show registration page again

        Intent intent=new Intent(LoginActivity.this,ToDoActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginFailuar() {
        progressDialog.dismissProgress();
        Log.i(TAG, "loginFailuar: ");
    }

    @Override
    public void showProgress() {
        progressDialog.showProgress("Login User...");
    }

    @Override
    public void closeProgress() {
        progressDialog.dismissProgress();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//for google social login

        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }


    //google social login signout
  /*  private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }*/
}
