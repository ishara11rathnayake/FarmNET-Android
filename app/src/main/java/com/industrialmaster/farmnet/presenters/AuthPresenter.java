package com.industrialmaster.farmnet.presenters;

import com.industrialmaster.farmnet.models.request.LoginRequest;
import com.industrialmaster.farmnet.models.request.SignUpRequest;

public interface AuthPresenter extends Presenter {

    void doLogin(LoginRequest loginRequest);

    void doSignup(SignUpRequest signUpRequest, String retypePassword);

    void doCheckAlreadyLogin();

    void doLogout();

}
