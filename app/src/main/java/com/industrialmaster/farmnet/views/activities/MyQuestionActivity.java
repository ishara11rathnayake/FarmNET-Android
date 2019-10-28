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

import cn.pedant.SweetAlert.SweetAlertDialog;

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
        setLoading(true);
        qandAPresenter.getQuestionByUserId();
    }

    public void deleteQuestion(String postId){
        showSweetAlert(SweetAlertDialog.WARNING_TYPE, ErrorMessageHelper.DELETE_CONFIRMATION,null,false,
                FarmnetConstants.OK , sDialog -> {
                    setLoading(true);
                    qandAPresenter.deleteQuestion(postId);
                },FarmnetConstants.CANCEL, SweetAlertDialog::dismissWithAnimation);
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
        showSweetAlert(SweetAlertDialog.ERROR_TYPE, "Oops..." , error,false, FarmnetConstants.OK ,
                SweetAlertDialog::dismissWithAnimation, null, null);
    }

    @Override
    public void onSuccess(String message) {
        setLoading(false);
        showSweetAlert(SweetAlertDialog.SUCCESS_TYPE, "Great!" ,message,false, FarmnetConstants.OK ,
                sDialog -> {
                    finish();
                    startActivity(getIntent());
                }, null, null);
    }

    @Override
    public void showMessage(String message) {

    }
}
