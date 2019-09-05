package com.industrialmaster.farmnet.network;

import com.industrialmaster.farmnet.models.request.LoginRequest;
import com.industrialmaster.farmnet.models.response.LoginResponse;
import com.industrialmaster.farmnet.utils.UrlManager;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FarmnetAPI {

    @POST(UrlManager.LOGIN)
    Observable<LoginResponse> doLogin(@Body LoginRequest loginRequest);

}
