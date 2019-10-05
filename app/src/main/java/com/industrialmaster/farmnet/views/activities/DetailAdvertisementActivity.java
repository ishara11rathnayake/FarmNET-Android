package com.industrialmaster.farmnet.views.activities;

import android.app.ActionBar;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Advertisement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailAdvertisementActivity extends AppCompatActivity {

    ImageButton mCloseImageButton;
    ImageView mAdImageView;
    TextView mAdTitleTextView;
    TextView mPriceTextView;
    TextView mDescriptionTextView;
    TextView mNameTextView;
    TextView mDateTextView;
    TextView mPhoneTextView;
    TextView mEmailTextView;
    LinearLayout mHashtagsLinearLayout;
    CircleImageView mProfileCircleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_advertisement);

        Gson gson = new Gson();
        Advertisement advertisement = gson.fromJson(getIntent().getStringExtra("adDetails"), Advertisement.class);

        mAdImageView = findViewById(R.id.img_view_ads);
        mAdTitleTextView = findViewById(R.id.tv_add_title);
        mPriceTextView = findViewById(R.id.tv_price);
        mDescriptionTextView = findViewById(R.id.tv_description);
        mHashtagsLinearLayout = findViewById(R.id.linear_layout_hashtags);
        mProfileCircleImageView = findViewById(R.id.cimgv_profile_image);
        mNameTextView = findViewById(R.id.tv_name);
        mDateTextView = findViewById(R.id.tv_date);
        mPhoneTextView = findViewById(R.id.tv_phone_value);
        mEmailTextView = findViewById(R.id.tv_email_value);
        mCloseImageButton = findViewById(R.id.img_btn_close);

        mCloseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Glide.with(this)
                .asBitmap()
                .load(advertisement.getImageUrl())
                .centerCrop()
                .into(mAdImageView);

        mAdTitleTextView.setText(advertisement.getAdTitle());
        mPriceTextView.setText(String.format("Rs.%s", advertisement.getPrice()));

        if(!TextUtils.isEmpty(advertisement.getAdDescription())){
            mDescriptionTextView.setText(advertisement.getAdDescription());
        }else {
            mDescriptionTextView.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(advertisement.getUser().getProfilePicUrl())){
            Glide.with(this)
                    .asBitmap()
                    .load(advertisement.getUser().getProfilePicUrl())
                    .centerCrop()
                    .into(mProfileCircleImageView);
        }

        mNameTextView.setText(advertisement.getUser().getName());
        mPhoneTextView.setText(advertisement.getContactNumber());
        mEmailTextView.setText(advertisement.getUser().getEmail());

        Date date = advertisement.getDate();
        DateFormat targetDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
        String formattedDate = targetDateFormat.format(date);
        mDateTextView.setText(formattedDate);

        if(advertisement.getTags().length != 0){

            String[] hastags = advertisement.getTags();

            if(hastags.length != 0) {

                final TextView[] myTextViews = new TextView[hastags.length];

                for (int x = 0; x < hastags.length; x++) {
                    final TextView rowTextView = new TextView(this);
                    rowTextView.setText(String.format("#%s", hastags[x]));
                    mHashtagsLinearLayout.addView(rowTextView);

                    Typeface typeface = ResourcesCompat.getFont(this, R.font.ubunturegular);
                    rowTextView.setTypeface(typeface);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                    params.setMargins(5,0,5,0);
                    rowTextView.setLayoutParams(params);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        rowTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    }

                    rowTextView.setPadding(10, 5, 10, 5);
                    rowTextView.setTextColor(getResources().getColor(R.color.white));

                    rowTextView.setBackground(ResourcesCompat.getDrawable(this.getResources(), R.drawable.tags_bg, null));
                    myTextViews[x] = rowTextView;
                }

            }

        }else {
            mHashtagsLinearLayout.setVisibility(View.GONE);
        }

    }
}
