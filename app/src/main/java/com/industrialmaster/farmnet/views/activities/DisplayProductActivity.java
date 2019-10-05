package com.industrialmaster.farmnet.views.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Deals;
import com.industrialmaster.farmnet.models.Timeline;
import com.industrialmaster.farmnet.presenters.DealsPresenter;
import com.industrialmaster.farmnet.presenters.DealsPresenterImpl;
import com.industrialmaster.farmnet.presenters.TimelinePresenter;
import com.industrialmaster.farmnet.presenters.TimelnePresenterImpl;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.DisplayProductView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DisplayProductActivity extends BaseActivity implements DisplayProductView {

    TimelinePresenter timelinePresenter;
    DealsPresenter dealsPresenter;
    public static String FARMNET_PREFS_NAME = "FarmnetPrefsFile";

    ImageView image_view_product;
    TextView tv_product_name, tv_description, tv_unit_price, tv_amount, tv_location,
            tv_owner, tv_published_date;
    ImageButton img_btn_close, img_btn_view_timeline;
    ImageButton mDeleteImageButton;

    String callingActivity;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_product);

        timelinePresenter = new TimelnePresenterImpl(this, DisplayProductActivity.this);
        dealsPresenter = new DealsPresenterImpl(this, this);

        Gson gson = new Gson();
        Deals deal = gson.fromJson(getIntent().getStringExtra("deal"), Deals.class);

        callingActivity = getIntent().getStringExtra("activity");

        img_btn_close = findViewById(R.id.img_btn_close);
        img_btn_view_timeline = findViewById(R.id.img_btn_view_timeline);
        mDeleteImageButton = findViewById(R.id.img_btn_delete);

        String currentUserId = getSharedPreferences(FARMNET_PREFS_NAME, Context.MODE_PRIVATE).getString(FarmnetConstants.USER_ID, "");

        if(!deal.getUser().getUserId().equals(currentUserId)){
            mDeleteImageButton.setVisibility(View.INVISIBLE);
        }

        mDeleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog("Warning", ErrorMessageHelper.DELETE_CONFIRMATION,false, FarmnetConstants.OK ,
                        (dialog, which) -> {
                            dealsPresenter.deleteProduct(deal.getDealId());
                        },
                        FarmnetConstants.CANCEL, (dialog, which) -> dialog.dismiss());
            }
        });

        image_view_product = findViewById(R.id.imgv_product_image);
        tv_product_name = findViewById(R.id.tv_product_name);
        tv_description = findViewById(R.id.tv_description);
        tv_unit_price = findViewById(R.id.tv_unit_price_value);
        tv_amount = findViewById(R.id.tv_amount_value);
        tv_location = findViewById(R.id.tv_location_value);
        tv_owner = findViewById(R.id.tv_owner_value);
        tv_published_date = findViewById(R.id.tv_date_value);

        Glide.with(this)
                .asBitmap()
                .load(deal.getProductImageUrl())
                .centerCrop()
                .into(image_view_product);

        tv_product_name.setText(deal.getProductName());
        tv_description.setText(deal.getDescription());
        tv_unit_price.setText(": Rs." + deal.getUnitPrice());
        tv_amount.setText(": " + deal.getAmount() + "Kg");
        tv_location.setText(": " + deal.getLocation());
        tv_owner.setText(": " + deal.getUser().getName());

        Date date = deal.getDate();
        DateFormat targetDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        String formattedDate = targetDateFormat.format(date);

        tv_published_date.setText(": " + formattedDate);

        img_btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_btn_view_timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoading(true);
                timelinePresenter.getTimelineById(deal.getTimelineId());
            }
        });
    }

    @Override
    public void setTimelineData(Timeline timeline) {
        setLoading(false);
        Intent intent = new Intent(this, TimelineActivity.class);
        Gson gson = new Gson();
        String timelineText = gson.toJson(timeline);
        intent.putExtra("timeline", timelineText);
        startActivity(intent);
    }

    @Override
    public void onError(String message) {
        setLoading(false);
        showAlertDialog("Error", message,false, FarmnetConstants.OK , (dialog, which) -> {},
                "", (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void onSuccess(String message) {
        setLoading(false);
        showAlertDialog("Success", message,false, FarmnetConstants.OK ,
                (dialog, which) -> {
                    finish();
                },
                "", (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void showMessage(String message) {

    }
}
