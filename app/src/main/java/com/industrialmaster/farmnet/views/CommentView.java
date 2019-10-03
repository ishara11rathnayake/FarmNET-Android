package com.industrialmaster.farmnet.views;

import com.industrialmaster.farmnet.models.Comment;

import java.util.List;

public interface CommentView extends  View{

    void onError(String message);

    void showAllComments(List<Comment> comments);

    void onSuccess(String message);
}
