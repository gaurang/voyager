package com.osi.uconectdriver;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.osi.uconectdriver.AsyncTask.CallAPI;
import com.osi.uconectdriver.Util.Session;
import com.osi.uconectdriver.Util.Util;
import com.osi.uconectdriver.dialogs.ProgressDialogView;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends ParentActivity implements View.OnClickListener {
    TextView headername;
    ImageView ic_back;


    EditText edt_oldpassword, edt_newpassword, edt_confirmpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        BindView(null, savedInstanceState);
    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        super.BindView(view, savedInstanceState);
        headername = (TextView) findViewById(R.id.headername);

        progressdialog = new ProgressDialogView(ChangePasswordActivity.this, "");
        ic_back = (ImageView) findViewById(R.id.ic_back);
        edt_oldpassword = (EditText) findViewById(R.id.edt_oldpassword);
        edt_newpassword = (EditText) findViewById(R.id.edt_newpassword);
        edt_confirmpassword = (EditText) findViewById(R.id.edt_confirmpassword);

        headername.setText("CHANGE PASSWORD");
        SetOnclicklistener();

    }

    @Override
    public void SetOnclicklistener() {
        ic_back.setOnClickListener(this);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_oldpassword.getText().toString().length() == 0) {
                    Util.ShowToast(ChangePasswordActivity.this, "Enter old password");
                } else if (edt_newpassword.getText().toString().length() == 0) {
                    Util.ShowToast(ChangePasswordActivity.this, "Enter new password");
                } else if (edt_confirmpassword.getText().toString().length() == 0) {
                    Util.ShowToast(ChangePasswordActivity.this, "Enter confirm Password");
                } else if (!edt_confirmpassword.getText().toString().equals(edt_newpassword.getText().toString())) {
                    Util.ShowToast(ChangePasswordActivity.this, "Password mismatch");
                } else {
                    try {
                        JSONObject jsonObject_main = new JSONObject();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject_main = getCommontHeaderParams();

                        jsonObject.put("driverId", Session.getUserID(ChangePasswordActivity.this));
                        jsonObject.put("oldPassword", edt_oldpassword.getText().toString().trim());
                        jsonObject.put("newPassword", edt_newpassword.getText().toString().trim());
                        jsonObject_main.put("body", jsonObject);
                        CallAPI(jsonObject_main);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    Intent intent = new Intent(LoginActivity.this, LandingPageActivity.class);
//                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                finish();
                break;
            case R.id.forgotpassword:
                Intent intent = new Intent(ChangePasswordActivity.this, ForgotpasswordActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void CallAPI(JSONObject params) {
        if (Util.isNetworkConnected(ChangePasswordActivity.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(CHANGEPASSWORDS, "CHANGEPASSWORDS", params, ChangePasswordActivity.this, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(ChangePasswordActivity.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetDetails_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {

                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(ChangePasswordActivity.this, "Password change successfully");
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
                    if (Util.isNetworkConnected(ChangePasswordActivity.this)) {
                        CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(ChangePasswordActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(ChangePasswordActivity.this, msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(ChangePasswordActivity.this, msg.getData().getString("msg"));
            }
        }
    };
}
