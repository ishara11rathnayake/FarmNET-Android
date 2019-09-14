package com.industrialmaster.farmnet.models.request;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateNewDealRequest {

    @SerializedName("name")
    private String productName;

    @SerializedName("price")
    private String unitPrice;

    @SerializedName("amount")
    private String amount;

    @SerializedName("description")
    private String description;

    @SerializedName("userId")
    private String userId;

    @SerializedName("location")
    private String location;

    private boolean hasImage;

    @SerializedName("productImage")
    private String productImage;

}
