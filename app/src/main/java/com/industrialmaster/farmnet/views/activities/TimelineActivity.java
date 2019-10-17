package com.industrialmaster.farmnet.views.activities;

import android.content.Context;
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
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.adapters.TimelineTaskRecyclerViewAdapter;

import java.util.Objects;

public class TimelineActivity extends BaseActivity {

    public static final String FARMNET_PREFS_NAME = "FarmnetPrefsFile";

    TextView mProductNameTextView;
    TextView mDescriptionTextView;
    ImageButton mCloseImageButton;
    ImageButton mCreateNewTaskImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        String mCurrentUserId = getSharedPreferences(FARMNET_PREFS_NAME, Context.MODE_PRIVATE).getString(FarmnetConstants.USER_ID, "");

        Gson gson = new Gson();
        Timeline timeline = gson.fromJson(getIntent().getStringExtra("timeline"), Timeline.class);

        mProductNameTextView = findViewById(R.id.tv_product_name);
        mDescriptionTextView = findViewById(R.id.tv_description);
        mCloseImageButton = findViewById(R.id.img_btn_close);
        mCreateNewTaskImageButton = findViewById(R.id.img_btn_add_new_task);

        if(!Objects.requireNonNull(mCurrentUserId).equals(timeline.getUser().getUserId())){
            mCreateNewTaskImageButton.setVisibility(View.INVISIBLE);
        }

        mCloseImageButton.setOnClickListener(v -> finish());

        mCreateNewTaskImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(TimelineActivity.this, AddNewTimelineTaskActivity.class);
            intent.putExtra("timelineId", timeline.getTimelineId());
            startActivity(intent);
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
