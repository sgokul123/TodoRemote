package com.todo.todo.login.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginInterface {

    private static final int RC_SIGN_IN = 9001;
    private static String EMAIL_PATTERN;
    private static String APP_ID = "308180782571605";           // Replace your App ID here
    AppCompatButton mButtonLogin;                                 //,mButtonfbLogin;
    AppCompatEditText mEditTextEmail, mEditTextPassword;
    AppCompatTextView textview1, textViewSignUp;
    SharedPreferences pref;
    SharedPreferences.Editor mSharedPref_editor;
    ProgressUtil progressDialog;
    LoginButton mFacebookLoginButton;
    SignInButton googleSignInButton;
    private String TAG = "LoginActivity";
    private String mStrUserPassword;
    private String mStrUserEmail = "";
    private String mStrUserName = "Sonawane Gokul";
    FrameLayout frameLayout;
    private Pattern mPattern;
    private LoginLoginPresenter loginLoginPresenter;
    private Matcher mMatcher;
    // Instance of Facebook Class
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mFBCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        facebookLogin();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_login);
        //facebook Login API call
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mFBCallbackManager = CallbackManager.Factory.create();
        progressDialog = new ProgressUtil(this);
        googleSignInButton = (SignInButton) findViewById(R.id.gsign_in_button);
        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);
        EMAIL_PATTERN = getString(R.string.email_pattern);
        mFacebookLoginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        mFacebookLoginButton.setReadPermissions(getString(R.string.gmail_access));
        mButtonLogin = (AppCompatButton) findViewById(R.id.button_signin);
        mEditTextEmail = (AppCompatEditText) findViewById(R.id.edittext_email);
        mEditTextPassword = (AppCompatEditText) findViewById(R.id.edittext_password);
        textview1 = (AppCompatTextView) findViewById(R.id.textview_forgot);
        textViewSignUp = (AppCompatTextView) findViewById(R.id.registation);
        frameLayout= (FrameLayout) findViewById(R.id.framlayout_login);
        Log.i(TAG, "onCreate: ");
        //call to data Access before load activity
        loginLoginPresenter = new LoginLoginPresenter(LoginActivity.this, getApplicationContext());
        pref = getSharedPreferences(Constants.ProfileeKey.SHAREDPREFERANCES_KEY, MODE_PRIVATE);
        if (!pref.getString(Constants.BundleKey.FR_USER_EMAIL, "").equals(""))
            mEditTextEmail.setText(pref.getString(Constants.BundleKey.FR_USER_EMAIL, "").toString());
        if (!pref.getString(Constants.BundleKey.USER_PASSWORD, "").equals(""))
            mEditTextPassword.setText(pref.getString(Constants.BundleKey.USER_PASSWORD, "").toString());
        mSharedPref_editor = pref.edit();
        setOnClickListener();
        googleAPICall();
    }


    /*
    * call tpo the google API
    * */
    private void googleAPICall() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void setOnClickListener() {
        googleSignInButton.setOnClickListener(this);
        mButtonLogin.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);

    }

    @Override
    public void enterFromBottomAnimation() {

    }

    @Override
    public void exitToBottomAnimation() {

    }

    /**
     * check is User  Login
     */

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.facebook_login_button:

                break;
            case R.id.button_signin:

                doAuthentication();
                break;
            case R.id.gsign_in_button:
                signIn();

                break;
            case R.id.registation:
                getSupportFragmentManager().beginTransaction().replace(R.id.login_layout, RegistrationFragment.newInstance("", "")).addToBackStack(null).commit();
                Log.i(TAG, "onClick: ");

                break;
            default:

                break;
        }

    }

    private Bundle getFacebookData(JSONObject object) {
        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL(getString(R.string.fb_url_start) + id + getString(R.string.fb_url_last));
                Log.i(getString(R.string.profile_pic), profile_pic + "");
                bundle.putString(getString(R.string.profile_pic), profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
            bundle.putString("idFacebook", id);
            if (object.has(getString(R.string.first_name)))
                bundle.putString(getString(R.string.first_name), object.getString(getString(R.string.first_name)));
            if (object.has(getString(R.string.last_name)))
                bundle.putString(getString(R.string.last_name), object.getString(getString(R.string.last_name)));
            if (object.has(getString(R.string.email_id)))
                bundle.putString(getString(R.string.email_id), object.getString(getString(R.string.email_id)));
            if (object.has(getString(R.string.gender)))
                bundle.putString(getString(R.string.gender), object.getString(getString(R.string.gender)));
            if (object.has(getString(R.string.birthday)))
                bundle.putString(getString(R.string.birthday), object.getString(getString(R.string.birthday)));
            if (object.has(getString(R.string.location)))
                bundle.putString(getString(R.string.location), object.getJSONObject(getString(R.string.location)).getString("name"));
            return bundle;
        } catch (JSONException e) {
            Log.d(TAG, "Error parsing JSON");
            return null;
        }
    }


    /*
    *
    * Facebook Social Login
    * */
    public void facebookLogin() {

        mFacebookLoginButton.registerCallback(mFBCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                frameLayout.setVisibility(View.VISIBLE);
                loginLoginPresenter.handleFacebookAccessToken(loginResult.getAccessToken());

                System.out.println("onSuccess");

                String accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());
                        // Get facebook data from login
                        Bundle bFacebookData = getFacebookData(object);
                        String emailid = bFacebookData.getString(getString(R.string.email_id));
                        mSharedPref_editor.putString(Constants.BundleKey.USER_REGISTER, getString(R.string.flag_true));
                        mSharedPref_editor.putString(Constants.BundleKey.USER_EMAIL, emailid);
                        mSharedPref_editor.putString(Constants.BundleKey.PROFILE_PIC, bFacebookData.getString(getString(R.string.profile_pic)));
                        mSharedPref_editor.putString(Constants.ProfileeKey.FIRST_NAME, bFacebookData.getString(getString(R.string.first_name)));
                        mSharedPref_editor.putString(Constants.ProfileeKey.LAST_NAME, bFacebookData.getString(getString(R.string.last_name)));
                        mSharedPref_editor.putString(Constants.BundleKey.FACEBOOK_LOGIN, getString(R.string.flag_true));
                        mSharedPref_editor.putString(Constants.ProfileeKey.PROFILE_IMAGE_URL, "");
                        mSharedPref_editor.commit();
                        Log.i(TAG, "onCompleted: " + bFacebookData.getString("id"));
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString(getString(R.string.fields), getString(R.string.fb_profile_fields));
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, getString(R.string.fb_login_cancel), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(LoginActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFBCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = result.getSignInAccount();
            loginLoginPresenter.firebaseAuthWithGoogle(account);
        } else {
            Log.i(TAG, "onActivityResult: ");
        }
    }

    private void doAuthentication() {
        mPattern = Pattern.compile(EMAIL_PATTERN);
        mStrUserEmail = mEditTextEmail.getText().toString();
        mStrUserPassword = mEditTextPassword.getText().toString();
        Log.i(TAG, "doAuthentication: " + mStrUserEmail);
        if (!(mStrUserEmail.equals("") && mStrUserPassword.equals(""))) {
            mMatcher = mPattern.matcher(mStrUserEmail);
            if (mMatcher.matches()) {
                loginLoginPresenter.getLogin(mStrUserEmail, mStrUserPassword);
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.fb_login_unAthorised), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.enter_valid), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void loginSuccess(RegistrationModel registrationModel, String userUid) {
        Log.i(TAG, "loginSuccess: " + userUid);
        mSharedPref_editor.putString(Constants.BundleKey.USER_REGISTER, getString(R.string.flag_true));
        mSharedPref_editor.putString(Constants.BundleKey.USER_EMAIL, registrationModel.getMailid());
        mSharedPref_editor.putString(Constants.BundleKey.FR_USER_EMAIL, registrationModel.getMailid());
        mSharedPref_editor.putString(Constants.BundleKey.USER_PASSWORD, registrationModel.getUserPassword());
        mSharedPref_editor.putString(Constants.BundleKey.USER_USER_UID, userUid);
        mSharedPref_editor.putString(Constants.ProfileeKey.FIRST_NAME, registrationModel.getUserFirstName());
        mSharedPref_editor.putString(Constants.ProfileeKey.LAST_NAME, registrationModel.getUserLastName());
        mSharedPref_editor.putString(Constants.ProfileeKey.MOBILE_NO, registrationModel.getMobileNo());
        mSharedPref_editor.putString(Constants.ProfileeKey.PROFILE_IMAGE_URL, registrationModel.getUserProfileImgurl());
        mSharedPref_editor.putString(Constants.BundleKey.USER_PROFILE_SERVER, getString(R.string.flag_true));
        mSharedPref_editor.putString(Constants.BundleKey.USER_NAME, mStrUserName);
        mSharedPref_editor.commit();
        ///  show registration page again
        Intent intent = new Intent(LoginActivity.this, ToDoActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void handleGoogleSignInResult(GoogleSignInAccount result, String uid) {

        GoogleSignInAccount acct = result;

        //Google Login
        Log.i(TAG, "handleGoogleSignInResult: " + acct.getPhotoUrl());
        mSharedPref_editor.putString(Constants.BundleKey.USER_REGISTER, getString(R.string.flag_true));
        mSharedPref_editor.putString(Constants.BundleKey.USER_EMAIL, acct.getEmail());
        mSharedPref_editor.putString(Constants.ProfileeKey.FIRST_NAME, acct.getDisplayName());
        mSharedPref_editor.putString(Constants.BundleKey.PROFILE_PIC, String.valueOf(acct.getPhotoUrl()));
        mSharedPref_editor.putString(Constants.ProfileeKey.PROFILE_IMAGE_URL, "");
        mSharedPref_editor.putString(Constants.BundleKey.USER_USER_UID, uid);
        mSharedPref_editor.putString(Constants.BundleKey.GOOGLE_LOGIN, getString(R.string.flag_true));
        mSharedPref_editor.commit();
        Intent intent = new Intent(LoginActivity.this, ToDoActivity.class);
        intent.putExtra(Constants.BundleKey.USER_EMAIL, mStrUserEmail);
        intent.putExtra(Constants.BundleKey.USER_NAME, mStrUserName);
        startActivity(intent);
        finish();

    }

    @Override
    public void loginFailuar() {
        progressDialog.dismissProgress();
        Toast.makeText(this, getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        progressDialog.showProgress(getString(R.string.login_user));
    }

    @Override
    public void closeProgress() {
        progressDialog.dismissProgress();
    }

    @Override
    public void facebookResponce(String uid) {
        mSharedPref_editor.putString(Constants.BundleKey.USER_USER_UID, uid);
        mSharedPref_editor.commit();
        Intent intent = new Intent(LoginActivity.this, ToDoActivity.class);
        startActivity(intent);
        finish();
    }


}
