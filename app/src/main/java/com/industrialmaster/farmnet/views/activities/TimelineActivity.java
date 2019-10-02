package com.industrialmaster.farmnet.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Timeline;
import com.industrialmaster.farmnet.views.adapters.TimelineTaskRecyclerViewAdapter;

public class TimelineActivity extends BaseActivity {

    TextView mProductNameTextView, mDescriptionTextView;
    ImageButton mCloseImageButton, mCreateNewTaskImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Gson gson = new Gson();
        Timeline timeline = gson.fromJson(getIntent().getStringExtra("timeline"), Timeline.class);

        mProductNameTextView = findViewById(R.id.tv_product_name);
        mDescriptionTextView = findViewById(R.id.tv_description);
        mCloseImageButton = findViewById(R.id.img_btn_close);
        mCreateNewTaskImageButton = findViewById(R.id.img_btn_add_new_task);

        mCloseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mCreateNewTaskImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimelineActivity.this, AddNewTimelineTaskActivity.class);
                intent.putExtra("timelineId", timeline.getTimelineId());
                startActivity(intent);
            }
        });

        mProductNameTextView.setText(timeline.getProductName());
        mDescriptionTextView.setText(timeline.getDescription());

        RecyclerView recyclerView = findViewById(R.id.recyclerview_timeline_task);
        TimelineTaskRecyclerViewAdapter adapter = new TimelineTaskRecyclerViewAdapter(this, timeline.getTasks(),
                timeline.getUser().getProfilePicUrl());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
