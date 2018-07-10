package com.app.voyager;

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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shadab.s on 14-01-2016.
 */
public class PromotionActivity extends ParentActivity implements View.OnClickListener {

    TextView headername;
    ImageView ic_back;
    EditText edt_promo;
    Button apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promotioncode_activity);
        progressdialog = new ProgressDialogView(PromotionActivity.this, "");
        BindView(null, savedInstanceState);
    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        super.BindView(view, savedInstanceState);
        headername = (TextView) findViewById(R.id.headername);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        edt_promo = (EditText) findViewById(R.id.edt_promo);
        apply = (Button) findViewById(R.id.apply);
        headername.setText("PROMOTION");
        SetOnclicklistener();
    }

    @Override
    public void SetOnclicklistener() {
        super.SetOnclicklistener();
        ic_back.setOnClickListener(this);
        apply.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ic_back:
              onBackPressed();
                break;
            case R.id.apply:
                if (edt_promo.getText().toString().trim().length() < 4) {
                    Util.ShowToast(PromotionActivity.this, "Enter valid promo code");
                } else {

                    try {
                        JSONObject jsonObject_main = new JSONObject();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject_main = getCommontHeaderParams();
                        jsonObject.put("customerId", Session.getUserID(PromotionActivity.this));
                        jsonObject.put("promoCode", edt_promo.getText().toString().trim());
                        jsonObject_main.put("body", jsonObject);
                        CallAPI(jsonObject_main);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//            Intent intent = new Intent(RegisterActivity.this, MobileVerificationActivity.class);
//            startActivity(intent);

                }
                break;
        }

    }
    @Override
    public void onBackPressed() {
        callLandingPage();
    }

    public void CallAPI(JSONObject params) {
        if (Util.isNetworkConnected(PromotionActivity.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(APPLYPROMOCODE, "APPLYPROMOCODE", params, PromotionActivity.this, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            PrintMessage("");
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(PromotionActivity.this, getString(R.string.nointernetmessage));
        }
    }






    Handler GetDetails_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);



                    Util.ShowToast(PromotionActivity.this, "successfully");

                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(PromotionActivity.this)) {
                        CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(PromotionActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(PromotionActivity.this, msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(PromotionActivity.this, msg.getData().getString("msg"));
            }
        }
    };
}