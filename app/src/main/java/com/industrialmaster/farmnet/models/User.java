package com.industrialmaster.farmnet.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class User {

    @NonNull
    @SerializedName("_id")
    private String userId;

    @NonNull
    private String email;

    @NonNull
    private String password;

    public User(@NonNull String id, @NonNull String email, @NonNull String password) {
        this.userId = id;
        this.email = email;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
