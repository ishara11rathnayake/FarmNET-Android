package com.industrialmaster.farmnet.presenters;

import com.industrialmaster.farmnet.models.request.CreateNewQuestionRequest;

public interface QandAPresenter extends Presenter {

    void getAllQuestions();

    void createNewQuestion(CreateNewQuestionRequest createNewQuestionRequest);
}
