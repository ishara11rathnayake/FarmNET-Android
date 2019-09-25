package com.industrialmaster.farmnet.views.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.industrialmaster.farmnet.R;

public class OtherProfileActivity extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;

    private ConstraintLayout mRateUserConstraintLayout;
    private Button mRateUserButton;

    private PopupWindow mRateUserPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        // Get the application context
        mContext = getApplicationContext();

        // Get the activity
        mActivity = OtherProfileActivity.this;

        // Get the widgets reference from XML layout
        mRateUserConstraintLayout = findViewById(R.id.cl_other_profile);
        mRateUserButton = findViewById(R.id.btn_rate_user);

        // Set a click listener for the mRateUserButton
        mRateUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                @SuppressLint("InflateParams") View customView = inflater.inflate(R.layout.layout_custom_popup, null);

                // Initialize a new instance of popup window
                mRateUserPopupWindow = new PopupWindow(customView, ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT);

                if(Build.VERSION.SDK_INT>=21){
                    mRateUserPopupWindow.setElevation(5.0f);
                }

                // Get a reference for the custom view close button
                ImageButton btn_close = customView.findViewById(R.id.btn_close);

                // Set a click listener for the popup window close button
                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dismiss the popup window
                        mRateUserPopupWindow.dismiss();
                    }
                });

                // Finally, show the popup window at the center location of root relative layout
                mRateUserPopupWindow.showAtLocation(mRateUserConstraintLayout, Gravity.CENTER,0,0);

            }
        });
    }
}
