package com.industrialmaster.farmnet.views;

import com.industrialmaster.farmnet.models.Timeline;

import java.util.List;

public interface CreateNewDealView extends View {
     void onSuccess(String message);

     void onError(String message);

     void setSpinnerValues(List<Timeline> timelines);

}
