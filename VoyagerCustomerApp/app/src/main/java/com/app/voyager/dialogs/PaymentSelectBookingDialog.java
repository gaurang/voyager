package com.app.voyager.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.voyager.AsyncTask.CallAPI;
import com.app.voyager.Dataset.AddedPaymentData;
import com.app.voyager.Dataset.BoonkingData;
import com.app.voyager.LandingPageActivity;
import com.app.voyager.R;
import com.app.voyager.Util.Session;
import com.app.voyager.Util.Util;
import com.app.voyager.interfaces.AsyncInterface;
import com.app.voyager.interfaces.myUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by osigroups on 1/14/2016.
 */
public class PaymentSelectBookingDialog extends Dialog implements DialogInterface.OnClickListener, myUrls, AsyncInterface {
    LandingPageActivity parentactivity;
    TextView tv_cancel;
    RadioGroup rdbtn_group;
    ArrayList<AddedPaymentData> AddedPaymentlist;
    BoonkingData boonkingData;

    public PaymentSelectBookingDialog(LandingPageActivity context, ArrayList<AddedPaymentData> AddedPaymentList, BoonkingData boonkingData) {
        super(context, R.style.Progressdialogthem);
        parentactivity = context;

        this.boonkingData = boonkingData;
        this.AddedPaymentlist = AddedPaymentList;
        setContentView(R.layout.dialog_bookingpaymentselect);
        rdbtn_group = (RadioGroup) findViewById(R.id.rdbtn_group);
        rdbtn_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                parentactivity.boonkingData.paymentId = AddedPaymentlist.get(checkedId).paymentId;
            }

        });

        tv_cancel = (TextView) findViewById(R.id.tv_cancel);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        for (int i = 0; i < AddedPaymentList.size(); i++) {
            View view = parentactivity.getLayoutInflater().inflate(R.layout.dialog_paymenttype_container, null);
//            ImageView imageView = (ImageView) view.findViewById(R.id.btn_img);
            RadioButton radioButton = (RadioButton) view.findViewById(R.id.radio0);
            radioButton.setText(AddedPaymentList.get(i).gatewayId);
            if (parentactivity.boonkingData.paymentId == AddedPaymentlist.get(i).paymentId)
                radioButton.setChecked(true);
            radioButton.setId(i);
            rdbtn_group.addView(view);
        }
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