package com.industrialmaster.farmnet.presenters;

import android.app.Activity;
import android.util.Log;

import com.industrialmaster.farmnet.models.Question;
import com.industrialmaster.farmnet.models.response.QuestionsResponse;
import com.industrialmaster.farmnet.network.DisposableManager;
import com.industrialmaster.farmnet.views.QandAView;
import com.industrialmaster.farmnet.views.View;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.support.constraint.Constraints.TAG;

public class QandAPresenterImpl extends BasePresenter implements QandAPresenter {

    QandAView qandAView;

    public QandAPresenterImpl(Activity activityContext, View view) {
        super(activityContext);
        if(view instanceof QandAView) {
            qandAView = (QandAView) view;
        }
    }

    @Override
    public void getAllQuestions() {
        getAllQuestionsObservable().subscribe(getAllQuestionsSubscriber());
    }

    public Observable<QuestionsResponse> getAllQuestionsObservable() {
        try {
            return getRetrofitClient().getAllQuestions()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Observer<QuestionsResponse> getAllQuestionsSubscriber(){
        return new Observer<QuestionsResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(QuestionsResponse questionsResponse) {
                List<Question> questions = questionsResponse.getQuestions();
                qandAView.showQuestions(questions);
            }

            @Override
            public void onError(Throwable e) {
                try {
                    qandAView.onError(handleApiError(e));
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
