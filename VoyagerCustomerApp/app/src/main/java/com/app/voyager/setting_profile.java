package com.app.voyager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by Brij on 03-04-2016.
 */
public class setting_profile extends Fragment {
        private Button change;
        private Button change1;
        private LinearLayout layout1;
        private LinearLayout layout2;
        private ViewPager viewPager;
        private Button logout;
        private EditText oldpassword;
        private EditText newpassword;
        private EditText confirmpassword;


        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.setting_profile, container, false);
            viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
            return rootView;
        }
}
