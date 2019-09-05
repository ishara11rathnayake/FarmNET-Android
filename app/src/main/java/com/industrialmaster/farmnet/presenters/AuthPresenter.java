package com.industrialmaster.farmnet.presenters;

import com.industrialmaster.farmnet.models.request.LoginRequest;

public interface AuthPresenter {

    void doLogin(LoginRequest loginRequest);

}
