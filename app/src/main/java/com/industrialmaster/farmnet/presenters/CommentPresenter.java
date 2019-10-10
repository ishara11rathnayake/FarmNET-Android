package com.industrialmaster.farmnet.presenters;

import com.industrialmaster.farmnet.models.Comment;

public interface CommentPresenter extends Presenter{

    void addNewComment(String userId,String postId, Comment comment);

    void getCommentingUserDetails();

}
