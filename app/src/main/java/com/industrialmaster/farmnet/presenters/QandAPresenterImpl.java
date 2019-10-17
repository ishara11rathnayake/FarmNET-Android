package com.industrialmaster.farmnet.presenters;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.industrialmaster.farmnet.models.Question;
import com.industrialmaster.farmnet.models.request.CreateNewQuestionRequest;
import com.industrialmaster.farmnet.models.response.CommonMessageResponse;
import com.industrialmaster.farmnet.models.response.CreateNewQuestionResponse;
import com.industrialmaster.farmnet.models.response.QuestionsResponse;
import com.industrialmaster.farmnet.network.DisposableManager;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.CreateNewQuestionView;
import com.industrialmaster.farmnet.views.MyQuestionsView;
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

    private QandAView qandAView;
    private CreateNewQuestionView createNewQuestionView;
    private MyQuestionsView myQuestionsView;

    private String errorMessage;

    private String userId = readSharedPreferences(FarmnetConstants.USER_ID, "");
    private String accessToken = "Bearer " + readSharedPreferences(FarmnetConstants.TOKEN_PREFS_KEY, FarmnetConstants.CheckUserLogin.LOGOUT_USER);


    public QandAPresenterImpl(Activity activityContext, View view) {
        super(activityContext);
        if(view instanceof QandAView) {
            qandAView = (QandAView) view;
        } else if(view instanceof  CreateNewQuestionView) {
            createNewQuestionView = (CreateNewQuestionView) view;
        } else if(view instanceof MyQuestionsView){
            myQuestionsView = (MyQuestionsView) view;
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

            createNewQuestionObservable(accessToken, createNewQuestionRequest).subscribe(createNewQuestionSubscriber());
        }
    }

    @Override
    public void searchQuestions(String searchText) {
        searchQuestionsObservable(accessToken, searchText).subscribe(searchQuestionsSubscriber());
    }

    @Override
    public void deleteQuestion(String questionId) {
        deleteQuestionObservable(accessToken, userId, questionId).subscribe(deleteQuestionSubscriber());
    }

    @Override
    public void getQuestionByUserId() {
        getQuestionByUserIdObservable(accessToken, userId).subscribe(getQuestionByUserIdSubscriber());
    }

    @Override
    public void updateQuestion(CreateNewQuestionRequest updateQuestionRequest, String questionId) {
        updateQuestionObservable(accessToken, questionId, updateQuestionRequest).subscribe(updateQuestionSubscriber());
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

    private Observable<QuestionsResponse> searchQuestionsObservable(String accessToken, String searchText) {
        try {
            return getRetrofitClient().searchQuestions(accessToken, searchText)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private Observer<QuestionsResponse> searchQuestionsSubscriber(){
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
                    Log.e(TAG, ex.toString());
                }
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private Observable<QuestionsResponse> getQuestionByUserIdObservable(String accessToken, String userId) {
        try {
            return getRetrofitClient().getQuestionsByUserId(accessToken, userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private Observer<QuestionsResponse> getQuestionByUserIdSubscriber(){
        return new Observer<QuestionsResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(QuestionsResponse questionsResponse) {
                List<Question> questions = questionsResponse.getQuestions();
                myQuestionsView.showMyQuestions(questions);
            }

            @Override
            public void onError(Throwable e) {
                try {
                    myQuestionsView.onError(handleApiError(e));
                } catch (Exception ex) {
                    Log.e(TAG, ex.toString());
                }
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private Observable<CommonMessageResponse> deleteQuestionObservable(String accessToken, String userId,
                                                                       String questionId) {
        try {
            return getRetrofitClient().deleteQuestion(accessToken, questionId, userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private Observer<CommonMessageResponse> deleteQuestionSubscriber(){
        return new Observer<CommonMessageResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(CommonMessageResponse commonMessageResponse) {
                myQuestionsView.onSuccess(commonMessageResponse.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                try {
                    myQuestionsView.onError(handleApiError(e));
                } catch (Exception ex) {
                    Log.e(TAG, ex.toString());
                }
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private Observable<CommonMessageResponse> updateQuestionObservable(String accessToken, String questionId,
                                                                       CreateNewQuestionRequest newQuestionRequest) {
        try {
            return getRetrofitClient().updateQuestion(accessToken, questionId, newQuestionRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private Observer<CommonMessageResponse> updateQuestionSubscriber(){
        return new Observer<CommonMessageResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(CommonMessageResponse commonMessageResponse) {
                createNewQuestionView.onSuccess(commonMessageResponse.getMessage());
        }

            @Override
            public void onError(Throwable e) {
                try {
                    createNewQuestionView.onError(handleApiError(e));
                } catch (Exception ex) {
                    Log.e(TAG, ex.toString());
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

}
