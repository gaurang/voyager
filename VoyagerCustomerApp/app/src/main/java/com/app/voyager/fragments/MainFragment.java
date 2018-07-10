package com.app.voyager.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.voyager.LandingPageActivity;
import com.app.voyager.interfaces.AsyncInterface;
import com.app.voyager.interfaces.InternetConnection;
import com.app.voyager.interfaces.MyInterface;
import com.app.voyager.interfaces.myUrls;


public class MainFragment extends android.support.v4.app.Fragment implements
        InternetConnection, myUrls, AsyncInterface, MyInterface {
    LandingPageActivity parentactivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        parentactivity = (LandingPageActivity) getActivity();
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void getStatusConnection(boolean isConnected) {
    }

    public void PrintMessage(String Error) {
        Log.d("########Call123 ", Error);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void BindView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void SetOnclicklistener() {

    }
}
