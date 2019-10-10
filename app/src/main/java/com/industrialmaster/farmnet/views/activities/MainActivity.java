package com.industrialmaster.farmnet.views.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
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

    DealsFragment dealsFragment;
    QandAFragment qandAFragment;
    AdvertisementFragment advertisementFragment;
    NotificationFragment notificationFragment;
    ArticleFragment articleFragment;

    String mUserType;

    AuthPresenter authPresenter = new AuthPresenterImpl(this, MainActivity.this );

    private DrawerLayout mDrawerlayout;
    private ImageView imgv_drawer_toggle, img_btn_new_question, imgv_logout;
    private TextView txt_header_topic;

    private ImageButton img_btn_create_new_deal, img_btn_new_article;
    private ImageButton mNewAdsImageButton;

    BottomNavigationView bottom_navigation_view;
    NavigationView drawer_navigation_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authPresenter.doCheckAlreadyLogin();

        mUserType = getSharedPreferences("FarmnetPrefsFile", Context.MODE_PRIVATE)
                .getString(FarmnetConstants.USER_TYPE, "");

        imgv_logout = findViewById(R.id.imgvlogout);
        txt_header_topic = findViewById(R.id.txtheadertopic);
        img_btn_new_question =findViewById(R.id.img_btn_new_question);
        mNewAdsImageButton = findViewById(R.id.img_btn_new_ads);
        img_btn_create_new_deal = findViewById(R.id.img_btn_new_deal);
        img_btn_new_article = findViewById(R.id.img_btn_new_article);

        //click on create new deal button
        img_btn_create_new_deal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateNewDealActivity.class));
            }
        });

        img_btn_new_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateNewArticleActivity.class));
            }
        });

        mNewAdsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateNewAdvertisementActivity.class));
            }
        });

        //click on logout button
        imgv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutAlert(ErrorMessageHelper.LOGOUT_CONFIRMATION);
            }
        });

    }

    //set fragment when clicking bottom navigation item
    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

    public void setMainActivityContent() {

        setContentView(R.layout.content_main);

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

        mDrawerlayout = findViewById(R.id.drawernavigation);
        imgv_drawer_toggle = findViewById(R.id.imgvdrawertoggle);

        bottom_navigation_view = findViewById(R.id.bottom_nav);
        dealsFragment = new DealsFragment();
        qandAFragment = new QandAFragment();
        advertisementFragment = new AdvertisementFragment();
        notificationFragment = new NotificationFragment();
        articleFragment = new ArticleFragment();

        drawer_navigation_view = findViewById(R.id.drawer_nav);

        imgv_drawer_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerlayout.openDrawer(Gravity.LEFT);
            }
        });

        bottom_navigation_view.setSelectedItemId(R.id.deals);
        setFragment(dealsFragment);

        //bottom navigation item selecting (change fragments)
        bottom_navigation_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.deals){
                    setFragment(dealsFragment);
                    txt_header_topic.setText(getResources().getString(R.string.crop_deals));
                    img_btn_new_question.setVisibility(View.GONE);
                    if(mUserType.equals(FarmnetConstants.UserTypes.FARMER)){
                        img_btn_create_new_deal.setVisibility(View.VISIBLE);
                    } else {
                        img_btn_create_new_deal.setVisibility(View.INVISIBLE);
                    }
                    img_btn_new_article.setVisibility(View.GONE);
                    mNewAdsImageButton.setVisibility(View.GONE);
                    return true;
                } else if(id == R.id.notification){
                    setFragment(notificationFragment);
                    txt_header_topic.setText(getResources().getString(R.string.notification));
                    img_btn_create_new_deal.setVisibility(View.INVISIBLE);
                    img_btn_new_question.setVisibility(View.GONE);
                    img_btn_new_article.setVisibility(View.GONE);
                    mNewAdsImageButton.setVisibility(View.GONE);
                    return true;
                } else if(id == R.id.question){
                    setFragment(qandAFragment);
                    txt_header_topic.setText("Q & A");
                    img_btn_create_new_deal.setVisibility(View.INVISIBLE);
                    img_btn_new_question.setVisibility(View.INVISIBLE);
                    img_btn_new_article.setVisibility(View.INVISIBLE);
                    mNewAdsImageButton.setVisibility(View.INVISIBLE);
                    return true;
                } else if(id == R.id.articles){
                    setFragment(articleFragment);
                    txt_header_topic.setText(getResources().getString(R.string.articles));
                    img_btn_create_new_deal.setVisibility(View.GONE);
                    img_btn_new_question.setVisibility(View.GONE);
                    if(mUserType.equals(FarmnetConstants.UserTypes.KNOWLEDGE_PROVIDER)){
                        img_btn_new_article.setVisibility(View.VISIBLE);
                    } else {
                        img_btn_new_article.setVisibility(View.INVISIBLE);
                    }
                    mNewAdsImageButton.setVisibility(View.GONE);
                    return true;
                } else if(id == R.id.advertisements){
                    setFragment(advertisementFragment);
                    txt_header_topic.setText(getResources().getString(R.string.ads));
                    if(mUserType.equals(FarmnetConstants.UserTypes.SERVICE_PROVIDER)){
                        mNewAdsImageButton.setVisibility(View.VISIBLE);
                    } else {
                        mNewAdsImageButton.setVisibility(View.INVISIBLE);
                    }
                    img_btn_create_new_deal.setVisibility(View.GONE);
                    img_btn_new_article.setVisibility(View.GONE);
                    img_btn_new_question.setVisibility(View.GONE);
                    return true;
                }
                return false;
            }
        });

        //navigation drawer item selecting
        drawer_navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.profile){
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                } else if(id == R.id.logout){
                    showLogoutAlert(ErrorMessageHelper.LOGOUT_CONFIRMATION);
                } else if(id == R.id.home){
                    startActivity(getIntent());
                } else  if(id == R.id.my_questions){
                    startActivity(new Intent(MainActivity.this, MyQuestionActivity.class));
                }
                return false;
            }
        });

    }

    public void showLogoutAlert(String message) {
        showAlertDialog("Logout", message, true, FarmnetConstants.LOGOUT, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                authPresenter.doLogout();
            }
        }, FarmnetConstants.CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    //check user is a new user or already logged user or logout user
    @Override
    public void setStarterScreen(String screenName) {
        if(screenName.equals(FarmnetConstants.CheckUserLogin.NEW_USER)){
            startActivity(new Intent(MainActivity.this, StartActivity.class));
            finish();
        } else if(screenName.equals(FarmnetConstants.CheckUserLogin.LOGOUT_USER)){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else if(screenName.equals(FarmnetConstants.CheckUserLogin.ALREADY_LOGGED_USER)){
            setMainActivityContent();
        }

    }

    @Override
    public void showMessage(String message) {
        /**
         * nothing to do with this function
         * need to remove
         * **/
    }

}
