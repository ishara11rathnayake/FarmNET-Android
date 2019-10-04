package com.industrialmaster.farmnet.presenters;

import com.industrialmaster.farmnet.models.request.CreateNewDealRequest;

public interface DealsPresenter extends Presenter {

    void getAllDeals();

    void createNewDeal(CreateNewDealRequest createNewDealRequest);

    void searchProduct(String searchText);

    void deleteProduct(String productId);
}
