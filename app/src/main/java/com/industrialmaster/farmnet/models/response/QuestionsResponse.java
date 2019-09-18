package com.industrialmaster.farmnet.models.response;

import com.google.gson.annotations.SerializedName;
import com.industrialmaster.farmnet.models.Question;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuestionsResponse {

    @SerializedName("count")
    private String count;

    @SerializedName("questions")
    private List<Question> questions;

}
