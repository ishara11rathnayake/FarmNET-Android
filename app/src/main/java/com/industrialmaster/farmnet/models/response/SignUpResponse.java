package com.industrialmaster.farmnet.models.response;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignUpResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("token")
    private String access_token;

}
