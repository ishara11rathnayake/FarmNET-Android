package com.industrialmaster.farmnet.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import com.industrialmaster.farmnet.network.FarmnetAPI;
import com.industrialmaster.farmnet.network.RetrofitClient;
import com.industrialmaster.farmnet.network.RetrofitException;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.HttpException;

public abstract class BasePresenter implements Presenter{

    protected Activity activity;
    public static final String FARMNET_PREFS_NAME = "FarmnetPrefsFile";
    protected String apiErrorMessage;

    protected View mView;

    public BasePresenter(Activity activity) {
        this.activity = activity;
    }

    protected FarmnetAPI getRetrofitClient() {
        return RetrofitClient.getInstance().create(FarmnetAPI.class);
    }

    protected String handleApiError(Throwable e){

        HttpException error=(HttpException)e;
        String message = error.response().message();

        if(e instanceof HttpException){
            switch (((HttpException) e).code()) {
                case RetrofitException.HTTP_UNAUTHORIZED:
                    apiErrorMessage = ErrorMessageHelper.UNAUTHORIZED_USER;
                    break;
                case RetrofitException.HTTP_FORBIDDEN:
                    apiErrorMessage = ErrorMessageHelper.FORBIDDEN;
                    break;
                case RetrofitException.HTTP_INTERNAL_ERROR:
                    apiErrorMessage = ErrorMessageHelper.SERVER_ERROR;
                    break;
                case RetrofitException.HTTP_BAD_REQUEST:
                    apiErrorMessage = ErrorMessageHelper.BAD_REQUEST;
                    break;
                default:
                    apiErrorMessage = message;

            }
        } else if(e instanceof IOException){
            apiErrorMessage = ErrorMessageHelper.CHECK_INTERNET_CONNECTION;
        }

        return apiErrorMessage;
    }

    protected SharedPreferences getSharedPreferences(String prefsName){
        return activity.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
}

    protected void saveSharedPreferences(String prefsKey, String prefsValue){
        SharedPreferences.Editor editor = getSharedPreferences(FARMNET_PREFS_NAME).edit();
        editor.putString(prefsKey, prefsValue);
        editor.apply();
    }

    protected String readSharedPreferences(String prefsKey, String prefsDefaultValue){
        return getSharedPreferences(FARMNET_PREFS_NAME).getString(prefsKey, prefsDefaultValue);
    }

}
