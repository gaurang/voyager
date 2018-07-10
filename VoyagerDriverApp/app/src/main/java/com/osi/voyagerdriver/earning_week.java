package com.osi.voyagerdriver;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Brij on 05-03-2016.
 */
public class earning_week extends Fragment {
    private ViewPager viewPager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.earning_week, container, false);
        viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
        return rootView;
    }
}