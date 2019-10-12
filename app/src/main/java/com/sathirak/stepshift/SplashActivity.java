package com.sathirak.stepshift;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start home activity
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstUsage = prefs.getBoolean("first_usage", true);

        if(isFirstUsage) {

            startActivity(new Intent(SplashActivity.this, Setup.class));


        } else {

            startActivity(new Intent(SplashActivity.this, ShiftDisplayActivity.class));
        }


        // close splash activity
        finish();
    }
}