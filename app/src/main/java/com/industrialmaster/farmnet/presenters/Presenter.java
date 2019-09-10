package com.industrialmaster.farmnet.presenters;

public interface Presenter<T> {

    void onCreate();

    void onStart();

    void onStop();

    void onDestroy();
}
