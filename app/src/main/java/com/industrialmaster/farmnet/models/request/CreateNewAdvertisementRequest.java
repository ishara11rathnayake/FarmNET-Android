package com.industrialmaster.farmnet.models.request;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNewAdvertisementRequest {

    @SerializedName("adTitle")
    private String adTitle;

    @SerializedName("adDescription")
    private String adDescription;

    @SerializedName("price")
    private Double price;

    @SerializedName("contactNumber")
    private String contactNumber;

    @SerializedName("hashtags")
    private String[] tags;

    @SerializedName("userId")
    private String userId;

    @SerializedName("adsImage")
    private String adsImage;

    private boolean hasImage;
}
