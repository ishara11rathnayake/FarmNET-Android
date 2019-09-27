package com.industrialmaster.farmnet.presenters;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.industrialmaster.farmnet.models.Deals;
import com.industrialmaster.farmnet.models.User;
import com.industrialmaster.farmnet.models.response.CommonMessageResponse;
import com.industrialmaster.farmnet.models.response.UserDetailsResponse;
import com.industrialmaster.farmnet.models.response.UserRatingResponse;
import com.industrialmaster.farmnet.network.DisposableManager;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.ProfileView;
import com.industrialmaster.farmnet.views.UpdateUserView;
import com.industrialmaster.farmnet.views.View;

import java.io.File;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.support.constraint.Constraints.TAG;

public class ProfilePresenterImpl extends BasePresenter implements ProfilePresenter {

    private ProfileView profileView;
    private UpdateUserView updateUserView;
    View mView;

    public ProfilePresenterImpl(Activity activityContext, View view) {
        super(activityContext);
        if(view instanceof ProfileView){
            profileView = (ProfileView) view;
        }else if(view instanceof UpdateUserView){
            updateUserView = (UpdateUserView) view;
        }
        this.mView = view;
    }

    @Override
    public void getUserDetails() {

        String userId = readSharedPreferences(FarmnetConstants.USER_ID, "");
        String accessToken = "Bearer " + readSharedPreferences(FarmnetConstants.TOKEN_PREFS_KEY, FarmnetConstants.CheckUserLogin.LOGOUT_USER);

        Objects.requireNonNull(getUserDetailsObservable(accessToken, userId)).subscribe(getUserDetailsSubscriber());
    }

    @Override
    public void updateUserDetails(User user) {
        String userId = readSharedPreferences(FarmnetConstants.USER_ID, "");
        String accessToken = "Bearer " + readSharedPreferences(FarmnetConstants.TOKEN_PREFS_KEY, FarmnetConstants.CheckUserLogin.LOGOUT_USER);

        RequestBody namePart = RequestBody.create(MultipartBody.FORM, user.getName());
        RequestBody addressPart = RequestBody.create(MultipartBody.FORM, user.getAddress());
        RequestBody phonePart = RequestBody.create(MultipartBody.FORM, user.getContactNumber());
        RequestBody nicPart = RequestBody.create(MultipartBody.FORM, user.getNic());

        RequestBody dobPart = null;
        if(user.getDob() != null){
            dobPart  = RequestBody.create(MultipartBody.FORM, user.getDob().toString());
        }

        MultipartBody.Part profileImagePart = null;
        if(!TextUtils.isEmpty(user.getProfilePicUrl())){
            File file = new File(user.getProfilePicUrl());
            RequestBody profileImageReqBody = RequestBody.create(MediaType.parse("image/*"), file);
            profileImagePart = MultipartBody.Part.createFormData("profileImage", file.getName(), profileImageReqBody);
        }



        updateUserDetailsObservable(accessToken, userId,profileImagePart, addressPart,
                phonePart, nicPart, dobPart, namePart).subscribe(updateUserDetailsSubscriber());

    }

    @Override
    public void getUserRating() {
        String userId = readSharedPreferences(FarmnetConstants.USER_ID, "");
        String accessToken = "Bearer " + readSharedPreferences(FarmnetConstants.TOKEN_PREFS_KEY, FarmnetConstants.CheckUserLogin.LOGOUT_USER);
        getUserRatingObservable(accessToken, userId).subscribe(getUserRatingSubscriber());
    }

    @Override
    public void getOtherUserDetails(String userId) {
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
                if(mView instanceof ProfileView){
                    List<Deals> deals = userDetailsResponse.getDeals();
                    profileView.showUserDetails(userDetailsResponse.getUser(), deals);
                } else if(mView instanceof UpdateUserView){
                    updateUserView.showUserDetails(userDetailsResponse.getUser());
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                try {
                    if(mView instanceof ProfileView) {
                        profileView.onError(handleApiError(e));
                    } else if(mView instanceof UpdateUserView){
                        updateUserView.onError(handleApiError(e));
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

    private Observable<CommonMessageResponse> updateUserDetailsObservable(String accessToken, String userId, MultipartBody.Part profileImage,
                                                                          RequestBody address, RequestBody phone, RequestBody nic,
                                                                          RequestBody dob, RequestBody name) {
        try {
            return getRetrofitClient().updateUserDetails(accessToken, userId, profileImage, address, phone, nic, dob, name)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private Observer<CommonMessageResponse> updateUserDetailsSubscriber(){
        return new Observer<CommonMessageResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(CommonMessageResponse commonMessageResponse) {
                updateUserView.onSuccess(commonMessageResponse.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                try {
                    updateUserView.onError(handleApiError(e));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private Observable<UserRatingResponse> getUserRatingObservable(String accessToken, String userId) {
        try {
            return getRetrofitClient().getUserRating(accessToken, userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private Observer<UserRatingResponse> getUserRatingSubscriber(){
        return new Observer<UserRatingResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(UserRatingResponse userRatingResponse) {
                profileView.showUserrating(userRatingResponse.getRatingScore());
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
