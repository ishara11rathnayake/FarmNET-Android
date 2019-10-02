package com.industrialmaster.farmnet.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Timeline;
import com.industrialmaster.farmnet.presenters.TimelinePresenter;
import com.industrialmaster.farmnet.presenters.TimelnePresenterImpl;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.TimelineListView;
import com.industrialmaster.farmnet.views.adapters.AdvertisementRecyclerViewAdapter;
import com.industrialmaster.farmnet.views.adapters.TimelineListRecyclerViewAdapter;

import java.util.List;

public class ProductTimelineListActivity extends BaseActivity implements TimelineListView {

    TimelinePresenter presenter;

    ImageButton mCreateNewTimelineImageButton, mCloseImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_timeline_list);

        mCreateNewTimelineImageButton = findViewById(R.id.img_btn_add_new_timeline);
        mCloseImageButton = findViewById(R.id.img_btn_close);

        mCreateNewTimelineImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductTimelineListActivity.this, CreateTimelineActivity.class));
            }
        });

        mCloseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        presenter = new TimelnePresenterImpl(this, ProductTimelineListActivity.this);
        setLoading(true);
        presenter.getTimelinesByUser();
    }

    @Override
    public void showTimelineList(List<Timeline> timelines) {
        setLoading(false);
        RecyclerView recyclerView = findViewById(R.id.recyclerview_timeline);
        TimelineListRecyclerViewAdapter adapter = new TimelineListRecyclerViewAdapter(this, timelines);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onError(String message) {
        setLoading(false);
        showAlertDialog("Error", message,false, FarmnetConstants.OK , (dialog, which) -> {},
                "", (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void onSuccess(String message) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showErrorMessage(String calledMethod, String error, String errorDescription) {

    }
}
