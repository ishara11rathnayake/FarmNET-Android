package com.industrialmaster.farmnet.views.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.request.CreateNewAdvertisementRequest;
import com.industrialmaster.farmnet.presenters.AdvertisementPresenter;
import com.industrialmaster.farmnet.presenters.AdvertisementsPresenterImpl;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.CreateNewAdsView;

public class CreateNewAdvertisementActivity extends BaseActivity implements CreateNewAdsView {

    AdvertisementPresenter presenter;

    ImageView imgv_ads_image;
    ImageButton btn_add_image_from_gallery, btn_add_image_from_camera, img_btn_close;
    Button btn_create_new_ads;

    TextInputEditText et_ads_title, et_ads_description, et_contact_number, et_price, et_tags;

    Uri image_uri, imageFilePath;
    boolean hasImage = false;

    private static final int IMAGE_PIK_CODE = 1000;
    private static final int GALLERY_PERMISSION_CODE = 1001;
    private static final int CAMERA_PERMISSION_CODE = 1002;
    private static final int IMAGE_CAPTURE_CODE = 1003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_advertisement);

        presenter = new AdvertisementsPresenterImpl(this, CreateNewAdvertisementActivity.this);

        imgv_ads_image = findViewById(R.id.imgv_ads_pic);
        img_btn_close = findViewById(R.id.img_btn_close);
        btn_add_image_from_gallery = findViewById(R.id.add_image_from_gallery);
        btn_add_image_from_camera = findViewById(R.id.add_image_from_camera);
        btn_create_new_ads = findViewById(R.id.btn_create_new_ads);

        et_ads_title = findViewById(R.id.et_ads_title);
        et_ads_description = findViewById(R.id.et_desc);
        et_contact_number = findViewById(R.id.et_phone);
        et_price = findViewById(R.id.et_price);
        et_tags =findViewById(R.id.et_tags);

        btn_create_new_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String realFilePath;

                CreateNewAdvertisementRequest newAdsRequest = new CreateNewAdvertisementRequest();

                newAdsRequest.setAdTitle(et_ads_title.getText().toString());
                newAdsRequest.setAdDescription(et_ads_description.getText().toString());
                newAdsRequest.setContactNumber(et_contact_number.getText().toString());

                if(!TextUtils.isEmpty(et_contact_number.getText().toString())) {
                    newAdsRequest.setPrice(Double.parseDouble(et_contact_number.getText().toString()));
                }

                if(hasImage == true) {
                    realFilePath = convertMediaUriToPath(imageFilePath);
                    newAdsRequest.setAdsImage(realFilePath);
                }

                String tagString = et_tags.getText().toString();
                String[] tags = tagString.split(" ");
                newAdsRequest.setTags(tags);

                newAdsRequest.setHasImage(hasImage);

                setLoading(true);
                presenter.createNewAdvertisement(newAdsRequest);
            }
        });

        //close create new deal activity
        img_btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = ErrorMessageHelper.DISCARD_CONFIRMATION;
                showAlertDialog("Warning", message,false, FarmnetConstants.OK , (dialog, which) -> {
                    finish();
                },FarmnetConstants.CANCEL, (dialog, which) -> dialog.dismiss());
            }
        });

        //handle btn_add_image_from_gallery button click
        btn_add_image_from_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        //handle btn_add_image_from_camera button click
        btn_add_image_from_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        //pick image from gallery clecking image view
        imgv_ads_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }

//    private void openCamera() {
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.TITLE,  "New Picture");
//        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
//        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//
//        //camera intent
//        Intent cameraIntent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
//        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
//    }

//    public void pickImageFromGallery() {
//        //intent to pick image
//        Intent intent = new Intent((Intent.ACTION_PICK));
//        intent.setType("image/*");
//        startActivityForResult(intent, IMAGE_PIK_CODE);
//    }

//    //handle results of runtime permission
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        //this method is called when user presses Allow or Deny from permission request popup
//        switch (requestCode) {
//            case GALLERY_PERMISSION_CODE: {
//                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    //permission was granted
//                    pickImageFromGallery();
//                } else {
//                    //permission was denied
//                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            case CAMERA_PERMISSION_CODE: {
//                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    //permission was granted
//                    openCamera();
//                } else {
//                    //permission was denied
//                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        }
//    }

    //handle result of picked image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == IMAGE_PIK_CODE){
            //set image to image view
            imgv_ads_image.setImageURI(data.getData());
            imageFilePath = data.getData();
            hasImage = true;
        } else if(resultCode == RESULT_OK && requestCode == IMAGE_CAPTURE_CODE){
            //set captured image to image view
            imgv_ads_image.setImageURI(image_uri);
            imageFilePath = image_uri;
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

    @Override
    public void showErrorMessage(String calledMethod, String error, String errorDescription) {

    }

//    public String convertMediaUriToPath(Uri uri) {
//        String [] proj={MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(uri, proj,  null, null, null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        String path = cursor.getString(column_index);
//        cursor.close();
//        return path;
//    }
}
