package com.industrialmaster.farmnet.views.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.request.CreateNewTimelineRequest;
import com.industrialmaster.farmnet.presenters.TimelinePresenter;
import com.industrialmaster.farmnet.presenters.TimelnePresenterImpl;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.TimelineView;

public class CreateTimelineActivity extends BaseActivity implements TimelineView {

    TimelinePresenter presenter;

    Button mAddNewTimelineButton;
    ImageButton mCloseImageButton;
    EditText mProductNameEditText, mDescriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_timeline);

        presenter = new TimelnePresenterImpl(this, CreateTimelineActivity.this);

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

                presenter.createNewTimeline(createNewTimelineRequest);
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

    @Override
    public void showErrorMessage(String calledMethod, String error, String errorDescription) {

    }
}
