package com.opatan.e_parking_admin.animates;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.opatan.e_parking_admin.activities.LoginActivity;
import com.opatan.e_parking_admin.activities.MainActivity;
import com.opatan.e_parking_admin.activities.OnBoardingActivity;
import com.opatan.e_parking_admin.datas.model.PrefManager;
import com.opatan.e_parking_admin.fragments.DashboardFragment;

public class AnimationProgressBar extends Animation {

    private ProgressBar progressBar;
    private TextView textView;
    private Context context;
    private Float from, to;
    private PrefManager prefs;

    public AnimationProgressBar(Context context, TextView textView, ProgressBar progressBar, Float from, Float to){
        this.context = context;
        this.textView = textView;
        this.progressBar = progressBar;
        this.from = from;
        this.to = to;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        float value = from * (to - from) * interpolatedTime;
        progressBar.setProgress((int)value);
        textView.setText((int)value + "%");
        if (value == 99){

            prefs = new PrefManager(context);

            if (prefs.isFirstTimeLaunch() == false){
                context.startActivity(new Intent(context, OnBoardingActivity.class));
            } else {

                context.startActivity(new Intent(context, MainActivity.class));
            }



        }
    }
}
