package com.app.voyager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.app.voyager.AsyncTask.CallAPI;
import com.app.voyager.Util.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class DummyActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }



    public void CallAPI(JSONObject params) {
        if (Util.isNetworkConnected(DummyActivity.this)) {
            try {
//                progressdialog.show();
                new CallAPI(LOGINAPI, "LOGINAPI", params, DummyActivity.this, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            //   progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(DummyActivity.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetDetails_Handler = new Handler() {
        public void handleMessage(Message msg) {
            System.out.println(msg.getData().toString());

            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {

                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(DummyActivity.this)) {
                        CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        Util.ShowToast(DummyActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    Util.ShowToast(DummyActivity.this, getString(R.string.nointernetmessage));
                }
            } else {

                Util.ShowToast(DummyActivity.this, getString(R.string.nointernetmessage));
            }
        }
    };
}
