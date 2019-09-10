package com.industrialmaster.farmnet.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.industrialmaster.farmnet.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DealsPostRecyclerViewAdapter extends  RecyclerView.Adapter<DealsPostRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "DealsPostRVAdapter";

    private ArrayList<String> mIamges = new ArrayList<>();
    private ArrayList<String> mUserName = new ArrayList<>();
    private ArrayList<String> mUnitPrice = new ArrayList<>();
    private Context mContext;

    public DealsPostRecyclerViewAdapter(Context mContext, ArrayList<String> mIamges, ArrayList<String> mUserName, ArrayList<String> mUnitPrice) {
        this.mIamges = mIamges;
        this.mUserName = mUserName;
        this.mUnitPrice = mUnitPrice;
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

        Glide.with(mContext)
                .asBitmap()
                .load(mIamges.get(i))
                .centerCrop()
                .into(viewHolder.imgv_product_pic);

        viewHolder.tv_unit_price.setText(mUnitPrice.get(i));
        viewHolder.tv_user_name.setText(mUserName.get(i));

        viewHolder.post_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + mUserName.get(i));

                Toast.makeText(mContext, mUserName.get(i), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView post_card_view;
        CircleImageView circleImageView_profile_pic;
        TextView tv_user_name;
        TextView tv_location;
        ImageView imgv_product_pic;
        TextView tv_unit_price;
        TextView tv_amount;
        TextView tv_description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView_profile_pic = itemView.findViewById(R.id.cimageview_profilepic);
            tv_user_name = itemView.findViewById(R.id.tv_name);
            tv_location = itemView.findViewById(R.id.tv_location);
            imgv_product_pic = itemView.findViewById(R.id.imgv_product_pic);
            tv_unit_price = itemView.findViewById(R.id.tv_unit_price);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_description = itemView.findViewById(R.id.tv_description);
            post_card_view = itemView.findViewById(R.id.cradview_dealpost);
        }
    }
}
