package com.industrialmaster.farmnet.utils;

public class UrlManager {

    public static final String API_BASE_URL = "https://farmnet-app-webservice.herokuapp.com/";
    public static final String LOGIN = "user/login";
    public static final String SIGNUP = "user/signup";
    public static final String GET_ALL_DEALS = "products";
    public static final String CREATE_NEW_DEAL = "products";
    public static final String GET_ALL_QUESTIONS = "questions";
    public static final String CREATE_NEW_QUESTION = "questions";
    public static final String GET_ALL_ADVERTISEMENTS = "advertisements";
    public static final String CREATE_NEW_ADVERTISEMENTS = "advertisements";
    public static final String GET_USER_DETAILS = "user/{userId}";
    public static final String UPDATE_USER_DETAILS = "user/{userId}";
    public static final String GET_USER_RATING = "ratings/{userId}";
    public static final String GET_RATED_USER_RATING = "ratings/userrating/{userId}&{ratedUserId}";
    public static final String RATE_USER = "ratings/{userId}&{ratedUserId}&{ratingScore}";
    public static final String REPORT_USER = "complaints";

}
