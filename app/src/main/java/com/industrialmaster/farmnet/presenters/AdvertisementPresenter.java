package com.industrialmaster.farmnet.presenters;

import com.industrialmaster.farmnet.models.request.CreateNewAdvertisementRequest;

public interface AdvertisementPresenter extends Presenter {

    void getAllAdvertisements();

    void createNewAdvertisement(CreateNewAdvertisementRequest createNewAdvertisementRequest);

}
