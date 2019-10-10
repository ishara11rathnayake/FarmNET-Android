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
    public static final String GET_ARTICLE_THUMBNAIL = "articles/thumbnails";
    public static final String CREATE_NEW_ARTICLE = "articles";
    public static final String GET_ALL_ARTICLE = "articles";
    public static final String GET_TIMELINES_BY_USER = "timelines/{userId}";
    public static final String CREATE_NEW_TIMELINE = "timelines";
    public static final String CREATE_NEW_TIMELINE_TASK = "timelines/tasks/{timelineId}";
    public static final String GET_TIMELINE_BY_ID = "timelines/timeline/{timelineId}";
    public static final String GET_USER_BY_ID = "user/user/{userId}";
    public static final String SEARCH_PRODUCT = "products/search/{searchText}";
    public static final String DELETE_PRODUCT = "products/{productId}";
    public static final String UPDATE_NO_OF_ANSWERS = "questions/answer/{questionId}";
    public static final String SEARCH_ADVERTISEMENT = "advertisements/search/{searchText}";
    public static final String SEARCH_QUESTIONS = "questions/search/{searchText}";
    public static final String GET_QUESTIONS_BY_USER_ID = "questions/byUser/{userId}";
    public static final String DELETE_QUESTION = "questions/{questionId}/{userId}";

}
