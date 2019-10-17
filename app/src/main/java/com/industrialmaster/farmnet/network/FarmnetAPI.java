package com.industrialmaster.farmnet.network;

import android.app.VoiceInteractor;

import com.industrialmaster.farmnet.models.request.ChangePasswordRequest;
import com.industrialmaster.farmnet.models.request.ComplaintRequest;
import com.industrialmaster.farmnet.models.request.CreateNewArticleRequest;
import com.industrialmaster.farmnet.models.request.CreateNewQuestionRequest;
import com.industrialmaster.farmnet.models.request.CreateNewTimelineRequest;
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
import com.industrialmaster.farmnet.models.response.TimelineByIdResponse;
import com.industrialmaster.farmnet.models.response.TimelineResponse;
import com.industrialmaster.farmnet.models.response.UserDetailsResponse;
import com.industrialmaster.farmnet.models.response.UserRatingResponse;
import com.industrialmaster.farmnet.utils.UrlManager;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
            @Part("location") RequestBody location,
            @Part("timelineId") RequestBody timelineId);

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

    @GET(UrlManager.GET_TIMELINES_BY_USER)
    Observable<TimelineResponse> getTimelinesByUser(
            @Header("Authorization") String authorization,
            @Path("userId") String userId
    );

    @POST(UrlManager.CREATE_NEW_TIMELINE)
    Observable<CommonMessageResponse> createNewTimeline(
            @Header("Authorization") String authorization,
            @Body CreateNewTimelineRequest createNewTimelineRequest);

    @Multipart
    @PATCH(UrlManager.CREATE_NEW_TIMELINE_TASK)
    Observable<CommonMessageResponse> createNewTimelineTask(
            @Header("Authorization") String authorization,
            @Path("timelineId") String timelineId,
            @Part("content") RequestBody content,
            @Part MultipartBody.Part timelineImage);

    @GET(UrlManager.GET_TIMELINE_BY_ID)
    Observable<TimelineByIdResponse> getTimelineById(
            @Header("Authorization") String authorization,
            @Path("timelineId") String timelineId
    );

    @GET(UrlManager.GET_USER_BY_ID)
    Observable<UserDetailsResponse> getUserById(
            @Header("Authorization") String authorization,
            @Path("userId") String userId
    );

    @GET(UrlManager.SEARCH_PRODUCT)
    Observable<ProductDealResponse> searchProducts(
            @Header("Authorization") String authorization,
            @Path("searchText") String searchText
    );

    @DELETE(UrlManager.DELETE_PRODUCT)
    Observable<CommonMessageResponse> deleteProduct(
            @Header("Authorization") String authorization,
            @Path("productId") String productId
    );

    @PATCH(UrlManager.UPDATE_NO_OF_ANSWERS)
    Observable<CommonMessageResponse> updateNoOfAnswers(
            @Header("Authorization") String authorization,
            @Path("questionId") String questionId
    );

    @GET(UrlManager.SEARCH_ADVERTISEMENT)
    Observable<AdvertisementsResponse> searchAdvertisement(
            @Header("Authorization") String authorization,
            @Path("searchText") String searchText
    );

    @GET(UrlManager.SEARCH_QUESTIONS)
    Observable<QuestionsResponse> searchQuestions(
            @Header("Authorization") String authorization,
            @Path("searchText") String searchText
    );

    @GET(UrlManager.GET_QUESTIONS_BY_USER_ID)
    Observable<QuestionsResponse> getQuestionsByUserId(
            @Header("Authorization") String authorization,
            @Path("userId") String userId
    );

    @DELETE(UrlManager.DELETE_QUESTION)
    Observable<CommonMessageResponse> deleteQuestion(
            @Header("Authorization") String authorization,
            @Path("questionId") String questionId,
            @Path("userId") String userId
    );

    @PATCH(UrlManager.UPDATE_QUESTION)
    Observable<CommonMessageResponse> updateQuestion(
            @Header("Authorization") String authorization,
            @Path("questionId") String questionId,
            @Body CreateNewQuestionRequest createNewQuestionRequest
    );

    @Multipart
    @PATCH(UrlManager.UPDATE_PRODUCT)
    Observable<CommonMessageResponse> updateDeal(
            @Header("Authorization") String authorization,
            @Part("name") RequestBody productName,
            @Part("price") RequestBody unitPrice,
            @Part("amount") RequestBody amount,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part productImage,
            @Part("location") RequestBody location,
            @Part("timelineId") RequestBody timelineId,
            @Path("productId") String productId
    );

    @GET(UrlManager.FILTER_PRODUCT)
    Observable<ProductDealResponse> filterProduct(
            @Header("Authorization") String authorization,
            @Query("minprice") int minPrice,
            @Query("maxprice") int maxPrice,
            @Query("minAmount") int minAmount,
            @Query("maxAmount") int maxAmount
    );

    @PATCH(UrlManager.CHANGE_PASSWORD)
    Observable<CommonMessageResponse> changePassword(
            @Header("Authorization") String authorization,
            @Path("userId") String userId,
            @Body ChangePasswordRequest changePasswordRequest
            );
}
