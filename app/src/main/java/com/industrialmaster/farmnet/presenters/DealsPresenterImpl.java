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

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
        Objects.requireNonNull(getAllDealsObservable()).subscribe(getAllDealsSubscriber());
    }

    @Override
    public void createNewDeal(CreateNewDealRequest createNewDealRequest) {
        boolean isValidate = createDealFielsValidate(createNewDealRequest);

        if(!isValidate) {
            createNewDealView.onError(errorMessage);
        } else {
            Objects.requireNonNull(createNewDealObservable(accessToken, createNewDealRequest)).subscribe(createNewDealSubscriber());
        }
    }

    @Override
    public void searchProduct(String searchText) {
        Objects.requireNonNull(searchDealsObservable(accessToken, searchText)).subscribe(searchDealsSubscriber());
    }

    @Override
    public void deleteProduct(String productId) {
        Objects.requireNonNull(deleteDealsObservable(accessToken, productId)).subscribe(deleteDealsSubscriber());
    }

    @Override
    public void updateDeal(CreateNewDealRequest createNewDealRequest, String dealId) {
        Objects.requireNonNull(updateDealObservable(accessToken, createNewDealRequest, dealId)).subscribe(updateDealSubscriber());
    }

    @Override
    public void filterDeals(int minPrice, int maxPrice, int minAmount, int maxAmount) {
        Objects.requireNonNull(filterDealsObservable(accessToken, minPrice, maxPrice, minAmount, maxAmount)).subscribe(filterDealsSubscriber());
    }

    @Override
    public void likeProduct(String productId) {
        Objects.requireNonNull(likeDealsObservable(accessToken, userID, productId)).subscribe();
    }

    private Observable<CreateNewDealResponse> createNewDealObservable(String accessToken, CreateNewDealRequest newDealRequest) {
        RequestBody productNamePart = RequestBody.create(MultipartBody.FORM, newDealRequest.getProductName());
        RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM, newDealRequest.getDescription());
        RequestBody locationPart = RequestBody.create(MultipartBody.FORM, newDealRequest.getLocation());
        RequestBody unitPricePart = RequestBody.create(MultipartBody.FORM, newDealRequest.getUnitPrice());
        RequestBody amountPart = RequestBody.create(MultipartBody.FORM, newDealRequest.getAmount());
        RequestBody latitudePart = RequestBody.create(MultipartBody.FORM, String.valueOf(newDealRequest.getLatitude()));
        RequestBody longitudePart = RequestBody.create(MultipartBody.FORM, String.valueOf(newDealRequest.getLongitude()));

        RequestBody timelineIdPart = null;
        if(!TextUtils.isEmpty(newDealRequest.getTimelineId())){
            timelineIdPart = RequestBody.create(MultipartBody.FORM, newDealRequest.getTimelineId());
        }

        File file = new File(newDealRequest.getProductImage());
        RequestBody productImageReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part productImagePart = MultipartBody.Part.createFormData("productImage", file.getName(), productImageReqBody);

        RequestBody userIdPart = RequestBody.create(MultipartBody.FORM, userID);


        try {
            return getRetrofitClient().createNewPost(accessToken,
                    productNamePart, unitPricePart, amountPart, descriptionPart, userIdPart, productImagePart,
                    locationPart, timelineIdPart, latitudePart, longitudePart)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private Observer<CreateNewDealResponse> createNewDealSubscriber(){
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

    private Observable<ProductDealResponse> getAllDealsObservable() {
        try {
            return getRetrofitClient().getAllDeals(userID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private Observer<ProductDealResponse> getAllDealsSubscriber(){
        return new Observer<ProductDealResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

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

    private Observable<CommonMessageResponse> updateDealObservable(String accessToken, CreateNewDealRequest updateDeal , String dealId) {

        RequestBody productNamePart = RequestBody.create(MultipartBody.FORM, updateDeal.getProductName());
        RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM, updateDeal.getDescription());
        RequestBody locationPart = RequestBody.create(MultipartBody.FORM, updateDeal.getLocation());
        RequestBody unitPricePart = RequestBody.create(MultipartBody.FORM, updateDeal.getUnitPrice());
        RequestBody amountPart = RequestBody.create(MultipartBody.FORM, updateDeal.getAmount());
        RequestBody latitudePart = RequestBody.create(MultipartBody.FORM, String.valueOf(updateDeal.getLatitude()));
        RequestBody longitudePart = RequestBody.create(MultipartBody.FORM, String.valueOf(updateDeal.getLongitude()));

        RequestBody timelineIdPart = null;
        if(!TextUtils.isEmpty(updateDeal.getTimelineId())){
            timelineIdPart = RequestBody.create(MultipartBody.FORM, updateDeal.getTimelineId());
        }

        MultipartBody.Part productImagePart = null;
        if(!TextUtils.isEmpty(updateDeal.getProductImage())){
            File file = new File(updateDeal.getProductImage());
            RequestBody productImageReqBody = RequestBody.create(MediaType.parse("image/*"), file);
            productImagePart = MultipartBody.Part.createFormData("productImage", file.getName(), productImageReqBody);
        }

        try {
            return getRetrofitClient().updateDeal(accessToken,
                    productNamePart, unitPricePart, amountPart, descriptionPart, productImagePart, locationPart,
                    timelineIdPart, latitudePart, longitudePart, dealId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private Observer<CommonMessageResponse> updateDealSubscriber(){
        return new Observer<CommonMessageResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(CommonMessageResponse commonMessageResponse) {
                createNewDealView.onSuccess(commonMessageResponse.getMessage());
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

    private Observable<ProductDealResponse> filterDealsObservable(String accessToken, int minPrice, int maxPrice,
                                                                  int minAmount, int maxAmount) {
        try {
            return getRetrofitClient().filterProduct(accessToken, minPrice, maxPrice, minAmount, maxAmount)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private Observer<ProductDealResponse> filterDealsSubscriber(){
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

    private Observable<CommonMessageResponse> likeDealsObservable(String accessToken, String userId, String productId) {
        try {
            return getRetrofitClient().likeDeal(accessToken, productId, userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
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
