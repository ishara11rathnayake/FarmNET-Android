package com.industrialmaster.farmnet.views.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.industrialmaster.farmnet.presenters.AuthPresenter;
import com.industrialmaster.farmnet.presenters.AuthPresenterImpl;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.FarmnetHomeView;
import com.industrialmaster.farmnet.views.fragments.AdvertisementFragment;
import com.industrialmaster.farmnet.views.fragments.ArticleFragment;
import com.industrialmaster.farmnet.views.fragments.DealsFragment;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.views.fragments.NotificationFragment;
import com.industrialmaster.farmnet.views.fragments.QandAFragment;

import static android.content.ContentValues.TAG;

public class MainActivity extends BaseActivity implements FarmnetHomeView {

    AuthPresenter authPresenter;

    DealsFragment dealsFragment;
    QandAFragment qandAFragment;
    AdvertisementFragment advertisementFragment;
    NotificationFragment notificationFragment;
    ArticleFragment articleFragment;

    String mUserType;

    private ImageView mNewQuestionImageView;
    private TextView mHeaderTopicTextView;
    private ImageButton mCreateNewDealImageButton;
    private ImageButton mNewArticleImageButton;
    private ImageButton mNewAdsImageButton;
    BottomNavigationView mBottomNavigationView;
    NavigationView mDrawerNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authPresenter = new AuthPresenterImpl(this, MainActivity.this );

        mUserType = getSharedPreferences("FarmnetPrefsFile", Context.MODE_PRIVATE)
                .getString(FarmnetConstants.USER_TYPE, "");

        authPresenter.doCheckAlreadyLogin();

        ImageView mLogoutImageView = findViewById(R.id.imgvlogout);
        mHeaderTopicTextView = findViewById(R.id.txtheadertopic);
        mNewQuestionImageView =findViewById(R.id.img_btn_new_question);
        mNewAdsImageButton = findViewById(R.id.img_btn_new_ads);
        mCreateNewDealImageButton = findViewById(R.id.img_btn_new_deal);
        mNewArticleImageButton = findViewById(R.id.img_btn_new_article);

        //click on create new deal button
        mCreateNewDealImageButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CreateNewDealActivity.class)));

        mNewArticleImageButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CreateNewArticleActivity.class)));

        mNewAdsImageButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CreateNewAdvertisementActivity.class)));

        //click on logout button
        mLogoutImageView.setOnClickListener(v -> showLogoutAlert(ErrorMessageHelper.LOGOUT_CONFIRMATION));

    }

    /**
     * set fragment when clicking bottom navigation item
     * @param fragment the fragment
     */
    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

    /**
     * set main activity content after checking already logged user or not
     */
    public void setMainActivityContent() {

        setContentView(R.layout.content_main);

        //Firebase push notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }

        DrawerLayout mDrawerlayout = findViewById(R.id.drawernavigation);
        ImageView mDrawerToggleImageView = findViewById(R.id.imgvdrawertoggle);

        mBottomNavigationView = findViewById(R.id.bottom_nav);
        dealsFragment = new DealsFragment();
        qandAFragment = new QandAFragment();
        advertisementFragment = new AdvertisementFragment();
        notificationFragment = new NotificationFragment();
        articleFragment = new ArticleFragment();

        mDrawerNavigationView = findViewById(R.id.drawer_nav);

        mDrawerToggleImageView.setOnClickListener(v -> mDrawerlayout.openDrawer(Gravity.START));

        mBottomNavigationView.setSelectedItemId(R.id.deals);
        setFragment(dealsFragment);

        //bottom navigation item selecting (change fragments)
        mBottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            return onBottomNavigationItemSelected(id);
        });

        //navigation drawer item selecting
        mDrawerNavigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            return onDrawerNavigationItemSelected(id);
        });

    }

    /**
     * on drawer navigation item selected
     * @param id drawer navigation item id
     * @return boolean
     */
    private boolean onDrawerNavigationItemSelected(int id){
        if(id == R.id.profile){
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        } else if(id == R.id.logout){
            showLogoutAlert(ErrorMessageHelper.LOGOUT_CONFIRMATION);
        } else if(id == R.id.home){
            finish();
            startActivity(getIntent());
        } else  if(id == R.id.my_questions){
            startActivity(new Intent(MainActivity.this, MyQuestionActivity.class));
        } else if(id == R.id.change_password){
            startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class));
        }
        return false;
    }

    /**
     * on bottom navigation item selected
     * @param id bottom navigation item id
     * @return boolean
     */
    private boolean onBottomNavigationItemSelected(int id){
        if(id == R.id.deals){
            setFragment(dealsFragment);
            mHeaderTopicTextView.setText(getResources().getString(R.string.crop_deals));
            mNewQuestionImageView.setVisibility(View.GONE);
            if(mUserType.equals(FarmnetConstants.UserTypes.FARMER)){
                mCreateNewDealImageButton.setVisibility(View.VISIBLE);
            } else {
                mCreateNewDealImageButton.setVisibility(View.INVISIBLE);
            }
            mNewArticleImageButton.setVisibility(View.GONE);
            mNewAdsImageButton.setVisibility(View.GONE);
            return true;
        } else if(id == R.id.notification){
            setFragment(notificationFragment);
            mHeaderTopicTextView.setText(getResources().getString(R.string.notification));
            mCreateNewDealImageButton.setVisibility(View.INVISIBLE);
            mNewQuestionImageView.setVisibility(View.GONE);
            mNewArticleImageButton.setVisibility(View.GONE);
            mNewAdsImageButton.setVisibility(View.GONE);
            return true;
        } else if(id == R.id.question){
            setFragment(qandAFragment);
            mHeaderTopicTextView.setText("Q & A");
            mCreateNewDealImageButton.setVisibility(View.INVISIBLE);
            mNewQuestionImageView.setVisibility(View.INVISIBLE);
            mNewArticleImageButton.setVisibility(View.INVISIBLE);
            mNewAdsImageButton.setVisibility(View.INVISIBLE);
            return true;
        } else if(id == R.id.articles){
            setFragment(articleFragment);
            mHeaderTopicTextView.setText(getResources().getString(R.string.articles));
            mCreateNewDealImageButton.setVisibility(View.GONE);
            mNewQuestionImageView.setVisibility(View.GONE);
            if(mUserType.equals(FarmnetConstants.UserTypes.KNOWLEDGE_PROVIDER)){
                mNewArticleImageButton.setVisibility(View.VISIBLE);
            } else {
                mNewArticleImageButton.setVisibility(View.INVISIBLE);
            }
            mNewAdsImageButton.setVisibility(View.GONE);
            return true;
        } else if(id == R.id.advertisements){
            setFragment(advertisementFragment);
            mHeaderTopicTextView.setText(getResources().getString(R.string.ads));
            if(mUserType.equals(FarmnetConstants.UserTypes.SERVICE_PROVIDER)){
                mNewAdsImageButton.setVisibility(View.VISIBLE);
            } else {
                mNewAdsImageButton.setVisibility(View.INVISIBLE);
            }
            mCreateNewDealImageButton.setVisibility(View.GONE);
            mNewArticleImageButton.setVisibility(View.GONE);
            mNewQuestionImageView.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    /**
     * show log out alert
     * @param message logout alert message
     */
    public void showLogoutAlert(String message) {
        showAlertDialog("Logout", message, true,
                FarmnetConstants.LOGOUT, (dialog, which) -> authPresenter.doLogout(),
                FarmnetConstants.CANCEL, (dialog, which) -> dialog.dismiss());
    }

    /**
     * check user is a new user or already logged user or logout user
     */
    @Override
    public void setStarterScreen(String screenName) {
        if (FarmnetConstants.CheckUserLogin.NEW_USER.equals(screenName)) {
            startActivity(new Intent(MainActivity.this, StartActivity.class));
            finish();
        } else if (FarmnetConstants.CheckUserLogin.LOGOUT_USER.equals(screenName)) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else if (FarmnetConstants.CheckUserLogin.ALREADY_LOGGED_USER.equals(screenName)) {
            setMainActivityContent();
        }
    }

    @Override
    public void showMessage(String message) {

    }

}
