package com.industrialmaster.farmnet.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Deals;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.activities.DisplayProductActivity;

import java.util.List;

public class DealsGridViewRecyclerViewAdapter extends RecyclerView.Adapter<DealsGridViewRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "DealsGridViewRecyclerViewAdapter";

    private List<Deals> mDeals;
    private Context mContext;

    public DealsGridViewRecyclerViewAdapter(Context mContext, List<Deals> mDeals) {
        this.mDeals = mDeals;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_abstract_deal_view, viewGroup,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tv_product_name.setText(mDeals.get(i).getProductName());
        Glide.with(mContext)
                .asBitmap()
                .load(mDeals.get(i).getProductImageUrl())
                .centerCrop()
                .into(viewHolder.imageview_product_image);

        viewHolder.tv_more_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DisplayProductActivity.class);
                Gson gson = new Gson();
                String deal = gson.toJson(mDeals.get(i));
                intent.putExtra("activity", FarmnetConstants.PROFILE);
                intent.putExtra("deal", deal);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDeals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView mAbsractDealCardview;
        ImageView imageview_product_image;
        TextView tv_product_name, tv_more_details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview_product_image = itemView.findViewById(R.id.imgv_product_image);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            mAbsractDealCardview = itemView.findViewById(R.id.cardview_abstract_deal);
            tv_more_details = itemView.findViewById(R.id.tv_view_more);
        }
    }
}
