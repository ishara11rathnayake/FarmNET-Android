package com.industrialmaster.farmnet.views.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Question;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class QuestionRecyclerViewAdapter extends RecyclerView.Adapter<QuestionRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "QuestionRVAdapter";

    private List<Question> mQuestions;
    private Context mContext;

    public QuestionRecyclerViewAdapter(Context mContext, List<Question> mQuestions) {
        this.mQuestions = mQuestions;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_question_view, viewGroup,false);
        QuestionRecyclerViewAdapter.ViewHolder holder = new QuestionRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called");

        Date date = mQuestions.get(i).getDate();
        DateFormat targetDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        String formattedDate = targetDateFormat.format(date);

        Glide.with(mContext)
                .asBitmap()
                .load(mQuestions.get(i).getUser().getProfilePicUrl())
                .centerCrop()
                .into(viewHolder.circleImageView_profile_pic);

        viewHolder.tv_user_name.setText(mQuestions.get(i).getUser().getName());
        viewHolder.tv_date.setText(formattedDate);
        viewHolder.tv_question.setText(mQuestions.get(i).getQuetion());

        if(mQuestions.get(i).getDescription().isEmpty() || mQuestions.get(i).getDescription() == null){
            viewHolder.tv_description.setVisibility(View.GONE);
        } else {
            viewHolder.tv_question.setText(mQuestions.get(i).getQuetion());
        }

        viewHolder.tv_no_of_answers.setText(Integer.toString(mQuestions.get(i).getNumberOfAnswers())+" Answers");

        String[] hastags = mQuestions.get(i).getHashtags();

        if(hastags.length != 0) {

            final TextView[] myTextViews = new TextView[hastags.length];

            for (int x = 0; x < hastags.length; x++) {
                final TextView rowTextView = new TextView(mContext);
                rowTextView.setText("#" + hastags[x]);
                viewHolder.linear_layout_hashtags.addView(rowTextView);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    rowTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }

                rowTextView.setPadding(10, 5, 10, 5);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    rowTextView.setBackground(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.custom_text_box, null));
                }
                myTextViews[i] = rowTextView;
            }

        }

        viewHolder.question_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + mQuestions.get(i).getQuestionId());

                Toast.makeText(mContext, mQuestions.get(i).getQuestionId(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mQuestions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView question_card_view;
        CircleImageView circleImageView_profile_pic;
        TextView tv_user_name;
        TextView tv_date;
        TextView tv_question;
        TextView tv_description;
        LinearLayout linear_layout_hashtags;
        TextView tv_no_of_answers;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            question_card_view = itemView.findViewById(R.id.cradview_question);
            circleImageView_profile_pic = itemView.findViewById(R.id.cimageview_profilepic);
            tv_user_name = itemView.findViewById(R.id.tv_name);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_question = itemView.findViewById(R.id.tv_question);
            tv_description = itemView.findViewById(R.id.tv_description);
            linear_layout_hashtags = itemView.findViewById(R.id.linear_layout_hashtags);
            tv_no_of_answers = itemView.findViewById(R.id.tv_no_of_answers);
        }
    }
}
