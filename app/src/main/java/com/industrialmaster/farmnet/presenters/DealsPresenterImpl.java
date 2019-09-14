package com.industrialmaster.farmnet.presenters;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.industrialmaster.farmnet.models.Deals;
import com.industrialmaster.farmnet.models.request.CreateNewDealRequest;
import com.industrialmaster.farmnet.models.response.CreateNewDealResponse;
import com.industrialmaster.farmnet.models.response.ProductDealResponse;
import com.industrialmaster.farmnet.network.DisposableManager;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.CreateNewDealView;
import com.industrialmaster.farmnet.views.DealsView;
import com.industrialmaster.farmnet.views.View;

import java.io.File;
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

public class DealsPresenterImpl extends BasePresenter implements DealsPresenter {

    DealsView dealsView;
    CreateNewDealView createNewDealView;

    String errorMessage;

    public DealsPresenterImpl(Activity activityContext, View view) {
        super(activityContext);
        if(view instanceof DealsView) {
            dealsView = (DealsView) view;
        } else if(view instanceof CreateNewDealView){
            createNewDealView = (CreateNewDealView) view;
        }
    }

    @Override
    public void getAllDeals() {
        getAllDealsObservable().subscribe(getAllDealsSubscriber());
    }

    @Override
    public void createNewDeal(CreateNewDealRequest createNewDealRequest) {

        boolean isValidate = createDealFielsValidate(createNewDealRequest);

        if(!isValidate) {
            createNewDealView.onError(errorMessage);
        } else {
            RequestBody productNamePart = RequestBody.create(MultipartBody.FORM, createNewDealRequest.getProductName());
            RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM, createNewDealRequest.getDescription());
            RequestBody locationPart = RequestBody.create(MultipartBody.FORM, createNewDealRequest.getLocation());
            RequestBody unitPricePart = RequestBody.create(MultipartBody.FORM, createNewDealRequest.getUnitPrice());
            RequestBody amountPart = RequestBody.create(MultipartBody.FORM, createNewDealRequest.getAmount());

            File file = new File(createNewDealRequest.getProductImage());
            RequestBody productImageReqBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part productImagePart = MultipartBody.Part.createFormData("productImage", file.getName(), productImageReqBody);

            String accessToken = "Bearer " + readSharedPreferences(FarmnetConstants.TOKEN_PREFS_KEY, FarmnetConstants.CheckUserLogin.LOGOUT_USER);
            String userID = readSharedPreferences(FarmnetConstants.USER_ID, "");

            RequestBody userIdPart = RequestBody.create(MultipartBody.FORM, userID);

            createNewDealObservable(accessToken, productNamePart, descriptionPart, locationPart,
                    unitPricePart, amountPart, productImagePart, userIdPart).subscribe(createNewDealSubscriber());
        }
    }

    public Observable<CreateNewDealResponse> createNewDealObservable(String accessToken, RequestBody productName, RequestBody descrption,
                                                                     RequestBody location, RequestBody unitPrice, RequestBody amount,
                                                                     MultipartBody.Part productImage, RequestBody userId) {
        try {
            return getRetrofitClient().createNewPost(accessToken,
                    productName, unitPrice, amount, descrption, userId, productImage, location)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Observer<CreateNewDealResponse> createNewDealSubscriber(){
        return new Observer<CreateNewDealResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(CreateNewDealResponse createNewDealResponse) {
                createNewDealView.onSuccess(createNewDealResponse.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                try {
                    createNewDealView.onError(e.toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private boolean createDealFielsValidate(CreateNewDealRequest createNewDealRequest) {

        String productName = createNewDealRequest.getProductName();
        String productPrice = createNewDealRequest.getUnitPrice();
        String productAmount = createNewDealRequest.getAmount();
        String description = createNewDealRequest.getDescription();
        String location = createNewDealRequest.getLocation();
        boolean hasImage = createNewDealRequest.isHasImage();

        if(TextUtils.isEmpty(productName) || TextUtils.isEmpty(description) ||
                TextUtils.isEmpty(location) || TextUtils.isEmpty(productAmount)||
                TextUtils.isEmpty(productPrice) || hasImage == false) {
            errorMessage = ErrorMessageHelper.FILL_ALL_THE_FIELDS;
            return false;
        }
        return true;
    }

    public Observable<ProductDealResponse> getAllDealsObservable() {
        try {
            return getRetrofitClient().getAllDeals()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Observer<ProductDealResponse> getAllDealsSubscriber(){
        return new Observer<ProductDealResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(ProductDealResponse productDealResponses) {
                List<Deals> productDeals = productDealResponses.getProducts();
                dealsView.showDeals(productDeals);
            }

            @Override
            public void onError(Throwable e) {
                try {
                    dealsView.onError(handleApiError(e));
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
