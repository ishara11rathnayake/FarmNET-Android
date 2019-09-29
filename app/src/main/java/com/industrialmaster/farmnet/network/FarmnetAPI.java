package com.industrialmaster.farmnet.network;

import com.industrialmaster.farmnet.models.Article;
import com.industrialmaster.farmnet.models.request.ComplaintRequest;
import com.industrialmaster.farmnet.models.request.CreateNewArticleRequest;
import com.industrialmaster.farmnet.models.request.CreateNewQuestionRequest;
import com.industrialmaster.farmnet.models.request.LoginRequest;
import com.industrialmaster.farmnet.models.request.SignUpRequest;
import com.industrialmaster.farmnet.models.response.AdvertisementsResponse;
import com.industrialmaster.farmnet.models.response.ArticleResponse;
import com.industrialmaster.farmnet.models.response.CommonMessageResponse;
import com.industrialmaster.farmnet.models.response.CreateNewAdsResponse;
import com.industrialmaster.farmnet.models.response.CreateNewDealResponse;
import com.industrialmaster.farmnet.models.response.CreateNewQuestionResponse;
import com.industrialmaster.farmnet.models.response.LoginResponse;
import com.industrialmaster.farmnet.models.response.ProductDealResponse;
import com.industrialmaster.farmnet.models.response.QuestionsResponse;
import com.industrialmaster.farmnet.models.response.SignUpResponse;
import com.industrialmaster.farmnet.models.response.ThumnailUrlResponse;
import com.industrialmaster.farmnet.models.response.UserDetailsResponse;
import com.industrialmaster.farmnet.models.response.UserRatingResponse;
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
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface FarmnetAPI {

    @POST(UrlManager.LOGIN)
    Observable<LoginResponse> doLogin(@Body LoginRequest loginRequest);

    @POST(UrlManager.SIGNUP)
    Observable<SignUpResponse> doSignup(@Body SignUpRequest signUpRequest);

    @GET(UrlManager.GET_ALL_DEALS)
    Observable<ProductDealResponse> getAllDeals();

    @Multipart
    @POST(UrlManager.CREATE_NEW_DEAL)
    Observable<CreateNewDealResponse> createNewPost(
            @Header("Authorization") String authorization,
            @Part("name") RequestBody productName,
            @Part("price") RequestBody unitPrice,
            @Part("amount") RequestBody amount,
            @Part("description") RequestBody description,
            @Part("userId") RequestBody userId,
            @Part MultipartBody.Part productImage,
            @Part("location") RequestBody location);

    @GET(UrlManager.GET_ALL_QUESTIONS)
    Observable<QuestionsResponse> getAllQuestions();

    @POST(UrlManager.CREATE_NEW_QUESTION)
    Observable<CreateNewQuestionResponse> createNewQuestion(
            @Header("Authorization") String authorization,
            @Body CreateNewQuestionRequest createNewQuestionRequest);

    @GET(UrlManager.GET_ALL_ADVERTISEMENTS)
    Observable<AdvertisementsResponse> getAllAdvertisements();

    @Multipart
    @POST(UrlManager.CREATE_NEW_ADVERTISEMENTS)
    Observable<CreateNewAdsResponse> createNewAdvertisement(
            @Header("Authorization") String authorization,
            @Part("adTitle") RequestBody adTitle,
            @Part("adDescription") RequestBody adDescription,
            @Part("contactNumber") RequestBody contactNumber,
            @Part("price") RequestBody price,
            @Part List<MultipartBody.Part> tags,
            @Part("userId") RequestBody userId,
            @Part MultipartBody.Part adsImage
    );

    @GET(UrlManager.GET_USER_DETAILS)
    Observable<UserDetailsResponse> getUserDetails(
            @Header("Authorization") String authorization,
            @Path("userId") String userId);

    @Multipart
    @PATCH(UrlManager.UPDATE_USER_DETAILS)
    Observable<CommonMessageResponse> updateUserDetails(
            @Header("Authorization") String authorization,
            @Path("userId") String userId,
            @Part MultipartBody.Part profileImage,
            @Part("address") RequestBody address,
            @Part("contactNumber") RequestBody contactNumber,
            @Part("nic") RequestBody nic,
            @Part("dob") RequestBody dob,
            @Part("name") RequestBody name
    );

    @GET(UrlManager.GET_USER_RATING)
    Observable<UserRatingResponse> getUserRating(
            @Header("Authorization") String authorization,
            @Path("userId") String userId
    );

    @GET(UrlManager.GET_RATED_USER_RATING)
    Observable<UserRatingResponse> getRatedUserRating(
            @Header("Authorization") String authorization,
            @Path("userId") String userId,
            @Path("ratedUserId") String ratedUserId
    );

    @POST(UrlManager.RATE_USER)
    Observable<CommonMessageResponse> rateUser(
            @Header("Authorization") String authorization,
            @Path("userId") String userId,
            @Path("ratedUserId") String ratedUserId,
            @Path("ratingScore") float ratingScore
    );

    @POST(UrlManager.REPORT_USER)
    Observable<CommonMessageResponse> reportUser(
            @Header("Authorization") String authorization,
            @Body ComplaintRequest complaintRequest);

    @Multipart
    @POST(UrlManager.GET_ARTICLE_THUMBNAIL)
    Observable<ThumnailUrlResponse> getThumnailUrl(
            @Header("Authorization") String authorization,
            @Part MultipartBody.Part thumbnail
    );

    @POST(UrlManager.CREATE_NEW_ARTICLE)
    Observable<CommonMessageResponse> createNewArticle(
            @Header("Authorization") String authorization,
            @Body CreateNewArticleRequest createNewArticleRequest);

    @GET(UrlManager.GET_ALL_ARTICLE)
    Observable<ArticleResponse> getAllArticles();



}
