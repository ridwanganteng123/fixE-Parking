package com.opatan.e_parking_petugas.animates;

import android.view.View;
import android.view.animation.TranslateAnimation;

public class Helper {

    public static void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(400);
        animate.setFillAfter(false);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public static void slideDown(View view) {
        view.setVisibility(View.GONE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(400);
        animate.setFillAfter(false);
        view.startAnimation(animate);
    }

}
