package com.industrialmaster.farmnet.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Article {

    @SerializedName("_id")
    private String articleId;

    @SerializedName("userId")
    private User user;

    @SerializedName("articleTitle")
    private String articleTitle;

    @SerializedName("content")
    private String content;

    @SerializedName("thumbnailUrl")
    private String thumbnailUrl;

    @SerializedName("date")
    private Date date;
}
