package com.industrialmaster.farmnet.views.adapters;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Question;
import com.industrialmaster.farmnet.views.activities.AnswerActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        return new QuestionRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called");

        Date date = mQuestions.get(i).getDate();
        DateFormat targetDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
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
            viewHolder.tv_description.setText(mQuestions.get(i).getDescription());
        }

        viewHolder.tv_no_of_answers.setText(String.format("%s Answers", Integer.toString(mQuestions.get(i).getNumberOfAnswers())));

        String[] hastags = mQuestions.get(i).getHashtags();

        if(hastags.length != 0) {

            final TextView[] myTextViews = new TextView[hastags.length];

            for (String hastag : hastags) {
                final TextView rowTextView = new TextView(mContext);
                rowTextView.setText(String.format("#%s", hastag));
                viewHolder.linear_layout_hashtags.addView(rowTextView);

                Typeface typeface = ResourcesCompat.getFont(mContext, R.font.ubunturegular);
                rowTextView.setTypeface(typeface);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                params.setMargins(5, 0, 5, 0);
                rowTextView.setLayoutParams(params);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    rowTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }

                rowTextView.setPadding(10, 5, 10, 5);

                rowTextView.setTextColor(mContext.getResources().getColor(R.color.white));

                rowTextView.setBackground(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.tags_bg, null));
//                myTextViews[i] = rowTextView;
            }

        }

        viewHolder.mAnswerImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AnswerActivity.class);
                Gson gson = new Gson();
                String question = gson.toJson(mQuestions.get(i));
                intent.putExtra("question", question);
                mContext.startActivity(intent);
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
        ImageButton mAnswerImageButton;

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
            mAnswerImageButton = itemView.findViewById(R.id.img_btn_answer);
        }
    }
}
