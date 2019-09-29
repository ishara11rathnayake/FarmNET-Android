package com.industrialmaster.farmnet.presenters;

import android.app.Activity;
import android.util.Log;

import com.industrialmaster.farmnet.models.Article;
import com.industrialmaster.farmnet.models.request.CreateNewArticleRequest;
import com.industrialmaster.farmnet.models.response.ArticleResponse;
import com.industrialmaster.farmnet.models.response.CommonMessageResponse;
import com.industrialmaster.farmnet.models.response.ThumnailUrlResponse;
import com.industrialmaster.farmnet.network.DisposableManager;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.ArticleView;
import com.industrialmaster.farmnet.views.CreateArticleView;
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

public class ArticlePresenterImpl extends  BasePresenter implements ArticlePresenter {

    private static final String TAG = "ArticlePresenterImpl";
    private String accessToken = "Bearer " + readSharedPreferences(FarmnetConstants.TOKEN_PREFS_KEY, FarmnetConstants.CheckUserLogin.LOGOUT_USER);
    private String userID = readSharedPreferences(FarmnetConstants.USER_ID, "");

    CreateArticleView createArticleView;
    ArticleView articleView;

    public ArticlePresenterImpl(Activity activityContext, View view) {
        super(activityContext);
        if(view instanceof CreateArticleView){
            createArticleView = (CreateArticleView) view;
        }else if(view instanceof ArticleView){
            articleView = (ArticleView) view;
        }
    }

    @Override
    public void getThumbnailUrl(String realFilePath) {
        File file = new File(realFilePath);
        RequestBody thumbnailReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part thumbnailPart= MultipartBody.Part.createFormData("thumbnail", file.getName(), thumbnailReqBody);

        getThumbnailUrlObservable(accessToken, thumbnailPart).subscribe(getThumbnailUrlSubscriber());
    }

    @Override
    public void createNewArticle(CreateNewArticleRequest createNewArticleRequest) {
        String thumbnailUrl = readSharedPreferences(FarmnetConstants.THUMBNAIL_URL_PRES_KEY, "");
        createNewArticleRequest.setUserId(userID);
        createNewArticleRequest.setThumbnailUrl(thumbnailUrl);
        createNewArticleObservable(accessToken, createNewArticleRequest).subscribe(createNewArticleSubscriber());
    }

    @Override
    public void getAllArticles() {
        getAllArticleObservable().subscribe(getAllArticleSubscriber());
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
                saveSharedPreferences(FarmnetConstants.THUMBNAIL_URL_PRES_KEY, thumnailUrlResponse.getUrl());
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

    public Observable<CommonMessageResponse> createNewArticleObservable(String accessToken, CreateNewArticleRequest newArticle) {
        try {
            return getRetrofitClient().createNewArticle(accessToken, newArticle)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Observer<CommonMessageResponse> createNewArticleSubscriber(){
        return new Observer<CommonMessageResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(CommonMessageResponse commonMessageResponse) {
                createArticleView.onSuccess(commonMessageResponse.getMessage());
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

    public Observable<ArticleResponse> getAllArticleObservable() {
        try {
            return getRetrofitClient().getAllArticles()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Observer<ArticleResponse> getAllArticleSubscriber(){
        return new Observer<ArticleResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(ArticleResponse articleResponse) {
                articleView.showArticles(articleResponse.getArticles());
            }

            @Override
            public void onError(Throwable e) {
                try {
                    articleView.onError(handleApiError(e));
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
