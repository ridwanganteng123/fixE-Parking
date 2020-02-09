package com.opatan.e_parking_petugas.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.opatan.e_parking_petugas.R;
import com.opatan.e_parking_petugas.animates.AnimationProgressBar;

public class SplashScreenActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView textView;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        View overlay = (View) findViewById(R.id.overlay);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textView = (TextView)findViewById(R.id.textView);

        progressBar.setMax(99);
        progressBar.setScaleY(2f);
        progressBarSplashScreen();


    }

    public void progressBarSplashScreen(){
        AnimationProgressBar animationProgressBar = new AnimationProgressBar(this, textView, progressBar,1f, 100f);
        animationProgressBar.setDuration(5000);
        progressBar.setAnimation(animationProgressBar);
    }
}