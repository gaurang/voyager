package com.app.voyager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Brij on 09-03-2016.
 */
public class setting_emergency extends Fragment implements View.OnClickListener{
    private ViewPager viewPager;
    private Button addcontact;

    RecyclerView mrecycler_score;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.setting_emergency, container, false);
        viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
        addcontact=(Button) rootView.findViewById(R.id.addcontact);
        addcontact.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v == addcontact) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, 1);
        }
    }
}
