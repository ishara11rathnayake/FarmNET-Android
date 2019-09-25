package com.industrialmaster.farmnet.views.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.utils.FarmnetConstants;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends BaseActivity {

    CircleImageView circle_image_view_profile_pic;
    ImageButton img_btn_close;
    EditText et_dob;
    DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        circle_image_view_profile_pic = findViewById(R.id.cimageview_profilepic);
        img_btn_close = findViewById(R.id.img_btn_close);

        et_dob = findViewById(R.id.et_dob);

        //pick image from gallery clicking image view
        circle_image_view_profile_pic.setOnClickListener(new View.OnClickListener() {
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

        et_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(EditProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                et_dob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
    }

    //handle result of picked image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == IMAGE_PIK_CODE){
            //set image to image view
            circle_image_view_profile_pic.setImageURI(data.getData());
            imageFilePath = data.getData();
            hasImage = true;
        }
    }
}
