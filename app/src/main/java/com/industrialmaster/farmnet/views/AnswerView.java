package com.industrialmaster.farmnet.views;

import com.industrialmaster.farmnet.models.Answer;
import com.industrialmaster.farmnet.models.User;

import java.util.List;

public interface AnswerView extends View {

    void onError(String message);

    void showAllAnswers(List<Answer> answers);

    void saveAnswer(User user);

    void onSuccess(String message);
}
