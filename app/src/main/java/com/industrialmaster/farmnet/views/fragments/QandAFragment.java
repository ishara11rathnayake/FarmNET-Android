package com.industrialmaster.farmnet.views.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.Resource;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Question;
import com.industrialmaster.farmnet.models.User;
import com.industrialmaster.farmnet.presenters.QandAPresenter;
import com.industrialmaster.farmnet.presenters.QandAPresenterImpl;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.QandAView;
import com.industrialmaster.farmnet.views.adapters.DealsPostRecyclerViewAdapter;
import com.industrialmaster.farmnet.views.adapters.QuestionRecyclerViewAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QandAFragment extends BaseFragment implements QandAView {

    View rootView;

    QandAPresenter qandAPresenter;

    public QandAFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_qand_a, container, false);

        qandAPresenter = new QandAPresenterImpl(getActivity(), QandAFragment.this);
        qandAPresenter.getAllQuestions();
        setLoading(true);

//        List<Question> questions = new ArrayList<>();
//        String[] hastags = {"pestiside", "bed bugs", "paddy"};
//        User user = new User("5d7de926013cc200044110d0", "ish@gmail.com");
//        user.setProfilePicUrl("https://farmnet-bucket.storage.googleapis.com/profile/156864762827336222932_2352322344791425_8535627277457686528_o.jpg");
//        user.setName("Ishara Rathnayake");
//
//        Question question = new Question("5d822ae70b1e8833f44743cf", "assd", "aasd", hastags,
//                new Date(), user, 0);
//
//        questions.add(question);



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

    @Override
    public void showErrorMessage(String calledMethod, String error, String errorDescription) {

    }
}
