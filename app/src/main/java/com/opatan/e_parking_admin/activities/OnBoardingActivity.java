package com.opatan.e_parking_admin.activities;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.opatan.e_parking_admin.R;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class OnBoardingActivity extends AppIntro2{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow();
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }

        setColorTransitionsEnabled(true);

        addSlide(AppIntroFragment.newInstance("JUDUL PERTAMA", "DESKRIPSI PERTAMA", R.drawable.illustrationonboarding2, ContextCompat.getColor(getApplicationContext(), R.color.onboarding1)));
        addSlide(AppIntroFragment.newInstance("JUDUL KEDUA", "DESKRIPSI KEDUA", R.drawable.illustrationonboarding, ContextCompat.getColor(getApplicationContext(), R.color.onboarding2)));
        addSlide(AppIntroFragment.newInstance("JUDUL KETIGA", "DESKRIPSI KETIGA", R.drawable.illustrationonboarding2, ContextCompat.getColor(getApplicationContext(), R.color.onboarding3)));

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        addSlide(AppIntroFragment.newInstance("JUDUL KETIGA", "DESKRIPSI KETIGA", R.drawable.illustrationonboarding2, ContextCompat.getColor(getApplicationContext(), R.color.gradient_end_color)));
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(OnBoardingActivity.this,LoginActivity.class));
        finish();
    }
}
