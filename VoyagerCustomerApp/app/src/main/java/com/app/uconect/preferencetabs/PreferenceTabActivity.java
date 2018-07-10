package com.app.uconect.preferencetabs;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.uconect.AsyncTask.CallAPI;
import com.app.uconect.ParentActivity;
import com.app.uconect.R;
import com.app.uconect.Util.Session;
import com.app.uconect.Util.Util;
import com.app.uconect.tabfragments.AccountsFragment;
import com.app.uconect.tabfragments.FavoritesFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by atul on 14/4/16.
 */
public class PreferenceTabActivity extends ParentActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView header;
    private ImageView ic_back;
    private TextView home,office;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout
        setContentView(R.layout.activity_tab_preferences);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        header = (TextView) findViewById(R.id.headername);
        header.setText("SETTINGS");
        ic_back = (ImageView) findViewById(R.id.ic_back);
        ic_back.setOnClickListener(this);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        tabLayout.getTabAt(0).getCustomView().setSelected(true);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewPager.setNestedScrollingEnabled(true);
        }
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                home = (TextView) findViewById(R.id.textView1_home);
                office = (TextView) findViewById(R.id.textView1_office);
                int a=viewPager.getCurrentItem();
                Log.i("sdfg", "aaaaaaaaaaaaaaaaaaa" + a);
                if(a==1)
                {
                    try {
                        JSONObject jsonObject_main = new JSONObject();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject_main = getCommontHeaderParams();

                        jsonObject.put("customerId", Session.getUserID(PreferenceTabActivity.this));
                        jsonObject_main.put("body", jsonObject);
                        CallGETPLACESAPI(jsonObject_main);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void setupTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_view, null);
        tabOne.setText("ACCOUNT");
        tabLayout.getTabAt(0).setCustomView(tabOne);
        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_view, null);
        tabTwo.setText("FAVORITE");
        tabLayout.getTabAt(1).setCustomView(tabTwo);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AccountsFragment(), "ONE");
        adapter.addFragment(new FavoritesFragment(), "TWO");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ic_back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void CallGETPLACESAPI(JSONObject params) {
        if (Util.isNetworkConnected(PreferenceTabActivity.this)) {
            try {
                new CallAPI(GETFAVOURITEPLACES, "GETFAVOURITEPLACES", params, PreferenceTabActivity.this, GetPlaces_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Util.ShowToast(PreferenceTabActivity.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetPlaces_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {


                    try {
                        JSONArray jsonArray = new JSONArray(msg.getData().getString("responce"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (jsonObject.getString("favLabel").equals("Home")) {
                                String a = jsonObject.getString("favName");
                                int n=a.indexOf("s:");
                                int b=a.indexOf("Phone");
                                home.setText(a.toString().trim().substring(n+2,b));
                                Log.i("asdfghjk", "asdfghm," + home);
                            } else {
                                String a = jsonObject.getString("favName");
                                int n=a.indexOf("s:");
                                int b=a.indexOf("Phone");
                                office.setText(a.toString().trim().substring(n+2,b));
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //   Util.ShowToast(PreferenceTabActivity.this, msg.getData().getString("responce"));

                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallGETPLACESAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(PreferenceTabActivity.this)) {
                        CallSessionID(GetPlaces_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {

                        Util.ShowToast(PreferenceTabActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {

                    Util.ShowToast(PreferenceTabActivity.this, msg.getData().getString("msg"));
                }
            } else {

                Util.ShowToast(PreferenceTabActivity.this, msg.getData().getString("msg"));

            }
        }
    };
}
