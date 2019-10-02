package com.industrialmaster.farmnet.views;

public interface TimelineView extends View {

    void onError(String message);

    void onSuccess(String message);
}
