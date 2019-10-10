package com.industrialmaster.farmnet.presenters;

import com.industrialmaster.farmnet.models.request.CreateNewQuestionRequest;

public interface QandAPresenter extends Presenter {

    void getAllQuestions();

    void createNewQuestion(CreateNewQuestionRequest createNewQuestionRequest);

    void searchQuestions(String searchText);

    void deleteQuestion(String questionId);

    void getQuestionByUserId();

    void updateQuestion(CreateNewQuestionRequest updateQuestionRequest, String questionId);
}
