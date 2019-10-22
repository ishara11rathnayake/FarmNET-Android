package com.industrialmaster.farmnet.views.activities;

import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageButton;

import com.appyvet.materialrangebar.RangeBar;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.User;
import com.industrialmaster.farmnet.presenters.AuthPresenter;
import com.industrialmaster.farmnet.presenters.AuthPresenterImpl;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.FilterUserView;
import com.industrialmaster.farmnet.views.adapters.SearchUserRecyclerViewAdapter;

import java.util.List;

public class FilterUserActivity extends BaseActivity implements FilterUserView {

    RangeBar mRatingRangeSeekbar;
    ImageButton mCloseImageButton;
    Button mSearchButton;
    TextInputEditText mUsernameEditText;
    RecyclerView mUserListRecyclerView;
    int mMinrating = 0;

    AuthPresenter authPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_user);

        mRatingRangeSeekbar = findViewById(R.id.seekbar_rating_range);
        mCloseImageButton = findViewById(R.id.image_button_close);
        mUsernameEditText = findViewById(R.id.edit_text_username);
        mSearchButton = findViewById(R.id.button_search);
        mUserListRecyclerView = findViewById(R.id.recyclerview_user_list);

        authPresenter = new AuthPresenterImpl(this,this);

        mCloseImageButton.setOnClickListener(v -> finish());

        mRatingRangeSeekbar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex, String leftPinValue, String rightPinValue) {

            }

            @Override
            public void onTouchStarted(RangeBar rangeBar) {

            }

            @Override
            public void onTouchEnded(RangeBar rangeBar) {
                mMinrating = rangeBar.getRightIndex();
            }

        });

        mSearchButton.setOnClickListener(v -> {
            setLoading(true);
            authPresenter.searchUser(mUsernameEditText.getText().toString(), mMinrating);
        });

    }

    @Override
    public void showUserList(List<User> userList) {
        setLoading(false);
        RecyclerView recyclerView = findViewById(R.id.recyclerview_user_list);
        SearchUserRecyclerViewAdapter adapter = new SearchUserRecyclerViewAdapter(this, userList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onError(String error) {
        setLoading(false);
        showAlertDialog("Error", error,false, FarmnetConstants.OK , (dialog, which) -> {},
                "", (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void onSuccess(String message) {
        setLoading(false);
        showAlertDialog("Success", message,false, FarmnetConstants.OK , (dialog, which) -> {},
                "", (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void showMessage(String message) {

    }
}
