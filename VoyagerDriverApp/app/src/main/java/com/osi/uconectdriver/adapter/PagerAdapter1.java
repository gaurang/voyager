package com.osi.uconectdriver.adapter;


/**
 * Created by Brij on 16-02-2016.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.osi.uconectdriver.setting_emergency;
import com.osi.uconectdriver.setting_profile;

public class PagerAdapter1 extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter1(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                setting_profile tab1 = new setting_profile();
                return tab1;
            case 1:
                setting_emergency tab2 = new setting_emergency();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}