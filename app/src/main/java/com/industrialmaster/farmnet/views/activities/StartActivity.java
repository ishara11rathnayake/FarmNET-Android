package com.industrialmaster.farmnet.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.industrialmaster.farmnet.views.activities.SignupActivity;
import com.industrialmaster.farmnet.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void clickOnletsGo(View view){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

}
