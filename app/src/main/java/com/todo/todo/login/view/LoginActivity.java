package com.todo.todo.login.view;

import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Arrays;
import java.util.List;
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
    SharedPreferences.Editor mSharedPref_editor;
    ProgressUtil progressDialog;
    LoginButton mFacebookLoginButton;
    SignInButton googleSignInButton;
    private  String TAG ="LoginActivity";
    private String mStrUserPassword;
    private String mStrUserEmail ="";
    private String mStrUserName ="Sonawane Gokul";
    private Uri mProfileImageUrl = Uri.parse("");
    private Pattern mPattern;
    private LoginLoginPresenter loginLoginPresenter;
    private Matcher mMatcher;
    // Instance of Facebook Class
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mFBCallbackManager;
    List<String> listPermission = Arrays.asList("email", "public_profile");
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
    @Override
    public void initialise() {
        setContentView(R.layout.activity_login);

        //facebook Login API call
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mFBCallbackManager = CallbackManager.Factory.create();
        // mButtonfbLogin=(AppCompatButton) findViewById(R.id.button_fb_login);

        progressDialog=new ProgressUtil(this);
        googleSignInButton = (SignInButton) findViewById(R.id.gsign_in_button);
        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);

        mFacebookLoginButton = (LoginButton)findViewById(R.id.facebook_login_button);
        mFacebookLoginButton.setReadPermissions("public_profile email");

        mButtonLogin =(AppCompatButton) findViewById(R.id.button_signin);
        mEditTextEmail=(AppCompatEditText) findViewById(R.id.edittext_email);
        mEditTextPassword=(AppCompatEditText) findViewById(R.id.edittext_password);
        textview1=(AppCompatTextView) findViewById(R.id.textview_forgot);
        textViewSignUp=(AppCompatTextView) findViewById(R.id.registation);

        Log.i(TAG, "onCreate: ");
        //call to data Access before load activity
        loginLoginPresenter = new LoginLoginPresenter(LoginActivity.this );

        pref = getSharedPreferences("testapp", MODE_PRIVATE);
        mSharedPref_editor = pref.edit();
        setOnClickListener();
    }

    @Override
    public void setOnClickListener() {
        googleSignInButton.setOnClickListener(this);
        mButtonLogin.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);

    }

    //check is User  Login

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.facebook_login_button:
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


    private Bundle getFacebookData(JSONObject object) {
        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            return bundle;
        }
        catch(JSONException e) {
            Log.d(TAG,"Error parsing JSON");
            return null;
        }
    }


    //Facebook Social Login
    public  void facebookLogin(){

        mFacebookLoginButton.registerCallback(mFBCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                System.out.println("onSuccess");

                String accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());
                        // Get facebook data from login
                        Bundle bFacebookData = getFacebookData(object);
                        int index;
                        String emailid=bFacebookData.getString("email");
                        index=emailid.indexOf("@");
                        mSharedPref_editor.putString(Constants.BundleKey.USER_REGISTER, "true");
                        mSharedPref_editor.putString(Constants.BundleKey.USER_USER_UID, emailid.substring(0,index)+bFacebookData.getString("idFacebook"));
                        mSharedPref_editor.putString(Constants.BundleKey.USER_EMAIL,emailid);
                        mSharedPref_editor.putString(Constants.BundleKey.PROFILE_PIC, bFacebookData.getString("profile_pic"));
                        mSharedPref_editor.putString(Constants.ProfileeKey.FIRST_NAME, bFacebookData.getString("first_name"));
                        mSharedPref_editor.putString(Constants.ProfileeKey.LAST_NAME, bFacebookData.getString("last_name"));
                        mSharedPref_editor.putString(Constants.BundleKey.FACEBOOK_LOGIN, "true");
                        mSharedPref_editor.commit();
                        Log.i(TAG, "onCompleted: "+bFacebookData.getString("id"));
                        Toast.makeText(LoginActivity.this, "id :"+bFacebookData.getString("first_name"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, ToDoActivity.class);
                        intent.putExtra(Constants.BundleKey.USER_EMAIL, mStrUserEmail);
                        intent.putExtra(Constants.BundleKey.USER_NAME, mStrUserName);
                        startActivity(intent);
                        finish();
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();

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
    public  void googleLogin(){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

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
            String googleemailid=acct.getEmail();
            Log.i(TAG, "handleSignInResult: "+acct.getPhotoUrl());
            mSharedPref_editor.putString(Constants.BundleKey.USER_REGISTER,"true");
            mSharedPref_editor.putString(Constants.BundleKey.USER_EMAIL,acct.getEmail() );
            mSharedPref_editor.putString(Constants.ProfileeKey.FIRST_NAME, acct.getDisplayName());
            mSharedPref_editor.putString(Constants.BundleKey.USER_USER_UID,googleemailid.substring(0,googleemailid.indexOf("@"))+acct.getId());
            mSharedPref_editor.putString(Constants.BundleKey.GOOGLE_LOGIN,"true");
            mSharedPref_editor.commit();

            Intent intent=new Intent(LoginActivity.this,ToDoActivity.class);
            intent.putExtra(Constants.BundleKey.USER_EMAIL, mStrUserEmail);
            intent.putExtra(Constants.BundleKey.USER_NAME, mStrUserName);
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
        mStrUserEmail = mEditTextEmail.getText().toString();
        mStrUserPassword = mEditTextPassword.getText().toString();
        Log.i(TAG, "doAuthentication: "+ mStrUserEmail);
        if (!(mStrUserEmail.equals("") && mStrUserPassword.equals(""))) {
            mMatcher = mPattern.matcher(mStrUserEmail);
            if (mMatcher.matches()) {
                loginLoginPresenter.getLogin(mStrUserEmail, mStrUserPassword);
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

        mSharedPref_editor.putString(Constants.BundleKey.USER_REGISTER,"true");
        mSharedPref_editor.putString(Constants.BundleKey.USER_EMAIL,registrationModel.getMailid());
        mSharedPref_editor.putString(Constants.BundleKey.USER_USER_UID,userUid);
        mSharedPref_editor.putString(Constants.ProfileeKey.FIRST_NAME,registrationModel.getUserFirstName());
        mSharedPref_editor.putString(Constants.ProfileeKey.LAST_NAME,registrationModel.getUserLastName());
        mSharedPref_editor.putString(Constants.ProfileeKey.MOBILE_NO,registrationModel.getMobileNo());
        mSharedPref_editor.putString(Constants.ProfileeKey.PROFILE_IMAGE_URL,registrationModel.getUserProfileImgurl());

        mSharedPref_editor.putString(Constants.BundleKey.USER_NAME, mStrUserName);
        mSharedPref_editor.commit();
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
//for google social loginLoginPresenter

        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }


    //google social loginLoginPresenter signout
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
