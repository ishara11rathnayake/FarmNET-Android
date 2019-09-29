package com.industrialmaster.farmnet.models.request;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNewArticleRequest {

    @SerializedName("userId")
    private String userId;

    @SerializedName("articleTitle")
    private String articleTitle;

    @SerializedName("content")
    private String content;

    @SerializedName("thumbnailUrl")
    private String thumbnailUrl;
}
