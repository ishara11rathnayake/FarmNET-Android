package com.industrialmaster.farmnet.presenters;

import android.app.Activity;
import android.util.Log;

import com.industrialmaster.farmnet.models.Advertisement;
import com.industrialmaster.farmnet.models.response.AdvertisementsResponse;
import com.industrialmaster.farmnet.network.DisposableManager;
import com.industrialmaster.farmnet.views.AdvertisementView;
import com.industrialmaster.farmnet.views.View;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.support.constraint.Constraints.TAG;

public class AdvertisementsPresenterImpl extends BasePresenter implements AdvertisementPresenter {

    AdvertisementView advertisementView;

    public AdvertisementsPresenterImpl(Activity activityContext, View view) {
        super(activityContext);

        if(view instanceof  AdvertisementView){
            advertisementView = (AdvertisementView) view;
        }
    }

    @Override
    public void getAllAdvertisements() {
        getAllAdvertisementsObservable().subscribe(getAllAdvertisementsSubscriber());
    }

    public Observable<AdvertisementsResponse> getAllAdvertisementsObservable() {
        try {
            return getRetrofitClient().getAllAdvertisements()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Observer<AdvertisementsResponse> getAllAdvertisementsSubscriber(){
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
