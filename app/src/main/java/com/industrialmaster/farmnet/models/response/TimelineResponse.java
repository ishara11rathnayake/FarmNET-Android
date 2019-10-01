package com.industrialmaster.farmnet.models.response;

import com.google.gson.annotations.SerializedName;
import com.industrialmaster.farmnet.models.Timeline;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimelineResponse {

    @SerializedName("timelines")
    private List<Timeline> timelines;

}
