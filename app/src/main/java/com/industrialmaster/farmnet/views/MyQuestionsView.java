package com.industrialmaster.farmnet.views;

import com.industrialmaster.farmnet.models.Question;

import java.util.List;

public interface MyQuestionsView extends View{

    void showMyQuestions(List<Question> questions);

    void onError(String error);

    void onSuccess(String message);

}
