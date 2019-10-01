package com.industrialmaster.farmnet.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Timeline;
import com.industrialmaster.farmnet.views.activities.TimelineActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TimelineListRecyclerViewAdapter extends RecyclerView.Adapter<TimelineListRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "AdvertisementRVAdapter";

    private Context mContext;
    private List<Timeline> mTimeline;

    public TimelineListRecyclerViewAdapter(Context mContext, List<Timeline> mTimeline) {
        this.mContext = mContext;
        this.mTimeline = mTimeline;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_timeline_list_view, viewGroup,false);
        TimelineListRecyclerViewAdapter.ViewHolder holder = new TimelineListRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Timeline timeline = mTimeline.get(i);
        viewHolder.tv_product_name.setText(timeline.getProductName());
        viewHolder.tv_description.setText(timeline.getDescription());

        Date date = timeline.getTasks().get(0).getDate();
        DateFormat targetDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = targetDateFormat.format(date);

        viewHolder.tv_date.setText(formattedDate);

        viewHolder.constraint_layout_timeline_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TimelineActivity.class);
                Gson gson = new Gson();
                String timeline = gson.toJson(mTimeline.get(i));
                intent.putExtra("timeline", timeline);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTimeline.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_product_name, tv_description, tv_date;
        ConstraintLayout constraint_layout_timeline_list;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_date = itemView.findViewById(R.id.tv_date);
            constraint_layout_timeline_list = itemView.findViewById(R.id.constraint_layout_timeline_list);
        }
    }
}
