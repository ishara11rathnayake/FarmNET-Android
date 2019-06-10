package com.industrialmaster.farmnet.services;

import com.industrialmaster.farmnet.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FarmnetAPI {

    @POST("user/login")
    Call<Void> login(@Body User user);

}
