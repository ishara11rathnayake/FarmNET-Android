package com.industrialmaster.farmnet.presenters;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.industrialmaster.farmnet.models.Advertisement;
import com.industrialmaster.farmnet.models.request.CreateNewAdvertisementRequest;
import com.industrialmaster.farmnet.models.response.AdvertisementsResponse;
import com.industrialmaster.farmnet.models.response.CreateNewAdsResponse;
import com.industrialmaster.farmnet.network.DisposableManager;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.AdvertisementView;
import com.industrialmaster.farmnet.views.CreateNewAdsView;
import com.industrialmaster.farmnet.views.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.support.constraint.Constraints.TAG;

public class AdvertisementsPresenterImpl extends BasePresenter implements AdvertisementPresenter {

    private AdvertisementView advertisementView;
    private CreateNewAdsView createNewAdsView;

    private String errorMessage;

    public AdvertisementsPresenterImpl(Activity activityContext, View view) {
        super(activityContext);

        if(view instanceof  AdvertisementView){
            advertisementView = (AdvertisementView) view;
        } else if(view instanceof CreateNewAdsView){
            createNewAdsView = (CreateNewAdsView) view;
        }
    }

    @Override
    public void getAllAdvertisements() {
        getAllAdvertisementsObservable().subscribe(getAllAdvertisementsSubscriber());
    }

    @Override
    public void createNewAdvertisement(CreateNewAdvertisementRequest createNewAdsRequest) {
        if(!validateFields(createNewAdsRequest)){
            createNewAdsView.onError(errorMessage);
        }else {
            RequestBody adsTitlePart = RequestBody.create(MultipartBody.FORM, createNewAdsRequest.getAdTitle());
            RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM, createNewAdsRequest.getAdDescription());
            RequestBody contactNumberPart = RequestBody.create(MultipartBody.FORM, createNewAdsRequest.getContactNumber());
            RequestBody pricePart = RequestBody.create(MultipartBody.FORM, createNewAdsRequest.getPrice().toString());

            List<MultipartBody.Part> tagListPart = new ArrayList<>();
            for(String tag : createNewAdsRequest.getTags()){
                tagListPart.add(MultipartBody.Part.createFormData("hashtags", tag));
            }

            File file = new File(createNewAdsRequest.getAdsImage());
            RequestBody adsImageReqBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part adsImagePart = MultipartBody.Part.createFormData("adsImage", file.getName(), adsImageReqBody);

            String accessToken = "Bearer " + readSharedPreferences(FarmnetConstants.TOKEN_PREFS_KEY, FarmnetConstants.CheckUserLogin.LOGOUT_USER);
            String userID = readSharedPreferences(FarmnetConstants.USER_ID, "");

            RequestBody userIdPart = RequestBody.create(MultipartBody.FORM, userID);

            createNewAdsObservable(accessToken, adsTitlePart, descriptionPart, contactNumberPart, pricePart,
                    tagListPart, userIdPart, adsImagePart).subscribe(createNewAdsSubscriber());
        }
    }

    private Observable<AdvertisementsResponse> getAllAdvertisementsObservable() {
        try {
            return getRetrofitClient().getAllAdvertisements()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private Observer<AdvertisementsResponse> getAllAdvertisementsSubscriber(){
        return new Observer<AdvertisementsResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(AdvertisementsResponse advertisementsResponse) {
                List<Advertisement> advertisements = advertisementsResponse.getAdvertisements();
                advertisementView.showAdvertisements(advertisements);
            }

            @Override
            public void onError(Throwable e) {
                try {
                    advertisementView.onError(handleApiError(e));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private Observable<CreateNewAdsResponse> createNewAdsObservable(String authorization, RequestBody adTitle, RequestBody adDescription,
                                                                    RequestBody contactNumber, RequestBody price, List<MultipartBody.Part> tags,
                                                                    RequestBody userId, MultipartBody.Part adsImage){
        try {
            return getRetrofitClient().createNewAdvertisement(authorization, adTitle, adDescription, contactNumber, price, tags, userId, adsImage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private Observer<CreateNewAdsResponse> createNewAdsSubscriber(){
        return new Observer<CreateNewAdsResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(CreateNewAdsResponse adsResponse) {
                String message = adsResponse.getMessage();
                createNewAdsView.onSuccess(message);
            }

            @Override
            public void onError(Throwable e) {
                try {
                    createNewAdsView.onError(handleApiError(e));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private boolean validateFields(CreateNewAdvertisementRequest newAdsRequest){
        if(TextUtils.isEmpty(newAdsRequest.getAdTitle()) || TextUtils.isEmpty(newAdsRequest.getAdDescription()) ||
                TextUtils.isEmpty(newAdsRequest.getContactNumber()) || !newAdsRequest.isHasImage()){
            errorMessage = ErrorMessageHelper.FILL_ADS_FIELDS;
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
