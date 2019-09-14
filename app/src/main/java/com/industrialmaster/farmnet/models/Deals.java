package com.industrialmaster.farmnet.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Deals {

    @SerializedName("_id")
    private String dealId;

    @SerializedName("name")
    private String productName;

    @SerializedName("price")
    private double unitPrice;

    @SerializedName("amount")
    private double amount;

    @SerializedName("description")
    private String description;

    @SerializedName("location")
    private String location;

    @SerializedName("user")
    private User user;

    @SerializedName("productImage")
    private String productImageUrl;

    @SerializedName("date")
    private Date date;
}
