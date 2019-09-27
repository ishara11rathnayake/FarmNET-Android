package com.industrialmaster.farmnet.models.request;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintRequest {

    @SerializedName("userId")
    private String userId;

    @SerializedName("complainedUserId")
    private String complainedUserId;

    @SerializedName("content")
    private String content;
}
