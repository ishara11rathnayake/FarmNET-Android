package com.industrialmaster.farmnet.presenters;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.industrialmaster.farmnet.models.Timeline;
import com.industrialmaster.farmnet.models.request.CreateNewTimelineRequest;
import com.industrialmaster.farmnet.models.request.CreateNewTimelineTaskRequest;
import com.industrialmaster.farmnet.models.response.CommonMessageResponse;
import com.industrialmaster.farmnet.models.response.TimelineByIdResponse;
import com.industrialmaster.farmnet.models.response.TimelineResponse;
import com.industrialmaster.farmnet.network.DisposableManager;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.CreateNewDealView;
import com.industrialmaster.farmnet.views.DisplayProductView;
import com.industrialmaster.farmnet.views.TimelineListView;
import com.industrialmaster.farmnet.views.TimelineView;
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

import static com.github.irshulx.EditorCore.TAG;

public class TimelnePresenterImpl extends BasePresenter implements TimelinePresenter {

    private TimelineListView timelineListView;
    private TimelineView timelineView;
    private CreateNewDealView createNewDealView;
    private DisplayProductView displayProductView;

    private View view;

    private String userId = readSharedPreferences(FarmnetConstants.USER_ID, "");
    private String accessToken = "Bearer " + readSharedPreferences(FarmnetConstants.TOKEN_PREFS_KEY, FarmnetConstants.CheckUserLogin.LOGOUT_USER);

    public TimelnePresenterImpl(Activity activityContext, View view) {
        super(activityContext);
        if(view instanceof TimelineListView){
            timelineListView = (TimelineListView) view;
            this.view = view;
        } else if(view instanceof TimelineView){
            timelineView = (TimelineView) view;
        } else if (view instanceof CreateNewDealView){
            createNewDealView = (CreateNewDealView) view;
            this.view = view;
        } else if (view instanceof DisplayProductView) {
            displayProductView = (DisplayProductView) view;
        }
    }

    @Override
    public void getTimelinesByUser() {
        getTimelinesByUserObservable(accessToken, userId).subscribe(getTimelinesByUserSubscriber());
    }

    @Override
    public void createNewTimeline(CreateNewTimelineRequest createNewTimelineRequest) {
        createNewTimelineRequest.setUserId(userId);
        createNewTimelineObservable(accessToken, createNewTimelineRequest).subscribe(createNewTimelineSubscriber());
    }

    @Override
    public void createNewTimelineTask(CreateNewTimelineTaskRequest createNewTimelineTaskRequest) {
        RequestBody contentPart = RequestBody.create(MultipartBody.FORM, createNewTimelineTaskRequest.getContent());

        MultipartBody.Part timelineImagePart = null;
        if(!TextUtils.isEmpty(createNewTimelineTaskRequest.getTimelineImage())){
            File file = new File(createNewTimelineTaskRequest.getTimelineImage());
            RequestBody timelineImageReqBody = RequestBody.create(MediaType.parse("image/*"), file);
            timelineImagePart = MultipartBody.Part.createFormData("timelineImage", file.getName(), timelineImageReqBody);
        }

        createNewTimelineTaskObservable(accessToken, createNewTimelineTaskRequest.getTimelineId(),
                contentPart, timelineImagePart).subscribe(createNewTimelineTaskSubscriber());
    }

    @Override
    public void getTimelineById(String timelineId) {
        if(TextUtils.isEmpty(timelineId)){
            displayProductView.onError(ErrorMessageHelper.NO_TIMELINE_ATTACHED);
        } else {
            getTimelineByIdObservable(accessToken, timelineId).subscribe(getTimelineByIdSubscriber());
        }
    }

    public Observable<TimelineResponse> getTimelinesByUserObservable(String accesToken, String userId) {
        try {
            return getRetrofitClient().getTimelinesByUser(accesToken, userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Observer<TimelineResponse> getTimelinesByUserSubscriber(){
        return new Observer<TimelineResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(TimelineResponse timelineResponse) {
                List<Timeline> timelines = timelineResponse.getTimelines();
                if(view instanceof  TimelineListView){
                    timelineListView.showTimelineList(timelines);
                } else if (view instanceof CreateNewDealView){
                    createNewDealView.setSpinnerValues(timelines);
                }
            }

            @Override
            public void onError(Throwable e) {
                try {
                    if(view instanceof  TimelineListView) {
                        timelineListView.onError(handleApiError(e));
                    } else if (view instanceof CreateNewDealView){
                        createNewDealView.onError(handleApiError(e));
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

    public Observable<CommonMessageResponse> createNewTimelineObservable(String accesToken,
                                                                         CreateNewTimelineRequest newTimeline) {
        try {
            return getRetrofitClient().createNewTimeline(accesToken, newTimeline)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Observer<CommonMessageResponse> createNewTimelineSubscriber(){
        return new Observer<CommonMessageResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(CommonMessageResponse commonMessageResponse) {
                timelineView.onSuccess(commonMessageResponse.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                try {
                    timelineView.onError(handleApiError(e));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onComplete() {

            }
        };
    }

    public Observable<CommonMessageResponse> createNewTimelineTaskObservable(String accesToken, String timelineId, RequestBody content,
                                                                             MultipartBody.Part timelineImage) {
        try {
            return getRetrofitClient().createNewTimelineTask(accesToken, timelineId, content, timelineImage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Observer<CommonMessageResponse> createNewTimelineTaskSubscriber(){
        return new Observer<CommonMessageResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(CommonMessageResponse commonMessageResponse) {
                timelineView.onSuccess(commonMessageResponse.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                try {
                    timelineView.onError(handleApiError(e));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onComplete() {

            }
        };
    }

    public Observable<TimelineByIdResponse> getTimelineByIdObservable(String accesToken, String timelineId) {
        try {
            return getRetrofitClient().getTimelineById(accesToken, timelineId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Observer<TimelineByIdResponse> getTimelineByIdSubscriber(){
        return new Observer<TimelineByIdResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(TimelineByIdResponse timelineByIdResponse) {
                displayProductView.setTimelineData(timelineByIdResponse.getTimeline());
            }

            @Override
            public void onError(Throwable e) {
                try {
                    displayProductView.onError(handleApiError(e));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onComplete() {

            }
        };
    }
}
