package com.industrialmaster.farmnet.views.activities;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.appyvet.materialrangebar.RangeBar;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Deals;
import com.industrialmaster.farmnet.presenters.DealsPresenter;
import com.industrialmaster.farmnet.presenters.DealsPresenterImpl;

import java.util.List;

public class FilterDealsActivity extends BaseActivity{

    RangeBar mPriceRangeSeekbar;
    TextView mMinPriceTextView;
    TextView mMaxPriceTextView;
    ImageButton mCloseImageButton;
    Button mSearchButton;
    TextInputEditText mMinAmountEditText;
    TextInputEditText mMaxAmountEditText;
    TextInputEditText mUsernameEditText;
    int mMaxPrice;
    int mMinPrice;
    int mMaxAmount;
    int mMinAmount;

    DealsPresenter dealsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_deals);

        mPriceRangeSeekbar = findViewById(R.id.seekbar_price_range);
        mMinPriceTextView = findViewById(R.id.text_view_min_price);
        mMaxPriceTextView = findViewById(R.id.text_view_max_price);
        mCloseImageButton = findViewById(R.id.image_button_close);
        mMinAmountEditText = findViewById(R.id.edit_text_amount_min);
        mMaxAmountEditText = findViewById(R.id.edit_text_amount_max);
        mUsernameEditText = findViewById(R.id.edit_text_username);
        mSearchButton = findViewById(R.id.button_search);

        mCloseImageButton.setOnClickListener(v -> finish());

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

        });

    }
}
