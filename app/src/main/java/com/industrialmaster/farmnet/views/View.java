package com.industrialmaster.farmnet.views;

public interface View {
    void showMessage(String message);
    void showErrorMessage(String calledMethod, String error, String errorDescription);
}
