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
import com.app.voyager.Util.Util;
import com.app.voyager.dialogs.ProgressDialogView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Brij on 17-07-2016.
 */
public class ForgotPasswordPin extends ParentActivity implements View.OnClickListener{
    TextView headername;
    ImageView ic_back;
    Button btn_verify, btn_resend;
    EditText editText_otp,newpassword,confirmpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword_pin);
        progressdialog = new ProgressDialogView(ForgotPasswordPin.this, "");
        BindView(null, savedInstanceState);
    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        super.BindView(view, savedInstanceState);

        email = getIntent().getExtras().getString("email");
        headername = (TextView) findViewById(R.id.headername);
        btn_verify = (Button) findViewById(R.id.btn_verify);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        editText_otp = (EditText) findViewById(R.id.editText_otp);
        newpassword = (EditText) findViewById(R.id.newpassword);
        confirmpassword = (EditText) findViewById(R.id.confirmpassword);
        headername.setText("Forgot password");
        SetOnclicklistener();
    }

    @Override
    public void SetOnclicklistener() {
        super.SetOnclicklistener();
        ic_back.setOnClickListener(this);
        btn_verify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                finish();
                break;
            case R.id.btn_verify:
                if (editText_otp.getText().toString().trim().length() != 4) {
                    Util.ShowToast(ForgotPasswordPin.this, "Enter Valid OTP");
                }else if(newpassword.getText().toString().trim().length() < 6)
                {
                    Util.ShowToast(ForgotPasswordPin.this, "Password too short");
                }else if(confirmpassword.getText().toString().trim().length() < 6)
                {
                    Util.ShowToast(ForgotPasswordPin.this, "Password too short");
                } else if(!newpassword.getText().toString().trim().equals(confirmpassword.getText().toString().trim()))
                {
                    Util.ShowToast(ForgotPasswordPin.this, "Password Does Not Match");
                } else  {

                    try {
                        JSONObject jsonObject_main = new JSONObject();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject_main = getCommontHeaderParams();
                        jsonObject.put("OTP", editText_otp.getText().toString().trim());
                        jsonObject.put("email", email);
                        jsonObject.put("password", newpassword.getText().toString().trim());
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
        if (Util.isNetworkConnected(ForgotPasswordPin.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(CPOTP, "CHANGEPASSWORD", params, ForgotPasswordPin.this, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(ForgotPasswordPin.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetDetails_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {

                    try {
                        JSONObject jsonObject = new JSONObject(msg.getData().getString("responce"));
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Intent intent = new Intent(ForgotPasswordPin.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        //   Session.setUserID(ForgotPasswordPin.this, jsonObject.getString("user_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
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
                    if (Util.isNetworkConnected(ForgotPasswordPin.this)) {
                        CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(ForgotPasswordPin.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(ForgotPasswordPin.this, msg.getData().getString("msg"));
                }

            }

    };


    String regId;
    String email;
    String mobile;
    String countryCode;
    Integer id;

}
