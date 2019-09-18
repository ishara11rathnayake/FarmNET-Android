package com.industrialmaster.farmnet.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Question {

    @SerializedName("_id")
    private String questionId;

    @SerializedName("question")
    private String quetion;

    @SerializedName("description")
    private String description;

    @SerializedName("hashtags")
    private String[] hashtags;

    @SerializedName("date")
    private Date date;

    @SerializedName("user")
    private User user;

    @SerializedName("numberOfAnswers")
    private int numberOfAnswers;

}
