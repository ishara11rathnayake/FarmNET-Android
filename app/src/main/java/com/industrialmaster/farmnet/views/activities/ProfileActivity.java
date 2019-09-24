package com.industrialmaster.farmnet.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.response.UserDetailsResponse;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.ProfileView;

public class ProfileActivity extends BaseActivity implements ProfileView {

    CardView cv_all_product;
    TextView tv_address, tv_contact_number;
    RatingBar rating_bar_profile;
    ImageButton img_btn_edit, img_btn_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        img_btn_edit = findViewById(R.id.img_btn_edit);
        img_btn_close = findViewById(R.id.img_btn_close);

        cv_all_product = findViewById(R.id.card_view_all_products);
        tv_address = findViewById(R.id.tv_address);
        tv_contact_number = findViewById(R.id.tv_contact_number);

        rating_bar_profile = findViewById(R.id.rating_bar_profile);

        rating_bar_profile.setRating((float) 4.65);

        img_btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        });

        //close create new deal activity
        img_btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = ErrorMessageHelper.DISCARD_CONFIRMATION;
                showAlertDialog("Warning", message,false, FarmnetConstants.OK , (dialog, which) -> {
                    finish();
                },FarmnetConstants.CANCEL, (dialog, which) -> dialog.dismiss());
            }
        });
    }

    @Override
    public void showUserDetails(UserDetailsResponse userDetailsResponse) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showErrorMessage(String calledMethod, String error, String errorDescription) {

    }
}
