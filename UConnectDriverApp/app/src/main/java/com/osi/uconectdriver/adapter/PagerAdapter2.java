package com.osi.uconectdriver.adapter;


/**
 * Created by Brij on 16-02-2016.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.osi.uconectdriver.earning_week;
import com.osi.uconectdriver.earnings_all;

public class PagerAdapter2 extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter2(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                earning_week tab1 = new  earning_week();
                return tab1;
            case 1:
                earnings_all tab2 = new earnings_all();
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