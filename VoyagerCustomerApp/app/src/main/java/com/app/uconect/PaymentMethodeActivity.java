package com.app.uconect;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.uconect.AsyncTask.CallAPI;
import com.app.uconect.Util.Constants;
import com.app.uconect.Util.Session;
import com.app.uconect.Util.Util;
import com.app.uconect.dialogs.ProgressDialogView;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PaymentMethodeActivity extends ParentActivity implements View.OnClickListener {
    TextView headername;
    ImageView ic_back;
    Button btn_skip;
    Button creditdebit;
    Button paypal, corporatepaynment;
    boolean isforaddpayment = false;
    String TAG = "PaymentMethodeActivity";
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
                    // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Ashish Patil")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
    private Integer rembursed;
    private String accountType;
    private String email1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_method);
        progressdialog = new ProgressDialogView(PaymentMethodeActivity.this, "");
        BindView(null, savedInstanceState);
        accountType=getIntent().getExtras().getString("accountType");
        email1=getIntent().getExtras().getString("email");
    }

    String Allinfo;

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        isforaddpayment = getIntent().getBooleanExtra("isforaddpayment", false);
        try {
            Allinfo = getIntent().getStringExtra("Allinfo");
            PrintMessage("Allinfo " + Allinfo + "  " + isforaddpayment);
            ParseInfo(Allinfo);
        } catch (JSONException e) {
            e.printStackTrace();
            Util.ShowToast(PaymentMethodeActivity.this, "Error please try again!!!");
            finish();
        }
        headername = (TextView) findViewById(R.id.headername);
        btn_skip = (Button) findViewById(R.id.btn_skip);
        btn_skip.setVisibility(View.GONE);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        creditdebit = (Button) findViewById(R.id.creditdebit);
        paypal = (Button) findViewById(R.id.paypal);
        headername.setText("Payment Method");
        SetOnclicklistener();
        super.BindView(view, savedInstanceState);
    }

    public void InitiatPayPal() {
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
    }

    public void onBuyPressed() {
        /*
         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
         * Change PAYMENT_INTENT_SALE to
         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
         *     later via calls from your server.
         *
         * Also, to include additional payment details and an item list, see getStuffToBuy() below.
         */
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

        /*
         * See getStuffToBuy(..) for examples of some available payment options.
         */

        Intent intent = new Intent(PaymentMethodeActivity.this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal("1.75"), "USD", "sample item",
                paymentIntent);
    }

    @Override
    public void SetOnclicklistener() {
        ic_back.setOnClickListener(this);
        if (!isforaddpayment) {
            ic_back.setVisibility(View.GONE);
        }
        btn_skip.setOnClickListener(this);
        creditdebit.setOnClickListener(this);
        paypal.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (isforaddpayment) {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                if (!isforaddpayment) {
                    Intent intent = new Intent(PaymentMethodeActivity.this, LandingPageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
                finish();
                break;
            case R.id.btn_skip:
                Intent intent = new Intent(PaymentMethodeActivity.this, LandingPageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;

            case R.id.creditdebit:
                intent = new Intent(PaymentMethodeActivity.this, AddPaymentMethodeActivity.class);
                intent.putExtra("isforaddpayment", isforaddpayment);
                intent.putExtra("Allinfo", Allinfo);
                intent.putExtra("accountType",accountType);
                startActivity(intent);
                break;


            case R.id.paypal:
                if (Util.isNetworkConnected(PaymentMethodeActivity.this)) {
                    //onBuyPressed();
                    onProfileSharingPressed();
                } else {
                    Util.ShowToast(PaymentMethodeActivity.this, getString(R.string.nointernetmessage));
                }
                break;
        }
    }

    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

        /**
         * TODO: Send the authorization response to your server, where it can
         * exchange the authorization code for OAuth access and refresh tokens.
         *
         * Your server must then store these tokens, so that your server code
         * can execute payments for this user in the future.
         *
         * A more complete example that includes the required app-server to
         * PayPal-server integration is available from
         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
         */

    }

    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private PayPalOAuthScopes getOauthScopes() {
        /* create the set of required scopes
         * Note: see https://developer.paypal.com/docs/integration/direct/identity/attributes/ for mapping between the
         * attributes you select for this app in the PayPal developer portal and the scopes required here.
         */
        Set<String> scopes = new HashSet<>(
                Arrays.asList(PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL, PayPalOAuthScopes.PAYPAL_SCOPE_ADDRESS));
        return new PayPalOAuthScopes(scopes);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PROFILE_SHARING) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth = data
                        .getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {

                    String authorization_code = auth.getAuthorizationCode();

                    sendAuthorizationToServer(auth);
                    try {
                        Log.d("authorization_code ", authorization_code);
                        Log.i("ProfileSharingExample", auth.toJSONObject().toString(4) + "    " + auth.describeContents());
                        Log.i("ProfileSharingExample", auth.toJSONObject().toString());
                        JSONObject jsonObject_main = new JSONObject();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject_main = getCommontHeaderParams();
                        jsonObject.put("fname", fname);
                        jsonObject.put("regId", regId);
                        jsonObject.put("lname", lname);
                        jsonObject.put("mobile", mobile);
                        jsonObject.put("password", password);
                        jsonObject.put("email", email);
                        jsonObject.put("countryCode", countryCode);

                        if (isforaddpayment) {
                            jsonObject.put("customerId", Session.getUserID(PaymentMethodeActivity.this));
                            Log.i("asdfghjkl","asdfghjkl"+Session.getUserID(PaymentMethodeActivity.this));
                            jsonObject.put("paymentType", "");
                            jsonObject.put("cardNo", "");
                            jsonObject.put("nameOnCard", "");
                            jsonObject.put("expDate", "");
                            jsonObject.put("issueDate", "");

//                        B - Business or P - Personal
                            if (accountType.equals("P")) {
                                jsonObject.put("accountType", "P");
                            }else{
                                jsonObject.put("accountType", "C");
                                jsonObject.put("email",email1);
                            }
                            jsonObject.put("gatewayId", "PayPal");
//                        gateway User Id – email for paypal
                            jsonObject.put("gatewayUserId", "");
                            jsonObject.put("gatewayToken", "");
                            jsonObject.put("authKey", auth.toJSONObject().toString());
                        } else {
                            if (Constants.loginDetails != null) {
                                if (Constants.loginDetails.facebookId.length() > 0) {
                                    jsonObject.put("authKeyFb", Constants.loginDetails.facebookId);
                                    jsonObject.put("signInVia", "F");
                                } else if (Constants.loginDetails.googleId.length() > 0) {
                                    jsonObject.put("authKeyG", Constants.loginDetails.googleId);
                                    jsonObject.put("signInVia", "G");
                                }
                            }
                            jsonObject.put("password", password);
                            JSONObject jsonObject1 = new JSONObject();
                            //payment mode cash/card
                            jsonObject1.put("paymentType", "");
                            jsonObject1.put("cardNo", "");
                            jsonObject1.put("nameOnCard", "");
                            jsonObject1.put("expDate", "");
                            jsonObject1.put("issueDate", "");
                            jsonObject.put("customerId", Session.getUserID(PaymentMethodeActivity.this));
                            Log.i("asdfghjkl", "asdfghjkl" + Session.getUserID(PaymentMethodeActivity.this));
//                        B - Business or P - Personal
                            if (accountType.equals("P")) {
                                jsonObject1.put("accountType", "P");
                            }else{
                                jsonObject1.put("accountType", "C");
                                jsonObject1.put("email",email1);
                            }
                            jsonObject1.put("gatewayId", "PayPal");
//                        gateway User Id – email for paypal
                            jsonObject1.put("gatewayUserId", "");
                            jsonObject1.put("gatewayToken", "");
                            jsonObject1.put("authKey", auth.toJSONObject().toString());
                            jsonObject.put("paymentList", new JSONArray().put(jsonObject1));
                        }
                        jsonObject_main.put("body", jsonObject);
                        CallAPI(jsonObject_main);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("ProfileSharingExample", "The user canceled.");
            } else if (resultCode == PayPalProfileSharingActivity.RESULT_EXTRAS_INVALID) {
                Log.i("ProfileSharingExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i("", confirm.toJSONObject().toString(4));
                        Log.i("", confirm.getPayment().toJSONObject().toString(4));
                        /**
                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.
                         *
                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                         */
                        Toast.makeText(
                                getApplicationContext(),
                                "PaymentConfirmation info received from PayPal", Toast.LENGTH_LONG)
                                .show();
                        Intent intent = new Intent(PaymentMethodeActivity.this, LandingPageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(
                        getApplicationContext(),
                        "The user canceled.", Toast.LENGTH_LONG)
                        .show();
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(
                        getApplicationContext(),
                        "An invalid Payment or PayPalConfiguration was submitted.", Toast.LENGTH_LONG)
                        .show();
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }

        }
    }

    int REQUEST_CODE_PROFILE_SHARING = 100;

    public void onProfileSharingPressed() {
        Intent intent = new Intent(PaymentMethodeActivity.this, PayPalProfileSharingActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PayPalProfileSharingActivity.EXTRA_REQUESTED_SCOPES, getOauthScopes());

        startActivityForResult(intent, REQUEST_CODE_PROFILE_SHARING);
    }

    String regId;
    String email, countryCode;
    String mobile, fname, lname, password,customerId;

    public void ParseInfo(String Allinfo) throws JSONException {
        JSONObject jsonObject = new JSONObject(Allinfo);
        email = jsonObject.getString("email");
        regId = jsonObject.getString("regId");
        mobile = jsonObject.getString("mobile");
        fname = jsonObject.getString("fname");
        lname = jsonObject.getString("lname");
        password = jsonObject.getString("password");
        mobile = jsonObject.getString("mobile");
        countryCode = jsonObject.getString("countryCode");
        customerId = Session.getUserID(PaymentMethodeActivity.this);
    }

    public void CallAPI(JSONObject params) {
        if (Util.isNetworkConnected(PaymentMethodeActivity.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                if(accountType.equals("C")){
                    new CallAPI(ADDPAYMENTUSER, "ADDPAYMENT", params, PaymentMethodeActivity.this, GetDetails_Handler, true);
                }else if (isforaddpayment) {
                    new CallAPI(ADDPAYMENT, "ADDPAYMENT", params, PaymentMethodeActivity.this, GetDetails_Handler, true);
                } else
                    new CallAPI(ADDPAYMENTUSER, "ADDPAYMENTUSER", params, PaymentMethodeActivity.this, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(PaymentMethodeActivity.this, getString(R.string.nointernetmessage));
        }
    }

    public void ProceedButtopn() {
        if (isforaddpayment) {
            Intent intentMessage = new Intent();
            intentMessage.putExtra("MESSAGE", "Success");
            setResult(2, intentMessage);
            finish();

        } else {
            Intent intent = new Intent(PaymentMethodeActivity.this, LandingPageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            ActivityCompat.finishAffinity(PaymentMethodeActivity.this);
        }
    }

    Handler GetDetails_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    try {
                        if (isforaddpayment) {
                        } else {
                            JSONObject jsonObject = new JSONObject(msg.getData().getString("responce"));
                            Session.setUserID(PaymentMethodeActivity.this, jsonObject.getString("customerId"));
                            Session.setEmailId(PaymentMethodeActivity.this, jsonObject.getString("email"));
                            Session.setLoginStatus(PaymentMethodeActivity.this, true);
                            Session.setAllInfo(PaymentMethodeActivity.this, msg.getData().getString("responce"));
                        }
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        ProceedButtopn();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Util.ShowToast(PaymentMethodeActivity.this, e.getMessage());
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
                    if (Util.isNetworkConnected(PaymentMethodeActivity.this)) {
                        CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(PaymentMethodeActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(PaymentMethodeActivity.this, msg.getData().getString("msg"));
                }
            } else

            {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(PaymentMethodeActivity.this, msg.getData().getString("msg"));
            }
        }
    };
}
