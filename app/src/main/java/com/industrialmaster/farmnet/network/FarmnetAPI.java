package com.industrialmaster.farmnet.network;

import com.industrialmaster.farmnet.models.request.LoginRequest;
import com.industrialmaster.farmnet.models.request.SignUpRequest;
import com.industrialmaster.farmnet.models.response.CreateNewDealResponse;
import com.industrialmaster.farmnet.models.response.LoginResponse;
import com.industrialmaster.farmnet.models.response.ProductDealResponse;
import com.industrialmaster.farmnet.models.response.SignUpResponse;
import com.industrialmaster.farmnet.utils.UrlManager;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FarmnetAPI {

    @POST(UrlManager.LOGIN)
    Observable<LoginResponse> doLogin(@Body LoginRequest loginRequest);

    @POST(UrlManager.SIGNUP)
    Observable<SignUpResponse> doSignup(@Body SignUpRequest signUpRequest);

    @GET(UrlManager.GET_ALL_DEALS)
    Observable<ProductDealResponse> getAllDeals();

    @Multipart
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST(UrlManager.CREATE_NEW_DEAL)
    Observable<CreateNewDealResponse> createNewPost(
            @Header("Authorization") RequestBody authorization,
            @Part("name") RequestBody productName,
            @Part("price") RequestBody unitPrice,
            @Part("amount") RequestBody amount,
            @Part("description") RequestBody description,
            @Part("userId") RequestBody userId,
            @Part("location") RequestBody location,
            @Part MultipartBody.Part productImage);

}
