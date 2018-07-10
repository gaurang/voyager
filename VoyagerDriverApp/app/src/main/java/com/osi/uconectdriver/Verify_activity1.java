package com.osi.uconectdriver;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.osi.uconectdriver.AsyncTask.CallAPI;
import com.osi.uconectdriver.Util.Util;
import com.osi.uconectdriver.dialogs.ProgressDialogView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Brij on 01-03-2016.
 */
public class Verify_activity1 extends ParentActivity implements View.OnClickListener{
    private TextView header;
    private Button resend;
    private ImageView ic_back;
    private EditText editText_otp;
    private Button btn_verify;
    private Button change;
    private static final String VERIFYMOBILE1 = "http://192.168.1.114:8080/uc/api/cse/verifyOTP";
    private static final String RESENDOTP1 = "http://192.168.1.114:8080/uc/api/cse/resentOTP";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.verify_activity1);
        BindView(null, savedInstanceState);
//        new CountDownTimer(6000, 1000) {
//            public void onFinish() {
//                Intent startActivity = new Intent(Verify_activity1.this,LandingPageActivity.class);
//                startActivity(startActivity);
//                finish();
//            }
//
//            public void onTick(long millisUntilFinished) {
//            }
//
//        }.start();
    }
    public void BindView(View view, Bundle savedInstanceState) {
        super.BindView(view, savedInstanceState);

//       email = getIntent().getExtras().getString("email");
//        regId = getIntent().getExtras().getString("regId");
//        mobile = getIntent().getExtras().getString("mobile");
        header= (TextView) findViewById(R.id.headername);
        resend = (Button) findViewById(R.id.resend);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        btn_verify = (Button) findViewById(R.id.verify);
        change=(Button) findViewById(R.id.change);
        editText_otp = (EditText) findViewById(R.id.editText_otp);
        header.setText("VERIFY MOBILE");
        SetOnclicklistener();
    }

    @Override
    public void SetOnclicklistener() {
        super.SetOnclicklistener();
        ic_back.setOnClickListener(this);
        btn_verify.setOnClickListener(this);
        resend.setOnClickListener(this);
//        change.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                finish();
                break;
//            case R.id.change:
//                Intent intent1=new Intent(Verify_activity1.this,newcse.class);
//                startActivity(intent1);
            case R.id.verify:
                if (editText_otp.getText().toString().trim().length() != 4) {
                    Util.ShowToast(Verify_activity1.this, "Enter Valid OTP");
                } else {

                    try {
                        JSONObject jsonObject_main = new JSONObject();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject_main = getCommontHeaderParams();
                        jsonObject.put("OTP", editText_otp.getText().toString().trim());
                        jsonObject.put("email", "abc@xyz.com");
                        jsonObject.put("driverId", "1");
                        jsonObject_main.put("body", jsonObject);
                        CallAPI(jsonObject_main);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case R.id.resend:

                try {
                    JSONObject jsonObject_main = new JSONObject();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject_main = getCommontHeaderParams();
                    jsonObject.put("OTP", editText_otp.getText().toString().trim());
                    jsonObject.put("email", "abc@xyz.com");
                    jsonObject.put("driverId", "1");
                    jsonObject_main.put("body", jsonObject);
                    CallResendAPI(jsonObject_main);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }

    }

    public void CallAPI(JSONObject params) {
        if (Util.isNetworkConnected(Verify_activity1.this)) {
            try {
                new CallAPI(VERIFYMOBILE1, "VERIFYMOBILE", params, Verify_activity1.this, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Util.ShowToast(Verify_activity1.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetDetails_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {

                    try {
                        JSONObject jsonObject = new JSONObject(msg.getData().getString("responce"));

                        //   Session.setUserID(Verify_activity1.this, jsonObject.getString("user_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(Verify_activity1.this, newcse.class);
                    intent.putExtra("Allinfo",msg.getData().getString("responce"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(Verify_activity1.this)) {
                        CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        Util.ShowToast(Verify_activity1.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    Util.ShowToast(Verify_activity1.this, msg.getData().getString("msg"));
                }
            } else {
                Util.ShowToast(Verify_activity1.this, msg.getData().getString("msg"));
            }
        }
    };


    public void CallResendAPI(JSONObject params) {
        if (Util.isNetworkConnected(Verify_activity1.this)) {
            try {
                
                new CallAPI(RESENDOTP1, "RESENDOTP", params, Verify_activity1.this, GetResend_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(Verify_activity1.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetResend_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {

                    Util.ShowToast(Verify_activity1.this, msg.getData().getString("responce"));
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
                    if (Util.isNetworkConnected(Verify_activity1.this)) {
                        CallSessionID(GetResend_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        Util.ShowToast(Verify_activity1.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    Util.ShowToast(Verify_activity1.this, msg.getData().getString("msg"));
                }
            } else {
                Util.ShowToast(Verify_activity1.this, msg.getData().getString("msg"));

            }
        }
    };
    String regId;
    String email;
    String mobile;
    }
