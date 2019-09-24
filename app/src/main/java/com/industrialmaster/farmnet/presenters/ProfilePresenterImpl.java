package com.industrialmaster.farmnet.presenters;

import android.app.Activity;

import com.industrialmaster.farmnet.views.ProfileView;
import com.industrialmaster.farmnet.views.View;

public class ProfilePresenterImpl extends BasePresenter implements ProfilePresenter {

    ProfileView profileView;

    public ProfilePresenterImpl(Activity activityContext, View view) {
        super(activityContext);
        if(view instanceof ProfileView){
            profileView = (ProfileView) view;
        }
    }

    @Override
    public void getUserDetails() {

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }
}
