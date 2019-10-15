package com.industrialmaster.farmnet.views.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.materialrangebar.RangeBar;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Deals;
import com.industrialmaster.farmnet.models.User;
import com.industrialmaster.farmnet.presenters.DealsPresenter;
import com.industrialmaster.farmnet.presenters.DealsPresenterImpl;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.DealsView;
import com.industrialmaster.farmnet.views.activities.FilterDealsActivity;
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
    ImageButton mFilterImageButton;
    ConstraintLayout mConstraintLayoutFilter;
    RangeBar mPriceRangeSeekbar;
    TextView mMinPriceTextView;
    TextView mMaxPriceTextView;
    Button mSearchButton;
    TextInputEditText mMinAmountEditText;
    TextInputEditText mMaxAmountEditText;
    TextInputEditText mUsernameEditText;
    boolean visibility = false;
    int mMaxPrice;
    int mMinPrice;
    int mMaxAmount;
    int mMinAmount;

    public DealsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_deals, container, false);

        mConstraintLayoutFilter = rootView.findViewById(R.id.constraint_layout_filter);
        mPriceRangeSeekbar = rootView.findViewById(R.id.seekbar_price_range);
        mMinPriceTextView = rootView.findViewById(R.id.text_view_min_price);
        mMaxPriceTextView = rootView.findViewById(R.id.text_view_max_price);
        mMinAmountEditText = rootView.findViewById(R.id.edit_text_amount_min);
        mMaxAmountEditText = rootView.findViewById(R.id.edit_text_amount_max);
        mUsernameEditText = rootView.findViewById(R.id.edit_text_username);
        mSearchButton = rootView.findViewById(R.id.button_search);

        mMinPriceTextView.setText(String.valueOf(mPriceRangeSeekbar.getTickInterval()));
        mMaxPriceTextView.setText(String.valueOf(mPriceRangeSeekbar.getTickEnd()));

        mPriceRangeSeekbar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex, String leftPinValue, String rightPinValue) {
                mMaxPriceTextView.setText(String.valueOf(rightPinIndex));
                mMinPriceTextView.setText(String.valueOf(leftPinIndex));
            }

            @Override
            public void onTouchStarted(RangeBar rangeBar) {

            }

            @Override
            public void onTouchEnded(RangeBar rangeBar) {
                mMaxPrice = rangeBar.getRightIndex();
                mMinPrice = rangeBar.getLeftIndex();
            }

        });

        mSearchButton.setOnClickListener(v -> {
            if(!TextUtils.isEmpty(mMinAmountEditText.getText())){
                mMinAmount = Integer.parseInt(mMinAmountEditText.getText().toString());
            }

            if(!TextUtils.isEmpty(mMaxAmountEditText.getText())){
                mMaxAmount = Integer.parseInt(mMaxAmountEditText.getText().toString());
            }

            dealsPresenter.filterDeals(mMinPrice, mMaxPrice, mMinAmount, mMaxAmount);
            mConstraintLayoutFilter.setVisibility(View.GONE);
            setLoading(true);
        });

        mFilterImageButton = rootView.findViewById(R.id.image_button_filter);

        mFilterImageButton.setOnClickListener(v -> {

            visibility = !visibility;
            if(visibility){
                mConstraintLayoutFilter.setVisibility(View.VISIBLE);
            }else {
                mConstraintLayoutFilter.setVisibility(View.GONE);
            }

        });

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

}
