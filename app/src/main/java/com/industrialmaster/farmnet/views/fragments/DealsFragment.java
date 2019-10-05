package com.industrialmaster.farmnet.views.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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

    View rootView;
    EditText mSearchEditText;

    public DealsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_deals, container, false);

        mSearchEditText = rootView.findViewById(R.id.etsearchdeals);
        mSearchEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (mSearchEditText.getRight() - mSearchEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        setLoading(true);
                        String searchText = mSearchEditText.getText().toString();

                        if(!TextUtils.isEmpty(searchText)){
                            dealsPresenter.searchProduct(searchText);
                            return true;
                        }else {
                            dealsPresenter.getAllDeals();
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        dealsPresenter = new DealsPresenterImpl(getActivity(), DealsFragment.this);
        dealsPresenter.getAllDeals();
        setLoading(true);

        return rootView;
    }


    @Override
    public void showDeals(List<Deals> deals) {
        setLoading(false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview_product_post);
        DealsPostRecyclerViewAdapter adapter = new DealsPostRecyclerViewAdapter(getActivity(), deals);
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
