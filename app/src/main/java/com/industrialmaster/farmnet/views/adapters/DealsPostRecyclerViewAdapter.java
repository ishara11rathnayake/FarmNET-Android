package com.industrialmaster.farmnet.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Deals;
import com.industrialmaster.farmnet.views.activities.CommentActivity;
import com.industrialmaster.farmnet.views.activities.DisplayProductActivity;
import com.industrialmaster.farmnet.views.activities.OtherProfileActivity;

import org.w3c.dom.Comment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DealsPostRecyclerViewAdapter extends  RecyclerView.Adapter<DealsPostRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "DealsPostRVAdapter";

    private List<Deals> mDeals;
    private Context mContext;

    public DealsPostRecyclerViewAdapter(Context mContext, List<Deals> mDeals) {
        this.mDeals = mDeals;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_post_view, viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called");

        Date date = mDeals.get(i).getDate();
        DateFormat targetDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        String formattedDate = targetDateFormat.format(date);

        Glide.with(mContext)
                .asBitmap()
                .load(mDeals.get(i).getProductImageUrl())
                .centerCrop()
                .into(viewHolder.imgv_product_pic);

        if(!mDeals.get(i).getUser().getProfilePicUrl().isEmpty() && mDeals.get(i).getUser().getProfilePicUrl() != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(mDeals.get(i).getUser().getProfilePicUrl())
                    .centerCrop()
                    .into(viewHolder.circleImageView_profile_pic);
        }

        viewHolder.tv_unit_price.setText("Rs. "+Double.toString(mDeals.get(i).getUnitPrice()));
        viewHolder.tv_user_name.setText(mDeals.get(i).getUser().getName());
        viewHolder.tv_description.setText(mDeals.get(i).getDescription());
        viewHolder.tv_amount.setText(Double.toString(mDeals.get(i).getAmount())+"Kg");
        viewHolder.tv_date.setText(formattedDate);

        viewHolder.tv_user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OtherProfileActivity.class);
                intent.putExtra("userId", mDeals.get(i).getUser().getUserId());
                mContext.startActivity(intent);
            }
        });

        viewHolder.imgv_product_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DisplayProductActivity.class);
                Gson gson = new Gson();
                String deal = gson.toJson(mDeals.get(i));
                intent.putExtra("deal", deal);
                mContext.startActivity(intent);
            }
        });

        viewHolder.mCommentImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, CommentActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDeals.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView post_card_view;
        CircleImageView circleImageView_profile_pic;
        TextView tv_user_name;
        TextView tv_date;
        ImageView imgv_product_pic;
        TextView tv_unit_price;
        TextView tv_amount;
        TextView tv_description;
        ImageButton mCommentImageButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView_profile_pic = itemView.findViewById(R.id.cimageview_profilepic);
            tv_user_name = itemView.findViewById(R.id.tv_name);
            tv_date = itemView.findViewById(R.id.tv_date);
            imgv_product_pic = itemView.findViewById(R.id.imgv_product_pic);
            tv_unit_price = itemView.findViewById(R.id.tv_unit_price);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_description = itemView.findViewById(R.id.tv_description);
            post_card_view = itemView.findViewById(R.id.cradview_dealpost);
            mCommentImageButton = itemView.findViewById(R.id.img_btn_comment);
        }
    }
}
