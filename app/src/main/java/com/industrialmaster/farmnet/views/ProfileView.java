package com.industrialmaster.farmnet.views;

import com.industrialmaster.farmnet.models.response.UserDetailsResponse;

public interface ProfileView extends View {

    void showUserDetails(UserDetailsResponse userDetailsResponse);
}
