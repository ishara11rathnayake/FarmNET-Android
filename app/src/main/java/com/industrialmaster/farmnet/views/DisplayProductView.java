package com.industrialmaster.farmnet.views;

import com.industrialmaster.farmnet.models.Timeline;

public interface DisplayProductView extends View{

    void setTimelineData(Timeline timeline);

    void onError(String message);

    void onSuccess(String message);

}
