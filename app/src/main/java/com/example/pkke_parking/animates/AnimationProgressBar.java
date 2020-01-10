package com.example.pkke_parking.animates;

import android.content.Context;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pkke_parking.activities.MainActivity;

public class AnimationProgressBar extends Animation {

    private ProgressBar progressBar;
    private TextView textView;
    private Context context;
    private Float from, to;

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
            context.startActivity(new Intent(context, MainActivity.class));
        }
    }
}
