package com.industrialmaster.farmnet.models.response;

import com.google.gson.annotations.SerializedName;
import com.industrialmaster.farmnet.models.Deals;

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
}
