package com.industrialmaster.farmnet.presenters;

import android.app.Activity;
import android.util.Log;

import com.industrialmaster.farmnet.models.Timeline;
import com.industrialmaster.farmnet.models.response.TimelineResponse;
import com.industrialmaster.farmnet.network.DisposableManager;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.TimelineListView;
import com.industrialmaster.farmnet.views.View;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.github.irshulx.EditorCore.TAG;

public class TimelnePresenterImpl extends BasePresenter implements TimelinePresenter {

    TimelineListView timelineListView;

    String userId = readSharedPreferences(FarmnetConstants.USER_ID, "");
    String accessToken = "Bearer " + readSharedPreferences(FarmnetConstants.TOKEN_PREFS_KEY, FarmnetConstants.CheckUserLogin.LOGOUT_USER);

    public TimelnePresenterImpl(Activity activityContext, View view) {
        super(activityContext);
        if(view instanceof TimelineListView){
            timelineListView = (TimelineListView) view;
        }
    }

    @Override
    public void getTimelinesByUser() {
        getTimelinesByUserObservable(accessToken, userId).subscribe(getTimelinesByUserSubscriber());
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
                timelineListView.showTimelineList(timelines);
            }

            @Override
            public void onError(Throwable e) {
                try {
                    timelineListView.onError(handleApiError(e));
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
