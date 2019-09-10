package com.industrialmaster.farmnet.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Deals;
import com.industrialmaster.farmnet.models.User;
import com.industrialmaster.farmnet.presenters.DealsPresenter;
import com.industrialmaster.farmnet.presenters.DealsPresenterImpl;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.DealsView;
import com.industrialmaster.farmnet.views.activities.LoginActivity;
import com.industrialmaster.farmnet.views.activities.MainActivity;
import com.industrialmaster.farmnet.views.adapters.DealsPostRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DealsFragment extends BaseFragment implements DealsView {

    private static final String TAG = "DealFragment";

    DealsPresenter dealsPresenter;

    private List<Deals> mDeals = new ArrayList<>();

    View rootView;

    public DealsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_deals, container, false);

        dealsPresenter = new DealsPresenterImpl(getActivity(), DealsFragment.this);
        dealsPresenter.getAllDeals();
        setLoading(true);

        return rootView;
    }

    @Override
    public void showDeals(List<Deals> deals) {
        setLoading(false);
        mDeals.addAll(deals);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview_product_post);
        DealsPostRecyclerViewAdapter adapter = new DealsPostRecyclerViewAdapter(getActivity(), mDeals);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onError(String error) {
        setLoading(false);
        showAlertDialog("Success", error,false, FarmnetConstants.OK , (dialog, which) -> {},"", (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showErrorMessage(String calledMethod, String error, String errorDescription) {

    }
}
