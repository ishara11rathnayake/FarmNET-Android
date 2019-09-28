package com.industrialmaster.farmnet.views;

public interface CreateArticleView extends View {

    void onSuccess(String message);

    void onError(String message);

    void onThumbnailUploadComplete(String url);
}
