package com.industrialmaster.farmnet.presenters;

import android.text.TextUtils;
import android.util.Log;

import com.industrialmaster.farmnet.models.request.LoginRequest;
import com.industrialmaster.farmnet.models.response.LoginResponse;
import com.industrialmaster.farmnet.network.DisposableManager;
import com.industrialmaster.farmnet.network.RetrofitException;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.views.AuthView;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import static android.support.constraint.Constraints.TAG;

public class AuthPresenterImpl extends BasePresenter implements AuthPresenter {

    AuthView view;
    String errorMessage;

    public AuthPresenterImpl(AuthView view) {
        super();
        this.view = view;
    }

    @Override
    public void doLogin(LoginRequest loginRequest) {

        boolean isValidate = loginFieldsValidate(loginRequest.getEmail(), loginRequest.getPassword());

        if(isValidate == false) {
            view.onError(errorMessage);
        } else {
            doLoginObservable(loginRequest).subscribe(doLoginSubscriber());
        }

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
                view.onSuccess(loginResponse.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                try {
                    view.onError(handleApiError(e));
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
}
