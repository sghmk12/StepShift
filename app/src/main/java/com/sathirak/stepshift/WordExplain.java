package com.sathirak.stepshift;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WordExplain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_explain);
    }

    public void openNext(View v){
        startActivity(new Intent(this, finalSetup.class));
    }
}
