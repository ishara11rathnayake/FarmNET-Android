package com.industrialmaster.farmnet.views.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.request.CreateNewAdvertisementRequest;
import com.industrialmaster.farmnet.presenters.AdvertisementPresenter;
import com.industrialmaster.farmnet.presenters.AdvertisementsPresenterImpl;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.CreateNewAdsView;

import java.util.Objects;

public class CreateNewAdvertisementActivity extends BaseActivity implements CreateNewAdsView {

    AdvertisementPresenter advertisementPresenter;

    ImageView mAdsImageView;
    ImageButton mAddImageFromGalleryImageButton;
    ImageButton mAddImageFromCameraImageButton;
    ImageButton mCloseImageButton;
    Button mCreateNewAdsButton;

    TextInputEditText mAdsTitleEditText;
    TextInputEditText mAdsDescriptionEditText;
    TextInputEditText mContactNumberEditText;
    TextInputEditText mPriceEditText;
    TextInputEditText mTagsEditText;

    Uri mImageUri;
    Uri mImageFilePath;
    boolean hasImage = false;

    private static final int IMAGE_PIK_CODE = 1000;
    private static final int GALLERY_PERMISSION_CODE = 1001;
    private static final int CAMERA_PERMISSION_CODE = 1002;
    private static final int IMAGE_CAPTURE_CODE = 1003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_advertisement);

        advertisementPresenter = new AdvertisementsPresenterImpl(this, CreateNewAdvertisementActivity.this);

        mAdsImageView = findViewById(R.id.imgv_ads_pic);
        mCloseImageButton = findViewById(R.id.img_btn_close);
        mAddImageFromGalleryImageButton = findViewById(R.id.add_image_from_gallery);
        mAddImageFromCameraImageButton = findViewById(R.id.add_image_from_camera);
        mCreateNewAdsButton = findViewById(R.id.btn_create_new_ads);

        mAdsTitleEditText = findViewById(R.id.et_ads_title);
        mAdsDescriptionEditText = findViewById(R.id.et_desc);
        mContactNumberEditText = findViewById(R.id.et_phone);
        mPriceEditText = findViewById(R.id.et_price);
        mTagsEditText =findViewById(R.id.et_tags);

        mCreateNewAdsButton.setOnClickListener(v -> onClickCreateNewAdvertisemet());

        //close create new deal activity
        mCloseImageButton.setOnClickListener(v -> {
            String message = ErrorMessageHelper.DISCARD_CONFIRMATION;
            showAlertDialog("Warning", message,false, FarmnetConstants.OK , (dialog, which) -> finish(),
                    FarmnetConstants.CANCEL, (dialog, which) -> dialog.dismiss());
        });

        //handle btn_add_image_from_gallery button click
        mAddImageFromGalleryImageButton.setOnClickListener(v -> onClickPickImageFromGallery());

        //handle btn_add_image_from_camera button click
        mAddImageFromCameraImageButton.setOnClickListener(v -> onClickTakePictureFromCamera());

        //pick image from gallery clicking image view
        mAdsImageView.setOnClickListener(v -> onClickPickImageFromGallery());
    }

    //handle result of picked image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == IMAGE_PIK_CODE){
            //set image to image view
            mAdsImageView.setImageURI(Objects.requireNonNull(data).getData());
            mImageFilePath = Objects.requireNonNull(data).getData();
            hasImage = true;
        } else if(resultCode == RESULT_OK && requestCode == IMAGE_CAPTURE_CODE){
            //set captured image to image view
            mAdsImageView.setImageURI(mImageUri);
            mImageFilePath = mImageUri;
            hasImage = true;
        }
    }

    @Override
    public void onSuccess(String message) {
        setLoading(false);
        showAlertDialog("Success", message,false, FarmnetConstants.OK , (dialog, which) -> {},
                "", (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void onError(String message) {
        setLoading(false);
        showAlertDialog("Error", message,false, FarmnetConstants.OK , (dialog, which) -> {},
                "", (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void showMessage(String message) {

    }

    /**
     * click on create new advertisement button
     */
    public void onClickCreateNewAdvertisemet(){
        String realFilePath;

        CreateNewAdvertisementRequest newAdsRequest = new CreateNewAdvertisementRequest();

        newAdsRequest.setAdTitle(Objects.requireNonNull(mAdsTitleEditText.getText()).toString());
        newAdsRequest.setAdDescription(Objects.requireNonNull(mAdsDescriptionEditText.getText()).toString());
        newAdsRequest.setContactNumber(Objects.requireNonNull(mContactNumberEditText.getText()).toString());

        if(!TextUtils.isEmpty(mContactNumberEditText.getText().toString())) {
            newAdsRequest.setPrice(Double.parseDouble(Objects.requireNonNull(mPriceEditText.getText()).toString()));
        }

        if(hasImage) {
            realFilePath = convertMediaUriToPath(mImageFilePath);
            newAdsRequest.setAdsImage(realFilePath);
        }

        String tagString = Objects.requireNonNull(mTagsEditText.getText()).toString();
        String[] tags = tagString.split(" ");
        newAdsRequest.setTags(tags);

        newAdsRequest.setHasImage(hasImage);

        setLoading(true);
        advertisementPresenter.createNewAdvertisement(newAdsRequest);
    }

    /**
     * click on pick image from gallery button or image view
     */
    public void onClickPickImageFromGallery(){
        //check runtime permission
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                //permission not enabled, request it.
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, GALLERY_PERMISSION_CODE);
            } else {
                //permission already granted
                pickImageFromGallery();
            }
        } else {
            //system OS < than marshmallow
            pickImageFromGallery();
        }
    }

    /**
     * click on take picture from camera button
     */
    public void onClickTakePictureFromCamera(){
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

}
