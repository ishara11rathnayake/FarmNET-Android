package com.industrialmaster.farmnet.views;

import com.industrialmaster.farmnet.models.User;

public interface ProfileView extends View {

    void showUserDetails(User user);

    void onError(String message);
}
