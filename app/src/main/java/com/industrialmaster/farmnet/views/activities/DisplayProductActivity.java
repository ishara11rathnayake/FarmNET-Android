package com.industrialmaster.farmnet.views.activities;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Deals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DisplayProductActivity extends AppCompatActivity {

    ImageView image_view_product;
    TextView tv_product_name, tv_description, tv_unit_price, tv_amount, tv_location,
            tv_owner, tv_published_date;
    ImageButton img_btn_close;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_product);

        Gson gson = new Gson();
        Deals deal = gson.fromJson(getIntent().getStringExtra("deal"), Deals.class);

        img_btn_close = findViewById(R.id.img_btn_close);

        image_view_product = findViewById(R.id.imgv_product_image);
        tv_product_name = findViewById(R.id.tv_product_name);
        tv_description = findViewById(R.id.tv_description);
        tv_unit_price = findViewById(R.id.tv_unit_price_value);
        tv_amount = findViewById(R.id.tv_amount_value);
        tv_location = findViewById(R.id.tv_location_value);
        tv_owner = findViewById(R.id.tv_owner_value);
        tv_published_date = findViewById(R.id.tv_date_value);

        Glide.with(this)
                .asBitmap()
                .load(deal.getProductImageUrl())
                .centerCrop()
                .into(image_view_product);

        tv_product_name.setText(deal.getProductName());
        tv_description.setText(deal.getDescription());
        tv_unit_price.setText(": Rs." + deal.getUnitPrice());
        tv_amount.setText(": " + deal.getAmount() + "Kg");
        tv_location.setText(": " + deal.getLocation());
        tv_owner.setText(": " + deal.getUser().getName());

        Date date = deal.getDate();
        DateFormat targetDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        String formattedDate = targetDateFormat.format(date);

        tv_published_date.setText(": " + formattedDate);

        img_btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}