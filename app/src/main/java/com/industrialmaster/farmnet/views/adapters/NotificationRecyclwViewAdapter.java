package com.industrialmaster.farmnet.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Notification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationRecyclwViewAdapter extends  RecyclerView.Adapter<NotificationRecyclwViewAdapter.NotificationViewHolder>{

    private List<Notification> mNotification;
    private Context mContext;

    public NotificationRecyclwViewAdapter(Context mContext, List<Notification> mNotification) {
        this.mNotification = mNotification;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_notification_view, viewGroup,false);
        return new NotificationRecyclwViewAdapter.NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, int i) {
        Notification notification = mNotification.get(i);
        String formattedDate;
        String time;
        DateFormat targetDateFormat;

        if(!TextUtils.isEmpty(notification.getUser().getProfilePicUrl())){
            Glide.with(mContext)
                    .asBitmap()
                    .load(notification.getUser().getProfilePicUrl())
                    .centerCrop()
                    .into(notificationViewHolder.mProfileImageCircleImageView);
        }

        String content = notification.getContent();
        notificationViewHolder.mContentTextView.setText(Html.fromHtml(content));

        Date date = notification.getDate();
        targetDateFormat = new SimpleDateFormat("MM/dd");
        formattedDate = targetDateFormat.format(date);

        targetDateFormat = new SimpleDateFormat("HH:mm");
        time = targetDateFormat.format(date);

        notificationViewHolder.mDateTextView.setText(formattedDate);
        notificationViewHolder.mTimeTextView.setText(time);
    }

    @Override
    public int getItemCount() {
        return mNotification.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        CircleImageView mProfileImageCircleImageView;
        TextView mContentTextView;
        TextView mDateTextView;
        TextView mTimeTextView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            mProfileImageCircleImageView = itemView.findViewById(R.id.cimgv_profile_image);
            mContentTextView = itemView.findViewById(R.id.tv_content);
            mDateTextView = itemView.findViewById(R.id.tv_date);
            mTimeTextView = itemView.findViewById(R.id.tv_time);
        }
    }
}
