package com.industrialmaster.farmnet.views;

import com.industrialmaster.farmnet.models.Advertisement;

import java.util.List;

public interface AdvertisementView extends View {

    void showAdvertisements(List<Advertisement> advertisements);

    void onError(String error);
}
