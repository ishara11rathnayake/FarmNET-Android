package com.industrialmaster.farmnet.models.response;

import com.google.gson.annotations.SerializedName;
import com.industrialmaster.farmnet.models.Deals;
import com.industrialmaster.farmnet.models.User;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductDealResponse {

    @SerializedName("count")
    private String count;

    @SerializedName("products")
    private List<Deals> products;

//    @Setter
//    @Getter
//    public class Deal {
//        @SerializedName("_id")
//        private String dealId;
//
//        @SerializedName("name")
//        private String productName;
//
//        @SerializedName("price")
//        private double unitPrice;
//
//        @SerializedName("amount")
//        private double amount;
//
//        @SerializedName("description")
//        private String description;
//
//        @SerializedName("location")
//        private String location;
//
//        @SerializedName("user")
//        private User user;
//
//        @SerializedName("productImage")
//        private String productImageUrl;
//    }

}
