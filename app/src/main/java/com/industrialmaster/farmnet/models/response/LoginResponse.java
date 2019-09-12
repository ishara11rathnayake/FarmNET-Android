package com.industrialmaster.farmnet.models.response;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

    @SerializedName("token")
    private String access_token;

    @SerializedName("message")
    private String message;

    @SerializedName("userId")
    private String userId;

}
