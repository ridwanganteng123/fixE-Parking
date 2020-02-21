package com.opatan.e_parking_user.datas.model;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private static final String PREF_NAME = "introSlider";
    private static final String IS_FIRST_TIME_LAUNC = "isFisrtTimeLaunch";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    public PrefManager(Context context) {
        this._context = context;
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void setIsFirstTimeLaunc(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNC, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return sharedPreferences.getBoolean(IS_FIRST_TIME_LAUNC,false);
    }
}
