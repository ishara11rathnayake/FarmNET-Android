package com.industrialmaster.farmnet.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.industrialmaster.farmnet.R;

public class StartActivity extends AppCompatActivity {

    Button mGetStartedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mGetStartedButton = findViewById(R.id.btngetstarted);

        mGetStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, SignupActivity.class));
                finish();
            }
        });

    }
}
