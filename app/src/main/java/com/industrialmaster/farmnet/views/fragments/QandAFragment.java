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
import com.industrialmaster.farmnet.views.adapters.DealsPostRecyclerViewAdapter;
import com.industrialmaster.farmnet.views.adapters.QuestionRecyclerViewAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QandAFragment extends Fragment {

    View rootView;

    public QandAFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_qand_a, container, false);

        List<Question> questions = new ArrayList<>();
        String[] hastags = {"pestiside", "bed bugs", "paddy"};
        User user = new User("5d7de926013cc200044110d0", "ish@gmail.com");
        user.setProfilePicUrl("https://farmnet-bucket.storage.googleapis.com/profile/156864762827336222932_2352322344791425_8535627277457686528_o.jpg");
        user.setName("Ishara Rathnayake");

        Question question = new Question("5d822ae70b1e8833f44743cf", "assd", "aasd", hastags,
                new Date(), user, 0);

        questions.add(question);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview_question);
        QuestionRecyclerViewAdapter adapter = new QuestionRecyclerViewAdapter(getActivity(), questions);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

}
