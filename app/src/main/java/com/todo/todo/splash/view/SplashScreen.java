package com.todo.todo.splash.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.todo.todo.R;
import com.todo.todo.home.view.ToDoActivity;
import com.todo.todo.login.view.LoginActivity;
import com.todo.todo.util.Constants;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 5000;
    AppCompatImageView image1;
    AppCompatTextView textView_title;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        textView_title=(AppCompatTextView) findViewById(R.id.textView_splash);
        Animation animationimage = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade);
        Animation animationtext = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.translate);
        textView_title.setAnimation(animationtext);
        image1 = (AppCompatImageView)findViewById(R.id.imageView_logo);
        image1.startAnimation(animationimage);
        Log.i("", "onCreate: ");
            pref = getSharedPreferences("testapp", MODE_PRIVATE);
            editor = pref.edit();


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Log.i("", "run: ");
                isLogin();


                // close this activity
               // finish();
            }
        }, SPLASH_TIME_OUT);
    }
    public  void isLogin(){

        Log.i("", "isLogin: ");
        if(pref.contains(Constants.BundleKey.USER_REGISTER))
        {
            Log.i("", "isLogin: ");
            String getStatus=pref.getString(Constants.BundleKey.USER_REGISTER, "nil");
            if(getStatus.equals("true")){
                Log.i("", "isLogin: ");
                String mStrEmail = pref.getString(Constants.BundleKey.USER_EMAIL, "abcd@gmail.com");
                //redirect to next activity
                String mStrName = pref.getString(Constants.BundleKey.USER_NAME, "Gokul Sonawane");
                Intent intent=new Intent(SplashScreen.this,ToDoActivity.class);
                intent.putExtra(Constants.BundleKey.USER_EMAIL,mStrEmail);
                intent.putExtra(Constants.BundleKey.USER_NAME,mStrName);
                startActivity(intent);
                finish();
            }else {
                Log.i("hghj", "isLogin: ");
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);
                finish();            }

        }else{
            Log.i("hghj", "isLogin: ");
            Intent i = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

}