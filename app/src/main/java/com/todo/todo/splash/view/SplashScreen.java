package com.todo.todo.splash.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.todo.todo.R;
import com.todo.todo.home.view.ToDoActivity;
import com.todo.todo.login.view.LoginActivity;
import com.todo.todo.util.Constants;
import io.fabric.sdk.android.Fabric;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 5000;
    AppCompatImageView image1;
    AppCompatTextView textView_title;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    boolean flag = false;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_screen);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference=FirebaseDatabase.getInstance().getReferenceFromUrl("https://todo-165105.firebaseio.com/").child("usersdata");
        databaseReference.keepSynced(true);

        textView_title = (AppCompatTextView) findViewById(R.id.textView_splash);
        Animation animationimage = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade);
        Animation animationtext = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.translate);
        textView_title.setAnimation(animationtext);
        image1 = (AppCompatImageView) findViewById(R.id.imageView_logo);
        image1.startAnimation(animationimage);
        Log.i("", "onCreate: ");
        pref = getSharedPreferences("testapp", MODE_PRIVATE);
        editor = pref.edit();
        isLogin();

        if (!isFinishing() && flag) {
            getToDoCall();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (flag) {
                        getToDoCall();
                    } else {
                        Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }, SPLASH_TIME_OUT);
        }
    }

    private void getToDoCall() {
        Log.i("", "isLogin: ");
        String mStrEmail = pref.getString(Constants.BundleKey.USER_EMAIL, "abcd@gmail.com");
        //redirect to next activity
        String mStrName = pref.getString(Constants.BundleKey.USER_NAME, "Gokul Sonawane");
        Intent intent = new Intent(SplashScreen.this, ToDoActivity.class);
        intent.putExtra(Constants.BundleKey.USER_EMAIL, mStrEmail);
        intent.putExtra(Constants.BundleKey.USER_NAME, mStrName);
        startActivity(intent);
        finish();
    }

    public void isLogin() {
        Log.i("", "isLogin: ");
        if (pref.contains(Constants.BundleKey.USER_REGISTER)) {
            Log.i("", "isLogin: ");
            String getStatus = pref.getString(Constants.BundleKey.USER_REGISTER, "nil");
            if (getStatus.equals("true")) {
                flag = true;
            } else {
                flag = false;
            }
        } else {
            flag = false;
        }
    }

}