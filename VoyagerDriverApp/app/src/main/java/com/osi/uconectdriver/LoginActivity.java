package com.osi.uconectdriver;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.osi.uconectdriver.AsyncTask.CallAPI;
import com.osi.uconectdriver.Util.Session;
import com.osi.uconectdriver.Util.Util;
import com.osi.uconectdriver.dialogs.ProgressDialogView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends ParentActivity implements View.OnClickListener {
    TextView headername;
    ImageView ic_back;
    EditText edt_emailid, edt_password;
    TextView forgotpassword;
    TextView show;
//    TextView newcse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BindView(null, savedInstanceState);
    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        headername = (TextView) findViewById(R.id.headername);
        forgotpassword = (TextView) findViewById(R.id.forgotpassword);
        progressdialog = new ProgressDialogView(LoginActivity.this, "");
//        newcse=(TextView)findViewById(R.id.newcse);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        edt_emailid = (EditText) findViewById(R.id.edt_emailid);
        edt_password = (EditText) findViewById(R.id.edt_password);
        show=(TextView)findViewById(R.id.show);
        headername.setText("SIGN IN");
        SetOnclicklistener();
        super.BindView(view, savedInstanceState);
    }

    @Override
    public void SetOnclicklistener() {
        ic_back.setOnClickListener(this);
        forgotpassword.setOnClickListener(this);
       // newcse.setOnClickListener(this);
        show.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        edt_password.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });
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
                        jsonObject.put("driverCode", "");
                        jsonObject.put("password", edt_password.getText().toString().trim());
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


    public void SynData( ) {


            try {
                JSONObject jsonObject_main = new JSONObject();
                JSONObject jsonObject = new JSONObject();
                jsonObject_main = getCommontHeaderParams();


                jsonObject_main.put("body", jsonObject);
                CallAPI(jsonObject_main);

            } catch (JSONException e) {
                e.printStackTrace();
            }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.newcse:
//                Intent intent2 = new Intent(LoginActivity.this, newcse.class);
//                startActivity(intent2);
//                break;
            case R.id.ic_back:
                Intent intent1 = new Intent(LoginActivity.this, Launch_activity.class);
                startActivity(intent1);
                break;
            case R.id.forgotpassword:
                Intent intent = new Intent(LoginActivity.this, ForgotpasswordActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void CallAPI(JSONObject params) {
//        Intent intent = new Intent(LoginActivity.this, LandingPageActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
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
                        JSONArray jsonArray=jsonObject.getJSONArray("drVehicles");
//                        JSONObject jsonObject1= new JSONObject(String.valueOf(jsonArray.getJSONObject(0)));
                        Session.setVehicleType(LoginActivity.this, jsonObject.getString("vehicleType"));
                        Session.setServiceType(LoginActivity.this, jsonObject.getString("serviceType"));
                        Session.setUserID(LoginActivity.this, jsonObject.getString("driverId"));
                        Session.setEmailId(LoginActivity.this, jsonObject.getString("email"));
                        Session.setLoginStatus(LoginActivity.this, true);
                        Session.setDefaultVehicleId(LoginActivity.this,jsonObject.getString("vehicleId"));
                        Session.setDefaultVehicle(LoginActivity.this,jsonObject.getString("defaultVehicle"));
                        Session.setAllInfo(LoginActivity.this, msg.getData().getString("responce"));
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Intent intent = new Intent(LoginActivity.this, LandingPageActivity.class);
                        intent.putExtra("key","0");
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
