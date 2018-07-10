package com.app.uconect;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.uconect.AsyncTask.CallAPI;
import com.app.uconect.CustumClaasess.OwnerInfo;
import com.app.uconect.Dataset.LoginDetails;
import com.app.uconect.Dataset.SpinnerModel;
import com.app.uconect.Util.Session;
import com.app.uconect.Util.Util;
import com.app.uconect.adapter.Countrycode_adapter;
import com.app.uconect.dialogs.ProgressDialogView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends SocialLoginActivity implements View.OnClickListener {
    TextView headername;
    ImageView ic_back;
    Spinner mySpinner;
    EditText edt_countrycode, edt_fname, edt_lname, edt_mobilenumber, edt_emailid, edt_password,edt_Confirm_password;
    Button btn_register;
    TelephonyManager telephonyManager;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressdialog = new ProgressDialogView(RegisterActivity.this, "Loading...");
        setContentView(R.layout.activity_register);
        GetHAshKey();
        BindView(null, savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
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

    }

    private String GEtEmailIds() {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(RegisterActivity.this)
                .getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                System.out.println("possibleEmail " + possibleEmail);

                return possibleEmail;
            }
        }
        return "";
    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Locale locale = Locale.getDefault();
        Getcountrycode = Getcountrycode();
        Log.d("IssuesEtc", "the local is " + telephonyManager.getSimCountryIso() + "  " + locale.getCountry() + "  " + locale.getISO3Country());
        headername = (TextView) findViewById(R.id.headername);
        edt_countrycode = (EditText) findViewById(R.id.edt_countrycode);
        edt_fname = (EditText) findViewById(R.id.edt_fname);
        edt_lname = (EditText) findViewById(R.id.edt_lname);
        btn_register = (Button) findViewById(R.id.btn_register);
        edt_mobilenumber = (EditText) findViewById(R.id.edt_mobilenumber);
        edt_emailid = (EditText) findViewById(R.id.edt_emailid);
        edt_password = (EditText) findViewById(R.id.edt_password);
        edt_Confirm_password=(EditText)findViewById(R.id.edt_Confirm_password);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        tv_login_fb = (ImageView) findViewById(R.id.tv_login_fb);
        setname();
        tv_login_googleplus = (ImageView) findViewById(R.id.tv_login_googleplus);
        mySpinner = (Spinner) findViewById(R.id.spinner1);
        Countrycode_adapter countrycode_adapter = new Countrycode_adapter(RegisterActivity.this, R.layout.spinner_countrycode, Getcountrycode);

        // Set adapter to spinner
        mySpinner.setAdapter(countrycode_adapter);
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edt_countrycode.setText(Getcountrycode.get(position).CountryName);
                edt_countrycode.setCompoundDrawablesWithIntrinsicBounds(Getcountrycode.get(position).Image, 0, 0, 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mySpinner.setSelection(setselectiondefault);
// Setting a Custom Adapter to the Spinner

        headername.setText("REGISTER");
        SetOnclicklistener();
        super.BindView(view, savedInstanceState);

        edt_emailid.setText(GEtEmailIds());
    }

    public void setname() {
        String Name = new OwnerInfo(RegisterActivity.this).OwnerGetinfoInfo(RegisterActivity.this);
        String Allname[] = Name.split(" ");
        String lastname = "";
        Log.d("number ", Name);
        for (int i = 0; i < Allname.length; i++) {
            if (i == 0) {
                edt_fname.setText(Allname[i] + "");
            } else {
                lastname = lastname.trim();
                lastname += Allname[i];
                edt_lname.setText(lastname + " ");
            }
        }
        try {
            TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();
            String regex = "^((\\+|00)(\\d{1,3})[\\s-]?)?(\\d{10})$";
            // mPhoneNumber="+917709728556";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(mPhoneNumber);
            if (m.matches()) {
                System.out.println("Country = " + m.group(3));
                System.out.println("Data = " + m.group(4));
            }
            edt_mobilenumber.setText(m.group(4));
        } catch (Exception e) {
        }
    }

    @Override
    public void SynData(LoginDetails loginDetails) {
        super.SynData(loginDetails);
        if (loginDetails != null) {
            edt_fname.setText(loginDetails.fName);
            edt_lname.setText(loginDetails.lName);
            edt_emailid.setText(loginDetails.emailId);
            edt_emailid.setEnabled(false);
            edt_emailid.setClickable(false);
        }
    }

    @Override
    public void SetOnclicklistener() {
        ic_back.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        edt_countrycode.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                finish();
                break;
            case R.id.btn_register:
                CallValidateRegister();
                break;
            case R.id.edt_countrycode:
                mySpinner.performClick();
                break;
        }
    }

    public void CallValidateRegister() {
        if (edt_fname.getText().toString().trim().length() == 0) {
            Util.ShowToast(RegisterActivity.this, "Enter first name");
        } else if (edt_lname.getText().toString().trim().length() == 0) {
            Util.ShowToast(RegisterActivity.this, "Enter last name");
        } else if (edt_mobilenumber.getText().toString().trim().length() == 0) {
            Util.ShowToast(RegisterActivity.this, "Enter mobile number");
        } else if (edt_emailid.getText().toString().trim().length() == 0) {
            Util.ShowToast(RegisterActivity.this, "Enter email id");
        } else if (edt_password.getText().toString().trim().length() == 0) {
            Util.ShowToast(RegisterActivity.this, "Enter password");
        } else if(edt_Confirm_password.getText().toString().trim().length() == 0){
            Util.ShowToast(RegisterActivity.this, "Enter Confirm password");
        }else if(!edt_password.getText().toString().equals(edt_Confirm_password.getText().toString())){
            Util.ShowToast(RegisterActivity.this, "Password & confirm password must be same");
        } else if (edt_mobilenumber.getText().toString().trim().length() < 10) {
            Util.ShowToast(RegisterActivity.this, "Enter 10 digit mobile number");
        } else if (!Util.isEmailValid(edt_emailid.getText().toString().trim())) {
            Util.ShowToast(RegisterActivity.this, "Enter valid email id");
        } else if (edt_password.getText().toString().trim().length() < 6) {
            Util.ShowToast(RegisterActivity.this, "Password must be greater than 6 digit");
        } else {

            try {
                JSONObject jsonObject_main = new JSONObject();
                JSONObject jsonObject = new JSONObject();
                jsonObject_main = getCommontHeaderParams();
                jsonObject.put("fname", edt_fname.getText().toString().trim());
                jsonObject.put("lname", edt_lname.getText().toString().trim());
                jsonObject.put("mobile", edt_mobilenumber.getText().toString().trim());
                jsonObject.put("email", edt_emailid.getText().toString().trim());
                jsonObject.put("password", edt_password.getText().toString());
                jsonObject.put("countryCode", edt_countrycode.getText().toString().trim());
                jsonObject.put("gcmRegId", Session.getToken(RegisterActivity.this));
                if (loginDetails != null) {
                    if (loginDetails.facebookId.length() > 0) {
                        jsonObject.put("authKeyFb", loginDetails.facebookId);
                        jsonObject.put("signInVia", "F");
                    } else if (loginDetails.googleId.length() > 0) {
                        jsonObject.put("authKeyG", loginDetails.googleId);
                        jsonObject.put("signInVia", "G");
                    }
                }
                jsonObject_main.put("body", jsonObject);
                CallAPI(jsonObject_main);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    ArrayList<SpinnerModel> Getcountrycode;
    int setselectiondefault = 0;

    public ArrayList<SpinnerModel> Getcountrycode() {
        String[] recourseList = this.getResources().getStringArray(R.array.CountryCodes);
        Getcountrycode = new ArrayList<>();
        try {
            for (int i = 0; i < recourseList.length; i++) {
                    SpinnerModel spinnerModel = new SpinnerModel();
                    spinnerModel.CountryName = recourseList[i].split(",")[0];
                    spinnerModel.Code = recourseList[i].split(",")[1].toLowerCase();
                    spinnerModel.Image = getResources().getIdentifier("drawable/" + recourseList[i].split(",")[1].toLowerCase(), null, getPackageName());
                if (telephonyManager.getSimCountryIso().trim().equalsIgnoreCase(recourseList[i].split(",")[1].toLowerCase().trim())) {
                    setselectiondefault = i;
                }
                Getcountrycode.add(spinnerModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Getcountrycode;
    }

    public void CallAPI(JSONObject params) {
        if (Util.isNetworkConnected(RegisterActivity.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(REGISTEREAPI, "REGISTEREAPI", params, RegisterActivity.this, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(RegisterActivity.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetDetails_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Intent intent = new Intent(RegisterActivity.this, MobileVerificationActivity.class);
                    try {
                        JSONObject jsonObject = new JSONObject(msg.getData().getString("responce"));
                        intent.putExtra("email", jsonObject.getString("email"));
                        intent.putExtra("regId", jsonObject.getString("regId"));
                        intent.putExtra("mobile", jsonObject.getString("mobile"));
                        intent.putExtra("countryCode",jsonObject.getString("countryCode"));
                        Session.setRegID(RegisterActivity.this, jsonObject.getString("regId"));
                        Session.setCc(RegisterActivity.this, jsonObject.getString("countryCode"));
                        Session.setName(RegisterActivity.this,jsonObject.getString("fname"));
                        Log.i("-------------","aaaaaaaa" + Session.getRegID(RegisterActivity.this));
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
                    if (Util.isNetworkConnected(RegisterActivity.this)) {
                        CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(RegisterActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(RegisterActivity.this, msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(RegisterActivity.this, msg.getData().getString("msg"));
            }
        }
    };

}
