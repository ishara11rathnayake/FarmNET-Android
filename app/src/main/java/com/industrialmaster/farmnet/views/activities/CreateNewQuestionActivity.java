package com.industrialmaster.farmnet.views.activities;


import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.request.CreateNewQuestionRequest;
import com.industrialmaster.farmnet.presenters.QandAPresenter;
import com.industrialmaster.farmnet.presenters.QandAPresenterImpl;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.CreateNewQuestionView;

public class CreateNewQuestionActivity extends BaseActivity implements CreateNewQuestionView {

    QandAPresenter presenter;

    ImageButton btn_img_close;
    Button btn_create_new_question;

    TextInputEditText et_question_title, et_question_body, et_tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_question);

        presenter = new QandAPresenterImpl(this, CreateNewQuestionActivity.this);

        btn_img_close = findViewById(R.id.img_btn_close);
        btn_create_new_question = findViewById(R.id.btn_create_new_question);

        et_question_title = findViewById(R.id.et_question_title);
        et_question_body = findViewById(R.id.et_question_body);
        et_tags = findViewById(R.id.et_question_tags);

        btn_img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = ErrorMessageHelper.DISCARD_CONFIRMATION;
                showAlertDialog("Warning", message,false, FarmnetConstants.OK , (dialog, which) -> {
                    finish();
                },FarmnetConstants.CANCEL, (dialog, which) -> dialog.dismiss());
            }
        });

        btn_create_new_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewQuestionRequest newQuestionRequest = new CreateNewQuestionRequest();
                newQuestionRequest.setQuestionTitle(et_question_title.getText().toString());
                newQuestionRequest.setQuestionBody(et_question_body.getText().toString());

                String tags = et_tags.getText().toString();
                String[] tagsArray = tags.split(" ");

                newQuestionRequest.setHashtags(tagsArray);

                presenter.createNewQuestion(newQuestionRequest);
            }
        });
    }

    @Override
    public void onSuccess(String message) {
        showAlertDialog("Success", message,false, FarmnetConstants.OK ,
                (dialog, which) -> {
                    finish();
                },
                "", (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void onError(String message) {
        showAlertDialog("Error", message,false, FarmnetConstants.OK , (dialog, which) -> {},
                "", (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showErrorMessage(String calledMethod, String error, String errorDescription) {

    }
}
