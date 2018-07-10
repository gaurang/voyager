package com.app.uconect.tabfragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.uconect.AsyncTask.CallAPI;
import com.app.uconect.Dataset.AddedPaymentData;
import com.app.uconect.ParentActivity;
import com.app.uconect.R;
import com.app.uconect.SeletectPaymentActivity;
import com.app.uconect.Util.Methods;
import com.app.uconect.Util.NetworkHelper;
import com.app.uconect.Util.Session;
import com.app.uconect.Util.Util;
import com.app.uconect.dialogs.ProgressDialogView;
import com.app.uconect.interfaces.AsyncInterface;
import com.app.uconect.interfaces.myUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by atul on 14/4/16.
 */
public class AccountsFragment extends Fragment implements View.OnClickListener,myUrls,AsyncInterface {
    private TextView txtedit;
    ProgressDialogView progressdialog;
    private EditText edtBusinessEmailId,edtPersonalEmailId;
    private TextView edtBusinessPayment,edtPersonalPayment,def,def1;
    private Button btnSave;
    AddedPaymentData addedPaymentData;
    RadioButton business,personal;
    boolean enable=false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_accounts_add,container,false);
        initComponent(view);
        if(NetworkHelper.isOnline(getActivity())){
            progressdialog = new ProgressDialogView(getActivity(), "Please wait..");
            progressdialog.show();
        try {
            JSONObject jsonObject_main = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject_main = ((ParentActivity)getActivity()).getCommontHeaderParams();
            jsonObject.put("customerId", Session.getUserID(getActivity()));
            jsonObject_main.put("body", jsonObject);
            CallGETPAYMENTAPI(jsonObject_main);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        }else {
            Methods.toastShort("Please check your internet connection..", getActivity());

        }
        return view;
    }

    private void initComponent(View view) {

        txtedit=(TextView)view.findViewById(R.id.txtedit);
        def=(TextView)view.findViewById(R.id.def);
        def1=(TextView)view.findViewById(R.id.def1);
        edtBusinessEmailId=(EditText)view.findViewById(R.id.edtBusinessEmailId);
        edtBusinessPayment=(TextView)view.findViewById(R.id.edtBusinessPayment);
        edtPersonalEmailId=(EditText)view.findViewById(R.id.edtPersonalEmailId);
        edtPersonalPayment=(TextView)view.findViewById(R.id.edtPersonalPayment);
        business=(RadioButton)view.findViewById(R.id.business);
        personal=(RadioButton)view.findViewById(R.id.personal);
        btnSave=(Button)view.findViewById(R.id.btnSave);
        business.setOnClickListener(this);
        personal.setOnClickListener(this);
        txtedit.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        edtPersonalPayment.setOnClickListener(this);
        edtBusinessPayment.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtedit:
                if(enable){
                    enable=false;
                }else{
                    enable=true;
                }
                uiComponent(enable);

                break;
            case R.id.edtBusinessEmailId:
                break;
            case R.id.edtBusinessPayment:
                validate();
                Intent intentGetMessage1 = new Intent(getActivity(), SeletectPaymentActivity.class);
                startActivityForResult(intentGetMessage1, 2);
                break;
            case R.id.edtPersonalEmailId:
                break;
            case R.id.edtPersonalPayment:
                Intent intentGetMessage = new Intent(getActivity(), SeletectPaymentActivity.class);
                startActivityForResult(intentGetMessage, 2);
                break;
            case R.id.business:
                personal.setChecked(false);
                try {
                    JSONObject jsonObject_main = new JSONObject();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject_main = ((ParentActivity)getActivity()).getCommontHeaderParams();
                    jsonObject.put("customerId", Session.getUserID(getActivity()));
                    jsonObject.put("accountType", "C");
                    jsonObject_main.put("body", jsonObject);
                    CallChangeDefaultAccountAPI(jsonObject_main);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.personal:
                business.setChecked(false);
                try {
                    JSONObject jsonObject_main = new JSONObject();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject_main = ((ParentActivity)getActivity()).getCommontHeaderParams();
                    jsonObject.put("customerId", Session.getUserID(getActivity()));
                    jsonObject.put("accountType", "P");
                    jsonObject_main.put("body", jsonObject);
                    CallChangeDefaultAccountAPI(jsonObject_main);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btnSave:
//                else if (addedPaymentData == null) {
//                Util.ShowToast(getActivity(), "Select Default payment");
//            }
                if (edtPersonalEmailId.getText().toString().trim().length() == 0) {
                    Util.ShowToast(getActivity(), "Enter email id");
                } else if (!Util.isEmailValid(edtPersonalEmailId.getText().toString().trim())) {
                    Util.ShowToast(getActivity(), "Enter valid email id");
                } else {
                    try {
                        JSONObject jsonObject_main = new JSONObject();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject_main = ((ParentActivity)getActivity()).getCommontHeaderParams();
                        jsonObject.put("customerId", Session.getUserID(getActivity()));
                        jsonObject.put("paymentId", addedPaymentData.paymentId);
                        jsonObject.put("email", edtBusinessEmailId.getText().toString().trim());
                        jsonObject.put("accountType", "C");
                        jsonObject_main.put("body", jsonObject);
                        CallGetProfilePAYMENTAPI(jsonObject_main);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }

    }

    private void validate() {
        if(edtBusinessEmailId.getText().toString().equals(null)){
            Toast.makeText(getActivity(), "Please Enter Email id",Toast.LENGTH_LONG).show();
        }
        else{
            try {
                JSONObject jsonObject_main = new JSONObject();
                JSONObject jsonObject = new JSONObject();
                jsonObject_main = ((ParentActivity)getActivity()).getCommontHeaderParams();
                jsonObject.put("email", edtBusinessEmailId.getText().toString().trim());
                jsonObject_main.put("body", jsonObject);
                CallVerify(jsonObject_main);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void CallVerify(JSONObject params) {
        if (Util.isNetworkConnected(getActivity())) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(VERIFYEMAILID, "ADDUPDATEACCOUNTPROFILE", params, getActivity(), GetVerify_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(getActivity(), getString(R.string.nointernetmessage));
        }
    }
    Handler GetVerify_Handler = new Handler() {
        public void handleMessage(Message msg) {

//            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {

                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    try {
                        JSONObject jsonObject = new JSONObject(msg.getData().getString("responce"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ((ParentActivity)getActivity()).ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallGetProfilePAYMENTAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(getActivity())) {
                        ((ParentActivity)getActivity()).CallSessionID(GetProfilepayment_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(getActivity(), getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(getActivity(), msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(getActivity(), msg.getData().getString("msg"));

            }
        }
    };

    public void CallGetProfilePAYMENTAPI(JSONObject params) {
        if (Util.isNetworkConnected(getActivity())) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(ADDUPDATEACCOUNTPROFILE, "ADDUPDATEACCOUNTPROFILE", params, getActivity(), GetProfilepayment_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(getActivity(), getString(R.string.nointernetmessage));
        }
    }
    Handler GetProfilepayment_Handler = new Handler() {
        public void handleMessage(Message msg) {

//            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {

                            progressdialog.dismissanimation(ProgressDialogView.ERROR);
                            Util.ShowToast(getActivity(), "Update successfully!");


                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ((ParentActivity)getActivity()).ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallGetProfilePAYMENTAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(getActivity())) {
                        ((ParentActivity)getActivity()).CallSessionID(GetProfilepayment_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(getActivity(), getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(getActivity(), msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(getActivity(), msg.getData().getString("msg"));

            }
        }
    };

    private void uiComponent(boolean enable) {
        if(enable){
            btnSave.setVisibility(View.VISIBLE);
            edtBusinessEmailId.setEnabled(true);
            edtBusinessPayment.setEnabled(true);
            edtPersonalEmailId.setEnabled(true);
            edtPersonalPayment.setEnabled(true);
        }else{
            btnSave.setVisibility(View.GONE);
            edtBusinessEmailId.setEnabled(false);
            edtBusinessPayment.setEnabled(false);
            edtPersonalEmailId.setEnabled(false);
            edtPersonalPayment.setEnabled(false);
        }

    }

    public void CallGETPAYMENTAPI(JSONObject params) {
        if (Util.isNetworkConnected(getActivity())) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(PaymentTPROFILE, "PaymentTPROFILE", params, getActivity(), GetGetpayment_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(getActivity(), getString(R.string.nointernetmessage));
        }
    }
    public void setValues(AddedPaymentData addedPaymentData) {
        edtBusinessEmailId.setText(""+addedPaymentData.corporateemail);
        edtBusinessPayment.setText(""+addedPaymentData.corporategateway);
        edtPersonalEmailId.setText(""+addedPaymentData.email);
        edtPersonalPayment.setText("" + addedPaymentData.gatewayId);
    }

    private String a="a";
    private String b="b";
    Handler GetGetpayment_Handler = new Handler() {
        public void handleMessage(Message msg) {
                Log.v("RESPONSE", "RESPONSE::" + msg.getData().toString());
//            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {

                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    AddedPaymentData addedPaymentData = new AddedPaymentData();
                    try {
                        JSONArray jsonArray = new JSONArray(msg.getData().getString("responce"));
                        for (int i = 0; i <jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if(jsonObject.getString("accountType").equals("C"))
                            {
                                a = jsonObject.getString("accountType");
                            }
                            if(jsonObject.getString("accountType").equals("P"))
                            {
                                b = jsonObject.getString("accountType");
                            }
                            if(a.equals("C") && b.equals("P"))
                            {
                                business.setEnabled(true);
                                personal.setEnabled(true);
                            }else if(a.equals("C"))
                            {
                                personal.setEnabled(false);
                                business.setEnabled(true);
                            }else if(b.equals("P"))
                            {
                                personal.setEnabled(true);
                                business.setEnabled(false);
                            }
                            if (jsonObject.getString("defaultAccount").equals("1") && jsonObject.getString("accountType").equals("C")) {
                                business.setChecked(true);
                                personal.setChecked(false);
                                if(a.equals("C") && b.equals("P")) {
                                    business.setText("Default");
                                    personal.setText("Make Default");
                                }
                                else{
                                    business.setText("Default");
                                    personal.setText("");
                                }
                            }else if (jsonObject.getString("defaultAccount").equals("1") && jsonObject.getString("accountType").equals("P")){
                                personal.setChecked(true);
                                business.setChecked(false);
                                if(a.equals("C") && b.equals("P")) {
                                    personal.setText("Default");
                                    business.setText("Make Default");
                                }
                                else{
                                    personal.setText("Default");
                                    business.setText("");
                                }
                            }

//                            if (jsonObject.getString("accountType").equalsIgnoreCase("C")) {
                            if (jsonObject.getString("accountType").equals("P")) {
                                addedPaymentData.id = 0;
                                if (jsonObject.has("defalt_payment"))
                                    addedPaymentData.defalt_payment = jsonObject.getInt("defalt_payment");
                                addedPaymentData.paymentId = jsonObject.getString("paymentId");
                                addedPaymentData.customerId = jsonObject.getString("customerId");
                                addedPaymentData.email = jsonObject.getString("email");
                                addedPaymentData.gatewayId = jsonObject.getString("gatewayId");
                                setValues(addedPaymentData);
//                            }
                            }
                            else{
                                addedPaymentData.id = 0;
                                if (jsonObject.has("defalt_payment"))
                                    addedPaymentData.defalt_payment = jsonObject.getInt("defalt_payment");
                                addedPaymentData.paymentId = jsonObject.getString("paymentId");
                                addedPaymentData.customerId = jsonObject.getString("customerId");
                                addedPaymentData.corporateemail = jsonObject.getString("email");
                                addedPaymentData.corporategateway = jsonObject.getString("gatewayId");
                                setValues(addedPaymentData);
                            }


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ((ParentActivity)getActivity()).ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallGETPAYMENTAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(getActivity())) {
                        ((ParentActivity)getActivity()).CallSessionID(GetGetpayment_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(getActivity(), getString(R.string.nointernetmessage));
                        getActivity().finish();
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(getActivity(), msg.getData().getString("msg"));
                    getActivity().finish();
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(getActivity(), msg.getData().getString("msg"));
                getActivity().finish();

            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (null != data) {

                addedPaymentData = (AddedPaymentData) data.getExtras().getSerializable("mDataset");
                edtBusinessPayment.setText(addedPaymentData.gatewayId);
                edtPersonalPayment.setText(addedPaymentData.gatewayId);
            }
        }
    }
    public void CallChangeDefaultAccountAPI(JSONObject params) {
        if (Util.isNetworkConnected(getActivity())) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(CHANGEDEFAULTACCOUNT, "CHANGEDEFAULTACCOUNT", params, getActivity(), GetChangeDefaultAccount_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(getActivity(), getString(R.string.nointernetmessage));
        }
    }
    Handler GetChangeDefaultAccount_Handler = new Handler() {
        public void handleMessage(Message msg) {

//            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {

                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(getActivity(), "Update successfully!");


                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ((ParentActivity)getActivity()).ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallGetProfilePAYMENTAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(getActivity())) {
                        ((ParentActivity)getActivity()).CallSessionID(GetProfilepayment_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(getActivity(), getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(getActivity(), msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(getActivity(), msg.getData().getString("msg"));

            }
        }
    };
}
