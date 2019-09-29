package com.industrialmaster.farmnet.presenters;

import com.industrialmaster.farmnet.models.request.CreateNewArticleRequest;

public interface ArticlePresenter extends Presenter {

    void getThumbnailUrl(String realFilePath);

    void createNewArticle(CreateNewArticleRequest createNewArticleRequest);

    void getAllArticles();
}
