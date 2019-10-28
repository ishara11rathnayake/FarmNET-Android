package com.industrialmaster.farmnet.views.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.request.CreateNewTimelineTaskRequest;
import com.industrialmaster.farmnet.presenters.TimelinePresenter;
import com.industrialmaster.farmnet.presenters.TimelnePresenterImpl;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.TimelineView;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddNewTimelineTaskActivity extends BaseActivity implements TimelineView {

    TimelinePresenter timelinePresenter;
    ImageButton mCloseImageButton;
    Button mAddNewTaskButton;
    EditText mContent;
    ImageView mTaskImageView;
    boolean hasImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_timeline_task);

        timelinePresenter = new TimelnePresenterImpl(this, this);

        mCloseImageButton = findViewById(R.id.img_btn_close);
        mAddNewTaskButton = findViewById(R.id.img_btn_create_task);
        mContent = findViewById(R.id.et_task_content);
        mTaskImageView = findViewById(R.id.imgv_task_pic);

        String timelineId = getIntent().getStringExtra("timelineId");

        mAddNewTaskButton.setOnClickListener(v -> {
            CreateNewTimelineTaskRequest newTimelineTaskRequest = new CreateNewTimelineTaskRequest();
            newTimelineTaskRequest.setTimelineId(timelineId);

            newTimelineTaskRequest.setContent(mContent.getText().toString());

            String realFilePath;

            if(hasImage) {
                realFilePath = convertMediaUriToPath(imageFilePath);
                newTimelineTaskRequest.setTimelineImage(realFilePath);
            }

            timelinePresenter.createNewTimelineTask(newTimelineTaskRequest);
        });

        mCloseImageButton.setOnClickListener(v -> {
            String message = ErrorMessageHelper.DISCARD_CONFIRMATION;
            showSweetAlert(SweetAlertDialog.WARNING_TYPE, message,null,false, FarmnetConstants.OK ,
                    sDialog -> finish(),FarmnetConstants.CANCEL, SweetAlertDialog::dismissWithAnimation);
        });

        //pick image from gallery clicking image view
        mTaskImageView.setOnClickListener(v -> {
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
        });
    }

    @Override
    public void onError(String message) {
        showSweetAlert(SweetAlertDialog.ERROR_TYPE, "Oops..." , message,false, FarmnetConstants.OK , SweetAlertDialog::dismissWithAnimation,
                null, null);
    }

    @Override
    public void onSuccess(String message) {
        showSweetAlert(SweetAlertDialog.SUCCESS_TYPE, "Great!" ,message,false, FarmnetConstants.OK ,
                sDialog -> finish(), null, null);
    }

    @Override
    public void showMessage(String message) {

    }

    //handle result of picked image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == IMAGE_PIK_CODE){
            //set image to image view
            mTaskImageView.setImageURI(Objects.requireNonNull(data).getData());
            imageFilePath = Objects.requireNonNull(data).getData();
            hasImage = true;
        }
    }
}
