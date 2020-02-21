package com.opatan.e_parking_user.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.opatan.e_parking_user.fragments.Onboarding1;
import com.opatan.e_parking_user.fragments.Onboarding2;
import com.opatan.e_parking_user.fragments.Onboarding3;

public class AdapterOnboarding extends FragmentStatePagerAdapter {


    public AdapterOnboarding(FragmentManager fm) {
        super(fm);
    }



    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Onboarding1 tab1 = new Onboarding1();
                return tab1;

            case 1:
                Onboarding2 tab2 = new Onboarding2();
                return tab2;


            case 2:
                Onboarding3 tab3 = new Onboarding3();
                return tab3;


            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return 3;
    }
}
