package com.industrialmaster.farmnet.models.response;

import com.google.gson.annotations.SerializedName;
import com.industrialmaster.farmnet.models.User;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchUserResponse {

    @SerializedName("users")
    private List<User> users;

    @SerializedName("count")
    private int count;

}
