package com.industrialmaster.farmnet.views;

public interface ChangePasswordView extends View {

    /**
     * function for display error
     * @param error the error message
     */
    void onError(String error);

    /**
     *  function for display success message
     * @param message the success message
     */
    void onSuccess(String message);

}
