package com.industrialmaster.farmnet.presenters;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.industrialmaster.farmnet.models.Advertisement;
import com.industrialmaster.farmnet.models.Article;
import com.industrialmaster.farmnet.models.Deals;
import com.industrialmaster.farmnet.models.User;
import com.industrialmaster.farmnet.models.request.ComplaintRequest;
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
    private View mCommanView;
    private String userId = readSharedPreferences(FarmnetConstants.USER_ID, "");
    private String accessToken = "Bearer " + readSharedPreferences(FarmnetConstants.TOKEN_PREFS_KEY, FarmnetConstants.CheckUserLogin.LOGOUT_USER);


    public ProfilePresenterImpl(Activity activityContext, View view) {
        super(activityContext);
        if(view instanceof ProfileView){
            profileView = (ProfileView) view;
        }else if(view instanceof UpdateUserView){
            updateUserView = (UpdateUserView) view;
        }
        this.mCommanView = view;
    }

    @Override
    public void getUserDetails() {
        Objects.requireNonNull(getUserDetailsObservable(accessToken, userId)).subscribe(getUserDetailsSubscriber());
    }

    @Override
    public void updateUserDetails(User user) {
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



        Objects.requireNonNull(updateUserDetailsObservable(accessToken, userId,profileImagePart, addressPart,
                phonePart, nicPart, dobPart, namePart)).subscribe(updateUserDetailsSubscriber());

    }

    @Override
    public void getUserRating() {
        Objects.requireNonNull(getUserRatingObservable(accessToken, userId)).subscribe(getUserRatingSubscriber());
    }

    @Override
    public void getOtherUserRating(String userId) {
        Objects.requireNonNull(getUserRatingObservable(accessToken, userId)).subscribe(getUserRatingSubscriber());
    }

    @Override
    public void getRatedUserRating(String userId) {
        String ratedUserId = readSharedPreferences(FarmnetConstants.USER_ID, "");
        Objects.requireNonNull(getRatedUserRatingObservable(accessToken, userId, ratedUserId)).subscribe(getRatedUserRatingSubscriber());
    }

    @Override
    public void rateUser(String userId, float rating) {
        String ratedUserId = readSharedPreferences(FarmnetConstants.USER_ID, "");
        Objects.requireNonNull(rateUserObservable(accessToken, userId, ratedUserId, rating)).subscribe(rateUserSubscriber());
    }

    @Override
    public void reportUser(String userId, String content) {
        String reportedUserId = readSharedPreferences(FarmnetConstants.USER_ID, "");
        ComplaintRequest complaintRequest = new ComplaintRequest();
        complaintRequest.setComplainedUserId(reportedUserId);
        complaintRequest.setUserId(userId);
        complaintRequest.setContent(content);

        Objects.requireNonNull(reportUserObservable(accessToken, complaintRequest)).subscribe(reportUserSubscriber());
    }

    @Override
    public void getOtherUserDetails(String userId) {
        Objects.requireNonNull(getUserDetailsObservable(accessToken, userId)).subscribe(getUserDetailsSubscriber());

    }

    /**
     * get user details observable
     * @param accessToken access tokrn
     * @param userId userId
     * @return Observable<UserDetailsResponse>
     */
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

    /**
     * get user details subscriber
     * @return
     */
    private Observer<UserDetailsResponse> getUserDetailsSubscriber(){
        return new Observer<UserDetailsResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(UserDetailsResponse userDetailsResponse) {
                if(mCommanView instanceof ProfileView){
                    switch (userDetailsResponse.getUser().getUserType()) {
                        case FarmnetConstants.UserTypes.FARMER:
                            List<Deals> deals = userDetailsResponse.getDeals();
                            profileView.showUserDetails(userDetailsResponse.getUser(), deals);
                            break;
                        case FarmnetConstants.UserTypes.SERVICE_PROVIDER:
                            List<Advertisement> advertisements = userDetailsResponse.getAdvertisements();
                            profileView.showUserDetails(userDetailsResponse.getUser(), advertisements);
                            break;
                        case FarmnetConstants.UserTypes.KNOWLEDGE_PROVIDER:
                            List<Article> articles = userDetailsResponse.getArticles();
                            profileView.showUserDetails(userDetailsResponse.getUser(), articles);
                            break;
                        case FarmnetConstants.UserTypes.BUYER:
                            profileView.showUserDetails(userDetailsResponse.getUser(), null);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + userDetailsResponse.getUser().getUserType());
                    }

                } else if(mCommanView instanceof UpdateUserView){
                    updateUserView.showUserDetails(userDetailsResponse.getUser());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                try {
                    if(mCommanView instanceof ProfileView) {
                        profileView.onError(handleApiError(e));
                    } else if(mCommanView instanceof UpdateUserView){
                        updateUserView.onError(handleApiError(e));
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

    /**
     * update user details observable
     * @param accessToken access token - String
     * @param userId user id - String
     * @param profileImage profile Image - MultipartBody.Part
     * @param address address - RequestBody
     * @param phone contact number - RequestBody
     * @param nic NIC - RequestBody
     * @param dob date of birth - RequestBody
     * @param name name of user - RequestBody
     * @return Observable<CommonMessageResponse>
     */
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
                saveSharedPreferences(FarmnetConstants.USERNAME, commonMessageResponse.getName());
                saveSharedPreferences(FarmnetConstants.USER_EMAIL, commonMessageResponse.getEmail());
                saveSharedPreferences(FarmnetConstants.PROFILE_PIC, commonMessageResponse.getProfileImage());
                updateUserView.onSuccess(commonMessageResponse.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                try {
                    updateUserView.onError(handleApiError(e));
                } catch (Exception ex) {
                    Log.e(TAG, ex.toString());
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
                Log.e(TAG, e.toString());
                try {
                    profileView.onError(handleApiError(e));
                } catch (Exception ex) {
                    Log.e(TAG, ex.toString());
                }
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private Observable<UserRatingResponse> getRatedUserRatingObservable(String accessToken, String userId, String ratedUserId) {
        try {
            return getRetrofitClient().getRatedUserRating(accessToken, userId, ratedUserId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private Observer<UserRatingResponse> getRatedUserRatingSubscriber(){
        return new Observer<UserRatingResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(UserRatingResponse userRatingResponse) {
                profileView.showRatingInRatePopup(userRatingResponse.getRatingScore());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                try {
                    profileView.onError(handleApiError(e));
                } catch (Exception ex) {
                    Log.e(TAG, ex.toString());
                }
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private Observable<CommonMessageResponse> rateUserObservable(String accessToken, String userId, String ratedUserId, float rating) {
        try {
            return getRetrofitClient().rateUser(accessToken, userId, ratedUserId, rating)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private Observer<CommonMessageResponse> rateUserSubscriber(){
        return new Observer<CommonMessageResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(CommonMessageResponse commonMessageResponse) {
                profileView.showMessage(commonMessageResponse.getMessage());
                profileView.showUserrating(commonMessageResponse.getRate());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                try {
                    profileView.onError(handleApiError(e));
                } catch (Exception ex) {
                    Log.e(TAG, ex.toString());
                }
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private Observable<CommonMessageResponse> reportUserObservable(String accessToken, ComplaintRequest complaintRequest) {
        try {
            return getRetrofitClient().reportUser(accessToken, complaintRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private Observer<CommonMessageResponse> reportUserSubscriber(){
        return new Observer<CommonMessageResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(CommonMessageResponse commonMessageResponse) {
                profileView.showMessage(commonMessageResponse.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                try {
                    profileView.onError(handleApiError(e));
                } catch (Exception ex) {
                    Log.e(TAG, ex.toString());
                }
            }

            @Override
            public void onComplete() {

            }
        };
    }
}
