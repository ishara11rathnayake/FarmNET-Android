package com.industrialmaster.farmnet.views.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.industrialmaster.farmnet.views.fragments.DealsFragment;
import com.industrialmaster.farmnet.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.deals);


        final DealsFragment dealsFragment = new DealsFragment();

        setFragment(dealsFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.deals){
                    setFragment(dealsFragment);
                    return true;
                } else if(id == R.id.notification){
                    return true;
                } else if(id == R.id.question){
                    return true;
                } else if(id == R.id.articles){
                    return true;
                } else if(id == R.id.advertisements){
                    return true;
                }
                return false;
            }
        });

    }

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

}
