package com.industrialmaster.farmnet.presenters;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.industrialmaster.farmnet.models.Answer;
import com.industrialmaster.farmnet.models.response.CommonMessageResponse;
import com.industrialmaster.farmnet.models.response.UserDetailsResponse;
import com.industrialmaster.farmnet.network.DisposableManager;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.AnswerView;
import com.industrialmaster.farmnet.views.View;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AnswerPresenterImpl extends BasePresenter implements AnswerPresenter {

    private static final String TAG = "AnswerPresenterImpl";
    AnswerView answerView;

    private DatabaseReference answerRef;

    private String userId = readSharedPreferences(FarmnetConstants.USER_ID, "");
    private String accessToken = "Bearer " + readSharedPreferences(FarmnetConstants.TOKEN_PREFS_KEY, FarmnetConstants.CheckUserLogin.LOGOUT_USER);


    public AnswerPresenterImpl(Activity activityContext, View view) {
        super(activityContext);
        if(view instanceof  AnswerView){
            answerView = (AnswerView) view;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        answerRef = database.getReference("answers");
    }

    @Override
    public void addNewAnswer(String questionId, Answer answer) {
        answerRef.child(questionId).push().setValue(answer);
        updateNoOfAnswersObservable(accessToken, questionId).subscribe();
        answerView.onSuccess("Successfully answered");
    }

    @Override
    public void getAnsweringUserDetails() {
        getUserByIdObservable(accessToken, userId).subscribe(getUserByIdSubscriber());
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

    private Observable<UserDetailsResponse> getUserByIdObservable(String accesToken, String userId) {
        try {
            return getRetrofitClient().getUserById(accesToken, userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private Observer<UserDetailsResponse> getUserByIdSubscriber(){
        return new Observer<UserDetailsResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(UserDetailsResponse userDetailsResponse) {
                answerView.saveAnswer(userDetailsResponse.getUser());
            }

            @Override
            public void onError(Throwable e) {
                try {
                    answerView.onError(handleApiError(e));
                } catch (Exception ex) {
                    Log.e(TAG, ex.toString());
                }
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private Observable<CommonMessageResponse> updateNoOfAnswersObservable(String accesToken, String questionId) {
        try {
            return getRetrofitClient().updateNoOfAnswers(accesToken, questionId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }
}
