package com.industrialmaster.farmnet.views.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
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

public class ProfileActivity extends BaseActivity implements ProfileView {

    ProfilePresenter profilePresenter;

    TextView mAddressTextView;
    TextView mContactNumberTextView;
    TextView mNameTextView;
    TextView mEmailTextView;
    RatingBar mProfileRatingBar;
    ImageButton mEditImageButton;
    ImageButton mCloseImageButton;
    ImageButton mTimelineListImageButton;
    CircleImageView mProfilePicCircleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePresenter = new ProfilePresenterImpl(this, ProfileActivity.this);

        mProfilePicCircleImageView = findViewById(R.id.cimageview_profilepic);
        mNameTextView = findViewById(R.id.tv_name);
        mProfileRatingBar = findViewById(R.id.rating_bar_profile);
        mEmailTextView = findViewById(R.id.tv_email);
        mAddressTextView = findViewById(R.id.tv_address);
        mContactNumberTextView = findViewById(R.id.tv_contact_number);

        mEditImageButton = findViewById(R.id.img_btn_edit);
        mCloseImageButton = findViewById(R.id.img_btn_close);
        mTimelineListImageButton = findViewById(R.id.img_btn_timeline_list);
        mTimelineListImageButton.setVisibility(View.INVISIBLE);

        setLoading(true);
        profilePresenter.getUserDetails();
        profilePresenter.getUserRating();

        mEditImageButton.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class)));

        //close create new deal activity
        mCloseImageButton.setOnClickListener(v -> finish());

        //directed to timeline list activity
        mTimelineListImageButton.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, ProductTimelineListActivity.class)));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    public <T> void showUserDetails(User user, List<T> products) {
        setLoading(false);
        List<Deals> deals = null;
        List<Advertisement> advertisements = null;
        List<Article> articles = null;
        RecyclerView recyclerView = findViewById(R.id.recyclerview_abstract_deal);
        TextView mProductType = findViewById(R.id.text_view_product_type);

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
                mTimelineListImageButton.setVisibility(View.VISIBLE);
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
                mTimelineListImageButton.setVisibility(View.INVISIBLE);
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
                mTimelineListImageButton.setVisibility(View.INVISIBLE);
                break;
            default:
                mProductType.setVisibility(View.GONE);
                mTimelineListImageButton.setVisibility(View.INVISIBLE);
                break;
        }

        mNameTextView.setText(user.getName());
        mEmailTextView.setText(user.getEmail());
        mContactNumberTextView.setText(user.getContactNumber());
        mAddressTextView.setText(user.getAddress());
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
        mProfileRatingBar.setRating(rating);
    }

    @Override
    public void showRatingInRatePopup(float rating) {

    }

    @Override
    public void showMessage(String message) {

    }

}
