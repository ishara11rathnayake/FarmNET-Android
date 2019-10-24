package com.industrialmaster.farmnet.views.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.industrialmaster.farmnet.models.Advertisement;
import com.industrialmaster.farmnet.models.Article;
import com.industrialmaster.farmnet.models.Deals;
import com.industrialmaster.farmnet.models.User;
import com.industrialmaster.farmnet.presenters.ProfilePresenter;
import com.industrialmaster.farmnet.presenters.ProfilePresenterImpl;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.ProfileView;
import com.industrialmaster.farmnet.views.adapters.AdvertisementRecyclerViewAdapter;
import com.industrialmaster.farmnet.views.adapters.ArticleRecyclerViewAdapter;
import com.industrialmaster.farmnet.views.adapters.DealsGridViewRecyclerViewAdapter;

import java.util.List;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherProfileActivity extends BaseActivity implements ProfileView {

    ProfilePresenter profilePresenter;

    TextView mAddressTextView;
    TextView mContactNumberTextView;
    TextView mNameTextView;
    TextView mEmailTextView;
    TextView mUserIdTextView;
    RatingBar mProfileRatingBar;
    ImageButton mCloseImageButton;
    CircleImageView mProfilePicCircleImageView;
    ConstraintLayout mPhoneConstraintLayout;
    ConstraintLayout mAddressConstraintLayout;

    private Context mContext;
    private Activity mActivity;

    private ConstraintLayout mRateUserConstraintLayout;
    private ConstraintLayout mReportConstraintLayout;

    private PopupWindow mRateUserPopupWindow;
    private PopupWindow mReportUserPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        profilePresenter = new ProfilePresenterImpl(this, OtherProfileActivity.this);

        mProfilePicCircleImageView = findViewById(R.id.cimageview_profilepic);
        mNameTextView = findViewById(R.id.tv_name);
        mProfileRatingBar = findViewById(R.id.rating_bar_profile);
        mEmailTextView = findViewById(R.id.tv_email);
        mAddressTextView = findViewById(R.id.tv_address);
        mContactNumberTextView = findViewById(R.id.tv_contact_number);
        mUserIdTextView = findViewById(R.id.tv_user_id);
        mPhoneConstraintLayout = findViewById(R.id.constraint_layout_phone);
        mAddressConstraintLayout = findViewById(R.id.constraint_layout_address);

        mCloseImageButton = findViewById(R.id.img_btn_close);

        setLoading(true);
        String userId = getIntent().getStringExtra("userId");
        profilePresenter.getOtherUserDetails(userId);
        profilePresenter.getOtherUserRating(userId);

        //close create new deal activity
        mCloseImageButton.setOnClickListener(v -> finish());

        // Get the application context
        mContext = getApplicationContext();

        // Get the activity
        mActivity = OtherProfileActivity.this;

        // Get the widgets reference from XML layout
        mRateUserConstraintLayout = findViewById(R.id.cl_other_profile);
        Button mRateUserButton = findViewById(R.id.btn_rate_user);
        Button mReportButton = findViewById(R.id.btn_report_user);

        // Set a click listener for the mReportButton
        mReportButton.setOnClickListener(v -> onClickReport());

        // Set a click listener for the mRateUserButton
        mRateUserButton.setOnClickListener(v -> profilePresenter.getRatedUserRating(userId));
    }

    @Override
    public <T> void showUserDetails(User user, List<T> products) {
        setLoading(false);

        List<Deals> deals = null;
        List<Advertisement> advertisements = null;
        List<Article> articles = null;
        RecyclerView recyclerView = findViewById(R.id.recyclerview_abstract_deal);
        TextView mProductType = findViewById(R.id.text_view_product_type);

        if(TextUtils.isEmpty(user.getContactNumber())){
            mPhoneConstraintLayout.setVisibility(View.GONE);
        }

        if(TextUtils.isEmpty(user.getAddress())){
            mAddressConstraintLayout.setVisibility(View.GONE);
        }

        //change profile view according to user type
        switch (user.getUserType()) {
            case FarmnetConstants.UserTypes.FARMER:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    deals = products.stream().filter(element -> element instanceof Deals)
                            .map(element -> (Deals) element)
                            .collect(Collectors.toList());
                }
                DealsGridViewRecyclerViewAdapter adapter = new DealsGridViewRecyclerViewAdapter(this, deals);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                recyclerView.setAdapter(adapter);
                mProductType.setText(FarmnetConstants.DEALS);
                break;
            case FarmnetConstants.UserTypes.SERVICE_PROVIDER:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    advertisements = products.stream().filter(element -> element instanceof Advertisement)
                            .map(element -> (Advertisement) element)
                            .collect(Collectors.toList());
                }
                AdvertisementRecyclerViewAdapter adsAdapter = new AdvertisementRecyclerViewAdapter(this, advertisements);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adsAdapter);
                mProductType.setText(FarmnetConstants.ADVERTISEMENTS);
                break;
            case FarmnetConstants.UserTypes.KNOWLEDGE_PROVIDER:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    articles = products.stream().filter(element -> element instanceof Article)
                            .map(element -> (Article) element)
                            .collect(Collectors.toList());
                }
                ArticleRecyclerViewAdapter articleAdapter = new ArticleRecyclerViewAdapter(this, articles);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(articleAdapter);
                mProductType.setText(FarmnetConstants.ARTICLES);
                break;
            default:
                mProductType.setVisibility(View.GONE);
                break;
        }

        mNameTextView.setText(user.getName());
        mEmailTextView.setText(user.getEmail());
        mContactNumberTextView.setText(user.getContactNumber());
        mAddressTextView.setText(user.getAddress());
        mUserIdTextView.setText(user.getUserId());
        if(!TextUtils.isEmpty(user.getProfilePicUrl())){
            Glide.with(this)
                    .asBitmap()
                    .load(user.getProfilePicUrl())
                    .centerInside()
                    .into(mProfilePicCircleImageView);
        }
    }

    @Override
    public void onError(String message) {
        showAlertDialog("Error", message,false, FarmnetConstants.OK , (dialog, which) -> {},
                "", (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void showUserrating(float rating) {
        setLoading(false);
        mProfileRatingBar.setRating(rating);
    }

    @Override
    public void showRatingInRatePopup(float rating) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        @SuppressLint("InflateParams") View customView = inflater.inflate(R.layout.layout_custom_popup, null);

        // Initialize a new instance of popup window
        mRateUserPopupWindow = new PopupWindow(customView, ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);

        mRateUserPopupWindow.setElevation(5.0f);

        // Get a reference for the views
        ImageButton mClosePopupImageButton = customView.findViewById(R.id.btn_close);
        RatingBar mProfilePopupRatingBar = customView.findViewById(R.id.rating_bar_profile);
        Button mRateUserPopupButton = customView.findViewById(R.id.btn_rate_user);

        // Set a click listener for the popup window close button
        mClosePopupImageButton.setOnClickListener(v -> mRateUserPopupWindow.dismiss());

        // Finally, show the popup window at the center location of root relative layout
        mRateUserPopupWindow.showAtLocation(mRateUserConstraintLayout, Gravity.CENTER,0,0);

        mProfilePopupRatingBar.setRating(rating);

        // Set a click listener for the popup window rate button
        mRateUserPopupButton.setOnClickListener(v -> {
            setLoading(true);
            float rating1 = mProfilePopupRatingBar.getRating();
            String userId = mUserIdTextView.getText().toString();
            profilePresenter.rateUser(userId, rating1);
            mRateUserPopupWindow.dismiss();
        });

    }

    @Override
    public void showMessage(String message) {
        setLoading(false);
        showAlertDialog("Success", message,false, FarmnetConstants.OK , (dialog, which) -> {},
                "", (dialog, which) -> dialog.dismiss());
    }

    /**
     * handle report user actions
     */
    private void onClickReport(){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        @SuppressLint("InflateParams") View customView = inflater.inflate(R.layout.layout_report_user_popup, null);

        // Initialize a new instance of popup window
        mReportUserPopupWindow = new PopupWindow(customView, ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);

        mReportUserPopupWindow.setElevation(5.0f);

        // Get a reference for the views
        ImageButton mCloseButton = customView.findViewById(R.id.btn_close);
        Button mReportUserButton = customView.findViewById(R.id.btn_report_user_popup);
        EditText mReportUserEditText = customView.findViewById(R.id.et_report_user);

        // Set a click listener for the popup window close button
        mCloseButton.setOnClickListener(v1 -> mReportUserPopupWindow.dismiss());

        // Finally, show the popup window at the center location of root relative layout
        mReportUserPopupWindow.showAtLocation(mRateUserConstraintLayout, Gravity.CENTER,0,0);
        mReportUserPopupWindow.setFocusable(true);
        mReportUserPopupWindow.update();

        // Set a click listener for the popup window rate button
        mReportUserButton.setOnClickListener(v12 -> {
            setLoading(true);
            String content = mReportUserEditText.getText().toString();
            String userId1 = mUserIdTextView.getText().toString();
            profilePresenter.reportUser(userId1, content);
            mReportUserPopupWindow.dismiss();
        });
    }
}
