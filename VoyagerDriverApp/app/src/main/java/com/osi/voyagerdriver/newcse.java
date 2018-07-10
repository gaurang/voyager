package com.osi.voyagerdriver;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.osi.voyagerdriver.AsyncTask.CallAPI;
import com.osi.voyagerdriver.Util.Util;
import com.osi.voyagerdriver.dialogs.ProgressDialogView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Brij on 12-03-2016.
 */
public class newcse extends ParentActivity implements View.OnClickListener{
    private TextView headername;
    private ImageView ic_back;
    private EditText edt_mobilenumber;
    private EditText edt_emailid;
    private Button verify;
    private EditText password;
    private EditText re_enterpassword;
    private static final String REGISTERAPI = "http://192.168.1.106:8080/uc/api/cse/changePassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newcse);
        headername = (TextView) findViewById(R.id.headername);
        headername.setText("FORGOT PASSWORD");
        //edt_emailid=(EditText) findViewById(R.id.edt_emailid);
        password=(EditText) findViewById(R.id.password);
        re_enterpassword=(EditText)findViewById(R.id.re_enterpassword);
        verify=(Button) findViewById(R.id.verify);
        verify.setOnClickListener(this);
        ic_back= (ImageView) findViewById(R.id.ic_back);
        ic_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == ic_back)
        {
            Intent intent = new Intent(newcse.this,LandingPageActivity.class);
            startActivity(intent);
        }else if(v == verify)
        {
            CallValidateRegister();
        }
    }
    public void CallValidateRegister() {
//        if (edt_emailid.getText().toString().trim().length() == 0) {
//            Util.ShowToast(newcse.this, "Enter email id");
//        } else {
        if (password.getText().toString().trim().length() == 0) {
            Util.ShowToast(newcse.this, "Enter password");
        } else if (re_enterpassword.getText().toString().trim().length() == 0) {
            Util.ShowToast(newcse.this, "Enter password");
        } else if (!password.getText().toString().equals(re_enterpassword.getText().toString())) {
            Util.ShowToast(newcse.this, "Password mismatch");
        } else {

            try {
                JSONObject jsonObject_main = new JSONObject();
                JSONObject jsonObject = new JSONObject();
                jsonObject_main = getCommontHeaderParams();
                jsonObject.put("password", password.getText().toString().trim());
                jsonObject.put("email", "abc@xyz.com");
                jsonObject_main.put("body", jsonObject);
                CallAPI(jsonObject_main);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void CallAPI(JSONObject params) {
        if (Util.isNetworkConnected(newcse.this)) {
            try {
                new CallAPI(REGISTERAPI, "REGISTEREAPI", params, newcse.this, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(newcse.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetDetails_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    // Session.setAllInfo(newcse.this,msg.getData().getString("responce"));
                    Intent intent = new Intent(newcse.this, LandingPageActivity.class);
                    try {
                        JSONObject jsonObject = new JSONObject(msg.getData().getString("responce"));
                        intent.putExtra("email", jsonObject.getString("email"));
                        intent.putExtra("regId", jsonObject.getString("regId"));
//                        intent.putExtra("mobile", jsonObject.getString("mobile"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    //    finish();
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(newcse.this)) {
                        CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        Util.ShowToast(newcse.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    Util.ShowToast(newcse.this, msg.getData().getString("msg"));
                }
            } else {
                Util.ShowToast(newcse.this, msg.getData().getString("msg"));
            }
        }
    };
}
