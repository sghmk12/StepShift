package com.sathirak.stepshift;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class Explanation extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_explanation);



        final VideoView videoView = (VideoView)findViewById(R.id.explanationVid);

        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.minute);
        videoView.setMinimumHeight((videoView.getWidth()/16)*9);

        MediaController mediaController = new
                MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);


    }

    public void openNext(View v){
        startActivity(new Intent(this, WordExplain.class));
    }
}