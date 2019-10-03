package com.industrialmaster.farmnet.views.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Comment;
import com.industrialmaster.farmnet.models.User;
import com.industrialmaster.farmnet.views.CommentView;
import com.industrialmaster.farmnet.views.adapters.CommentsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentActivity extends BaseActivity implements CommentView {

    ImageButton mCloseImageButton;
    ImageButton mSendImageButton;
    RecyclerView mCommentRecyclerView;
    EditText mWriteCommentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        mCommentRecyclerView = findViewById(R.id.recyclerview_comment);
        mWriteCommentEditText = findViewById(R.id.et_write_comment);

        List<Comment> comments = new ArrayList<>();

        User user = new User();
        user.setName("Ishara");

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setCommentId("111");
        comment.setContent("vjsdkjshkjsasdc");
        comment.setDate(new Date());

        comments.add(comment);
        comments.add(comment);

        mSendImageButton = findViewById(R.id.img_btn_send);
        mSendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentsRecyclerViewAdapter adapter = new CommentsRecyclerViewAdapter(CommentActivity.this, comments);
                mCommentRecyclerView.setAdapter(adapter);
                mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
            }
        });

        mCloseImageButton = findViewById(R.id.img_btn_close);
        mCloseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void showAllComments(List<Comment> comments) {

    }

    @Override
    public void onSuccess(String message) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showErrorMessage(String calledMethod, String error, String errorDescription) {

    }
}
