package com.example.pkke_parking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OnBoardingActivity extends AppCompatActivity {


    private TextView[] dots;
    private LinearLayout dotsLayout;
    private ViewPager sliderViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        sliderViewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.linear_layout_onboarding);

        sliderViewPager.setAdapter(new AdapterOnboarding(this));

        updateIndicators(0);

        sliderViewPager.addOnPageChangeListener(onPageChangeListener);

    }

    public void updateIndicators(int position) {

        dots = new TextView[3];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.Black));
            dots[i].setGravity(View.TEXT_ALIGNMENT_GRAVITY);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.colorCiriKhas));
        }

    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            updateIndicators(position);

            switch (position) {
                case 0:
                    sliderViewPager.setBackgroundColor(getResources().getColor(R.color.colorCiriKhas));
                    break;
                case 1:
                    sliderViewPager.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    break;
                case 2:
                    sliderViewPager.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    break;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}
