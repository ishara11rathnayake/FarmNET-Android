package com.industrialmaster.farmnet.models.response;

import com.google.gson.annotations.SerializedName;
import com.industrialmaster.farmnet.models.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsResponse {

    @SerializedName("user")
    private User user;

}
