package com.chelosky.msostplayer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.chelosky.msostplayer.R;
import com.chelosky.msostplayer.helpers.DataHelper;
import com.chelosky.msostplayer.helpers.UserPreferencesHelper;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_TIME_OUT = 1500;
    private final int PERMISSION_ALL = 615;
    String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        UserPreferencesHelper.initializeUserPreferences(SplashActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!hasPermissions(SplashActivity.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(SplashActivity.this, PERMISSIONS, PERMISSION_ALL);
                }else{
                    DataHelper.getOstInformation(SplashActivity.this);
                }
                //isStoragePermissionGranted();
            }
        }, SPLASH_TIME_OUT);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return true;
        }
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_ALL:{
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //Toast.makeText(this, "SI", Toast.LENGTH_SHORT).show();
                    DataHelper.getOstInformation(this);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "SE REQUIEREN DE ESOS PERMISOS PARA QUE LA APP FUNCIONE. LA APP SE CERRARÁ EN 5 SEG", Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            System.exit(0);
                        }
                    },5000);

                }
            }
        }
    }

    public void goToMain(){
        TextView txtView = (TextView)findViewById(R.id.txtINFO);
        txtView.setText("Creating DB...");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MenuActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(R.anim.fade, R.anim.hold);
                finish();
            }
        },2000);
    }
}
