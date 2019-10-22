package com.industrialmaster.farmnet.presenters;

import com.industrialmaster.farmnet.models.request.ChangePasswordRequest;
import com.industrialmaster.farmnet.models.request.LoginRequest;
import com.industrialmaster.farmnet.models.request.SignUpRequest;

public interface AuthPresenter extends Presenter {

    /**
     * login function
     * @param loginRequest the login request
     */
    void doLogin(LoginRequest loginRequest);

    /**
     *  sign up function
     * @param signUpRequest the sign up request
     * @param retypePassword the retype password for confirm password
     */
    void doSignup(SignUpRequest signUpRequest, String retypePassword);

    /**
     * function for check already logged user or new user
     */
    void doCheckAlreadyLogin();

    /**
     * function for logout
     */
    void doLogout();

    /**
     *  function for change password
     * @param changePasswordRequest the change password request
     * @param confirmPassword the new password confirmation
     */
    void changePassword(ChangePasswordRequest changePasswordRequest, String confirmPassword);

    /**
     * search user by name, email or rating
     * @param searchText username or email to search
     * @param rating minimum user rating
     */
    void searchUser(String searchText, int rating);

}
