package com.industrialmaster.farmnet.presenters;

import com.industrialmaster.farmnet.models.User;

public interface ProfilePresenter extends Presenter {

    void getUserDetails();

    void updateUserDetails(User user);

    void getOtherUserDetails(String userId);

    void getUserRating();

    void getOtherUserRating(String userId);

    void getRatedUserRating(String userId);

    void rateUser(String userId, float rating);

    void reportUser(String userId, String content);
}
