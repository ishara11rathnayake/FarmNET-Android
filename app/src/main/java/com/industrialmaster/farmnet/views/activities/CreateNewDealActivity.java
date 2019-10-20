package com.industrialmaster.farmnet.views.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.industrialmaster.farmnet.models.request.CreateNewDealRequest;
import com.industrialmaster.farmnet.presenters.DealsPresenter;
import com.industrialmaster.farmnet.presenters.DealsPresenterImpl;
import com.industrialmaster.farmnet.presenters.TimelinePresenter;
import com.industrialmaster.farmnet.presenters.TimelnePresenterImpl;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.CreateNewDealView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CreateNewDealActivity extends BaseActivity implements CreateNewDealView, OnMapReadyCallback {

    private static final String TAG = "CreateNewDealActivity";
    DealsPresenter presenter;
    TimelinePresenter timelinePresenter;

    ImageView imgv_product_pic;
    ImageButton btn_add_image_from_gallery, btn_add_image_from_camera, img_btn_close;
    Button btn_create_new_deal;
    Spinner spinner_timelineId;

    ImageView mMapImageView;

    //fiels of UI
    TextInputEditText et_product_name, et_unit_price,
            et_amount, et_description, et_locaton;

    boolean hasImage = false;
    String timelineId = null;
    List<String> mTimelineNames;
    List<String> mTimelineValues;
    private GoogleMap mGoogleMap;
    SupportMapFragment mapFragment;
    LatLng latLng;

    private static final int IMAGE_PIK_CODE = 1000;
    private static final int GALLERY_PERMISSION_CODE = 1001;
    private static final int CAMERA_PERMISSION_CODE = 1002;
    private static final int IMAGE_CAPTURE_CODE = 1003;
    private static final int GET_LOCATION_CODE = 111;

    private Deals deal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_deal);

        //initialize presenter
        presenter = new DealsPresenterImpl(this, CreateNewDealActivity.this);
        timelinePresenter = new TimelnePresenterImpl(this, CreateNewDealActivity.this);

        Gson gson = new Gson();
        deal = gson.fromJson(getIntent().getStringExtra("deal"), Deals.class);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getView().setVisibility(View.GONE);

        //Views
        mMapImageView = findViewById(R.id.image_view_get_location);
        imgv_product_pic = findViewById(R.id.imgv_product_pic);
        btn_add_image_from_gallery = findViewById(R.id.add_image_from_gallery);
        btn_add_image_from_camera = findViewById(R.id.add_image_from_camera);
        img_btn_close = findViewById(R.id.img_btn_close);
        btn_create_new_deal = findViewById(R.id.btn_create_new_deal);
        spinner_timelineId = findViewById(R.id.spinner_timelineId);

        et_product_name = findViewById(R.id.et_product_name);
        et_unit_price = findViewById(R.id.et_unit_price);
        et_amount = findViewById(R.id.et_amount);
        et_description = findViewById(R.id.et_desc);
        et_locaton = findViewById(R.id.et_location);

        timelinePresenter.getTimelinesByUser();

        mTimelineNames = new ArrayList<>();
        mTimelineNames.add("Select Timeline");

        mTimelineValues = new ArrayList<>();
        mTimelineValues.add("default");

        if(deal != null){
            et_product_name.setText(deal.getProductName());

            Glide.with(this)
                    .asBitmap()
                    .load(deal.getProductImageUrl())
                    .centerCrop()
                    .into(imgv_product_pic);

            et_unit_price.setText(String.valueOf(deal.getUnitPrice()));
            et_amount.setText(String.valueOf(deal.getAmount()));
            et_description.setText(deal.getDescription());
            et_locaton.setText(deal.getLocation());
        }

        spinner_timelineId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timelineId = mTimelineValues.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //close create new deal activity
        img_btn_close.setOnClickListener(v -> {
            String message = ErrorMessageHelper.DISCARD_CONFIRMATION;
            showAlertDialog("Warning", message,false, FarmnetConstants.OK , (dialog, which) -> {
                startActivity(new Intent(CreateNewDealActivity.this, MainActivity.class));
                finish();
            },FarmnetConstants.CANCEL, (dialog, which) -> dialog.dismiss());
        });

        //save new deal
        btn_create_new_deal.setOnClickListener(v -> onCreateNewDealClick());

        //handle btn_add_image_from_gallery button click
        btn_add_image_from_gallery.setOnClickListener(v -> onPickImageFromGalley());

        //handle btn_add_image_from_camera button click
        btn_add_image_from_camera.setOnClickListener(v -> onTakeImageFromCamera());

        //pick image from gallery clicking image view
        imgv_product_pic.setOnClickListener(v -> onPickImageFromGalley());

        mMapImageView.setOnClickListener(v -> {
          Intent intent = new Intent(CreateNewDealActivity.this, SelectLocationActivity.class);
          startActivityForResult(intent, GET_LOCATION_CODE);
        });

    }

    /**
     * on create new deal click
     */
    private void onCreateNewDealClick(){
        setLoading(true);

        String realFilePath;

        CreateNewDealRequest createNewDealRequest = new CreateNewDealRequest();

        if(hasImage == true) {
            realFilePath = convertMediaUriToPath(imageFilePath);
            createNewDealRequest.setProductImage(realFilePath);
        }

        createNewDealRequest.setProductName(et_product_name.getText().toString());
        createNewDealRequest.setUnitPrice(et_unit_price.getText().toString());
        createNewDealRequest.setAmount(et_amount.getText().toString());
        createNewDealRequest.setDescription(et_description.getText().toString());
        createNewDealRequest.setLocation(et_locaton.getText().toString());
        createNewDealRequest.setLatitude(latLng.latitude);
        createNewDealRequest.setLongitude(latLng.longitude);
        createNewDealRequest.setHasImage(hasImage);

        if(!timelineId.equals(FarmnetConstants.DEFAULT)){
            createNewDealRequest.setTimelineId(timelineId);
        }

        if(deal != null){
            presenter.updateDeal(createNewDealRequest, deal.getDealId());
        } else {
            presenter.createNewDeal(createNewDealRequest);
        }
    }

    /**
     * on pick image from gallery or image view click
     */
    private void onPickImageFromGalley(){
        //check runtime permission
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                //permission not granted, request it.
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, GALLERY_PERMISSION_CODE);
            } else {
                //permission already granted
                pickImageFromGallery();
            }
        } else {
            //system os less than marshmallow
            pickImageFromGallery();
        }
    }

    /**
     * on take image from camera click
     */
    private void onTakeImageFromCamera(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //permission not enabled, request it.
                String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, CAMERA_PERMISSION_CODE);
            } else {
                //permission already granted
                openCamera();
            }
        } else {
            //system OS < marshmallow
            openCamera();
        }
    }

    //handle result of picked image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == IMAGE_PIK_CODE){
            //set image to image view
            imgv_product_pic.setImageURI(data.getData());
            imageFilePath = data.getData();
            hasImage = true;
        } else if(resultCode == RESULT_OK && requestCode == IMAGE_CAPTURE_CODE){
            //set captured image to image view
            imgv_product_pic.setImageURI(image_uri);
            imageFilePath = image_uri;
            hasImage = true;
        }

        if(requestCode == GET_LOCATION_CODE && resultCode == RESULT_OK) {
            et_locaton.setText(data.getStringExtra("address"));
            double latitude = data.getDoubleExtra("latitude", 6.93194);
            double longitude = data.getDoubleExtra("longitude", 79.84778);
            mapFragment.getView().setVisibility(View.VISIBLE);
            mapFragment.getMapAsync(this);
            latLng = new LatLng(latitude, longitude);
        }

    }

    @Override
    public void onSuccess(String message) {
        setLoading(false);
        showAlertDialog("Success", message,false, FarmnetConstants.OK ,
                (dialog, which) -> {
                    finish();
                    startActivity(new Intent(CreateNewDealActivity.this, MainActivity.class));
                },
                "", (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void onError(String message) {
        setLoading(false);
        showAlertDialog("Error", message,false, FarmnetConstants.OK , (dialog, which) -> {},
                "", (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void setSpinnerValues(List<Timeline> timelines) {
        for(Timeline timeline : timelines) {
            mTimelineNames.add(timeline.getProductName());
            mTimelineValues.add(timeline.getTimelineId());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, mTimelineNames);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spinner_timelineId.setAdapter(adapter);
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        String myaddress = null;
        Geocoder geocoder = new Geocoder(CreateNewDealActivity.this, Locale.ENGLISH);
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
