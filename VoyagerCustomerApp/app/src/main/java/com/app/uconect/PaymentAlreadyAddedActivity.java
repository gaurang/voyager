package com.app.uconect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.uconect.AsyncTask.CallAPI;
import com.app.uconect.Dataset.AddedPaymentData;
import com.app.uconect.RecyclerviewAdapter.AddedPaymntmthdAdapter;
import com.app.uconect.Util.Session;
import com.app.uconect.Util.Util;
import com.app.uconect.dialogs.ProgressDialogView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PaymentAlreadyAddedActivity extends ParentActivity implements View.OnClickListener {
    TextView headername;
    ImageView ic_back;

    String TAG = "PaymentMethodeActivity";
    AddedPaymntmthdAdapter addedPaymntmthdAdapter;
     ArrayList<AddedPaymentData> AddedPaymentList = new ArrayList<>();
    RecyclerView mrecycler_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alreadyadded_payment);
        progressdialog = new ProgressDialogView(PaymentAlreadyAddedActivity.this, "");
        BindView(null, savedInstanceState);
    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        headername = (TextView) findViewById(R.id.headername);

        ic_back = (ImageView) findViewById(R.id.ic_back);


        mrecycler_score = (RecyclerView) findViewById(R.id.recyclerview_list);
        mrecycler_score.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                PaymentAlreadyAddedActivity.this);
        mrecycler_score.setLayoutManager(mLayoutManager);

        headername.setText("Payment Method");
        SetOnclicklistener();
        findViewById(R.id.add_card_payment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentAlreadyAddedActivity.this, Payment.class);
                intent.putExtra("Allinfo", Session.getAllInfo(PaymentAlreadyAddedActivity.this));
                intent.putExtra("isforaddpayment", true);
                startActivity(intent);
            }
        });
        super.BindView(view, savedInstanceState);
    }


    @Override
    public void SetOnclicklistener() {
        ic_back.setOnClickListener(this);
        try {
            JSONObject jsonObject_main = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject_main = getCommontHeaderParams();
            jsonObject.put("customerId", Session.getUserID(PaymentAlreadyAddedActivity.this));
            jsonObject_main.put("body", jsonObject);
            CallGETPAYMENTTYPEAPI(jsonObject_main);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    int GETRESULTADDEDPAYMENT = 100;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
               onBackPressed();
                break;
            case R.id.btn_skip:
                finish();
                break;
            case R.id.creditdebit:
                Intent intent = new Intent(PaymentAlreadyAddedActivity.this, PaymentMethodeActivity.class);
                intent.putExtra("Allinfo", Session.getAllInfo(PaymentAlreadyAddedActivity.this));
                intent.putExtra("isforaddpayment", true);
                startActivityForResult(intent, GETRESULTADDEDPAYMENT);
            case R.id.paypal:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        callLandingPage();
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.payment_activity, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_addpayment) {
////            Intent intent = new Intent(PaymentAlreadyAddedActivity.this, PaymentMethodeActivity.class);
////            intent.putExtra("Allinfo", Session.getAllInfo(PaymentAlreadyAddedActivity.this));
////            intent.putExtra("isforaddpayment", true);
////            startActivity(intent);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    public void CallGETPAYMENTTYPEAPI(JSONObject params) {
        if (Util.isNetworkConnected(PaymentAlreadyAddedActivity.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(GETPAYMENTUSER, "GETPAYMENTUSER", params, PaymentAlreadyAddedActivity.this, GetPaymentType_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(PaymentAlreadyAddedActivity.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetPaymentType_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {

                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    //   Util.ShowToast(PaymentAlreadyAddedActivity.this, msg.getData().getString("responce"));
                    try {
                        JSONArray jsonArray = new JSONArray(msg.getData().getString("responce"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectdata = jsonArray.getJSONObject(i);
                            AddedPaymentData notificationData = new AddedPaymentData();
                            notificationData.id = i;
                            notificationData.customerId = jsonObjectdata.getString("customerId");
                            ;
                            notificationData.paymentId = jsonObjectdata.getString("paymentId");
                            notificationData.gatewayId = jsonObjectdata.getString("gatewayId");
                            AddedPaymentList.add(notificationData);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    addedPaymntmthdAdapter = new AddedPaymntmthdAdapter(AddedPaymentList, PaymentAlreadyAddedActivity.this);
                    mrecycler_score.setAdapter(addedPaymntmthdAdapter);
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallGETPAYMENTTYPEAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(PaymentAlreadyAddedActivity.this)) {
                        CallSessionID(GetPaymentType_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(PaymentAlreadyAddedActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(PaymentAlreadyAddedActivity.this, msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(PaymentAlreadyAddedActivity.this, msg.getData().getString("msg"));

            }
        }
    };
}
