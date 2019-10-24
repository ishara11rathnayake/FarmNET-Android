package com.industrialmaster.farmnet.views;

import com.industrialmaster.farmnet.models.Deals;
import com.industrialmaster.farmnet.models.User;

import java.util.List;

public interface ProfileView extends View {

    <T> void showUserDetails(User user, List<T> products);

    void onError(String message);

    void showUserrating(float rating);

    void showRatingInRatePopup(float rating);
}
