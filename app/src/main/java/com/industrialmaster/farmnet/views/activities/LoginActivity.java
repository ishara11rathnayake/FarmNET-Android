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
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();

                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setEmail(email);
                loginRequest.setPassword(password);

                setLoading(true);

                presenter.doLogin(loginRequest);
            }
        });

        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onSuccess(String message) {
        setLoading(false);
        showAlertDialog("Success", message,false, FarmnetConstants.OK , (dialog, which) -> {
          startActivity(new Intent(LoginActivity.this, MainActivity.class));
          finish();
        },"", (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void onError(String message) {
        setLoading(false);
        showAlertDialog("Error", message, false, FarmnetConstants.OK , (dialog, which) -> {},
                "", (dialog, which) -> dialog.dismiss());
    }

    @Override
    protected void onDestroy() {
        DisposableManager.dispose();
        super.onDestroy();
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showErrorMessage(String calledMethod, String error, String errorDescription) {

    }
}
