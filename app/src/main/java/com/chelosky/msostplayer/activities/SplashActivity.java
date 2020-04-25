package com.chelosky.msostplayer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.chelosky.msostplayer.R;
import com.chelosky.msostplayer.helpers.UserPreferencesHelper;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        UserPreferencesHelper.initializeUserPreferences(SplashActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MenuActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(R.anim.fade, R.anim.hold);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
