package com.industrialmaster.farmnet.presenters;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.industrialmaster.farmnet.models.request.ChangePasswordRequest;
import com.industrialmaster.farmnet.models.request.LoginRequest;
import com.industrialmaster.farmnet.models.request.SignUpRequest;
import com.industrialmaster.farmnet.models.response.CommonMessageResponse;
import com.industrialmaster.farmnet.models.response.LoginResponse;
import com.industrialmaster.farmnet.models.response.SignUpResponse;
import com.industrialmaster.farmnet.network.DisposableManager;
import com.industrialmaster.farmnet.network.RetrofitException;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.AuthView;
import com.industrialmaster.farmnet.views.ChangePasswordView;
import com.industrialmaster.farmnet.views.FarmnetHomeView;
import com.industrialmaster.farmnet.views.View;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class AuthPresenterImpl extends BasePresenter implements AuthPresenter {

    private static final String TAG = AuthPresenterImpl.class.getSimpleName();

    private static final String REGEX_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String REGEX_PASSWRD = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";

    private String accessToken = "Bearer " + readSharedPreferences(FarmnetConstants.TOKEN_PREFS_KEY, FarmnetConstants.CheckUserLogin.LOGOUT_USER);
    private String userID = readSharedPreferences(FarmnetConstants.USER_ID, "");

    private AuthView authView;
    private FarmnetHomeView homeView;
    private ChangePasswordView changePasswordView;

    private String errorMessage;

    public AuthPresenterImpl(Activity activityContext, View view) {
        super(activityContext);
        if(view instanceof AuthView) {
            authView = (AuthView) view;
        } else if(view instanceof FarmnetHomeView){
            homeView = (FarmnetHomeView) view;
        } else if( view instanceof  ChangePasswordView){
            changePasswordView = (ChangePasswordView) view;
        }
    }

    @Override
    public void doLogin(LoginRequest loginRequest) {

        boolean isValidate = loginFieldsValidate(loginRequest.getEmail(), loginRequest.getPassword());

        if(!isValidate) {
            authView.onError(errorMessage);
        } else {
            Objects.requireNonNull(doLoginObservable(loginRequest)).subscribe(doLoginSubscriber());
        }

    }

    @Override
    public void doSignup(SignUpRequest signUpRequest, String retypePassword) {

        boolean isValidate = signUpFieldsValidate(signUpRequest.getEmail(), signUpRequest.getName(), signUpRequest.getPassword(),
                signUpRequest.getUser_type(), retypePassword);

        if(!isValidate){
            authView.onError(errorMessage);
        } else {
            Objects.requireNonNull(doSignUpObservable(signUpRequest)).subscribe(doSignUpSubscriber());
        }

    }

    @Override
    public void doCheckAlreadyLogin() {
        String token = readSharedPreferences(FarmnetConstants.TOKEN_PREFS_KEY, FarmnetConstants.CheckUserLogin.NEW_USER);
        if(token.equals(FarmnetConstants.CheckUserLogin.NEW_USER)){
            homeView.setStarterScreen(FarmnetConstants.CheckUserLogin.NEW_USER);
        } else if(token.equals(FarmnetConstants.CheckUserLogin.LOGOUT_USER)){
            homeView.setStarterScreen(FarmnetConstants.CheckUserLogin.LOGOUT_USER);
        } else {
            homeView.setStarterScreen(FarmnetConstants.CheckUserLogin.ALREADY_LOGGED_USER);
        }
    }

    @Override
    public void doLogout() {
        saveSharedPreferences(FarmnetConstants.TOKEN_PREFS_KEY, FarmnetConstants.CheckUserLogin.LOGOUT_USER);
        homeView.setStarterScreen(FarmnetConstants.CheckUserLogin.LOGOUT_USER);
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest, String confirmPassword) {
        boolean isValidate = validatePassword(changePasswordRequest.getNewPassword(), confirmPassword);

        if(!isValidate){
            changePasswordView.onError(errorMessage);
        } else {
            Objects.requireNonNull(changePasswordObservable(changePasswordRequest)).subscribe(changePasswordSubscriber());
        }
    }

    private Observable<LoginResponse> doLoginObservable(LoginRequest loginRequest) {
        try {
            return getRetrofitClient().doLogin(loginRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private Observer<LoginResponse> doLoginSubscriber(){
        return new Observer<LoginResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(LoginResponse loginResponse) {
                saveSharedPreferences(FarmnetConstants.TOKEN_PREFS_KEY, loginResponse.getAccess_token());
                saveSharedPreferences(FarmnetConstants.USER_ID, loginResponse.getUserId());
                saveSharedPreferences(FarmnetConstants.USER_TYPE, loginResponse.getUserType());
                authView.onSuccess(loginResponse.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                try {
                    authView.onError(handleApiError(e));
                } catch (Exception ex) {
                    Log.e(TAG, ex.toString());
                }

            }

            @Override
            public void onComplete() {

            }
        };
    }

    private Observable<SignUpResponse> doSignUpObservable(SignUpRequest signUpRequest) {
        try {
            return getRetrofitClient().doSignup(signUpRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());


        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private Observer<SignUpResponse> doSignUpSubscriber(){
        return new Observer<SignUpResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(SignUpResponse signUpResponse) {
                saveSharedPreferences(FarmnetConstants.TOKEN_PREFS_KEY, signUpResponse.getAccess_token());
                saveSharedPreferences(FarmnetConstants.USER_ID, signUpResponse.getUserId());
                saveSharedPreferences(FarmnetConstants.USER_TYPE, signUpResponse.getUserType());
                authView.onSuccess(signUpResponse.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                try {
                    if(((HttpException) e).code() == RetrofitException.CONFLICT){
                        authView.onError("Email already used");
                    } else {
                        authView.onError(handleApiError(e));
                    }
                } catch (Exception ex) {
                    Log.e(TAG, ex.toString());
                }
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private Observable<CommonMessageResponse> changePasswordObservable(ChangePasswordRequest changePasswordRequest) {
        try {
            return getRetrofitClient().changePassword(accessToken, userID, changePasswordRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());


        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private Observer<CommonMessageResponse> changePasswordSubscriber(){
        return new Observer<CommonMessageResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(CommonMessageResponse commonMessageResponse) {
                if(commonMessageResponse.getStatusCode().equals("200")){
                    changePasswordView.onSuccess(commonMessageResponse.getMessage());
                } else if(commonMessageResponse.getStatusCode().equals("409")){
                    changePasswordView.onError(commonMessageResponse.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                try {
                    changePasswordView.onError(handleApiError(e));
                } catch (Exception ex) {
                    Log.e(TAG, ex.toString());
                }
            }

            @Override
            public void onComplete() {

            }
        };
    }


    private boolean loginFieldsValidate(String email, String password){
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            errorMessage = ErrorMessageHelper.ENTER_EMAIL_AND_PASSWORD;
            return false;
        } else if(!email.matches(REGEX_EMAIL)){
            errorMessage = ErrorMessageHelper.INVALID_EMAIL;
            return false;
        }

        return true;

    }

    private boolean signUpFieldsValidate(String email, String name, String password, String userType, String retypePassword){

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(name) || TextUtils.isEmpty(password) ||
                userType.equals("Select user type") || TextUtils.isEmpty(retypePassword)){
            errorMessage = ErrorMessageHelper.FILL_ALL_THE_FIELDS;
            return false;
        } else if(!email.matches(REGEX_EMAIL)){
            errorMessage = ErrorMessageHelper.INVALID_EMAIL;
            return false;
        } else return validatePassword(password, retypePassword);

    }

    private boolean validatePassword (String password, String confirmPassword) {

        if (!password.matches(REGEX_PASSWRD)) {
            errorMessage = ErrorMessageHelper.INVALID_PASSWORD;
            return false;
        } else if(!password.equals(confirmPassword)){
            errorMessage = ErrorMessageHelper.PASSWORD_CONFIRMATION;
            return false;
        }

        return true;
    }



}
