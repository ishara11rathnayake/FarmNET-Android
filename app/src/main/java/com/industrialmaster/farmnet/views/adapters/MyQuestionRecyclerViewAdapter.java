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
import com.industrialmaster.farmnet.views.activities.CreateNewQuestionActivity;
import com.industrialmaster.farmnet.views.activities.MyQuestionActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyQuestionRecyclerViewAdapter extends RecyclerView.Adapter<MyQuestionRecyclerViewAdapter.QuestionViewHolder> {

    private static final String TAG = "MyQuestionRecyclerViewAdapter";

    private List<Question> mQuestions;
    private Context mContext;

    public MyQuestionRecyclerViewAdapter(Context mContext, List<Question> mQuestions) {
        this.mQuestions = mQuestions;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_question_delete_view, viewGroup,false);
        return new MyQuestionRecyclerViewAdapter.QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder questionViewHolder, int i) {

        Question mQuestion = mQuestions.get(i);

        Date date = mQuestion.getDate();
        DateFormat targetDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
        String formattedDate = targetDateFormat.format(date);

        Glide.with(mContext)
                .asBitmap()
                .load(mQuestion.getUser().getProfilePicUrl())
                .centerCrop()
                .into(questionViewHolder.mProfilePicCircleImageView);

        questionViewHolder.mNameTextView.setText(mQuestion.getUser().getName());
        questionViewHolder.mDateTextView.setText(formattedDate);
        questionViewHolder.mQuestionTextView.setText(mQuestion.getQuetion());

        if(mQuestions.get(i).getDescription().isEmpty() || mQuestions.get(i).getDescription() == null){
            questionViewHolder.mDescriptionTextView.setVisibility(View.GONE);
        } else {
            questionViewHolder.mDescriptionTextView.setText(mQuestion.getDescription());
        }

        questionViewHolder.mNoOfAnswersTextView.setText(String.format("%s Answers", Integer.toString(mQuestion.getNumberOfAnswers())));

        String[] hastags = mQuestion.getHashtags();

        if(hastags.length != 0) {

            final TextView[] myTextViews = new TextView[hastags.length];

            for (String hastag : hastags) {
                final TextView rowTextView = new TextView(mContext);
                rowTextView.setText(String.format("#%s", hastag));
                questionViewHolder.mHastagsLinearLayout.addView(rowTextView);

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

        questionViewHolder.mEditImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CreateNewQuestionActivity.class);
                Gson gson = new Gson();
                String question = gson.toJson(mQuestions.get(i));
                intent.putExtra("question", question);
                mContext.startActivity(intent);
            }
        });

        questionViewHolder.mDeleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyQuestionActivity)mContext).deleteQuestion(mQuestions.get(i).getQuestionId());
            }
        });

        questionViewHolder.mAnswerImageButton.setOnClickListener(new View.OnClickListener() {
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


    public class QuestionViewHolder extends RecyclerView.ViewHolder{

        CardView mQuestionCardView;
        CircleImageView mProfilePicCircleImageView;
        TextView mNameTextView;
        TextView mDateTextView;
        TextView mQuestionTextView;
        TextView mDescriptionTextView;
        LinearLayout mHastagsLinearLayout;
        TextView mNoOfAnswersTextView;
        ImageButton mDeleteImageButton;
        ImageButton mEditImageButton;
        ImageButton mAnswerImageButton;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            mQuestionCardView = itemView.findViewById(R.id.cradview_question);
            mProfilePicCircleImageView = itemView.findViewById(R.id.cimageview_profilepic);
            mNameTextView = itemView.findViewById(R.id.tv_name);
            mDateTextView = itemView.findViewById(R.id.tv_date);
            mQuestionTextView = itemView.findViewById(R.id.tv_question);
            mDescriptionTextView = itemView.findViewById(R.id.tv_description);
            mHastagsLinearLayout = itemView.findViewById(R.id.linear_layout_hashtags);
            mNoOfAnswersTextView = itemView.findViewById(R.id.tv_no_of_answers);
            mDeleteImageButton = itemView.findViewById(R.id.img_btn_delete);
            mEditImageButton = itemView.findViewById(R.id.img_btn_edit);
            mAnswerImageButton = itemView.findViewById(R.id.img_btn_answer);
        }
    }

}
