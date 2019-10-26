package com.industrialmaster.farmnet.views.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;

import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Question;
import com.industrialmaster.farmnet.presenters.QandAPresenter;
import com.industrialmaster.farmnet.presenters.QandAPresenterImpl;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.MyQuestionsView;
import com.industrialmaster.farmnet.views.adapters.MyQuestionRecyclerViewAdapter;
import java.util.List;

public class MyQuestionActivity extends BaseActivity implements MyQuestionsView {

    QandAPresenter qandAPresenter;
    ImageButton mCloseImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_question);

        mCloseImageButton = findViewById(R.id.img_btn_close);
        mCloseImageButton.setOnClickListener(v -> finish());

        qandAPresenter = new QandAPresenterImpl(this, this);
        qandAPresenter.getQuestionByUserId();
    }

    public void deleteQuestion(String postId){
        showAlertDialog("Warning", ErrorMessageHelper.DELETE_CONFIRMATION,false, FarmnetConstants.OK ,
                (dialog, which) -> {
                    setLoading(true);
                    qandAPresenter.deleteQuestion(postId);
                },
                FarmnetConstants.CANCEL, (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void showMyQuestions(List<Question> questions) {
        setLoading(false);
        RecyclerView recyclerView = findViewById(R.id.recyclerview_my_questions);
        MyQuestionRecyclerViewAdapter adapter = new MyQuestionRecyclerViewAdapter(this, questions);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onError(String error) {
        setLoading(false);
        showAlertDialog("Error", error,false, FarmnetConstants.OK , (dialog, which) -> {},
                "", (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void onSuccess(String message) {
        setLoading(false);
        showAlertDialog("Success", message,false, FarmnetConstants.OK ,
                (dialog, which) -> {
                        finish();
                        startActivity(getIntent());
                    },
                "", (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void showMessage(String message) {

    }
}
