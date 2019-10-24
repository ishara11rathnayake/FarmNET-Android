package com.industrialmaster.farmnet.models.response;

import com.google.gson.annotations.SerializedName;
import com.industrialmaster.farmnet.models.Advertisement;
import com.industrialmaster.farmnet.models.Article;
import com.industrialmaster.farmnet.models.Deals;
import com.industrialmaster.farmnet.models.User;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDetailsResponse {

    @SerializedName("user")
    private User user;

    @SerializedName("deals")
    private List<Deals> deals;

    @SerializedName("advertisements")
    private List<Advertisement> advertisements;

    @SerializedName("articles")
    private List<Article> articles;

}
