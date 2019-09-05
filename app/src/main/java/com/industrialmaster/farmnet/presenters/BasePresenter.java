package com.industrialmaster.farmnet.presenters;

import android.app.Activity;

import com.industrialmaster.farmnet.network.FarmnetAPI;
import com.industrialmaster.farmnet.network.RetrofitClient;
import com.industrialmaster.farmnet.network.RetrofitException;

import java.io.IOException;

import retrofit2.HttpException;

public abstract class BasePresenter {

    protected Activity activity;
    protected String apiErrorMessage;

    protected FarmnetAPI getRetrofitClient() {
        return RetrofitClient.getInstance().create(FarmnetAPI.class);
    }

    protected String handleApiError(Throwable e){

        if(e instanceof HttpException){
            switch (((HttpException) e).code()) {
                case RetrofitException.HTTP_UNAUTHORIZED:
                    apiErrorMessage = "Unauthorised User";
                    break;
                case RetrofitException.HTTP_FORBIDDEN:
                    apiErrorMessage = "Forbidden";
                    break;
                case RetrofitException.HTTP_INTERNAL_ERROR:
                    apiErrorMessage = "Internal Server Error";
                    break;
                case RetrofitException.HTTP_BAD_REQUEST:
                    apiErrorMessage = "Bad Request";
                    break;
                default:
                    apiErrorMessage = "Unexpected error occurred!";

            }
        } else if(e instanceof IOException){
            apiErrorMessage = "Please check your internet connection";
        }

        return apiErrorMessage;
    }

}
