package com.industrialmaster.farmnet.models.response;

import com.google.gson.annotations.SerializedName;
import com.industrialmaster.farmnet.models.Timeline;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimelineByIdResponse {

    @SerializedName("timeline")
    private Timeline timeline;
}
