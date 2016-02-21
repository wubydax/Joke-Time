package com.wubydax.jokedisplayinglibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DisplayJokeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_joke);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String jokeText = extras.getString("JOKE_STRING", getString(R.string.nothing_to_display));
            ((TextView) findViewById(R.id.jokeTextView)).setText(jokeText);

        }
    }
}
