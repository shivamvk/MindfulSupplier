package com.example.shivamvk.mindfulsupplier;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private FrameLayout flMainActivity;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_recommended:
                    getSupportActionBar().show();
                    fragment = new RecommendedFragment();
                    break;
                case R.id.navigation_active_loads:
                    getSupportActionBar().show();
                    fragment = new ActiveLoadsFragment();
                    break;
                case R.id.navigation_my_account:
                    getSupportActionBar().hide();
                    fragment = new AccountFragment();
                    break;
            }

            if (fragment != null){
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_main_activity, fragment);
                fragmentTransaction.commit();
            }

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (SharedPrefManager.getInstance(this).getName() == null){
            startActivity(new Intent(MainActivity.this, OnBoardingActivity.class));
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flMainActivity = findViewById(R.id.fl_main_activity);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Fragment fragment = new RecommendedFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_main_activity, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPrefManager.getInstance(this).clearFilterSharedPref();
    }
}
