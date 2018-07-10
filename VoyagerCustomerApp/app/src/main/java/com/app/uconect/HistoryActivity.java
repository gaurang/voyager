package com.app.uconect;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.uconect.AsyncTask.CallAPI;
import com.app.uconect.Dataset.HistoryData;
import com.app.uconect.RecyclerviewAdapter.HistoryAdapter;
import com.app.uconect.Util.Session;
import com.app.uconect.Util.Util;
import com.app.uconect.dialogs.ProgressDialogView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by brij on 14-02-2016.
 */
public class HistoryActivity extends ParentActivity implements View.OnClickListener {

    TextView headername;
    ImageView ic_back;
    HistoryAdapter historyAdapter;
    ArrayList<HistoryData> Historylist = new ArrayList<>();
    RecyclerView mrecycler_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        progressdialog = new ProgressDialogView(HistoryActivity.this, "");
        BindView(null, savedInstanceState);
    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        super.BindView(view, savedInstanceState);
        headername = (TextView) findViewById(R.id.headername);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        noHistory = (TextView) findViewById(R.id.nohistory);

        mrecycler_score = (RecyclerView) findViewById(R.id.recyclerview_list);
        mrecycler_score.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                HistoryActivity.this);
        mrecycler_score.setLayoutManager(mLayoutManager);

        headername.setText("HISTORY");
        SetOnclicklistener();
    }

    @Override
    public void SetOnclicklistener() {
        super.SetOnclicklistener();
        ic_back.setOnClickListener(this);
        try {
            JSONObject jsonObject_main = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject_main = getCommontHeaderParams();
            jsonObject.put("customerId", Session.getUserID(HistoryActivity.this));
            jsonObject_main.put("body", jsonObject);
            CallAPI(jsonObject_main);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ic_back:
                onBackPressed();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        callLandingPage();
    }

    public void CallAPI(JSONObject params) {
        if (Util.isNetworkConnected(HistoryActivity.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(GETHISTORY, "GETHISTORY", params, HistoryActivity.this, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(HistoryActivity.this, getString(R.string.nointernetmessage));
        }
    }

    private TextView noHistory;
    private boolean isJSON;
    Handler GetDetails_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);

                        try {

                            JSONArray jsonArray = new JSONArray(msg.getData().getString("responce"));
                            noHistory.setVisibility(View.GONE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                HistoryData historyData = new HistoryData();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                historyData.bookingId = jsonObject.getString("bookingId");
                                historyData.customerId = jsonObject.getString("customerId");
                                historyData.sourceLatitude = jsonObject.getString("sourceLatitude");
                                historyData.sourceLongitude = jsonObject.getString("sourceLongitude");
                                historyData.destLatitude = jsonObject.getString("destLatitude");
                                historyData.paymentId = jsonObject.getString("paymentId");
                                historyData.vehicleType = jsonObject.getString("vehicleType");
                                historyData.status = jsonObject.getString("status");
                                historyData.createDate = jsonObject.getString("createDate");
                                historyData.modifiedDate = jsonObject.getString("modifiedDate");
                                historyData.corpCustId = jsonObject.getString("corpCustId");
                                historyData.srcPlace = jsonObject.getString("srcPlace");
                                historyData.destPlace = jsonObject.getString("destPlace");
                                historyData.finalFare = jsonObject.getString("rideTotalAmt");
                                historyData.driverName = jsonObject.getString("driverName");
                                historyData.driverPhoto = jsonObject.getString("driverPhoto");
                                historyData.accountType = jsonObject.getString("accountType");
                                historyData.car = jsonObject.getString("carNumber") + " " + jsonObject.getString("make") + "-" + jsonObject.getString("model");

                                Historylist.add(historyData);
                            }


                            historyAdapter = new HistoryAdapter(Historylist, HistoryActivity.this);
                            mrecycler_score.setAdapter(historyAdapter);
                            if (Historylist.size() == 0) {
                                noHistory.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            noHistory.setVisibility(View.VISIBLE);
                            noHistory.setText(msg.getData().getString("responce"));

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
                    if (Util.isNetworkConnected(HistoryActivity.this)) {
                        CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(HistoryActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(HistoryActivity.this, msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(HistoryActivity.this, msg.getData().getString("msg"));
            }
        }
    };
}