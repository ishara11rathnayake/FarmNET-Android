package com.industrialmaster.farmnet.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Advertisement;
import com.industrialmaster.farmnet.views.activities.DetailAdvertisementActivity;

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
                .into(viewHolder.mAdImageView);

        viewHolder.mADTitleTextView.setText(mAdvertisement.get(i).getAdTitle());

        if(mAdvertisement.get(i).getPrice() != null) {
            viewHolder.mPriceTextView.setText(String.format("Rs. %s", mAdvertisement.get(i).getPrice()));
        }

        viewHolder.mMoreDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailAdvertisementActivity.class);
                Gson gson = new Gson();
                String adDetails = gson.toJson(mAdvertisement.get(i));
                intent.putExtra("adDetails", adDetails);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAdvertisement.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView mAdvertisementCardView;
        ImageView mAdImageView;
        TextView mADTitleTextView;
        TextView mPriceTextView;
        Button mMoreDetailButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mAdvertisementCardView = itemView.findViewById(R.id.cradview_advertisement);
            mAdImageView = itemView.findViewById(R.id.imgv_ads_pic);
            mADTitleTextView = itemView.findViewById(R.id.tv_ad_topic);
            mPriceTextView = itemView.findViewById(R.id.tv_price);
            mMoreDetailButton = itemView.findViewById(R.id.btn_more_details);
        }
    }

}
