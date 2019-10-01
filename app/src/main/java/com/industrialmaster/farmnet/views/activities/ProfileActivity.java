package com.industrialmaster.farmnet.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Deals;
import com.industrialmaster.farmnet.models.User;
import com.industrialmaster.farmnet.presenters.ProfilePresenter;
import com.industrialmaster.farmnet.presenters.ProfilePresenterImpl;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.ProfileView;
import com.industrialmaster.farmnet.views.adapters.DealsGridViewRecyclerViewAdapter;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseActivity implements ProfileView {

    ProfilePresenter presenter;

    TextView tv_address, tv_contact_number, tv_name, tv_email;
    RatingBar rating_bar_profile;
    ImageButton img_btn_edit, img_btn_close, img_btn_timeline_list;
    CircleImageView cimageview_profilepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        presenter = new ProfilePresenterImpl(this, ProfileActivity.this);

        cimageview_profilepic = findViewById(R.id.cimageview_profilepic);
        tv_name = findViewById(R.id.tv_name);
        rating_bar_profile = findViewById(R.id.rating_bar_profile);
        tv_email = findViewById(R.id.tv_email);
        tv_address = findViewById(R.id.tv_address);
        tv_contact_number = findViewById(R.id.tv_contact_number);

        img_btn_edit = findViewById(R.id.img_btn_edit);
        img_btn_close = findViewById(R.id.img_btn_close);
        img_btn_timeline_list = findViewById(R.id.img_btn_timeline_list);

        setLoading(true);
        presenter.getUserDetails();
        presenter.getUserRating();

        img_btn_edit.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class)));

        //close create new deal activity
        img_btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //directed to timeline list activity
        img_btn_timeline_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ProductTimelineListActivity.class));
            }
        });
    }

    @Override
    public void showUserDetails(User user, List<Deals> deals) {
        setLoading(false);
        tv_name.setText(user.getName());
        tv_email.setText(user.getEmail());
        tv_contact_number.setText(user.getContactNumber());
        tv_address.setText(user.getAddress());
        if(!TextUtils.isEmpty(user.getProfilePicUrl())){
            Glide.with(this)
                    .asBitmap()
                    .load(user.getProfilePicUrl())
                    .centerInside()
                    .into(cimageview_profilepic);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerview_abstract_deal);
        DealsGridViewRecyclerViewAdapter adapter = new DealsGridViewRecyclerViewAdapter(this, deals);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onError(String message) {
        showAlertDialog("Error", message,false, FarmnetConstants.OK , (dialog, which) -> {},
                "", (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void showUserrating(float rating) {
        rating_bar_profile.setRating(rating);
    }

    @Override
    public void showRatingInRatePopup(float rating) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showErrorMessage(String calledMethod, String error, String errorDescription) {

    }
}
