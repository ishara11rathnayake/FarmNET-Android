package com.industrialmaster.farmnet.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.industrialmaster.farmnet.models.request.LoginRequest;
import com.industrialmaster.farmnet.network.DisposableManager;
import com.industrialmaster.farmnet.presenters.AuthPresenter;
import com.industrialmaster.farmnet.presenters.AuthPresenterImpl;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.AuthView;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends BaseActivity implements AuthView {

    AuthPresenter presenter;

    EditText et_email, et_password;
    Button btn_login;
    TextView txt_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new AuthPresenterImpl(this, LoginActivity.this);

        et_email = findViewById(R.id.etloginemail);
        et_password = findViewById(R.id.etloginpassword);
        btn_login = findViewById(R.id.btnlogin);
        txt_signup = findViewById(R.id.txtsignup);

        //click login
        btn_login.setOnClickListener(v -> {
            String email = et_email.getText().toString().trim();
            String password = et_password.getText().toString().trim();

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(email);
            loginRequest.setPassword(password);

            setLoading(true);
            presenter.doLogin(loginRequest);
        });

        txt_signup.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
        });
    }

    @Override
    public void onSuccess(String message) {
        setLoading(false);
        showSweetAlert(SweetAlertDialog.SUCCESS_TYPE, "Great!" ,message,false, FarmnetConstants.OK ,
                sDialog -> {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }, null, null);
    }

    @Override
    public void onError(String message) {
        setLoading(false);
        showSweetAlert(SweetAlertDialog.ERROR_TYPE, "Oops..." , message,false, FarmnetConstants.OK ,
                SweetAlertDialog::dismissWithAnimation, null, null);
    }

    @Override
    protected void onDestroy() {
        DisposableManager.dispose();
        super.onDestroy();
    }

    @Override
    public void showMessage(String message) {

    }
}
