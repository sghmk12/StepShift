package com.sathirak.stepshift;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView lone = (TextView)findViewById(R.id.linksText);
        TextView ltwo = (TextView)findViewById(R.id.linksText2);
        lone.setClickable(true);
        lone.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='http://www.emc2-explained.info/Time-Dilation/'> Relative Time Explained </a>";
        lone.setText(Html.fromHtml(text));
        ltwo.setClickable(true);
        ltwo.setMovementMethod(LinkMovementMethod.getInstance());
        String texttwo = "<a href='https://www.youtube.com/watch?v=JwCYwte9NKg'> Youtube Explanation </a>";
        ltwo.setText(Html.fromHtml(texttwo));
    }
}
