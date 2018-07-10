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
import com.app.voyager.Util.Session;
import com.app.voyager.Util.Util;
import com.app.voyager.dialogs.ProgressDialogView;
import com.app.voyager.preferencetabs.ProfileTabActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shadab.s on 14-01-2016.
 */
public class MobileVerificationActivity extends ParentActivity implements View.OnClickListener {

    TextView headername;
    ImageView ic_back;
    Button btn_verify, btn_resend;
    EditText editText_otp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verifymobile);
        progressdialog = new ProgressDialogView(MobileVerificationActivity.this, "");
        BindView(null, savedInstanceState);
    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        super.BindView(view, savedInstanceState);

        email = getIntent().getExtras().getString("email");
        regId = getIntent().getExtras().getString("regId");
        mobile = getIntent().getExtras().getString("mobile");
        id=getIntent().getExtras().getInt("id");
                headername = (TextView) findViewById(R.id.headername);
        btn_verify = (Button) findViewById(R.id.btn_verify);
        btn_resend = (Button) findViewById(R.id.btn_resend);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        editText_otp = (EditText) findViewById(R.id.editText_otp);
        headername.setText("VERIFY MOBILE");
        SetOnclicklistener();
    }

    @Override
    public void SetOnclicklistener() {
        super.SetOnclicklistener();
        ic_back.setOnClickListener(this);
        btn_verify.setOnClickListener(this);
        btn_resend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                finish();
                break;
            case R.id.btn_verify:
                if (editText_otp.getText().toString().trim().length() != 4) {
                    Util.ShowToast(MobileVerificationActivity.this, "Enter Valid OTP");
                } else {

                    try {
                        JSONObject jsonObject_main = new JSONObject();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject_main = getCommontHeaderParams();
                        jsonObject.put("OTP", editText_otp.getText().toString().trim());
                        jsonObject.put("email", email);
                        jsonObject.put("regId", regId);
                        jsonObject_main.put("body", jsonObject);
                        CallAPI(jsonObject_main);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case R.id.btn_resend:

                try {
                    JSONObject jsonObject_main = new JSONObject();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject_main = getCommontHeaderParams();
                    jsonObject.put("OTP", editText_otp.getText().toString().trim());
                    jsonObject.put("email", email);
                    jsonObject.put("regId", regId);
                    jsonObject_main.put("body", jsonObject);
                    CallResendAPI(jsonObject_main);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }

    }

    public void CallAPI(JSONObject params) {
        if (Util.isNetworkConnected(MobileVerificationActivity.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(VERIFYMOBILE, "VERIFYMOBILE", params, MobileVerificationActivity.this, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(MobileVerificationActivity.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetDetails_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {

                    try {
                        JSONObject jsonObject = new JSONObject(msg.getData().getString("responce"));
                     //   Session.setUserID(MobileVerificationActivity.this, jsonObject.getString("user_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    if(id == 1)
                    {
                        getUpdate();
                    }
                    else {
                        Intent intent = new Intent(MobileVerificationActivity.this, Payment.class);
                        intent.putExtra("Allinfo", msg.getData().getString("responce"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
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
                    if (Util.isNetworkConnected(MobileVerificationActivity.this)) {
                        CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(MobileVerificationActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(MobileVerificationActivity.this, msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(MobileVerificationActivity.this, msg.getData().getString("msg"));
            }
        }
    };


    public void CallResendAPI(JSONObject params) {
        if (Util.isNetworkConnected(MobileVerificationActivity.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(RESENDOTP, "RESENDOTP", params, MobileVerificationActivity.this, GetResend_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(MobileVerificationActivity.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetResend_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {

                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(MobileVerificationActivity.this, msg.getData().getString("responce"));
                    editText_otp.setText("");
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallResendAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(MobileVerificationActivity.this)) {
                        CallSessionID(GetResend_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(MobileVerificationActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(MobileVerificationActivity.this, msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(MobileVerificationActivity.this, msg.getData().getString("msg"));

            }
        }
    };
    String regId;
    String email;
    String mobile;
    String countryCode;
    Integer id;
    private void getUpdate() {
        if (mobile.toString().trim().length() == 0) {
            Util.ShowToast(MobileVerificationActivity.this, "Enter Mobile Number");
        } else {
            try {
                JSONObject jsonObject_main = new JSONObject();
                JSONObject jsonObject = new JSONObject();
                jsonObject_main = ((ParentActivity)MobileVerificationActivity.this).getCommontHeaderParams();
                jsonObject.put("customerId", Session.getUserID(MobileVerificationActivity.this));
                jsonObject.put("mobile", mobile.toString().trim());
                jsonObject_main.put("body", jsonObject);
                CallAPI2(jsonObject_main);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void CallAPI2(JSONObject params) {
        if (Util.isNetworkConnected(MobileVerificationActivity.this)) {
            try {
                new CallAPI(UPDATEPROFILE, "UPDATEDETAILS", params, MobileVerificationActivity.this, Update_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(MobileVerificationActivity.this, getString(R.string.nointernetmessage));
        }
    }

    Handler Update_Handler = new Handler() {
        public void handleMessage(Message msg) {
//            Log.v("UPDATE","UPDATE::::"+msg.toString());
            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Intent intent = new Intent(MobileVerificationActivity.this, ProfileTabActivity.class);

                    //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(MobileVerificationActivity.this)) {
                        CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(MobileVerificationActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(MobileVerificationActivity.this, msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(MobileVerificationActivity.this, msg.getData().getString("msg"));
            }
        }
    };


}