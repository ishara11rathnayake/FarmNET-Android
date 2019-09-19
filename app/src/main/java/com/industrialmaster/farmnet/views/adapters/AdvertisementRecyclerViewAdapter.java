package com.industrialmaster.farmnet.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Advertisement;

import java.util.List;

public class AdvertisementRecyclerViewAdapter extends RecyclerView.Adapter<AdvertisementRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "AdvertisementRVAdapter";

    private Context mContext;
    private List<Advertisement> mAdvertisement;

    public AdvertisementRecyclerViewAdapter(Context mContext, List<Advertisement> mAdvertisement) {
        this.mContext = mContext;
        this.mAdvertisement = mAdvertisement;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_advertisement_view, viewGroup,false);
        AdvertisementRecyclerViewAdapter.ViewHolder holder = new AdvertisementRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Glide.with(mContext)
                .asBitmap()
                .load(mAdvertisement.get(i).getImageUrl())
                .centerInside()
                .into(viewHolder.imgv_ad_pic);

        viewHolder.tv_ad_topic.setText(mAdvertisement.get(i).getAdTitle());

        if(mAdvertisement.get(i).getPrice() != null) {
            viewHolder.tv_price.setText("Rs. " + Double.toString(mAdvertisement.get(i).getPrice()));
        }
    }

    @Override
    public int getItemCount() {
        return mAdvertisement.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView advertisement_card_view;
        ImageView imgv_ad_pic;
        TextView tv_ad_topic;
        TextView tv_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            advertisement_card_view = itemView.findViewById(R.id.cradview_advertisement);
            imgv_ad_pic = itemView.findViewById(R.id.imgv_ads_pic);
            tv_ad_topic = itemView.findViewById(R.id.tv_ad_topic);
            tv_price = itemView.findViewById(R.id.tv_price);
        }
    }

}
