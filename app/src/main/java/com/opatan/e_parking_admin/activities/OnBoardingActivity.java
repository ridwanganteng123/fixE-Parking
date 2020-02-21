package com.opatan.e_parking_admin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.opatan.e_parking_admin.R;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.opatan.e_parking_admin.adapters.AdapterOnboarding;
import com.opatan.e_parking_admin.datas.model.PrefManager;
import com.opatan.e_parking_admin.fragments.DashboardFragment;

import me.relex.circleindicator.CircleIndicator;

public class OnBoardingActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private AdapterOnboarding AdapterOnboarding;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = new PrefManager(this);
//        if (!prefManager.isFirstTimeLaunch()) {
//            launchHomeScreen();
//            finish();
//        }
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.viewpager);

        viewPager = findViewById(R.id.viewPager);

        CircleIndicator indicator = findViewById(R.id.indicator);

        AdapterOnboarding = new AdapterOnboarding(getSupportFragmentManager());

        viewPager.setAdapter(AdapterOnboarding);

        indicator.setViewPager(viewPager);

        View overlay = (View) findViewById(R.id.overlay);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        AdapterOnboarding.registerDataSetObserver(indicator.getDataSetObserver());
    }
    private void launchHomeScreen() {
        prefManager.setIsFirstTimeLaunc(false);
        startActivity(new Intent(OnBoardingActivity.this, DashboardFragment.class));
        finish();
    }
}
