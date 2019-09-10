package com.industrialmaster.farmnet.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.views.adapters.DealsPostRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DealsFragment extends Fragment {

    private static final String TAG = "DealFragment";

    private ArrayList<String> mIamgeUrls = new ArrayList<>();
    private ArrayList<String> mUserNames = new ArrayList<>();
    private ArrayList<String> mUnitPrices = new ArrayList<>();

    public DealsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_deals, container, false);

        initImageBitmaps();

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview_product_post);
        DealsPostRecyclerViewAdapter adapter = new DealsPostRecyclerViewAdapter(getActivity(), mIamgeUrls, mUserNames, mUnitPrices);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps");

        mIamgeUrls.add("https://farmnet-bucket.storage.googleapis.com/product/1561480120372redis.png");
        mUserNames.add("Sandalu Kalpanee");
        mUnitPrices.add("Rs:2500.00");

        mIamgeUrls.add("https://farmnet-bucket.storage.googleapis.com/product/1561480120372redis.png");
        mUserNames.add("Ishara Rathnayake");
        mUnitPrices.add("Rs:2450.00");
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
    }
}
