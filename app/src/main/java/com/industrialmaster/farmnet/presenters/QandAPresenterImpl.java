package com.industrialmaster.farmnet.presenters;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.industrialmaster.farmnet.models.Question;
import com.industrialmaster.farmnet.models.request.CreateNewQuestionRequest;
import com.industrialmaster.farmnet.models.response.CreateNewQuestionResponse;
import com.industrialmaster.farmnet.models.response.QuestionsResponse;
import com.industrialmaster.farmnet.network.DisposableManager;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.CreateNewQuestionView;
import com.industrialmaster.farmnet.views.QandAView;
import com.industrialmaster.farmnet.views.View;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import static android.support.constraint.Constraints.TAG;

public class QandAPresenterImpl extends BasePresenter implements QandAPresenter {

    QandAView qandAView;
    CreateNewQuestionView createNewQuestionView;

    String errorMessage;

    public QandAPresenterImpl(Activity activityContext, View view) {
        super(activityContext);
        if(view instanceof QandAView) {
            qandAView = (QandAView) view;
        } else if(view instanceof  CreateNewQuestionView) {
            createNewQuestionView = (CreateNewQuestionView) view;
        }
    }

    @Override
    public void getAllQuestions() {
        getAllQuestionsObservable().subscribe(getAllQuestionsSubscriber());
    }

    @Override
    public void createNewQuestion(CreateNewQuestionRequest createNewQuestionRequest) {

        if(!validateFields(createNewQuestionRequest)) {
            createNewQuestionView.onError(errorMessage);
        } else {
            String userId = readSharedPreferences(FarmnetConstants.USER_ID, "");
            createNewQuestionRequest.setUserId(userId);

            String accessToken = "Bearer " + readSharedPreferences(FarmnetConstants.TOKEN_PREFS_KEY, FarmnetConstants.CheckUserLogin.LOGOUT_USER);

            createNewQuestionObservable(accessToken, createNewQuestionRequest).subscribe(createNewQuestionSubscriber());
        }
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

    public Observable<CreateNewQuestionResponse> createNewQuestionObservable(String accesToken, CreateNewQuestionRequest createNewQuestionRequest) {
        try {
            return getRetrofitClient().createNewQuestion(accesToken, createNewQuestionRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Observer<CreateNewQuestionResponse> createNewQuestionSubscriber(){
        return new Observer<CreateNewQuestionResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(CreateNewQuestionResponse createNewQuestionResponse) {
                String message = createNewQuestionResponse.getMessage();
                createNewQuestionView.onSuccess(message);
            }

            @Override
            public void onError(Throwable e) {
                try {
                    createNewQuestionView.onError(handleApiError(e));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private boolean validateFields(CreateNewQuestionRequest newQuestionReq) {

        if(TextUtils.isEmpty(newQuestionReq.getQuestionTitle())){
            errorMessage = ErrorMessageHelper.FILL_QUESTION_TITLE;
            return false;
        }

        return true;
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
