package com.industrialmaster.farmnet.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.industrialmaster.farmnet.R;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "SplashScreenActivity";
    private ImageView mLogoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mLogoImageView = findViewById(R.id.image_view_logo);
        Animation logoAnim = AnimationUtils.loadAnimation(this, R.anim.logo_transition);
        mLogoImageView.startAnimation(logoAnim);

        final Intent intent = new Intent(this, MainActivity.class);

        Thread timer = new Thread() {
          public void run() {
              try {
                  sleep(3000);
                  startActivity(intent);
              }catch (InterruptedException e){
                  Log.e(TAG, e.toString());
              }finally {
                  finish();
              }
          }
        };

        timer.start();
    }
}
