package com.industrialmaster.farmnet.views.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.request.CreateNewTimelineRequest;
import com.industrialmaster.farmnet.presenters.TimelinePresenter;
import com.industrialmaster.farmnet.presenters.TimelnePresenterImpl;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.TimelineView;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CreateTimelineActivity extends BaseActivity implements TimelineView {

    TimelinePresenter timelinePresenter;

    Button mAddNewTimelineButton;
    ImageButton mCloseImageButton;
    EditText mProductNameEditText;
    EditText mDescriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_timeline);

        timelinePresenter = new TimelnePresenterImpl(this, CreateTimelineActivity.this);

        mAddNewTimelineButton = findViewById(R.id.img_btn_add_new_task);
        mCloseImageButton = findViewById(R.id.img_btn_close);
        mProductNameEditText = findViewById(R.id.et_product_name);
        mDescriptionEditText = findViewById(R.id.et_description);

        mAddNewTimelineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewTimelineRequest createNewTimelineRequest = new CreateNewTimelineRequest();
                createNewTimelineRequest.setProductName(mProductNameEditText.getText().toString());
                createNewTimelineRequest.setDescription(mDescriptionEditText.getText().toString());
                setLoading(true);
                timelinePresenter.createNewTimeline(createNewTimelineRequest);
            }
        });

        mCloseImageButton.setOnClickListener(v -> {
            String message = ErrorMessageHelper.DISCARD_CONFIRMATION;
            showSweetAlert(SweetAlertDialog.WARNING_TYPE, message,null,false, FarmnetConstants.OK ,
                    sDialog -> finish(),FarmnetConstants.CANCEL, SweetAlertDialog::dismissWithAnimation);
        });
    }

    @Override
    public void onError(String message) {
        setLoading(false);
        showSweetAlert(SweetAlertDialog.ERROR_TYPE, "Oops..." , message,false, FarmnetConstants.OK , SweetAlertDialog::dismissWithAnimation,
                null, null);

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
}
