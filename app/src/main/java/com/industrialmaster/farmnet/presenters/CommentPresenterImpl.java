package com.industrialmaster.farmnet.presenters;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.industrialmaster.farmnet.models.Comment;
import com.industrialmaster.farmnet.views.CommentView;
import com.industrialmaster.farmnet.views.View;

import java.util.ArrayList;
import java.util.List;

public class CommentPresenterImpl extends BasePresenter implements CommentPresenter {

    private CommentView commentView;

    private DatabaseReference commentRef;

    public CommentPresenterImpl(Activity activityContext, View view) {
        super(activityContext);
        if(view instanceof CommentView) {
            commentView = (CommentView) view;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        commentRef = database.getReference("comments");
    }

    @Override
    public void addNewComment(String postId, Comment comment) {
        commentRef.child(postId).push().setValue(comment);
        commentView.onSuccess("Successfully commented");
    }

    @Override
    public void getComment(String postId) {

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }
}
