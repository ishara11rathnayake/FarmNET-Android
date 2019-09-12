package com.industrialmaster.farmnet.presenters;

import android.app.Activity;
import android.util.Log;

import com.industrialmaster.farmnet.models.Deals;
import com.industrialmaster.farmnet.models.request.CreateNewDealRequest;
import com.industrialmaster.farmnet.models.response.ProductDealResponse;
import com.industrialmaster.farmnet.network.DisposableManager;
import com.industrialmaster.farmnet.views.DealsView;
import com.industrialmaster.farmnet.views.View;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.support.constraint.Constraints.TAG;

public class DealsPresenterImpl extends BasePresenter implements DealsPresenter {

    DealsView dealsView;

    public DealsPresenterImpl(Activity activityContext, View view) {
        super(activityContext);
        dealsView = (DealsView) view;
    }

    @Override
    public void getAllDeals() {
        getAllDealsObservable().subscribe(getAllDealsSubscriber());
    }

    @Override
    public void createNewDeal(CreateNewDealRequest createNewDealRequest) {
        boolean isValidate = createDealFielsValidate(createNewDealRequest);
    }

    private boolean createDealFielsValidate(CreateNewDealRequest createNewDealRequest) {

        String productName = createNewDealRequest.getProductName();
        double productPrice = createNewDealRequest.getUnitPrice();
        double productAmount = createNewDealRequest.getAmount();
        String description = createNewDealRequest.getDescription();
        String userId = createNewDealRequest.getUserId();
        String location = createNewDealRequest.getLocation();

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
