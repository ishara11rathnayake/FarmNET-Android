package com.industrialmaster.farmnet.views.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Question;
import com.industrialmaster.farmnet.models.User;
import com.industrialmaster.farmnet.presenters.QandAPresenter;
import com.industrialmaster.farmnet.presenters.QandAPresenterImpl;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.QandAView;
import com.industrialmaster.farmnet.views.activities.CreateNewQuestionActivity;
import com.industrialmaster.farmnet.views.adapters.DealsPostRecyclerViewAdapter;
import com.industrialmaster.farmnet.views.adapters.QuestionRecyclerViewAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class QandAFragment extends BaseFragment implements QandAView {

    View rootView;

    QandAPresenter qandAPresenter;

    EditText mNewQuestionEditText;
    EditText mSearchQuizEditText;
    CircleImageView mProfilePicCircleProfilePic;

    String mProfilePic;
    private static final String FARMNET_PREFS = "FarmnetPrefsFile";

    public QandAFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_qand_a, container, false);

        qandAPresenter = new QandAPresenterImpl(getActivity(), QandAFragment.this);
        qandAPresenter.getAllQuestions();
        setLoading(true);

        mProfilePic = getActivity().getSharedPreferences(FARMNET_PREFS, Context.MODE_PRIVATE)
                .getString(FarmnetConstants.PROFILE_PIC, "");

        mNewQuestionEditText = rootView.findViewById(R.id.et_question);
        mSearchQuizEditText = rootView.findViewById(R.id.edit_text_search_quiz);
        mProfilePicCircleProfilePic = rootView.findViewById(R.id.cimageview_profilepic);

        if(!TextUtils.isEmpty(mProfilePic)){
            Glide.with(this)
                    .asBitmap()
                    .load(mProfilePic)
                    .centerCrop()
                    .into(mProfilePicCircleProfilePic);
        }

        //search questions
        mSearchQuizEditText.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= (mSearchQuizEditText.getRight() - mSearchQuizEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    setLoading(true);
                    String searchText = mSearchQuizEditText.getText().toString();

                    if(!TextUtils.isEmpty(searchText)){
                        qandAPresenter.searchQuestions(mSearchQuizEditText.getText().toString());
                        return true;
                    }else {
                        qandAPresenter.getAllQuestions();
                        return true;
                    }
                }
            }
            return false;
        });

        //directed to new question activity
        mNewQuestionEditText.setOnClickListener(v -> startActivity(new Intent(getActivity(), CreateNewQuestionActivity.class)));

        return rootView;
    }

    @Override
    public void showQuestions(List<Question> questions) {
        setLoading(false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview_question);
        QuestionRecyclerViewAdapter adapter = new QuestionRecyclerViewAdapter(getActivity(), questions);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onError(String error) {
        setLoading(false);
        showAlertDialog("Error", error,false, FarmnetConstants.OK , (dialog, which) -> {},
                "", (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void showMessage(String message) {

    }

}
