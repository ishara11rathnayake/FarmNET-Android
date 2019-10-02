package com.industrialmaster.farmnet.presenters;

import com.industrialmaster.farmnet.models.request.CreateNewTimelineRequest;
import com.industrialmaster.farmnet.models.request.CreateNewTimelineTaskRequest;

public interface TimelinePresenter extends Presenter {

    void getTimelinesByUser();

    void createNewTimeline(CreateNewTimelineRequest createNewTimelineRequest);

    void createNewTimelineTask(CreateNewTimelineTaskRequest createNewTimelineTaskRequest);
}
