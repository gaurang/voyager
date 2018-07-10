package com.app.uconect.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.uconect.AsyncTask.CallAPI;
import com.app.uconect.Dataset.AddedPaymentData;
import com.app.uconect.LandingPageActivity;
import com.app.uconect.R;
import com.app.uconect.RideEstimateActivity;
import com.app.uconect.SeletectPaymentActivity;
import com.app.uconect.Util.Session;
import com.app.uconect.Util.Util;
import com.app.uconect.dialogs.BookingProfileSelectDialog;
import com.app.uconect.dialogs.ProgressDialogView;
import com.app.uconect.dialogs.PromocodeDialog;
import com.app.uconect.dialogs.RateCardDialog;
import com.app.uconect.trackride;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class LandingStepRide2 extends MainFragment implements View.OnClickListener {
    LandingPageActivity parentactivity;
    ProgressDialogView progressDialogView;
    TextView ratecaerd, rideestimate, promocode, cardpaymnt, profilepaymentselction;
    Button btn_cancel, btn_confirm;
    ArrayList<AddedPaymentData> AddedPaymentList = new ArrayList<>();
    ArrayList<AddedPaymentData> addedPaymentList = new ArrayList<>();
    private AddedPaymentData addedPaymentData;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        parentactivity = (LandingPageActivity) getActivity();
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentactivity.DisableMapFunctionality(false);
        View view = inflater.inflate(R.layout.fragment_landingpage2, container,
                false);

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }

            {
                try {
                    JSONObject jsonObject_main = new JSONObject();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject_main = parentactivity.getCommontHeaderParams();
                    jsonObject.put("customerId", Session.getUserID(parentactivity));
                    jsonObject_main.put("body", jsonObject);
                    CallGETPAYMENTTYPEAPI(jsonObject_main);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ratecaerd = (TextView) view.findViewById(R.id.ratecaerd);
        rideestimate = (TextView) view.findViewById(R.id.rideestimate);
        promocode = (TextView) view.findViewById(R.id.promocode);
        cardpaymnt = (TextView) view.findViewById(R.id.cardpaymnt);
        profilepaymentselction = (TextView) view.findViewById(R.id.profilepaymentselction);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        addedPaymentList = new ArrayList<>();
        addedPaymentData = new AddedPaymentData();

        SetOnclicklistener();
    }

    @Override
    public void SetOnclicklistener() {
        super.SetOnclicklistener();
        ratecaerd.setOnClickListener(this);
        rideestimate.setOnClickListener(this);
        promocode.setOnClickListener(this);
        cardpaymnt.setOnClickListener(this);
        profilepaymentselction.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void getStatusConnection(boolean isConnected) {
    }

    public void PrintMessage(String Error) {
        Log.d("########Call123 ", Error);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        final int animatorId = (enter) ? R.anim.push_up_in
                : R.anim.push_up_out;
        Animation anim = AnimationUtils.loadAnimation(parentactivity,
                animatorId);

        return anim;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ratecaerd:

                CallAPIFetchDataLocation();
                break;
            case R.id.btn_cancel:
//                Intent intent = new Intent(parentactivity, LandingPageActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                ActivityCompat.finishAffinity(parentactivity);
                parentactivity.FragmentManagement(LANDINGPAGESTEP1, REPLACE,
                        null, true, TAG_LANDINGPAGESTEP1);
                break;
            case R.id.btn_confirm:
                if (parentactivity.SourceData.Placename.length() > 0 && parentactivity.DestinationData.Placename.length() > 0) {
//                    Intent intent = new Intent(parentactivity, trackride.class);
//                    startActivity(intent);
                    Log.i("aaaaaaaaaaaaaaa","pppppppppppppppp"+Session.getAccountType(getContext()));
                    Log.i("aaaaaaaaaaaaaaa","pppppppppppppppp"+Session.getPaymentID(getContext()));
                    book();
                } else {
                    Util.ShowToast(parentactivity, "Select Destination");
                }
                break;

            case R.id.rideestimate:
                if (parentactivity.SourceData.Placename.length() > 0 && parentactivity.DestinationData.Placename.length() > 0) {
                    CallAPIRideEstmt();
                } else {
                    Util.ShowToast(parentactivity, "Select Destination");
                }

                break;
            case R.id.promocode:
                new PromocodeDialog(parentactivity).show();
                break;
            case R.id.cardpaymnt:
                Intent intentGetMessage1 = new Intent(getActivity(), SeletectPaymentActivity.class);
                startActivityForResult(intentGetMessage1, 2);

            break;
            case R.id.profilepaymentselction:

                if (addedPaymentList.size() == 0) {
                    try {
                        JSONObject jsonObject_main = new JSONObject();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject_main = parentactivity.getCommontHeaderParams();
                        jsonObject.put("customerId", Session.getUserID(parentactivity));
                        jsonObject_main.put("body", jsonObject);
                        CallGETPAYMENTAPI(jsonObject_main);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    new BookingProfileSelectDialog(parentactivity, addedPaymentList, parentactivity.boonkingData).show();
                }
                break;
        }

    }

    private void book() {
        JSONObject params = new JSONObject();
        if (profilepaymentselction.getText().toString().trim().length() == 0) {
            Util.ShowToast(getContext(), "Please select profile type");
        } else if (cardpaymnt.getText().toString().trim().length() == 0) {
            Util.ShowToast(getContext(), "Please select payment type");
        } else{
            try {

                JSONObject jsonObject = new JSONObject();
                params = parentactivity.getCommontHeaderParams();
                jsonObject.put("customerId", Session.getUserID(parentactivity));
                jsonObject.put("paymentId",  Session.getPaymentID(getContext()));
                jsonObject.put("vehicleType", parentactivity.boonkingData.vehicleType + "");
                jsonObject.put("accountType", Session.getAccountType(getContext()));
                jsonObject.put("srcPlace", parentactivity.SourceData.Placename);
                jsonObject.put("destPlace", parentactivity.DestinationData.Placename);
                jsonObject.put("sourceLatitude", parentactivity.SourceData.latitude);
                jsonObject.put("sourceLongitude", parentactivity.SourceData.longitude);
                jsonObject.put("destLatitude", parentactivity.DestinationData.latitude);
                jsonObject.put("destLongitude", parentactivity.DestinationData.longitude);
                params.put("body", jsonObject);
                CallAPIBook(params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void CallAPIBook(JSONObject params) {
        if (Util.isNetworkConnected(parentactivity)) {
            try {

                parentactivity.progressdialog.show();
                new CallAPI(CONFIRM, "CONFIRM", params, parentactivity, GetBookRide_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            parentactivity.progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(parentactivity, getString(R.string.nointernetmessage));
        }
    }

    private String bookingId;
    Handler GetBookRide_Handler = new Handler() {
        public void handleMessage(Message msg) {
            parentactivity.progressdialog.SetDissmiss();
            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    try {
                        JSONObject jsonObject = new JSONObject(msg.getData().getString("responce"));
                        bookingId=jsonObject.getString("bookingId");
                        Session.setBookingId(getContext(),jsonObject.getString("bookingId"));
                        btn_confirm.setEnabled(false);
                        btn_cancel.setEnabled(false);
                        parentactivity.progressdialog = new ProgressDialogView(getContext(), "Please wait while we are searching for your ride");
                        parentactivity.progressdialog.show();
                        Log.i("aaaaaaaaaaaaaaaa","aaaaaaaaaaaa"+bookingId);
                        track();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    parentactivity.ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPIRideEstmt();
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

    private void track() {
        JSONObject params = new JSONObject();
        try {

            JSONObject jsonObject = new JSONObject();
            params = parentactivity.getCommontHeaderParams();
            jsonObject.put("customerId", Session.getUserID(parentactivity));
            jsonObject.put("bookingId",bookingId);
            params.put("body", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Util.isNetworkConnected(parentactivity)) {
            try {
                new CallAPI(TRACKRIDE, "TRACKRIDE", params, parentactivity, GetTrackRide_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Util.ShowToast(parentactivity, getString(R.string.nointernetmessage));
        }
    }

    private Toast toast;
    Handler GetTrackRide_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    try {
                        JSONObject jsonobject = new JSONObject(msg.getData().getString("responce"));
                        if(jsonobject.getString("status").equals("BKD")) {
//                            toast = Toast.makeText(getActivity(), "Please wait while we are searching for your ride", Toast.LENGTH_SHORT);
//                            toast.show();
                            track();
                        }else if(jsonobject.getString("status").equals("CON")){
                            //toast.cancel();
                            parentactivity.progressdialog.dismiss();
                            Intent intent = new Intent(parentactivity, trackride.class);
                            intent.putExtra("allInfo", msg.getData().getString("responce"));
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    parentactivity.ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPIRideEstmt();
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


    /*********************************************************
     * Start for get Ride Estemate
     **************************************/
    public void CallAPIRideEstmt() {
        JSONObject params = new JSONObject();
        try {

            JSONObject jsonObject = new JSONObject();
            params = parentactivity.getCommontHeaderParams();
            jsonObject.put("customerId", Session.getUserID(parentactivity));
            jsonObject.put("vehicleType", parentactivity.boonkingData.vehicleType + "");
            jsonObject.put("accountType",Session.getAccountType(getContext()));
            jsonObject.put("sourceLatitude", parentactivity.SourceData.latitude);
            jsonObject.put("sourceLongitude", parentactivity.SourceData.longitude);
            jsonObject.put("destLatitude", parentactivity.DestinationData.latitude);
            jsonObject.put("destLongitude", parentactivity.DestinationData.longitude);
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
            Util.ShowToast(parentactivity, getString(R.string.nointernetmessage));
        }
    }

    Handler Getridestmt_Handler = new Handler() {
        public void handleMessage(Message msg) {
            parentactivity.progressdialog.SetDissmiss();
            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    try {

                        //Session.getTarrifplans(msg.getData().getString("responce"));
                        Intent intent = new Intent(parentactivity, RideEstimateActivity.class);
                        intent.putExtra("Data", msg.getData().getString("responce"));
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    parentactivity.ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPIRideEstmt();
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
    /**********************************************
     * Start Get Location
     *********************************/
    public void CallAPIFetchDataLocation() {
        JSONObject params = new JSONObject();
        try {

            JSONObject jsonObject = new JSONObject();

            params = parentactivity.getCommontHeaderParams();
            jsonObject.put("customerId", Session.getUserID(parentactivity));
            jsonObject.put("vehicleType", parentactivity.boonkingData.vehicleType + "");
            jsonObject.put("sourceLatitude", parentactivity.SourceData.latitude);
            jsonObject.put("sourceLongitude", parentactivity.SourceData.longitude);
            jsonObject.put("destLatitude", parentactivity.DestinationData.latitude);
            jsonObject.put("destLongitude", parentactivity.DestinationData.longitude);
            params.put("body", jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Util.isNetworkConnected(parentactivity)) {
            try {
                parentactivity.progressdialog.show();
                new CallAPI(FARECARD, "FARECARD", params, parentactivity, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Util.ShowToast(parentactivity, getString(R.string.nointernetmessage));
        }
    }

    Handler GetDetails_Handler = new Handler() {
        public void handleMessage(Message msg) {
            parentactivity.progressdialog.dismiss();
            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {

                    try {
                        new RateCardDialog(parentactivity, Session.getTarrifplans(msg.getData().getString("responce")), parentactivity.boonkingData.vehicleName + "").show();
//
//                        JSONArray jsonarray = new JSONArray(msg.getData().getString("responce"));
//                        Session.getTarrifplans(msg.getData().getString("responce"));
//                        Intent intent = new Intent(parentactivity, RideEstimateActivity.class);
//                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    parentactivity.ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPIFetchDataLocation();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(parentactivity)) {
                        parentactivity.CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {

                    }
                } else {

                }
            } else {

            }


        }
    };
    /******************************************** End Of get Location ****************************************************/
    /***********************************************
     * Start of get Payment Profile
     ****************************************/
    public void CallGETPAYMENTAPI(JSONObject params) {
        if (Util.isNetworkConnected(parentactivity)) {
            try {

                parentactivity.progressdialog.show();
                new CallAPI(PaymentTPROFILE, "PaymentTPROFILE", params, parentactivity, GetGetpayment_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            parentactivity.progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(parentactivity, getString(R.string.nointernetmessage));
        }
    }


    Handler GetGetpayment_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {

                    parentactivity.progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    try {
                        JSONArray jsonArray = new JSONArray(msg.getData().getString("responce"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            {
                                addedPaymentData.id = 0;
                                if (jsonObject.has("defalt_payment"))
                                    addedPaymentData.defalt_payment = jsonObject.getInt("defalt_payment");
                                addedPaymentData.paymentId = jsonObject.getString("paymentId");
                                addedPaymentData.customerId = jsonObject.getString("customerId");
                                addedPaymentData.accountType = jsonObject.getString("accountType");
                                addedPaymentData.email = jsonObject.getString("email");
                                addedPaymentData.gatewayId = jsonObject.getString("gatewayId");

                                addedPaymentList.add(addedPaymentData);

                            }
                        }
                        new BookingProfileSelectDialog(parentactivity, addedPaymentList, parentactivity.boonkingData).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    parentactivity.ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallGETPAYMENTAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(parentactivity)) {
                        parentactivity.CallSessionID(GetGetpayment_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        parentactivity.progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(parentactivity, getString(R.string.nointernetmessage));

                    }
                } else {
                    parentactivity.progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(parentactivity, msg.getData().getString("msg"));

                }
            } else {
                parentactivity.progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(parentactivity, msg.getData().getString("msg"));

            }
        }
    };
/******************************************** End Of get Payment Profile****************************/
    /********************************************
     * Start Get Payment Type
     ***********************/

    public void CallGETPAYMENTTYPEAPI(JSONObject params) {
        if (Util.isNetworkConnected(parentactivity)) {
            try {


                new CallAPI(PaymentTPROFILE, "PaymentTPROFILE", params, parentactivity, GetPaymentType_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            parentactivity.progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(parentactivity, getString(R.string.nointernetmessage));
        }
    }
    public void setValues(AddedPaymentData notificationData) {
        cardpaymnt.setText("" + notificationData.gatewayId);
        if (notificationData.accountType.equals("C")) {
            profilepaymentselction.setText("Business");
        }
        else if (notificationData.accountType.equals("P")){
            profilepaymentselction.setText("Personal");
        }
    }

    Handler GetPaymentType_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    AddedPaymentList = new ArrayList<>();

                    //   Util.ShowToast(parentactivity, msg.getData().getString("responce"));
                    try {
                        JSONArray jsonArray = new JSONArray(msg.getData().getString("responce"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectdata = jsonArray.getJSONObject(i);
                            AddedPaymentData notificationData = new AddedPaymentData();
                            notificationData.id = i;
                            notificationData.customerId = jsonObjectdata.getString("customerId");
                            notificationData.paymentId = jsonObjectdata.getString("paymentId");
                            Session.setPaymentId(getContext(),jsonObjectdata.getString("paymentId"));
                            if(jsonObjectdata.getInt("defaultAccount") == 1) {
                                notificationData.accountType = jsonObjectdata.getString("accountType");
                                Session.setAccountType(getContext(),jsonObjectdata.getString("accountType"));
                                notificationData.gatewayId = jsonObjectdata.getString("gatewayId");
                            }
                            AddedPaymentList.add(notificationData);
                            setValues(notificationData);
                        }
                        //new PaymentSelectBookingDialog(parentactivity, AddedPaymentList, parentactivity.boonkingData).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    parentactivity.ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallGETPAYMENTTYPEAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(parentactivity)) {
                        parentactivity.CallSessionID(GetPaymentType_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        parentactivity.progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(parentactivity, getString(R.string.nointernetmessage));
                    }
                } else {
                    parentactivity.progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(parentactivity, msg.getData().getString("msg"));
                }
            } else {
                parentactivity.progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(parentactivity, msg.getData().getString("msg"));

            }
        }
    };
    /************************************** End Of payment tye ************************************/

    /*********************************************************
     * Start for get Ride Estemate
     **************************************/
    public void CallAPIBook() {
        JSONObject params = new JSONObject();
        if (parentactivity.boonkingData.vehicleType == null || parentactivity.boonkingData.paymentId == null || parentactivity.boonkingData.accountType == null || parentactivity.SourceData.latitude == 0 || parentactivity.SourceData.longitude == 0) {
        } else {
            try {
                JSONObject jsonObject = new JSONObject();
                params = parentactivity.getCommontHeaderParams();
                jsonObject.put("customerId", Session.getUserID(parentactivity));
                jsonObject.put("vehicleType", parentactivity.boonkingData.vehicleType);
                jsonObject.put("promoId", parentactivity.boonkingData.promoId);
                jsonObject.put("paymentId", parentactivity.boonkingData.paymentId);
                jsonObject.put("accountType", parentactivity.boonkingData.accountType);
                jsonObject.put("sourceLatitude", parentactivity.SourceData.latitude);
                jsonObject.put("sourceLongitude", parentactivity.SourceData.longitude);
                jsonObject.put("destLatitude", parentactivity.DestinationData.latitude);
                jsonObject.put("destLongitude", parentactivity.DestinationData.longitude);


                params.put("body", jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (Util.isNetworkConnected(parentactivity)) {
                try {
                    parentactivity.progressdialog.show();
                    new CallAPI(RIDEESTAMATE, "FARECARD", params, parentactivity, Getbooknow_Handler, true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Util.ShowToast(parentactivity, getString(R.string.nointernetmessage));
            }
        }
    }

    Handler Getbooknow_Handler = new Handler() {
        public void handleMessage(Message msg) {
            parentactivity.progressdialog.SetDissmiss();
            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    try {

                        //Session.getTarrifplans(msg.getData().getString("responce"));
                        Intent intent = new Intent(parentactivity, RideEstimateActivity.class);
                        intent.putExtra("Data", msg.getData().getString("responce"));
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    parentactivity.ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPIRideEstmt();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(parentactivity)) {
                        parentactivity.CallSessionID(Getbooknow_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {

                    }
                } else {

                }
            } else {

            }


        }
    };

    /***********************************
     * End Book Confirm
     ****************************************/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (null != data) {

                addedPaymentData = (AddedPaymentData) data.getExtras().getSerializable("mDataset");
                cardpaymnt.setText(addedPaymentData.gatewayId);
            }
        }
    }

    public void onBackPressed() {
        parentactivity.onBackPressed();
        parentactivity.finish();
    }
}
