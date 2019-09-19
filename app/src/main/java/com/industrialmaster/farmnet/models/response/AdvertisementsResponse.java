package com.industrialmaster.farmnet.models.response;

import com.google.gson.annotations.SerializedName;
import com.industrialmaster.farmnet.models.Advertisement;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdvertisementsResponse {

    @SerializedName("count")
    private int count;

    @SerializedName("advertisements")
    private List<Advertisement> advertisements;
}
