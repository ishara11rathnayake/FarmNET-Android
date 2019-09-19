package com.industrialmaster.farmnet.models.request;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNewQuestionRequest {

    @SerializedName("question")
    private String questionTitle;

    @SerializedName("description")
    private String questionBody;

    @SerializedName("hashtags")
    private String[] hashtags;

    @SerializedName("userId")
    private String userId;
}
