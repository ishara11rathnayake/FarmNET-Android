package com.industrialmaster.farmnet.views;

import com.industrialmaster.farmnet.models.Deals;

import java.util.List;

public interface DealsView extends View {

    void showDeals(List<Deals> deals);

    void onError(String error);
}
