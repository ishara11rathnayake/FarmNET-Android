package com.industrialmaster.farmnet.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {

    @NonNull
    @SerializedName("_id")
    private String userId;

    @NonNull
    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    @SerializedName("profileImage")
    private String profilePicUrl;

    public User(@NonNull String id, @NonNull String email) {
        this.userId = id;
        this.email = email;
    }

}
