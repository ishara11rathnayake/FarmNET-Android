package com.industrialmaster.farmnet.models;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Comment {

    private String commentId;
    private User user;
    private String content;
    private Date date;

}
