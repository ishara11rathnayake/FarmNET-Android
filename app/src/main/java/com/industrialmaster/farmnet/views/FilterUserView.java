package com.industrialmaster.farmnet.views;

import com.industrialmaster.farmnet.models.User;

import java.util.List;

public interface FilterUserView extends View {

    void showUserList(List<User> userList);

    void onError(String error);

    void onSuccess(String message);

}
