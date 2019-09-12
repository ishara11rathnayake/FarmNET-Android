package com.industrialmaster.farmnet.views.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.industrialmaster.farmnet.R;

public class CreateNewDealActivity extends BaseActivity {

    ImageView imgv_product_pic;
    ImageButton btn_add_image_from_gallery;
    ImageButton btn_add_image_from_camera;

    Uri image_uri;

    private static final int IMAGE_PIK_CODE = 1000;
    private static final int GALLERY_PERMISSION_CODE = 1001;
    private static final int CAMERA_PERMISSION_CODE = 1002;
    private static final int IMAGE_CAPTURE_CODE = 1003;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_deal);

        //Views
        imgv_product_pic = findViewById(R.id.imgv_product_pic);
        btn_add_image_from_gallery = findViewById(R.id.add_image_from_gallery);
        btn_add_image_from_camera = findViewById(R.id.add_image_from_camera);

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
        imgv_product_pic.setOnClickListener(new View.OnClickListener() {
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

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,  "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        //camera intent
        Intent cameraIntent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    public void pickImageFromGallery() {
        //intent to pick image
        Intent intent = new Intent((Intent.ACTION_PICK));
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PIK_CODE);
    }

    //handle results of runtime permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //this method is called when user presses Allow or Deny from permission request popup
        switch (requestCode) {
            case GALLERY_PERMISSION_CODE: {
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission was granted
                    pickImageFromGallery();
                } else {
                    //permission was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }

            case CAMERA_PERMISSION_CODE: {
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission was granted
                    openCamera();
                } else {
                    //permission was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    //handle result of picked image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == IMAGE_PIK_CODE){
            //set image to image view
            imgv_product_pic.setImageURI(data.getData());
        } else if(resultCode == RESULT_OK && requestCode == IMAGE_CAPTURE_CODE){
            //set captured image to image view
            imgv_product_pic.setImageURI(image_uri);
        }
    }
}
