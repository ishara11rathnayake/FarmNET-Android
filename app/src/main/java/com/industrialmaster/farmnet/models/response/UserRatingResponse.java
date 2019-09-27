package com.industrialmaster.farmnet.models.response;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRatingResponse {

    @SerializedName("userId")
    private String userId;

    @SerializedName("ratingScore")
    private float ratingScore;

}
