package com.app.voyager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.voyager.AsyncTask.CallAPI;
import com.app.voyager.Util.Session;
import com.app.voyager.Util.Util;
import com.app.voyager.dialogs.ProgressDialogView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class AddPaymentMethodeActivity extends ParentActivity implements View.OnClickListener {
    TextView headername;
    ImageView ic_back;
    Button btnAddPayment;
    String Allinfo;
    EditText name,number,cvv;
    Spinner month,spinYear;
    int GETRESULTADDEDPAYMENT=100;
    private String accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment);
        try {
            Allinfo = getIntent().getStringExtra("Allinfo");
            ParseInfo(Allinfo);
        } catch (JSONException e) {
            e.printStackTrace();
            Util.ShowToast(AddPaymentMethodeActivity.this, "Error please try again!!!");
            finish();
        }
       accountType=getIntent().getExtras().getString("accountType");
        progressdialog = new ProgressDialogView(AddPaymentMethodeActivity.this, "");
        BindView(null, savedInstanceState);
       ArrayList<String> years = new ArrayList<String>();
       int thisYear = Calendar.getInstance().get(Calendar.YEAR);
       for (int i = thisYear-2000; i <= (thisYear-2000)+50; i++) {
           years.add(Integer.toString(i));
       }
       ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
       name=(EditText) findViewById(R.id.cardholdername);
       number=(EditText) findViewById(R.id.cardnumber);
       cvv=(EditText) findViewById(R.id.cvvnumber);
       spinYear = (Spinner)findViewById(R.id.year);
       month = (Spinner)findViewById(R.id.month);
       spinYear.setAdapter(adapter);
    }


    @Override
    public void BindView(View view, Bundle savedInstanceState) {

        super.BindView(view, savedInstanceState);
        headername = (TextView) findViewById(R.id.headername);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        btnAddPayment = (Button) findViewById(R.id.btnAddPayment);
        headername.setText("Add Payment");
        SetOnclicklistener();

    }

    @Override
    public void SetOnclicklistener() {
        ic_back.setOnClickListener(this);
        btnAddPayment.setOnClickListener(this);

    }

    String regId;
    String email, countryCode;
    String mobile, fname, lname, password,customerId;

    public void ParseInfo(String Allinfo) throws JSONException {
        JSONObject jsonObject = new JSONObject(Allinfo);
        email = jsonObject.getString("email");
        regId = jsonObject.getString("regId");
        mobile = jsonObject.getString("mobile");
        fname = jsonObject.getString("fname");
        lname = jsonObject.getString("lname");
        password = jsonObject.getString("password");
        mobile = jsonObject.getString("mobile");
        countryCode = jsonObject.getString("countryCode");
        customerId = Session.getUserID(AddPaymentMethodeActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                finish();
                break;
            case R.id.btnAddPayment:
                try {
                    JSONObject jsonObject_main = new JSONObject();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject_main = getCommontHeaderParams();
                    jsonObject.put("fname", fname);
                    jsonObject.put("password", password);
                    jsonObject.put("regId", regId);
                    jsonObject.put("lname", lname);
                    jsonObject.put("mobile", mobile);
                    jsonObject.put("email", email);
                    jsonObject.put("countryCode", countryCode);
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("issueDate", "");
                    jsonObject1.put("paymentType", "");
                    jsonObject.put("issueDate", "");
                    jsonObject1.put("cardNo", number.getText().toString());
                    jsonObject1.put("nameOnCard", name.getText().toString());
                    jsonObject1.put("expDate", month.getSelectedItem()+"/"+spinYear.getSelectedItem());
                    jsonObject1.put("cvv", cvv.getText().toString());
                    jsonObject1.put("customerId", "");
//                        B- Business or P-Personal
                    jsonObject1.put("accountType", accountType);
                    jsonObject1.put("gatewayId", "PayWay");
//                        gateway User Id – email for paypal
                    jsonObject1.put("gatewayUserId", "");
                    jsonObject1.put("gatewayToken", "");
                    jsonObject1.put("authKey", "");
                    jsonObject.put("paymentList", new JSONArray().put(jsonObject1));
                    jsonObject_main.put("body", jsonObject);
                    CallAPI(jsonObject_main);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void CallAPI(JSONObject params) {
        if (Util.isNetworkConnected(AddPaymentMethodeActivity.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(ADDPAYMENTUSER, "ADDPAYMENTUSER", params, AddPaymentMethodeActivity.this, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(AddPaymentMethodeActivity.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetDetails_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(msg.getData().getString("responce"));
                        Session.setUserID(AddPaymentMethodeActivity.this, jsonObject.getString("customerId"));
                        Session.setEmailId(AddPaymentMethodeActivity.this, jsonObject.getString("email"));
                        Session.setLoginStatus(AddPaymentMethodeActivity.this, true);
                        Session.setAllInfo(AddPaymentMethodeActivity.this, msg.getData().getString("responce"));
                        Intent intent = new Intent(AddPaymentMethodeActivity.this, LandingPageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
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
                    if (Util.isNetworkConnected(AddPaymentMethodeActivity.this)) {
                        CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(AddPaymentMethodeActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(AddPaymentMethodeActivity.this, msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(AddPaymentMethodeActivity.this, msg.getData().getString("msg"));
            }
        }
    };

}
