package com.app.uconect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.uconect.AsyncTask.CallAPI;
import com.app.uconect.Dataset.LoginDetails;
import com.app.uconect.Util.Session;
import com.app.uconect.Util.Util;
import com.app.uconect.dialogs.ProgressDialogView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends SocialLoginActivity implements View.OnClickListener {
    TextView headername;
    ImageView ic_back;
    EditText edt_emailid, edt_password;
    TextView forgotpassword;
    private Intent intent;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Initializing our broadcast receiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GcmIntentService

            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully
                if (intent.getAction().equals(GcmIntentService.REGISTRATION_SUCCESS)) {
                    //Getting the registration token from the intent
                    String token = intent.getStringExtra("token");
                    //Displaying the token as toast

                    //if the intent is not with success then displaying error messages
                } else if (intent.getAction().equals(GcmIntentService.REGISTRATION_ERROR)) {
                    Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                }
            }
        };
        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if (ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            //Starting intent to register device
            Log.i("aaaaaaaaaaaaaaaaaaaaaaa", "Registration error");
            Intent itent = new Intent(this, GcmIntentService.class);
            startService(itent);
        }
        GetHAshKey();
        BindView(null, savedInstanceState);
    }
    //Registering receiver on activity resume
    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GcmIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GcmIntentService.REGISTRATION_ERROR));
    }


    //Unregistering receiver on activity paused
    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        headername = (TextView) findViewById(R.id.headername);
        forgotpassword = (TextView) findViewById(R.id.forgotpassword);
        progressdialog = new ProgressDialogView(LoginActivity.this, "");
        ic_back = (ImageView) findViewById(R.id.ic_back);
        edt_emailid = (EditText) findViewById(R.id.edt_emailid);
        edt_password = (EditText) findViewById(R.id.edt_password);
        tv_login_fb = (ImageView) findViewById(R.id.tv_login_fb);
        tv_login_googleplus = (ImageView) findViewById(R.id.tv_login_googleplus);
        headername.setText("SIGN IN");
        SetOnclicklistener();
        super.BindView(view, savedInstanceState);
    }

    @Override
    public void SetOnclicklistener() {
        ic_back.setOnClickListener(this);
        forgotpassword.setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_emailid.getText().toString().trim().length() == 0) {
                    Util.ShowToast(LoginActivity.this, "Enter email id");
                } else if (edt_password.getText().toString().trim().length() == 0) {
                    Util.ShowToast(LoginActivity.this, "Enter password");
                } else if (!Util.isEmailValid(edt_emailid.getText().toString().trim())) {
                    Util.ShowToast(LoginActivity.this, "Enter valid email id");
                } /*else if (edt_password.getText().toString().trim().length() < 6) {
                    Util.ShowToast(LoginActivity.this, "Enter valid Password");
                } */ else {
                    try {
                        JSONObject jsonObject_main = new JSONObject();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject_main = getCommontHeaderParams();

                        jsonObject.put("email", edt_emailid.getText().toString().trim());
                        jsonObject.put("signInVia", "");
                        jsonObject.put("password", edt_password.getText().toString().trim());
                        jsonObject.put("gcmRegId", Session.getToken(LoginActivity.this));
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
    public void SynData(LoginDetails loginDetails) {
        super.SynData(loginDetails);
        if (loginDetails != null) {
            try {
                JSONObject jsonObject_main = new JSONObject();
                JSONObject jsonObject = new JSONObject();
                jsonObject_main = getCommontHeaderParams();

                jsonObject.put("email", loginDetails.emailId);

                if (loginDetails.facebookId.length() > 0) {
                    jsonObject.put("authKeyFb",loginDetails.facebookId);
                    jsonObject.put("signInVia", "F");
                } else if (loginDetails.googleId.length() > 0) {
                    jsonObject.put("authKeyG", loginDetails.googleId);
                    jsonObject.put("signInVia", "G");
                } else return;
                jsonObject_main.put("body", jsonObject);
                CallAPI(jsonObject_main);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                finish();
                break;
            case R.id.forgotpassword:
                intent = new Intent(LoginActivity.this, ForgotpasswordActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void CallAPI(JSONObject params) {
        if (Util.isNetworkConnected(LoginActivity.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(LOGINAPI, "LOGINAPI", params, LoginActivity.this, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(LoginActivity.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetDetails_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    Session.setAllInfo(LoginActivity.this, msg.getData().getString("responce"));
                    try {
                        JSONObject jsonObject = new JSONObject(msg.getData().getString("responce"));
                        Session.setUserID(LoginActivity.this, jsonObject.getString("customerId"));
                        Session.setEmailId(LoginActivity.this, jsonObject.getString("email"));
                        Session.setLoginStatus(LoginActivity.this, true);
                        Session.setAllInfo(LoginActivity.this, msg.getData().getString("responce"));
                        Session.setName(LoginActivity.this, jsonObject.getString("fname"));
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("paymentList"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        }
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Intent intent = new Intent(LoginActivity.this, LandingPageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        ActivityCompat.finishAffinity(LoginActivity.this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Util.ShowToast(LoginActivity.this, e.getMessage());
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
                    if (Util.isNetworkConnected(LoginActivity.this)) {
                        CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(LoginActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(LoginActivity.this, msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(LoginActivity.this, msg.getData().getString("msg"));
            }
        }
    };
}
