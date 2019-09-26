package com.industrialmaster.farmnet.presenters;

import com.industrialmaster.farmnet.models.User;

public interface ProfilePresenter extends Presenter {

    void getUserDetails();

    void updateUserDetails(User user);
}
