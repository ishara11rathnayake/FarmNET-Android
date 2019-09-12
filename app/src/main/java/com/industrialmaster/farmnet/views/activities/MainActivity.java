package com.industrialmaster.farmnet.views.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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
import com.industrialmaster.farmnet.views.AuthView;
import com.industrialmaster.farmnet.views.FarmnetHomeView;
import com.industrialmaster.farmnet.views.fragments.DealsFragment;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.views.fragments.QandAFragment;

public class MainActivity extends BaseActivity implements FarmnetHomeView {

    DealsFragment dealsFragment;
    QandAFragment qandAFragment;

    AuthPresenter presenter = new AuthPresenterImpl(this, MainActivity.this );

    private DrawerLayout mDrawerlayout;
    private ImageView imgv_drawer_toggle;
    private TextView txt_header_topic;

    private ImageButton img_btn_create_new_deal;

    BottomNavigationView bottom_navigation_view;
    ImageView imgv_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter.doCheckAlreadyLogin();

        imgv_logout = findViewById(R.id.imgvlogout);
        txt_header_topic = findViewById(R.id.txtheadertopic);

        img_btn_create_new_deal = findViewById(R.id.img_btn_new_deal);

        img_btn_create_new_deal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateNewDealActivity.class));
            }
        });

        imgv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutAlert(ErrorMessageHelper.LOGOUT_CONFIRMATION);
            }
        });

    }

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

    public void setMainActivityContent() {

        setContentView(R.layout.content_main);

        mDrawerlayout = findViewById(R.id.drawernavigation);
        imgv_drawer_toggle = findViewById(R.id.imgvdrawertoggle);

        bottom_navigation_view = findViewById(R.id.bottom_nav);
        dealsFragment = new DealsFragment();
        qandAFragment = new QandAFragment();

        imgv_drawer_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerlayout.openDrawer(Gravity.LEFT);
            }
        });

        bottom_navigation_view.setSelectedItemId(R.id.deals);
        setFragment(dealsFragment);

        bottom_navigation_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.deals){
                    setFragment(dealsFragment);
                    txt_header_topic.setText("Crop Deals");
                    return true;
                } else if(id == R.id.notification){
                    txt_header_topic.setText("Notification");
                    return true;
                } else if(id == R.id.question){
                    setFragment(qandAFragment);
                    txt_header_topic.setText("Q & A");
                    return true;
                } else if(id == R.id.articles){
                    txt_header_topic.setText("Articles");
                    return true;
                } else if(id == R.id.advertisements){
                    txt_header_topic.setText("Ads");
                    return true;
                }
                return false;
            }
        });

    }

    public void showLogoutAlert(String message) {
        showAlertDialog("Logout", message, true, FarmnetConstants.LOGOUT, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.doLogout();
            }
        }, FarmnetConstants.CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

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

    }

    @Override
    public void showErrorMessage(String calledMethod, String error, String errorDescription) {

    }
}
