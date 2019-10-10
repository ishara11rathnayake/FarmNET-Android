package com.industrialmaster.farmnet.models;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notification {

    private String id;
    private String content;
    private Date date;
    private User user;

}
