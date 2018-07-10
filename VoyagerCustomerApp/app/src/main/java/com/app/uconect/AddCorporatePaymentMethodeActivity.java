package com.app.uconect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.uconect.AsyncTask.CallAPI;
import com.app.uconect.Util.Constants;
import com.app.uconect.Util.Session;
import com.app.uconect.Util.Util;
import com.app.uconect.dialogs.ProgressDialogView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddCorporatePaymentMethodeActivity extends ParentActivity implements View.OnClickListener {

    TextView headername;
    ImageView ic_back;
    Button btnAddPayment;
    String Allinfo;
    int GETRESULTADDEDPAYMENT = 100;
    EditText edt_corpoid,edt_pin;
    boolean isforaddpayment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_corporate_payment);
        isforaddpayment = getIntent().getBooleanExtra("isforaddpayment", false);
        try {
            Allinfo = getIntent().getStringExtra("Allinfo");
            ParseInfo(Allinfo);
        } catch (JSONException e) {
            e.printStackTrace();
            Util.ShowToast(AddCorporatePaymentMethodeActivity.this, "Error please try again!!!");
            finish();
        }
        progressdialog = new ProgressDialogView(AddCorporatePaymentMethodeActivity.this, "");
        BindView(null, savedInstanceState);
    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {

        super.BindView(view, savedInstanceState);
        headername = (TextView) findViewById(R.id.headername);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        btnAddPayment = (Button) findViewById(R.id.btnAddPayment);
        edt_corpoid = (EditText) findViewById(R.id.edt_corpoid);
        edt_pin = (EditText) findViewById(R.id.edt_pin);
        headername.setText("Add Payment");
        SetOnclicklistener();

    }

    @Override
    public void SetOnclicklistener() {
        ic_back.setOnClickListener(this);
        btnAddPayment.setOnClickListener(this);

    }

    String regId;
    String email;
    String mobile, fname, lname, password;

    public void ParseInfo(String Allinfo) throws JSONException {
        JSONObject jsonObject = new JSONObject(Allinfo);
        email = jsonObject.getString("email");
        regId = jsonObject.getString("regId");
        mobile = jsonObject.getString("mobile");
        fname = jsonObject.getString("fname");
        lname = jsonObject.getString("lname");
        password = jsonObject.getString("password");
        mobile = jsonObject.getString("mobile");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                finish();
                break;
            case R.id.btnAddPayment:
                if (edt_corpoid.getText().toString().length() == 0) {
                    Util.ShowToast(AddCorporatePaymentMethodeActivity.this, "Enter email id");
                } else if (!Util.isEmailValid(edt_corpoid.getText().toString())) {
                    Util.ShowToast(AddCorporatePaymentMethodeActivity.this, "Enter valid email id");
                } else {
                    try {
                        JSONObject jsonObject_main = new JSONObject();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject_main = getCommontHeaderParams();
                        jsonObject.put("fname", fname);
                        jsonObject.put("lname", lname);
                        jsonObject.put("mobile", mobile);
                        jsonObject.put("email", email);
                        jsonObject.put("password", password);
                        jsonObject.put("regId",Session.getRegID(AddCorporatePaymentMethodeActivity.this));
                        jsonObject.put("countryCode",Session.getCc(AddCorporatePaymentMethodeActivity.this));
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("paymentType", "");
                        jsonObject1.put("cardNo", "");
                        jsonObject1.put("nameOnCard", "");
                        jsonObject1.put("expDate", "");
                        jsonObject1.put("issueDate", "");
                        jsonObject1.put("customerId", Session.getUserID(AddCorporatePaymentMethodeActivity.this));
                        if (Constants.loginDetails != null) {
                            if (Constants.loginDetails.facebookId.length() > 0) {
                                jsonObject.put("authKeyFb", Constants.loginDetails.facebookId);
                                jsonObject.put("signInVia", "F");
                            } else if (Constants.loginDetails.googleId.length() > 0) {
                                jsonObject.put("authKeyG", Constants.loginDetails.googleId);
                                jsonObject.put("signInVia", "G");
                            }
                        }
//                        B- Business or P-Personal
                        jsonObject1.put("accountType", "C");
                        jsonObject1.put("gatewayId", "");
//                        gateway User Id – email for paypal
                        jsonObject1.put("gatewayUserId", "");
                        jsonObject1.put("gatewayToken", "");
                        jsonObject1.put("authKey", "");
                        jsonObject1.put("email", edt_corpoid.getText().toString());
                        jsonObject1.put("pin",edt_pin.getText().toString().trim());
                        jsonObject.put("paymentList", new JSONArray().put(jsonObject1));

                        jsonObject_main.put("body", jsonObject);
                        CallAPI(jsonObject_main);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public void CallAPI(JSONObject params) {
        if (Util.isNetworkConnected(AddCorporatePaymentMethodeActivity.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                if (isforaddpayment)
                    new CallAPI(ADDPAYMENT, "ADDPAYMENT", params, AddCorporatePaymentMethodeActivity.this, GetDetails_Handler, true);
                else
                    new CallAPI(VERIFYEMAILID, "ADDPAYMENTUSER", params, AddCorporatePaymentMethodeActivity.this, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(AddCorporatePaymentMethodeActivity.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetDetails_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    if (isforaddpayment) {
                        finish();
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(msg.getData().getString("responce"));
                            progressdialog.dismissanimation(ProgressDialogView.ERROR);
                            if(jsonObject.getString("rembursed")== "1") {
                                Session.setUserID(AddCorporatePaymentMethodeActivity.this, jsonObject.getString("customerId"));
                                Session.setEmailId(AddCorporatePaymentMethodeActivity.this, jsonObject.getString("email"));
                                Session.setLoginStatus(AddCorporatePaymentMethodeActivity.this, true);
                                Session.setAllInfo(AddCorporatePaymentMethodeActivity.this, msg.getData().getString("responce"));
                                Intent intent = new Intent(AddCorporatePaymentMethodeActivity.this, LandingPageActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                ActivityCompat.finishAffinity(AddCorporatePaymentMethodeActivity.this);

                            }else if(jsonObject.getString("rembursed") == "0"){
                                Intent intent = new Intent(AddCorporatePaymentMethodeActivity.this, PaymentMethodeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("Allinfo",Allinfo);
                                intent.putExtra("accountType","C");
                                intent.putExtra("email",edt_corpoid.getText().toString());
                                startActivity(intent);
                                ActivityCompat.finishAffinity(AddCorporatePaymentMethodeActivity.this);
                            }else{
                                Util.ShowToast(AddCorporatePaymentMethodeActivity.this,msg.getData().getString("responce"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Util.ShowToast(AddCorporatePaymentMethodeActivity.this, e.getMessage());
                        }
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
                    if (Util.isNetworkConnected(AddCorporatePaymentMethodeActivity.this)) {
                        CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(AddCorporatePaymentMethodeActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(AddCorporatePaymentMethodeActivity.this, msg.getData().getString("responce"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(AddCorporatePaymentMethodeActivity.this, msg.getData().getString("responce"));
            }
        }
    };

}
