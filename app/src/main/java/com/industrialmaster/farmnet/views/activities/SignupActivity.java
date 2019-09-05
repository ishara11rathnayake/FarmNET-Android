package com.industrialmaster.farmnet.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.industrialmaster.farmnet.views.activities.LoginActivity;
import com.industrialmaster.farmnet.R;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Spinner spinner = findViewById(R.id.spinnerUserType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_types, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

    }

    public void clickOnHaveAccount(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}


