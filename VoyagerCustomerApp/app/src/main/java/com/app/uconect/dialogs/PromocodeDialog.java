package com.app.uconect.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.uconect.AsyncTask.CallAPI;
import com.app.uconect.LandingPageActivity;
import com.app.uconect.R;
import com.app.uconect.Util.Session;
import com.app.uconect.Util.Util;
import com.app.uconect.interfaces.AsyncInterface;
import com.app.uconect.interfaces.myUrls;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by osigroups on 1/14/2016.
 */
public class PromocodeDialog extends Dialog implements DialogInterface.OnClickListener, myUrls, AsyncInterface {
    LandingPageActivity parentactivity;
    TextView tv_apply, tv_cancel;
    EditText edtv_promocode;

    public PromocodeDialog(LandingPageActivity context) {
        super(context, R.style.Progressdialogthem);
        parentactivity = context;
        setContentView(R.layout.dialog_promocode);
        tv_apply = (TextView) findViewById(R.id.tv_apply);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        edtv_promocode = (EditText) findViewById(R.id.edtv_promocode);
        tv_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtv_promocode.getText().toString().trim().length() == 0) {
                    Util.ShowToast(parentactivity, "Enter Promo code");
                } else {
                    CallAPIApplyPromocode();
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    /*********************************************************
     * Start for get Ride Estemate
     **************************************/
    public void CallAPIApplyPromocode() {
        JSONObject params = new JSONObject();
        try {

            JSONObject jsonObject = new JSONObject();
            params = parentactivity.getCommontHeaderParams();
            jsonObject.put("customerId", Session.getUserID(parentactivity));
            jsonObject.put("vehicleType", "TXI");
            jsonObject.put("sourceLatitude", parentactivity.SourceData.latitude);
            jsonObject.put("sourceLongitude", parentactivity.SourceData.longitude);
            jsonObject.put("destLatitude", parentactivity.SourceData.latitude);
            jsonObject.put("destLongitude", parentactivity.SourceData.longitude);
            params.put("body", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Util.isNetworkConnected(parentactivity)) {
            try {
                parentactivity.progressdialog.show();
                new CallAPI(RIDEESTAMATE, "FARECARD", params, parentactivity, Getridestmt_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Util.ShowToast(parentactivity, parentactivity.getString(R.string.nointernetmessage));
        }
    }

    Handler Getridestmt_Handler = new Handler() {
        public void handleMessage(Message msg) {
            parentactivity.progressdialog.SetDissmiss();
            parentactivity.PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    parentactivity.boonkingData.promoId = 1;
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    parentactivity.ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPIApplyPromocode();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(parentactivity)) {
                        parentactivity.CallSessionID(Getridestmt_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {

                    }
                } else {

                }
            } else {

            }


        }
    };
/******************************** End Of ride Estemate ***********************************/
/**********************************************/
}