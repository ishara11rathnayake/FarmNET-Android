package com.industrialmaster.farmnet.models.request;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateNewDealRequest {

    @SerializedName("name")
    private String productName;

    @SerializedName("price")
    private double unitPrice;

    @SerializedName("amount")
    private double amount;

    @SerializedName("description")
    private String description;

    @SerializedName("userId")
    private String userId;

    @SerializedName("location")
    private String location;

}
