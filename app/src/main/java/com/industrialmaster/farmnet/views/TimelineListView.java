package com.industrialmaster.farmnet.views;

import com.industrialmaster.farmnet.models.Timeline;

import java.util.List;

public interface TimelineListView extends View {

    void showTimelineList(List<Timeline> timelines);

    void onError(String message);

    void onSuccess(String message);
}
