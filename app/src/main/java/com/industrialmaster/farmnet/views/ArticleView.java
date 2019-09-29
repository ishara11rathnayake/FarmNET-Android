package com.industrialmaster.farmnet.views;

import com.industrialmaster.farmnet.models.Article;

import java.util.List;

public interface ArticleView extends View {

    void showArticles(List<Article> articles);

    void onError(String message);
}
