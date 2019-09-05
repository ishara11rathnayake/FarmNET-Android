package com.industrialmaster.farmnet.models.request;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;
}
