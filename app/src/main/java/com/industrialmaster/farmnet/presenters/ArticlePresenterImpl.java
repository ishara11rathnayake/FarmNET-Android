package com.industrialmaster.farmnet.presenters;

import android.app.Activity;
import android.util.Log;

import com.industrialmaster.farmnet.models.response.ThumnailUrlResponse;
import com.industrialmaster.farmnet.network.DisposableManager;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.CreateArticleView;
import com.industrialmaster.farmnet.views.View;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ArticlePresenterImpl extends  BasePresenter implements ArticlePresenter {

    private static final String TAG = "ArticlePresenterImpl";

    CreateArticleView createArticleView;

    public ArticlePresenterImpl(Activity activityContext, View view) {
        super(activityContext);
        if(view instanceof CreateArticleView){
            createArticleView = (CreateArticleView) view;
        }
    }

    @Override
    public void getThumbnailUrl(String realFilePath) {
        File file = new File(realFilePath);
        RequestBody thumnailReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part thumbnailPart= MultipartBody.Part.createFormData("thumbnail", file.getName(), thumnailReqBody);

        String accessToken = "Bearer " + readSharedPreferences(FarmnetConstants.TOKEN_PREFS_KEY, FarmnetConstants.CheckUserLogin.LOGOUT_USER);

        getThumbnailUrlObservable(accessToken, thumbnailPart).subscribe(getThumbnailUrlSubscriber());
    }

    public Observable<ThumnailUrlResponse> getThumbnailUrlObservable(String accessToken, MultipartBody.Part thumnailPart) {
        try {
            return getRetrofitClient().getThumnailUrl(accessToken, thumnailPart)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Observer<ThumnailUrlResponse> getThumbnailUrlSubscriber(){
        return new Observer<ThumnailUrlResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(ThumnailUrlResponse thumnailUrlResponse) {
                createArticleView.onThumbnailUploadComplete(thumnailUrlResponse.getUrl());
            }

            @Override
            public void onError(Throwable e) {
                try {
                    createArticleView.onError(handleApiError(e));
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
