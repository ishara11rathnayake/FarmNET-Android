package com.industrialmaster.farmnet.models.response;

import com.google.gson.annotations.SerializedName;
import com.industrialmaster.farmnet.models.Article;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleResponse {

    @SerializedName("count")
    private String count;

    @SerializedName("articles")
    private List<Article> articles;
}
