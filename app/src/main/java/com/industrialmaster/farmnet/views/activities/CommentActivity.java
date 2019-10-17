package com.industrialmaster.farmnet.views.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Comment;
import com.industrialmaster.farmnet.models.Deals;
import com.industrialmaster.farmnet.models.User;
import com.industrialmaster.farmnet.presenters.CommentPresenter;
import com.industrialmaster.farmnet.presenters.CommentPresenterImpl;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.CommentView;
import com.industrialmaster.farmnet.views.adapters.CommentsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentActivity extends BaseActivity implements CommentView {

    CommentPresenter commentPresenter;

    ImageButton mCloseImageButton;
    ImageButton mSendImageButton;
    RecyclerView mCommentRecyclerView;
    EditText mWriteCommentEditText;
    String postId;
    String userId;
    Deals deal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference commentRef = database.getReference("comments");

        commentPresenter = new CommentPresenterImpl(this, this);

        Gson gson = new Gson();
        deal = gson.fromJson(getIntent().getStringExtra("deal"), Deals.class);

        postId = deal.getDealId();
        userId = deal.getUser().getUserId();

        mCommentRecyclerView = findViewById(R.id.recyclerview_comment);
        mWriteCommentEditText = findViewById(R.id.et_write_comment);
        mCloseImageButton = findViewById(R.id.img_btn_close);
        mSendImageButton = findViewById(R.id.img_btn_send);

        mSendImageButton.setOnClickListener(v -> commentPresenter.getCommentingUserDetails());

        mCloseImageButton.setOnClickListener(v -> finish());

        commentRef.child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Comment> comments = new ArrayList<>();
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    Comment comment = dsp.getValue(Comment.class);
                    comments.add(comment);
                }
                showAllComments(comments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onError(String message) {
        setLoading(false);
        showAlertDialog("Error", message,false, FarmnetConstants.OK , (dialog, which) -> {},
                "", (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void showAllComments(List<Comment> comments) {
        setLoading(false);
        CommentsRecyclerViewAdapter adapter = new CommentsRecyclerViewAdapter(CommentActivity.this, comments);
        mCommentRecyclerView.setAdapter(adapter);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
    }

    @Override
    public void onSuccess(String message) {
        setLoading(false);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        mWriteCommentEditText.getText().clear();
    }

    @Override
    public void saveComment(User user) {
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setDate(new Date());
        comment.setContent(mWriteCommentEditText.getText().toString());

        commentPresenter.addNewComment(userId, postId, comment);
    }

    @Override
    public void showMessage(String message) {

    }

}
