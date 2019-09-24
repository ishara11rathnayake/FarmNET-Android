package com.industrialmaster.farmnet.presenters;

import android.app.Activity;
import android.util.Log;

import com.industrialmaster.farmnet.models.response.UserDetailsResponse;
import com.industrialmaster.farmnet.network.DisposableManager;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.ProfileView;
import com.industrialmaster.farmnet.views.View;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.support.constraint.Constraints.TAG;

public class ProfilePresenterImpl extends BasePresenter implements ProfilePresenter {

    private ProfileView profileView;

    public ProfilePresenterImpl(Activity activityContext, View view) {
        super(activityContext);
        if(view instanceof ProfileView){
            profileView = (ProfileView) view;
        }
    }

    @Override
    public void getUserDetails() {

        String userId = readSharedPreferences(FarmnetConstants.USER_ID, "");

        String accessToken = "Bearer " + readSharedPreferences(FarmnetConstants.TOKEN_PREFS_KEY, FarmnetConstants.CheckUserLogin.LOGOUT_USER);
        Objects.requireNonNull(getUserDetailsObservable(accessToken, userId)).subscribe(getUserDetailsSubscriber());
    }

    private Observable<UserDetailsResponse> getUserDetailsObservable(String accessToken, String userId) {
        try {
            return getRetrofitClient().getUserDetails(accessToken, userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private Observer<UserDetailsResponse> getUserDetailsSubscriber(){
        return new Observer<UserDetailsResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(UserDetailsResponse userDetailsResponse) {
                profileView.showUserDetails(userDetailsResponse.getUser());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                try {
                    profileView.onError(handleApiError(e));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onComplete() {

            }
        };
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
