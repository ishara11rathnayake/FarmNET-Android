package com.industrialmaster.farmnet.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Answer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnswersRecyclwerViewAdapter extends RecyclerView.Adapter<AnswersRecyclwerViewAdapter.AnswerViewHolder>{

    private List<Answer> mAnswers;
    private Context mContext;

    public AnswersRecyclwerViewAdapter(Context mContext, List<Answer> mAnswers) {
        this.mAnswers = mAnswers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_answer_view, viewGroup,false);
        return new AnswersRecyclwerViewAdapter.AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder answerViewHolder, int i) {
        Answer answer = mAnswers.get(i);
        String formattedDate;
        String time;
        DateFormat targetDateFormat;

        if(!TextUtils.isEmpty(answer.getUser().getProfilePicUrl())){
            Glide.with(mContext)
                    .asBitmap()
                    .load(answer.getUser().getProfilePicUrl())
                    .centerCrop()
                    .into(answerViewHolder.mProfileImageCircleImageView);
        }

        answerViewHolder.mUsernameTextView.setText(answer.getUser().getName());
        answerViewHolder.mContentTextView.setText(answer.getContent());

        Date date = answer.getDate();
        targetDateFormat = new SimpleDateFormat("MM/dd");
        formattedDate = targetDateFormat.format(date);

        targetDateFormat = new SimpleDateFormat("HH:mm");
        time = targetDateFormat.format(date);

        answerViewHolder.mDateTextView.setText(formattedDate);
        answerViewHolder.mTimeTextView.setText(time);

    }

    @Override
    public int getItemCount() {
        return mAnswers.size();
    }

    public class AnswerViewHolder extends RecyclerView.ViewHolder {

        CircleImageView mProfileImageCircleImageView;
        TextView mUsernameTextView;
        TextView mContentTextView;
        TextView mDateTextView;
        TextView mTimeTextView;

        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            mProfileImageCircleImageView = itemView.findViewById(R.id.cimgv_profile_image);
            mUsernameTextView = itemView.findViewById(R.id.tv_username);
            mContentTextView = itemView.findViewById(R.id.tv_content);
            mDateTextView = itemView.findViewById(R.id.tv_date);
            mTimeTextView = itemView.findViewById(R.id.tv_time);
        }
    }
}
