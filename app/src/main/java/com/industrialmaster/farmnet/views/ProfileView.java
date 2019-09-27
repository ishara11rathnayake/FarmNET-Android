package com.industrialmaster.farmnet.views;

import com.industrialmaster.farmnet.models.Deals;
import com.industrialmaster.farmnet.models.User;

import java.util.List;

public interface ProfileView extends View {

    void showUserDetails(User user, List<Deals> deals);

    void onError(String message);

    void showUserrating(float rating);
}
