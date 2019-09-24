package com.industrialmaster.farmnet.views.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Advertisement;
import com.industrialmaster.farmnet.presenters.AdvertisementPresenter;
import com.industrialmaster.farmnet.presenters.AdvertisementsPresenterImpl;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.AdvertisementView;
import com.industrialmaster.farmnet.views.activities.CreateNewAdvertisementActivity;
import com.industrialmaster.farmnet.views.adapters.AdvertisementRecyclerViewAdapter;
import com.industrialmaster.farmnet.views.adapters.QuestionRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdvertisementFragment extends BaseFragment implements AdvertisementView {

    View rootView;

    AdvertisementPresenter presenter;

    EditText et_search_ads;

    public AdvertisementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_advertisement, container, false);

        presenter = new AdvertisementsPresenterImpl(getActivity(), AdvertisementFragment.this);
        presenter.getAllAdvertisements();
        setLoading(true);

        et_search_ads = rootView.findViewById(R.id.et_search_ads);
        et_search_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateNewAdvertisementActivity.class));
            }
        });

        return rootView;
    }

    @Override
    public void showAdvertisements(List<Advertisement> advertisements) {
        setLoading(false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview_ads);
        AdvertisementRecyclerViewAdapter adapter = new AdvertisementRecyclerViewAdapter(getActivity(), advertisements);
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
