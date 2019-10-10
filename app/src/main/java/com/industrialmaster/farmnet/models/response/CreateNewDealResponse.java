package com.industrialmaster.farmnet.models.response;

import com.google.gson.annotations.SerializedName;
import com.industrialmaster.farmnet.models.User;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateNewDealResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("productId")
    private String productId;

    @SerializedName("date")
    private Date date;

    @SerializedName("user")
    private String userId;

    @SerializedName("name")
    private String productName;

    @SerializedName("userName")
    private User user;

}
