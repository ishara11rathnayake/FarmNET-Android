package com.industrialmaster.farmnet.models;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Answer {

    private String answerId;
    private User user;
    private String content;
    private Date date;

}
