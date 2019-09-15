package com.industrialmaster.farmnet.presenters;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.industrialmaster.farmnet.models.request.LoginRequest;
import com.industrialmaster.farmnet.models.request.SignUpRequest;
import com.industrialmaster.farmnet.models.response.LoginResponse;
import com.industrialmaster.farmnet.models.response.SignUpResponse;
import com.industrialmaster.farmnet.network.DisposableManager;
import com.industrialmaster.farmnet.network.RetrofitException;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.AuthView;
import com.industrialmaster.farmnet.views.FarmnetHomeView;
import com.industrialmaster.farmnet.views.View;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import static android.support.constraint.Constraints.TAG;

public class AuthPresenterImpl extends BasePresenter implements AuthPresenter {

    AuthView authView;
    FarmnetHomeView homeView;

    String errorMessage;

    public AuthPresenterImpl(Activity activityContext, View view) {
        super(activityContext);
        if(view instanceof AuthView) {
            authView = (AuthView) view;
        } else if(view instanceof FarmnetHomeView){
            homeView = (FarmnetHomeView) view;
        }
    }

    @Override
    public void doLogin(LoginRequest loginRequest) {

        boolean isValidate = loginFieldsValidate(loginRequest.getEmail(), loginRequest.getPassword());

        if(isValidate == false) {
            authView.onError(errorMessage);
        } else {
            doLoginObservable(loginRequest).subscribe(doLoginSubscriber());
        }

    }

    @Override
    public void doSignup(SignUpRequest signUpRequest, String retypePassword) {

        boolean isValidate = signUpFieldsValidate(signUpRequest.getEmail(), signUpRequest.getName(), signUpRequest.getPassword(),
                signUpRequest.getUser_type(), retypePassword);

        if(isValidate == false){
            authView.onError(errorMessage);
        } else {
            doSignUpObservable(signUpRequest).subscribe(doSignUpSubscriber());
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

    public Observable<LoginResponse> doLoginObservable(LoginRequest loginRequest) {
        try {
            return getRetrofitClient().doLogin(loginRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Observer<LoginResponse> doLoginSubscriber(){
        return new Observer<LoginResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(LoginResponse loginResponse) {
                saveSharedPreferences(FarmnetConstants.TOKEN_PREFS_KEY, loginResponse.getAccess_token());
                saveSharedPreferences(FarmnetConstants.USER_ID, loginResponse.getUserId());
                authView.onSuccess(loginResponse.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                try {
                    authView.onError(handleApiError(e));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void onComplete() {

            }
        };
    }

    public Observable<SignUpResponse> doSignUpObservable(SignUpRequest signUpRequest) {
        try {
            return getRetrofitClient().doSignup(signUpRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());


        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Observer<SignUpResponse> doSignUpSubscriber(){
        return new Observer<SignUpResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(SignUpResponse signUpResponse) {
                saveSharedPreferences(FarmnetConstants.TOKEN_PREFS_KEY, signUpResponse.getAccess_token());
                saveSharedPreferences(FarmnetConstants.USER_ID, signUpResponse.getUserId());
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
                    ex.printStackTrace();
                }

            }

            @Override
            public void onComplete() {

            }
        };
    }


    private boolean loginFieldsValidate(String email, String password){
        final String regex_email = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            errorMessage = ErrorMessageHelper.ENTER_EMAIL_AND_PASSWORD;
            return false;
        }
        else if(!email.matches(regex_email)){
            errorMessage = ErrorMessageHelper.INVALID_EMAIL;
            return false;
        }

        return true;

    }

    private boolean signUpFieldsValidate(String email, String name, String password, String userType, String retypePassword){
        final String regex_email = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final String regex_password = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(name) || TextUtils.isEmpty(password) ||
                userType.equals("Select user type") || TextUtils.isEmpty(retypePassword)){
            errorMessage = ErrorMessageHelper.FILL_ALL_THE_FIELDS;
            return false;
        } else if(!email.matches(regex_email)){
            errorMessage = ErrorMessageHelper.INVALID_EMAIL;
            return false;
        } else if (!password.matches(regex_password)) {
            errorMessage = ErrorMessageHelper.INVALID_PASSWORD;
            return false;
        } else if(!password.equals(retypePassword)){
            errorMessage = ErrorMessageHelper.PASSWORD_CONFIRMATION;
            return false;
        }

        return true;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }
}
