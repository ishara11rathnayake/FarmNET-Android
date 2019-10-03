package com.industrialmaster.farmnet.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Comment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsRecyclerViewAdapter extends RecyclerView.Adapter<CommentsRecyclerViewAdapter.CommentViewHolder>{

    private List<Comment> mComments;
    private Context mContext;

    public CommentsRecyclerViewAdapter(Context mContext, List<Comment> mComments) {
        this.mComments = mComments;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_comment_view, viewGroup,false);
        return new CommentsRecyclerViewAdapter.CommentViewHolder(view);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder viewHolder, int i) {
        Comment comment = mComments.get(i);
        String formattedDate;
        String time;
        DateFormat targetDateFormat;

        if(!TextUtils.isEmpty(comment.getUser().getProfilePicUrl())){
            Glide.with(mContext)
                    .asBitmap()
                    .load(comment.getUser().getProfilePicUrl())
                    .centerCrop()
                    .into(viewHolder.mProfileImageCircleImageView);
        }

        viewHolder.mUsernameTextView.setText(comment.getUser().getName());
        viewHolder.mContentTextView.setText(comment.getContent());

        Date date = comment.getDate();
        targetDateFormat = new SimpleDateFormat("MM/dd");
        formattedDate = targetDateFormat.format(date);

        targetDateFormat = new SimpleDateFormat("HH:mm");
        time = targetDateFormat.format(date);

        viewHolder.mDateTextView.setText(formattedDate);
        viewHolder.mTimeTextView.setText(time);
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        CircleImageView mProfileImageCircleImageView;
        TextView mUsernameTextView;
        TextView mContentTextView;
        TextView mDateTextView;
        TextView mTimeTextView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            mProfileImageCircleImageView = itemView.findViewById(R.id.cimgv_profile_image);
            mUsernameTextView = itemView.findViewById(R.id.tv_username);
            mContentTextView = itemView.findViewById(R.id.tv_content);
            mDateTextView = itemView.findViewById(R.id.tv_date);
            mTimeTextView = itemView.findViewById(R.id.tv_time);
        }
    }
}
