package com.industrialmaster.farmnet.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Advertisement {

    @SerializedName("_id")
    private String adsId;

    @SerializedName("adTitle")
    private String adTitle;

    @SerializedName("adDescription")
    private String adDescription;

    @SerializedName("price")
    private Double price;

    @SerializedName("adsImage")
    private String imageUrl;

    @SerializedName("contactNumber")
    private String contactNumber;

    @SerializedName("hashtags")
    private String[] tags;

    @SerializedName("date")
    private Date date;

    @SerializedName("user")
    private User user;
}
