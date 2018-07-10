package com.app.voyager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;

import com.app.voyager.AsyncTask.CallAPI;
import com.app.voyager.Util.Session;
import com.app.voyager.Util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SplashScreenActivity extends ParentActivity {
    String CONFIG1="http://192.168.1.114:8080/uc/api/config";
    private JSONObject jsonObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        CallAPIFetchDataLocation();
    }

    @Override
    public void onBackPressed() {

    }

    class SplashTimer extends CountDownTimer {
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public SplashTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            if (Session.getLoginStatus(SplashScreenActivity.this)) {
                Intent intent = new Intent(SplashScreenActivity.this, LandingPageActivity.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(SplashScreenActivity.this);
            } else {
                Intent intent = new Intent(SplashScreenActivity.this, RegisterOptionjActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        requestMultiplePermissions();
    }

    int REQUEST_LOCATION = 100;
    String REQUESTPERMISSINALLOW[] = {Manifest.permission.MAPS_RECEIVE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.USE_CREDENTIALS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.AUTHENTICATE_ACCOUNTS};

    private void requestMultiplePermissions() {


        List<String> permissions = new ArrayList<>();
        for (int i = 0; i < REQUESTPERMISSINALLOW.length; i++) {
            int hasLocPermission = ContextCompat.checkSelfPermission(this, REQUESTPERMISSINALLOW[i]);
            if (hasLocPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(REQUESTPERMISSINALLOW[i]);
            }
        }

        if (!permissions.isEmpty()) {
            String[] params = permissions.toArray(new String[permissions.size()]);
            ActivityCompat.requestPermissions(this, params, REQUEST_LOCATION);
        } else {
            Starttimer();
            // We already have permission, so handle as normal
        }
    }

    SplashTimer splashTimer;

    public void Starttimer() {
        if (Util.isNetworkConnected(SplashScreenActivity.this)) {
            if (splashTimer == null) {
                splashTimer = new SplashTimer(3000, 1000);
                splashTimer.start();
            }
        } else {
            Util.ShowToast(SplashScreenActivity.this, getString(R.string.nointernetmessage));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Starttimer();
                } else {
                    Util.ShowToast(SplashScreenActivity.this, "Please Accept all permissions");
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void CallAPIFetchDataLocation() {
        JSONObject params = new JSONObject();
        try {

            JSONObject jsonObject = new JSONObject();

            params = getCommontHeaderParams();
            jsonObject.put("customerId", Session.getUserID(SplashScreenActivity.this));

            params.put("body", jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Util.isNetworkConnected(SplashScreenActivity.this)) {
            try {

                new CallAPI(CONFIG, "CONFIG", params, SplashScreenActivity.this, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Util.ShowToast(SplashScreenActivity.this, getString(R.string.nointernetmessage));
            ActivityCompat.finishAffinity(this);
        }
    }


    Handler GetDetails_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    Session.setConfig(SplashScreenActivity.this, msg.getData().getString("responce"));

                    try {
                        jsonObject = new JSONObject(msg.getData().getString("responce"));
                        JSONObject jsonObject1 = new JSONObject(String.valueOf(jsonObject.getJSONObject("REWARD")));
                        JSONObject jsonObject2 = new JSONObject(String.valueOf(jsonObject1.getJSONObject("DRIVER")));
                        Session.setUnit(SplashScreenActivity.this,jsonObject2.getString("unit"));
                        Log.i("aaaaaaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaa" + jsonObject2.getString("unit"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    try {
//                        Intent intent = new Intent(SplashScreenActivity.this, RideEstimateActivity.class);
//                        startActivity(intent);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPIFetchDataLocation();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(SplashScreenActivity.this)) {
                        CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {

                    }
                } else {

                }
            } else {

            }


        }
    };
}
