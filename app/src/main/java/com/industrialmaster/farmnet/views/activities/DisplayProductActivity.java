package com.industrialmaster.farmnet.views.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DisplayProductActivity extends BaseActivity implements DisplayProductView, OnMapReadyCallback {

    private static final String TAG = "DisplayProductActivity";

    TimelinePresenter timelinePresenter;
    DealsPresenter dealsPresenter;
    public static final String FARMNET_PREFS_NAME = "FarmnetPrefsFile";

    private GoogleMap mGoogleMap;
    SupportMapFragment mapFragment;

    ImageView image_view_product;
    TextView tv_product_name, tv_description, tv_unit_price, tv_amount, tv_location,
            tv_owner, tv_published_date;
    ImageButton img_btn_close, img_btn_view_timeline;
    ImageButton mDeleteImageButton;
    ImageButton mUpdateImageButton;

    private LatLng latLng;

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

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        img_btn_close = findViewById(R.id.img_btn_close);
        img_btn_view_timeline = findViewById(R.id.img_btn_view_timeline);
        mDeleteImageButton = findViewById(R.id.img_btn_delete);
        mUpdateImageButton = findViewById(R.id.img_btn_update);

        String currentUserId = getSharedPreferences(FARMNET_PREFS_NAME, Context.MODE_PRIVATE).getString(FarmnetConstants.USER_ID, "");

        if(!deal.getUser().getUserId().equals(currentUserId)){
            mDeleteImageButton.setVisibility(View.INVISIBLE);
            mUpdateImageButton.setVisibility(View.INVISIBLE);
        }

        mDeleteImageButton.setOnClickListener(v ->
                showSweetAlert(SweetAlertDialog.WARNING_TYPE, ErrorMessageHelper.DELETE_CONFIRMATION,null,false,
                FarmnetConstants.OK, sDialog -> {
                    setLoading(true);
                    dealsPresenter.deleteProduct(deal.getDealId());
                },FarmnetConstants.CANCEL, SweetAlertDialog::dismissWithAnimation));

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
        latLng = new LatLng(deal.getLatitude(), deal.getLongitude());

        Date date = deal.getDate();
        DateFormat targetDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
        String formattedDate = targetDateFormat.format(date);

        tv_published_date.setText(": " + formattedDate);

        img_btn_close.setOnClickListener(v -> finish());

        img_btn_view_timeline.setOnClickListener(v -> {
            setLoading(true);
            timelinePresenter.getTimelineById(deal.getTimelineId());
        });

        mUpdateImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(DisplayProductActivity.this, CreateNewDealActivity.class);
            intent.putExtra("deal", getIntent().getStringExtra("deal"));
            startActivity(intent);
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
        showSweetAlert(SweetAlertDialog.ERROR_TYPE, "Oops..." , message,false, FarmnetConstants.OK ,
                SweetAlertDialog::dismissWithAnimation, null, null);
    }

    @Override
    public void onSuccess(String message) {
        setLoading(false);
        showSweetAlert(SweetAlertDialog.SUCCESS_TYPE, "Great!" ,message,false, FarmnetConstants.OK ,
                sDialog -> finish(), null, null);
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        String myaddress = null;
        Geocoder geocoder = new Geocoder(DisplayProductActivity.this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            myaddress = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(myaddress);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
        mGoogleMap.addMarker(markerOptions);
    }
}
