package com.industrialmaster.farmnet.views;

import com.industrialmaster.farmnet.models.Question;

import java.util.List;

public interface QandAView extends View {

    void showQuestions(List<Question> questions);

    void onError(String error);
}
