package com.industrialmaster.farmnet.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Deals;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.activities.CommentActivity;
import com.industrialmaster.farmnet.views.activities.DisplayProductActivity;
import com.industrialmaster.farmnet.views.activities.OtherProfileActivity;
import com.industrialmaster.farmnet.views.fragments.DealsFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class DealsPostRecyclerViewAdapter extends  RecyclerView.Adapter<DealsPostRecyclerViewAdapter.DealViewHolder>{

    private static final String TAG = "DealsPostRVAdapter";

    private List<Deals> mDeals;
    private Context mContext;
    private Fragment mFragment;

    public DealsPostRecyclerViewAdapter(Context mContext, List<Deals> mDeals, Fragment mFragment) {
        this.mDeals = mDeals;
        this.mContext = mContext;
        this.mFragment = mFragment;
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_post_view, viewGroup,false);
        return new DealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called");

        Date date = mDeals.get(i).getDate();
        DateFormat targetDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
        String formattedDate = targetDateFormat.format(date);

        Glide.with(mContext)
                .asBitmap()
                .load(mDeals.get(i).getProductImageUrl())
                .centerCrop()
                .into(viewHolder.mProductPicImageView);

        if(!mDeals.get(i).getUser().getProfilePicUrl().isEmpty() && mDeals.get(i).getUser().getProfilePicUrl() != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(mDeals.get(i).getUser().getProfilePicUrl())
                    .centerCrop()
                    .into(viewHolder.mProfilePicCircleImageView);
        }

        viewHolder.mUnitPriceTextView.setText(String.format("Rs. %s", Double.toString(mDeals.get(i).getUnitPrice())));
        viewHolder.mUserNameTextView.setText(mDeals.get(i).getUser().getName());
        viewHolder.mDescriptionTextView.setText(mDeals.get(i).getDescription());
        viewHolder.mAmountTextView.setText(String.format("%sKg", Double.toString(mDeals.get(i).getAmount())));
        viewHolder.mDateTextView.setText(formattedDate);

        viewHolder.mUserNameTextView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, OtherProfileActivity.class);
            intent.putExtra("userId", mDeals.get(i).getUser().getUserId());
            mContext.startActivity(intent);
        });

        viewHolder.mProductPicImageView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, DisplayProductActivity.class);
            Gson gson = new Gson();
            String deal = gson.toJson(mDeals.get(i));
            intent.putExtra("activity", FarmnetConstants.HOME);
            intent.putExtra("deal", deal);
            mContext.startActivity(intent);
        });

        viewHolder.mCommentImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, CommentActivity.class);
            Gson gson = new Gson();
            String deal = gson.toJson(mDeals.get(i));
            intent.putExtra("deal", deal);
            mContext.startActivity(intent);
        });

        viewHolder.mCommentTextView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, CommentActivity.class);
            Gson gson = new Gson();
            String deal = gson.toJson(mDeals.get(i));
            intent.putExtra("deal", deal);
            mContext.startActivity(intent);
        });

        final boolean[] liked = {false};
        viewHolder.mLikeImageButton.setOnClickListener(v -> {
            liked[0] = !liked[0];
            if(liked[0]){
                viewHolder.mLikeImageButton.setImageResource(R.drawable.ic_filledlike);
            }else {
                viewHolder.mLikeImageButton.setImageResource(R.drawable.ic_like);
            }
            ((DealsFragment)mFragment).likeDeal(mDeals.get(i).getDealId());
        });
    }

    @Override
    public int getItemCount() {
        return mDeals.size();
    }


    public class DealViewHolder extends RecyclerView.ViewHolder {

        CardView mPostCardView;
        CircleImageView mProfilePicCircleImageView;
        TextView mUserNameTextView;
        TextView mDateTextView;
        ImageView mProductPicImageView;
        TextView mUnitPriceTextView;
        TextView mAmountTextView;
        TextView mDescriptionTextView;
        ImageButton mCommentImageButton;
        ImageButton mLikeImageButton;
        TextView mCommentTextView;
        TextView mLikeTextView;

        public DealViewHolder(@NonNull View itemView) {
            super(itemView);
            mProfilePicCircleImageView = itemView.findViewById(R.id.cimageview_profilepic);
            mUserNameTextView = itemView.findViewById(R.id.tv_name);
            mDateTextView = itemView.findViewById(R.id.tv_date);
            mProductPicImageView = itemView.findViewById(R.id.imgv_product_pic);
            mUnitPriceTextView = itemView.findViewById(R.id.tv_unit_price);
            mAmountTextView = itemView.findViewById(R.id.tv_amount);
            mDescriptionTextView = itemView.findViewById(R.id.tv_description);
            mPostCardView = itemView.findViewById(R.id.cradview_dealpost);
            mCommentImageButton = itemView.findViewById(R.id.img_btn_comment);
            mLikeImageButton = itemView.findViewById(R.id.image_button_like);
            mCommentTextView = itemView.findViewById(R.id.text_view_comment);
            mLikeTextView = itemView.findViewById(R.id.text_view_like);
        }
    }
}
