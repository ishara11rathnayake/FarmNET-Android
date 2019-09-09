package com.industrialmaster.farmnet.views;

public interface AuthView extends View {
    void onSuccess(String message);
    void onError(String message);

}
