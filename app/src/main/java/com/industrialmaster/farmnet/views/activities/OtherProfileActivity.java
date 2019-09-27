package com.industrialmaster.farmnet.views.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
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

public class OtherProfileActivity extends BaseActivity implements ProfileView {

    ProfilePresenter presenter;

    TextView tv_address, tv_contact_number, tv_name, tv_email, tv_user_id;
    RatingBar rating_bar_profile;
    ImageButton img_btn_close;
    CircleImageView cimageview_profilepic;

    private Context mContext;
    private Activity mActivity;

    private ConstraintLayout mRateUserConstraintLayout, mReportConstraintLayout;
    private Button mRateUserButton, mReportButton;

    private PopupWindow mRateUserPopupWindow;
    private PopupWindow mReportUserPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        presenter = new ProfilePresenterImpl(this, OtherProfileActivity.this);

        cimageview_profilepic = findViewById(R.id.cimageview_profilepic);
        tv_name = findViewById(R.id.tv_name);
        rating_bar_profile = findViewById(R.id.rating_bar_profile);
        tv_email = findViewById(R.id.tv_email);
        tv_address = findViewById(R.id.tv_address);
        tv_contact_number = findViewById(R.id.tv_contact_number);
        tv_user_id = findViewById(R.id.tv_user_id);

        img_btn_close = findViewById(R.id.img_btn_close);

        setLoading(true);
        String userId = getIntent().getStringExtra("userId");
        presenter.getOtherUserDetails(userId);
        presenter.getOtherUserRating(userId);

        //close create new deal activity
        img_btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Get the application context
        mContext = getApplicationContext();

        // Get the activity
        mActivity = OtherProfileActivity.this;

        // Get the widgets reference from XML layout
        mRateUserConstraintLayout = findViewById(R.id.cl_other_profile);
        mRateUserButton = findViewById(R.id.btn_rate_user);
        mReportButton = findViewById(R.id.btn_report_user);

        // Set a click listener for the mReportButton
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                @SuppressLint("InflateParams") View customView = inflater.inflate(R.layout.layout_report_user_popup, null);

                // Initialize a new instance of popup window
                mReportUserPopupWindow = new PopupWindow(customView, ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT);

                if(Build.VERSION.SDK_INT>=21){
                    mReportUserPopupWindow.setElevation(5.0f);
                }

                // Get a reference for the custom view close button
                ImageButton btn_close = customView.findViewById(R.id.btn_close);

                // Set a click listener for the popup window close button
                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dismiss the popup window
                        mReportUserPopupWindow.dismiss();
                    }
                });

                // Finally, show the popup window at the center location of root relative layout
                mReportUserPopupWindow.showAtLocation(mRateUserConstraintLayout, Gravity.CENTER,0,0);
                mReportUserPopupWindow.setFocusable(true);
                mReportUserPopupWindow.update();

                EditText et_report_user = customView.findViewById(R.id.et_report_user);

                // Get a reference for the custom view rate button
                Button btn_report_user = customView.findViewById(R.id.btn_report_user_popup);

                // Set a click listener for the popup window rate button
                btn_report_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setLoading(true);
                        String content = et_report_user.getText().toString();
                        String userId = tv_user_id.getText().toString();
                        presenter.reportUser(userId, content);
                        mReportUserPopupWindow.dismiss();
                    }
                });
            }
        });

        // Set a click listener for the mRateUserButton
        mRateUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getRatedUserRating(userId);
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
        tv_user_id.setText(user.getUserId());
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
        setLoading(false);
        rating_bar_profile.setRating(rating);
    }

    @Override
    public void showRatingInRatePopup(float rating) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        @SuppressLint("InflateParams") View customView = inflater.inflate(R.layout.layout_custom_popup, null);

        // Initialize a new instance of popup window
        mRateUserPopupWindow = new PopupWindow(customView, ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);

        if(Build.VERSION.SDK_INT>=21){
            mRateUserPopupWindow.setElevation(5.0f);
        }

        // Get a reference for the custom view close button
        ImageButton btn_close = customView.findViewById(R.id.btn_close);

        // Set a click listener for the popup window close button
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the popup window
                mRateUserPopupWindow.dismiss();
            }
        });

        // Finally, show the popup window at the center location of root relative layout
        mRateUserPopupWindow.showAtLocation(mRateUserConstraintLayout, Gravity.CENTER,0,0);

        RatingBar rating_bar_profile = customView.findViewById(R.id.rating_bar_profile);
        rating_bar_profile.setRating(rating);

        // Get a reference for the custom view rate button
        Button btn_rate_user = customView.findViewById(R.id.btn_rate_user);

        // Set a click listener for the popup window rate button
        btn_rate_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoading(true);
                float rating = rating_bar_profile.getRating();
                String userId = tv_user_id.getText().toString();
                presenter.rateUser(userId, rating);
                mRateUserPopupWindow.dismiss();
            }
        });

    }

    @Override
    public void showMessage(String message) {
        setLoading(false);
        showAlertDialog("Success", message,false, FarmnetConstants.OK , (dialog, which) -> {},
                "", (dialog, which) -> {dialog.dismiss();});
    }

    @Override
    public void showErrorMessage(String calledMethod, String error, String errorDescription) {

    }
}
