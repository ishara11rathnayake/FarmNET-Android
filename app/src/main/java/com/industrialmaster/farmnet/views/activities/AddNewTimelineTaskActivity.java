package com.industrialmaster.farmnet.views.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.request.CreateNewTimelineTaskRequest;
import com.industrialmaster.farmnet.presenters.TimelinePresenter;
import com.industrialmaster.farmnet.presenters.TimelnePresenterImpl;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.TimelineView;

public class AddNewTimelineTaskActivity extends BaseActivity implements TimelineView {

    TimelinePresenter presenter;
    ImageButton mCloseImageButton;
    Button mAddNewTaskButton;
    EditText mContent;
    ImageView mTaskImageView;
    boolean hasImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_timeline_task);

        presenter = new TimelnePresenterImpl(this, this);

        mCloseImageButton = findViewById(R.id.img_btn_close);
        mAddNewTaskButton = findViewById(R.id.img_btn_create_task);
        mContent = findViewById(R.id.et_task_content);
        mTaskImageView = findViewById(R.id.imgv_task_pic);

        String timelineId = getIntent().getStringExtra("timelineId");

        mAddNewTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewTimelineTaskRequest newTimelineTaskRequest = new CreateNewTimelineTaskRequest();
                newTimelineTaskRequest.setTimelineId(timelineId);

                newTimelineTaskRequest.setContent(mContent.getText().toString());

                String realFilePath;

                if(hasImage == true) {
                    realFilePath = convertMediaUriToPath(imageFilePath);
                    newTimelineTaskRequest.setTimelineImage(realFilePath);
                }

                presenter.createNewTimelineTask(newTimelineTaskRequest);
            }
        });

        mCloseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = ErrorMessageHelper.DISCARD_CONFIRMATION;
                showAlertDialog("Warning", message,false, FarmnetConstants.OK , (dialog, which) -> {
                    finish();
                },FarmnetConstants.CANCEL, (dialog, which) -> dialog.dismiss());
            }
        });

        //pick image from gallery clicking image view
        mTaskImageView.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onError(String message) {
        showAlertDialog("Error", message,false, FarmnetConstants.OK , (dialog, which) -> {},
                "", (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void onSuccess(String message) {
        showAlertDialog("Success", message,false, FarmnetConstants.OK ,
                (dialog, which) -> {
                    finish();
                },
                "", (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void showMessage(String message) {

    }

    //handle result of picked image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == IMAGE_PIK_CODE){
            //set image to image view
            mTaskImageView.setImageURI(data.getData());
            imageFilePath = data.getData();
            hasImage = true;
        }
    }
}
