package com.industrialmaster.farmnet.models.request;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateNewTimelineTaskRequest {

    @SerializedName("content")
    private String content;

    @SerializedName("timelineImage")
    private String timelineImage;

    private String timelineId;
}
