package com.industrialmaster.farmnet.views.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Timeline;

public class TimelineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Gson gson = new Gson();
        Timeline timeline = gson.fromJson(getIntent().getStringExtra("timeline"), Timeline.class);
    }
}
