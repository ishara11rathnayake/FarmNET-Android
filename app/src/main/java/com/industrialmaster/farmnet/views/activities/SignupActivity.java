package com.industrialmaster.farmnet.views.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.industrialmaster.farmnet.models.request.LoginRequest;
import com.industrialmaster.farmnet.models.request.SignUpRequest;
import com.industrialmaster.farmnet.network.DisposableManager;
import com.industrialmaster.farmnet.presenters.AuthPresenter;
import com.industrialmaster.farmnet.presenters.AuthPresenterImpl;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.AuthView;
import com.industrialmaster.farmnet.views.activities.LoginActivity;
import com.industrialmaster.farmnet.R;

public class SignupActivity extends BaseActivity implements AuthView {

    AuthPresenter presenter;

    Spinner spinner;
    EditText et_email, et_password, et_retype_password;
    Button btn_signup;
    TextView txt_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        presenter = new AuthPresenterImpl(this, SignupActivity.this);

        spinner = findViewById(R.id.spinnerusertype);
        et_email = findViewById(R.id.etsignupemail);
        et_password = findViewById(R.id.etsignuppassword);
        et_retype_password = findViewById(R.id.etsignupretypepassword);
        btn_signup = findViewById(R.id.btnsignup);
        txt_login = findViewById(R.id.txtlogin);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_types, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String retypePassword = et_retype_password.getText().toString().trim();
                String userType = spinner.getSelectedItem().toString().trim();

                SignUpRequest signUpRequest = new SignUpRequest();
                signUpRequest.setEmail(email);
                signUpRequest.setPassword(password);
                signUpRequest.setUser_type(userType);

                setLoading(true);

                presenter.doSignup(signUpRequest, retypePassword);
            }
        });

    }

    public void clickOnHaveAccount(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSuccess(String message) {
        setLoading(false);
        showAlertDialog("Success", message, false, FarmnetConstants.OK , (dialog, which) -> {
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
            finish();
        }, "", (dialog, which) -> dialog.dismiss());
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


