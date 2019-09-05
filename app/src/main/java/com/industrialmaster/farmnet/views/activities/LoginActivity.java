package com.industrialmaster.farmnet.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.industrialmaster.farmnet.models.request.LoginRequest;
import com.industrialmaster.farmnet.network.DisposableManager;
import com.industrialmaster.farmnet.presenters.AuthPresenter;
import com.industrialmaster.farmnet.presenters.AuthPresenterImpl;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.views.AuthView;

public class LoginActivity extends BaseActivity implements AuthView {

    AuthPresenter presenter;

    EditText et_email, et_password;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new AuthPresenterImpl(this);

        et_email = findViewById(R.id.etloginemail);
        et_password = findViewById(R.id.etloginpassword);
        btn_login = findViewById(R.id.btnlogin);

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
    }

    public void navigationToSignUpActivity(View view){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void navigationToHomeActivity(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSuccess(String message) {
        setLoading(false);
        showAlertDialog("Success", message, false);
    }

    @Override
    public void onError(String message) {
        setLoading(false);
        showAlertDialog("Error", message, false);
    }

    @Override
    protected void onDestroy() {
        DisposableManager.dispose();
        super.onDestroy();
    }
}
