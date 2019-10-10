package com.industrialmaster.farmnet.presenters;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.industrialmaster.farmnet.models.Deals;
import com.industrialmaster.farmnet.models.User;
import com.industrialmaster.farmnet.models.request.CreateNewDealRequest;
import com.industrialmaster.farmnet.models.response.CommonMessageResponse;
import com.industrialmaster.farmnet.models.response.CreateNewDealResponse;
import com.industrialmaster.farmnet.models.response.ProductDealResponse;
import com.industrialmaster.farmnet.network.DisposableManager;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.CreateNewDealView;
import com.industrialmaster.farmnet.views.DealsView;
import com.industrialmaster.farmnet.views.DisplayProductView;
import com.industrialmaster.farmnet.views.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import lombok.Setter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.support.constraint.Constraints.TAG;

public class DealsPresenterImpl extends BasePresenter implements DealsPresenter {

    private static final  String TAG = "DealsPresenterImpl";

    private DealsView dealsView;
    private CreateNewDealView createNewDealView;
    private DisplayProductView displayProductView;

    private DatabaseReference dealsRef;

    private String errorMessage;

    private String accessToken = "Bearer " + readSharedPreferences(FarmnetConstants.TOKEN_PREFS_KEY, FarmnetConstants.CheckUserLogin.LOGOUT_USER);
    private String userID = readSharedPreferences(FarmnetConstants.USER_ID, "");

    public DealsPresenterImpl(Activity activityContext, View view) {
        super(activityContext);
        if(view instanceof DealsView) {
            dealsView = (DealsView) view;
        } else if(view instanceof CreateNewDealView){
            createNewDealView = (CreateNewDealView) view;
        } else if(view instanceof  DisplayProductView){
            displayProductView = (DisplayProductView) view;
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dealsRef = database.getReference("deals");
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

            RequestBody timelineIdPart = null;
            if(!TextUtils.isEmpty(createNewDealRequest.getTimelineId())){
                timelineIdPart = RequestBody.create(MultipartBody.FORM, createNewDealRequest.getTimelineId());
            }

            File file = new File(createNewDealRequest.getProductImage());
            RequestBody productImageReqBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part productImagePart = MultipartBody.Part.createFormData("productImage", file.getName(), productImageReqBody);

            RequestBody userIdPart = RequestBody.create(MultipartBody.FORM, userID);

            createNewDealObservable(accessToken, productNamePart, descriptionPart, locationPart,
                    unitPricePart, amountPart, productImagePart, userIdPart, timelineIdPart).subscribe(createNewDealSubscriber());
        }
    }

    @Override
    public void searchProduct(String searchText) {

        searchDealsObservable(accessToken, searchText).subscribe(searchDealsSubscriber());

    }

    @Override
    public void deleteProduct(String productId) {
        deleteDealsObservable(accessToken, productId).subscribe(deleteDealsSubscriber());
    }

    public Observable<CreateNewDealResponse> createNewDealObservable(String accessToken, RequestBody productName, RequestBody descrption,
                                                                     RequestBody location, RequestBody unitPrice, RequestBody amount,
                                                                     MultipartBody.Part productImage, RequestBody userId, RequestBody timelineId) {
        try {
            return getRetrofitClient().createNewPost(accessToken,
                    productName, unitPrice, amount, descrption, userId, productImage, location, timelineId)
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

                DealsPresenterImpl.FirebaseDeal deal = new DealsPresenterImpl.FirebaseDeal();

                deal.setDealId(createNewDealResponse.getProductId());
                deal.setProductName(createNewDealResponse.getProductName());
                deal.setUserId(createNewDealResponse.getUserId());
                deal.setDate(createNewDealResponse.getDate());
                deal.setUser(createNewDealResponse.getUser());

                dealsRef.child(createNewDealResponse.getUserId()).push()
                        .setValue(deal);
                createNewDealView.onSuccess(createNewDealResponse.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                try {
                    createNewDealView.onError(handleApiError(e));
                } catch (Exception ex) {
                    Log.e(TAG, ex.toString());
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

    private Observable<ProductDealResponse> searchDealsObservable(String accessToken, String searchText) {
        try {
            return getRetrofitClient().searchProducts(accessToken, searchText)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private Observer<ProductDealResponse> searchDealsSubscriber(){
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
                    Log.e(TAG, ex.toString());
                }
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private Observable<CommonMessageResponse> deleteDealsObservable(String accessToken, String dealId) {
        try {
            return getRetrofitClient().deleteProduct(accessToken, dealId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private Observer<CommonMessageResponse> deleteDealsSubscriber(){
        return new Observer<CommonMessageResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(CommonMessageResponse commonMessageResponse) {
                displayProductView.onSuccess(commonMessageResponse.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                try {
                    displayProductView.onError(handleApiError(e));
                } catch (Exception ex) {
                    Log.e(TAG, ex.toString());
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

    @Getter
    @Setter
    public static class FirebaseDeal {
        private String dealId;
        private String productName;
        private String userId;
        private Date date;
        private User user;
    }
}
