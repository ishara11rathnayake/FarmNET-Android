package com.industrialmaster.farmnet.presenters;

import com.industrialmaster.farmnet.models.Answer;

public interface AnswerPresenter extends Presenter {

    void addNewAnswer(String questionId, Answer answer);

    void getAnsweringUserDetails();
}
