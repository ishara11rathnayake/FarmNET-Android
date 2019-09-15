package com.industrialmaster.farmnet.views.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.industrialmaster.farmnet.R;

public class ProfileActivity extends AppCompatActivity {

    CardView cv_all_product;
    TextView tv_address;
    TextView tv_contact_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        cv_all_product = findViewById(R.id.card_view_all_products);
        tv_address = findViewById(R.id.tv_address);
        tv_contact_number = findViewById(R.id.tv_contact_number);
    }
}
