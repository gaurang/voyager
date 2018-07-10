package com.osi.uconectdriver;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.osi.uconectdriver.AsyncTask.CallAPI;
import com.osi.uconectdriver.Dataset.ChangeVehicleData;
import com.osi.uconectdriver.RecyclerviewAdapter.ChangeVehicleAdapter;
import com.osi.uconectdriver.Util.Session;
import com.osi.uconectdriver.Util.Util;
import com.osi.uconectdriver.dialogs.ProgressDialogView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Brij on 05-03-2016.
 */
public class changevehicle extends ParentActivity implements View.OnClickListener{
    private ImageView ic_back;
    private TextView header;
    ChangeVehicleAdapter changeVehicleAdapter;
    ArrayList<ChangeVehicleData> changeList = new ArrayList<>();
    RecyclerView mrecycler_score;
    private int statusCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changevehicle);
        progressdialog = new ProgressDialogView(changevehicle.this, "");
        BindView(null, savedInstanceState);
    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        super.BindView(view, savedInstanceState);
        header = (TextView) findViewById(R.id.headername);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        mrecycler_score = (RecyclerView) findViewById(R.id.recycler_view);
        mrecycler_score.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(changevehicle.this);
        mrecycler_score.setLayoutManager(mLayoutManager);

        header.setText("CHOOSE YOUR VEHICLE");
        SetOnclicklistener();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(changevehicle.this,LandingPageActivity.class);
        intent.putExtra("key","0");
        startActivity(intent);
        finish();
    }

    public void SetOnclicklistener() {
        super.SetOnclicklistener();
        ic_back.setOnClickListener(this);
//        for (int i = 0; i < 10; i++) {
//
//            ChangeVehicleData ChangeVehicleData = new ChangeVehicleData();
//            ChangeVehicleData.model= ("HONDA CIVIC - MH1234");
//            ChangeList.add(ChangeVehicleData);
//        }
//
//        ChangeVehicleAdapter = new ChangeVehicleAdapter(ChangeList, changevehicle.this);
//        mrecycler_score.setAdapter(ChangeVehicleAdapter);
        try {
            JSONObject jsonObject_main = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject_main = getCommontHeaderParams();
            jsonObject.put("driverId", Session.getUserID(changevehicle.this));
            jsonObject_main.put("body", jsonObject);
            CallAPI(jsonObject_main);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void CallAPI(JSONObject params) {


        if (Util.isNetworkConnected(changevehicle.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(CHANGEVEHICLE, "CHANGEVEHICLE", params, changevehicle.this, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(changevehicle.this, getString(R.string.nointernetmessage));
        }
    }

Handler GetDetails_Handler = new Handler() {
    public void handleMessage(Message msg) {

        PrintMessage("Handler " + msg.getData().toString());
        if (msg.getData().getBoolean("flag")) {
            if (msg.getData().getInt("code") == SUCCESS) {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                //changeVehicleData;
                try {
                    JSONArray jsonArray = new JSONArray(msg.getData().getString("responce"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ChangeVehicleData changeVehicleData = new ChangeVehicleData();
                        changeVehicleData.model = jsonObject.getString("make")+" "+jsonObject.getString("model")+" "+jsonObject.getString("registrationId");
                        //Log.i("AAAAAAAAAAAAAA","VVVVVVVVVVVVVVV"+ jsonObject.getString("make")+" "+jsonObject.getString("model"));
                        changeVehicleData.id=jsonObject.getString("vehicleId");
                       // changeList.add(changeVehicleData);
                        changeList.add(i,changeVehicleData);
                    }

                    changeVehicleAdapter = new ChangeVehicleAdapter(changeList, changevehicle.this);
                    mrecycler_score.setAdapter(changeVehicleAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                ParseSessionDetails(msg.getData().getString("responce"));
                try {
                    CallAPI(new JSONObject(msg.getData()
                            .getString("mExtraParam")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                if (Util.isNetworkConnected(changevehicle.this)) {
                    CallSessionID(GetDetails_Handler, msg.getData()
                            .getString("mExtraParam"));
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(changevehicle.this, getString(R.string.nointernetmessage));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(changevehicle.this, msg.getData().getString("msg"));
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(changevehicle.this, msg.getData().getString("msg"));
        }
    }
};
}
