package com.industrialmaster.farmnet.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task {

    @SerializedName("_id")
    private String taskId;

    @SerializedName("content")
    private String content;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("date")
    private Date date;

}
