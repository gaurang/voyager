package com.app.uconect.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.uconect.LandingPageActivity;
import com.app.uconect.interfaces.AsyncInterface;
import com.app.uconect.interfaces.InternetConnection;
import com.app.uconect.interfaces.MyInterface;
import com.app.uconect.interfaces.myUrls;


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
