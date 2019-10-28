package com.industrialmaster.farmnet.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.widget.Button;
import android.widget.ImageButton;

import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.request.ChangePasswordRequest;
import com.industrialmaster.farmnet.presenters.AuthPresenter;
import com.industrialmaster.farmnet.presenters.AuthPresenterImpl;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.ChangePasswordView;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ChangePasswordActivity extends BaseActivity implements ChangePasswordView {

    AuthPresenter authPresenter;

    TextInputEditText mOldPasswordEditText;
    TextInputEditText mNewPasswordEditText;
    TextInputEditText mConfirmPasswordEditText;
    ImageButton mCloseImageButton;
    Button mResetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        authPresenter = new AuthPresenterImpl(this, this);

        mOldPasswordEditText = findViewById(R.id.edit_text_old_password);
        mNewPasswordEditText = findViewById(R.id.edit_text_new_password);
        mConfirmPasswordEditText = findViewById(R.id.edit_text_confirm_password);
        mCloseImageButton = findViewById(R.id.image_button_close);
        mResetButton = findViewById(R.id.button_reset);

        mCloseImageButton.setOnClickListener(v -> finish());

        mResetButton.setOnClickListener(v -> resetButtonClicked());
    }

    /**
     * function for reset button click
     */
    private void resetButtonClicked() {
        String mOldPassword = Objects.requireNonNull(mOldPasswordEditText.getText()).toString();
        String mNewPassword = Objects.requireNonNull(mNewPasswordEditText.getText()).toString();
        String mConfirmationPassword = Objects.requireNonNull(mConfirmPasswordEditText.getText()).toString();

        ChangePasswordRequest mChangePasswordRequest = new ChangePasswordRequest(mOldPassword, mNewPassword);

        setLoading(true);
        authPresenter.changePassword(mChangePasswordRequest, mConfirmationPassword);

    }

    @Override
    public void showMessage(String message) {
        setLoading(false);
    }

    @Override
    public void onError(String error) {
        setLoading(false);
        showSweetAlert(SweetAlertDialog.ERROR_TYPE, "Oops..." , error,false, FarmnetConstants.OK , SweetAlertDialog::dismissWithAnimation,
                null, null);
    }

    @Override
    public void onSuccess(String message) {
        setLoading(false);
        showSweetAlert(SweetAlertDialog.SUCCESS_TYPE, "Great!" ,message,false, FarmnetConstants.OK ,
                sDialog -> {
                    finish();
                    startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                }, null, null);
    }
}
