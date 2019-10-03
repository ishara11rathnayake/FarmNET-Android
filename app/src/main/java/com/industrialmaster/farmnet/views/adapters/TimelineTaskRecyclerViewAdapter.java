package com.industrialmaster.farmnet.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Task;
import com.industrialmaster.farmnet.models.Timeline;
import com.industrialmaster.farmnet.utils.FarmnetConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TimelineTaskRecyclerViewAdapter extends RecyclerView.Adapter<TimelineTaskRecyclerViewAdapter.BaseViewHolder>{

    private static final String TAG = "TimelineListRVAdapter";

    private Context mContext;
    private List<Task> mTask;
    private String mProfileImage;

    public TimelineTaskRecyclerViewAdapter(Context mContext, List<Task> mTask, String profileUrl) {
        this.mContext = mContext;
        this.mTask = mTask;
        this.mProfileImage = profileUrl;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if(i == FarmnetConstants.ONLY_TEXT_ITEM_VIEW){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_timeline_item_view, viewGroup,false);
            return new TimelineTaskRecyclerViewAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_timeline_item_view_with_image, viewGroup,false);
            return new TimelineTaskRecyclerViewAdapter.ImageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder viewHolder, int i) {
        Task task = mTask.get(i);

        viewHolder.setData(task);

    }

    @Override
    public int getItemCount() {
        return mTask.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(TextUtils.isEmpty(mTask.get(position).getImageUrl())) {
            return FarmnetConstants.ONLY_TEXT_ITEM_VIEW;
        }else {
            return FarmnetConstants.IMAGE_ITEM_VIEW;
        }
    }

    public abstract class BaseViewHolder extends RecyclerView.ViewHolder{

        abstract void setData(Task task);

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }

    public class ViewHolder extends BaseViewHolder{

        TextView tv_timiline_date, tv_content;
        CircleImageView cimgv_profile_image;

        @Override
        void setData(Task task) {

            tv_content.setText(task.getContent());
            Glide.with(mContext)
                    .asBitmap()
                    .load(mProfileImage)
                    .centerInside()
                    .into(cimgv_profile_image);

            Date date = task.getDate();
            @SuppressLint("SimpleDateFormat") DateFormat targetDateFormat = new SimpleDateFormat("MM/dd");
            String formattedDate = targetDateFormat.format(date);
            tv_timiline_date.setText(formattedDate);

        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_timiline_date = itemView.findViewById(R.id.tv_timiline_date);
            tv_content = itemView.findViewById(R.id.tv_content);
            cimgv_profile_image = itemView.findViewById(R.id.cimgv_profile_image);
        }
    }

    public class ImageViewHolder extends BaseViewHolder{

        TextView tv_timiline_date, tv_content;
        CircleImageView cimgv_profile_image;
        ImageView imgv_task_image;

        @Override
        void setData(Task task) {
            tv_content.setText(task.getContent());
            Glide.with(mContext)
                    .asBitmap()
                    .load(mProfileImage)
                    .centerInside()
                    .into(cimgv_profile_image);

            Glide.with(mContext)
                    .asBitmap()
                    .load(task.getImageUrl())
                    .centerInside()
                    .into(imgv_task_image);

            Date date = task.getDate();
            DateFormat targetDateFormat = new SimpleDateFormat("MM/dd");
            String formattedDate = targetDateFormat.format(date);
            tv_timiline_date.setText(formattedDate);
        }

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_timiline_date = itemView.findViewById(R.id.tv_timiline_date);
            tv_content = itemView.findViewById(R.id.tv_content);
            cimgv_profile_image = itemView.findViewById(R.id.cimgv_profile_image);
            imgv_task_image = itemView.findViewById(R.id.imgv_task);
        }
    }
}
