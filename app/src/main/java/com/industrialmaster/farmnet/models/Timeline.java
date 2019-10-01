package com.industrialmaster.farmnet.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Timeline {

    @SerializedName("_id")
    private String timelineId;

    @SerializedName("productName")
    private String productName;

    @SerializedName("description")
    private String description;

    @SerializedName("userId")
    private User user;

    @SerializedName("tasks")
    private List<Task> tasks;

}
