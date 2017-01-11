package com.blogspot.mowael.zgo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.blogspot.mowael.zgo.utilities.Constants;
import com.blogspot.mowael.zgo.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intentIntro = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intentIntro);
                finish();
            }
        }, Constants.SPLASH_TIME_OUT);
    }
}
