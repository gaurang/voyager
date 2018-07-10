package com.app.voyager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.voyager.AsyncTask.CallAPI;
import com.app.voyager.Dataset.AddedPaymentData;
import com.app.voyager.Util.Session;
import com.app.voyager.Util.Util;
import com.app.voyager.dialogs.ProgressDialogView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shadab.s on 20-01-2016.
 */
public class PersonalActivity extends ParentActivity implements View.OnClickListener {

    TextView headername;
    ImageView ic_back;
    Button submit, default_payment;
    EditText edt_emailid;
    AddedPaymentData addedPaymentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressdialog = new ProgressDialogView(PersonalActivity.this, "");

        setContentView(R.layout.personal_activity);
        BindView(null, savedInstanceState);
    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        super.BindView(view, savedInstanceState);
        headername = (TextView) findViewById(R.id.headername);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        edt_emailid = (EditText) findViewById(R.id.edt_emailid);
        submit = (Button) findViewById(R.id.submit);
        default_payment = (Button) findViewById(R.id.default_payment);


        headername.setText("Buissness Profile");
        SetOnclicklistener();
    }

    @Override
    public void SetOnclicklistener() {
        super.SetOnclicklistener();
        ic_back.setOnClickListener(this);
        submit.setOnClickListener(this);
        default_payment.setOnClickListener(this);
        try {
            JSONObject jsonObject_main = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject_main = getCommontHeaderParams();
            jsonObject.put("customerId", Session.getUserID(PersonalActivity.this));

            jsonObject_main.put("body", jsonObject);
            CallGETPAYMENTAPI(jsonObject_main);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ic_back:
                finish();
                break;
            case R.id.submit:
                if (edt_emailid.getText().toString().trim().length() == 0) {
                    Util.ShowToast(PersonalActivity.this, "Enter email id");
                } else if (!Util.isEmailValid(edt_emailid.getText().toString().trim())) {
                    Util.ShowToast(PersonalActivity.this, "Enter valid email id");
                } else if (addedPaymentData == null) {
                    Util.ShowToast(PersonalActivity.this, "Select Default payment");
                } else {
                    try {
                        JSONObject jsonObject_main = new JSONObject();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject_main = getCommontHeaderParams();
                        jsonObject.put("customerId", Session.getUserID(PersonalActivity.this));
                        jsonObject.put("paymentId", addedPaymentData.paymentId);
                        jsonObject.put("email", edt_emailid.getText().toString().trim());
                        jsonObject.put("accountType", "P");
                        jsonObject_main.put("body", jsonObject);
                        CallADDPAYMENTAPI(jsonObject_main);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.default_payment:
                Intent intentGetMessage = new Intent(this, SeletectPaymentActivity.class);
                startActivityForResult(intentGetMessage, 2);
                break;
        }
    }

    public void setValues(AddedPaymentData addedPaymentData) {
        edt_emailid.setText(addedPaymentData.email);
        default_payment.setText(addedPaymentData.gatewayId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (null != data) {

                addedPaymentData = (AddedPaymentData) data.getExtras().getSerializable("mDataset");
                default_payment.setText(addedPaymentData.gatewayId);
            }
        }
    }

    public void CallADDPAYMENTAPI(JSONObject params) {
        if (Util.isNetworkConnected(PersonalActivity.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(ADDUPDATEACCOUNTPROFILE, "ADDUPDATEACCOUNTPROFILE", params, PersonalActivity.this, GetPlaces_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(PersonalActivity.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetPlaces_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {

                    progressdialog.dismissanimation(ProgressDialogView.ERROR);


                    Util.ShowToast(PersonalActivity.this, "Update successfully!");

                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallADDPAYMENTAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(PersonalActivity.this)) {
                        CallSessionID(GetPlaces_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(PersonalActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(PersonalActivity.this, msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(PersonalActivity.this, msg.getData().getString("msg"));

            }
        }
    };

    public void CallGETPAYMENTAPI(JSONObject params) {
        if (Util.isNetworkConnected(PersonalActivity.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(PaymentTPROFILE, "PaymentTPROFILE", params, PersonalActivity.this, GetGetpayment_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(PersonalActivity.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetGetpayment_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {

                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    AddedPaymentData addedPaymentData = new AddedPaymentData();
                    try {
                        JSONArray jsonArray = new JSONArray(msg.getData().getString("responce"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if (jsonObject.getString("accountType").equalsIgnoreCase("P")) {
                                addedPaymentData.id = 0;
                                if (jsonObject.has("defalt_payment"))
                                    addedPaymentData.defalt_payment = jsonObject.getInt("defalt_payment");
                                addedPaymentData.paymentId = jsonObject.getString("paymentId");
                                addedPaymentData.customerId = jsonObject.getString("customerId");
                                addedPaymentData.email = jsonObject.getString("email");
                                addedPaymentData.gatewayId = jsonObject.getString("gatewayId");

                                setValues(addedPaymentData);
                                break;
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallGETPAYMENTAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(PersonalActivity.this)) {
                        CallSessionID(GetGetpayment_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(PersonalActivity.this, getString(R.string.nointernetmessage));
                        finish();
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(PersonalActivity.this, msg.getData().getString("msg"));
                    finish();
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(PersonalActivity.this, msg.getData().getString("msg"));
                finish();

            }
        }
    };
}