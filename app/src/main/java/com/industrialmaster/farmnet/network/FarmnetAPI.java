package com.industrialmaster.farmnet.network;

import com.industrialmaster.farmnet.models.request.LoginRequest;
import com.industrialmaster.farmnet.models.request.SignUpRequest;
import com.industrialmaster.farmnet.models.response.LoginResponse;
import com.industrialmaster.farmnet.models.response.ProductDealResponse;
import com.industrialmaster.farmnet.models.response.SignUpResponse;
import com.industrialmaster.farmnet.utils.UrlManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface FarmnetAPI {

    @POST(UrlManager.LOGIN)
    Observable<LoginResponse> doLogin(@Body LoginRequest loginRequest);

    @POST(UrlManager.SIGNUP)
    Observable<SignUpResponse> doSignup(@Body SignUpRequest signUpRequest);

    @GET(UrlManager.GET_ALL_DEALS)
    Observable<ProductDealResponse> getAllDeals();

}
