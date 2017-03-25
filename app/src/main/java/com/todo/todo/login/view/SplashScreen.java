package com.todo.todo.login.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.todo.todo.R;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 5000;
    AppCompatImageView image1;
    AppCompatTextView textView_title;
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

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}