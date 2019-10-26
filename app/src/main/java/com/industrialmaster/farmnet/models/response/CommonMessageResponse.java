package com.industrialmaster.farmnet.models.response;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommonMessageResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("profileImage")
    private String profileImage;

    @SerializedName("rate")
    private float rate;
}
