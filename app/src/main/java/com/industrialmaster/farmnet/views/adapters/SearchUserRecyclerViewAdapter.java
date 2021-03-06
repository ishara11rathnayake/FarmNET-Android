package com.industrialmaster.farmnet.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.User;
import com.industrialmaster.farmnet.views.activities.FilterUserActivity;
import com.industrialmaster.farmnet.views.activities.OtherProfileActivity;
import com.industrialmaster.farmnet.views.activities.ProfileActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchUserRecyclerViewAdapter extends RecyclerView.Adapter<SearchUserRecyclerViewAdapter.UserViewHolder> {

    private List<User> mUsers;
    private Context mContext;

    public SearchUserRecyclerViewAdapter(Context mContext, List<User> mUsers) {
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_user_search_view, viewGroup,false);
        return new SearchUserRecyclerViewAdapter.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int i) {
        User user = mUsers.get(i);

        if(!TextUtils.isEmpty(user.getProfilePicUrl())){
            Glide.with(mContext)
                    .asBitmap()
                    .load(user.getProfilePicUrl())
                    .centerCrop()
                    .into(userViewHolder.mProfileImageCircleImageView);
        }

        userViewHolder.mUsernameTextView.setText(user.getName());
        userViewHolder.mUserRatingBar.setRating((float) user.getRating());

        userViewHolder.mSearchUserConstraintLayout.setOnClickListener(v -> {
            Intent intent;
            if(!((FilterUserActivity)mContext).checkOwnPost(user.getUserId())){
                intent = new Intent(mContext, OtherProfileActivity.class);
            } else {
                intent = new Intent(mContext, ProfileActivity.class);
            }
            intent.putExtra("userId", user.getUserId());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        CircleImageView mProfileImageCircleImageView;
        TextView mUsernameTextView;
        RatingBar mUserRatingBar;
        ConstraintLayout mSearchUserConstraintLayout;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mProfileImageCircleImageView = itemView.findViewById(R.id.cimgv_profile_image);
            mUsernameTextView = itemView.findViewById(R.id.text_view_name);
            mUserRatingBar = itemView.findViewById(R.id.rating_bar_profile);
            mSearchUserConstraintLayout = itemView.findViewById(R.id.constraint_layout_search_user);
        }
    }
}
