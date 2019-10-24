package com.industrialmaster.farmnet.views.activities;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Answer;
import com.industrialmaster.farmnet.models.Question;
import com.industrialmaster.farmnet.models.User;
import com.industrialmaster.farmnet.presenters.AnswerPresenter;
import com.industrialmaster.farmnet.presenters.AnswerPresenterImpl;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.AnswerView;
import com.industrialmaster.farmnet.views.adapters.AnswersRecyclwerViewAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnswerActivity extends BaseActivity implements AnswerView {

    AnswerPresenter answerPresenter;

    ImageButton mCloseImageButton;
    ImageButton mSendImageButton;
    RecyclerView mAnswersRecyclerView;
    EditText mAnswerEditText;
    Question question;
    String questionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        answerPresenter = new AnswerPresenterImpl(this, this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference answerRef = database.getReference("answers");

        Gson gson = new Gson();
        question = gson.fromJson(getIntent().getStringExtra("question"), Question.class);
        questionId = question.getQuestionId();

        mCloseImageButton = findViewById(R.id.img_btn_close);
        mAnswersRecyclerView = findViewById(R.id.recyclerview_answers);
        mAnswerEditText = findViewById(R.id.et_write_answer);
        mSendImageButton = findViewById(R.id.img_btn_send);

        mCloseImageButton.setOnClickListener(v -> finish());

        mSendImageButton.setOnClickListener(v -> answerPresenter.getAnsweringUserDetails());

        answerRef.child(questionId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Answer> answers = new ArrayList<>();
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    Answer answer = dsp.getValue(Answer.class);
                    answers.add(answer);
                }
                showAllAnswers(answers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onError(String message) {
        setLoading(false);
        showAlertDialog("Error", message,false, FarmnetConstants.OK , (dialog, which) -> {},
                "", (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void showAllAnswers(List<Answer> answers) {
        setLoading(false);
        AnswersRecyclwerViewAdapter adapter = new AnswersRecyclwerViewAdapter(AnswerActivity.this, answers);
        mAnswersRecyclerView.setAdapter(adapter);
        mAnswersRecyclerView.setLayoutManager(new LinearLayoutManager(AnswerActivity.this));
    }

    @Override
    public void saveAnswer(User user) {
        Answer answer = new Answer();
        String content = mAnswerEditText.getText().toString();
        answer.setContent(content);
        answer.setDate(new Date());
        answer.setUser(user);

        answerPresenter.addNewAnswer(questionId, answer);
    }

    @Override
    public void onSuccess(String message) {
        setLoading(false);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        mAnswerEditText.getText().clear();
    }

    @Override
    public void showMessage(String message) {

    }
}
