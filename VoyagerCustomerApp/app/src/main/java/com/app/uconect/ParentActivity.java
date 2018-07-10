package com.app.uconect;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.System;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.uconect.AsyncTask.CallAPI;
import com.app.uconect.Util.Session;
import com.app.uconect.Util.Util;
import com.app.uconect.dialogs.ProgressDialogView;
import com.app.uconect.interfaces.AsyncInterface;
import com.app.uconect.interfaces.MyInterface;
import com.app.uconect.interfaces.myUrls;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by osigroups on 1/9/2016.
 */
public class ParentActivity extends AppCompatActivity implements myUrls, AsyncInterface, MyInterface {
    public ProgressDialogView progressdialog;
    String PermisonsList[] = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
    public static final String TAG = "ParentActivity";
    private GoogleApiClient client;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        if (!CheckEnableGPS()) {
           /* this.finishAndRemoveTask();
            finish();*/
            return;
        }
        if (Util.isNetworkConnected(ParentActivity.this)) {
        } else {
            Util.ShowToast(ParentActivity.this, getString(R.string.nointernetmessage));
            finish();
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(android.R.color.background_dark));
            }
        } catch (Exception e) {
        }

    }

    private boolean CheckEnableGPS() {
        String provider = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.equals("")) {
            return true;
        } else {

            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            Toast.makeText(ParentActivity.this, "Kindly enable GPS",
                    Toast.LENGTH_LONG).show();
            return false;
        }

    }

    public void CallSessionID(Handler GetDetails_Handler, String jsonObject) {
        if (Util.isNetworkConnected(ParentActivity.this)) {
            try {
                new CallAPI(LOGINAPI, "LOGINAPI", new JSONObject(), ParentActivity.this, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {

            //   progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(ParentActivity.this, getString(R.string.nointernetmessage));
        }
    }

    public void ParseSessionDetails(String Data) {
    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void SetOnclicklistener() {

    }


    public void PrintMessage(String Data) {
        Log.d("###### Call123 ", Data);
    }

    public JSONObject getCommontHeaderParams() throws JSONException {
        String device_id = "";
        try {

            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            device_id = tm.getDeviceId();

        } catch (Exception ex) {
        }
        JSONObject jsonObjectmain = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deviceId", System.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID));
        jsonObject.put("imeiNo", device_id);
        jsonObject.put("model", Build.MODEL);
        jsonObject.put("browser", "");
        jsonObject.put("osInfo", "Android");
        jsonObject.put("mobileNo", "");
        jsonObject.put("appId", device_id);

        jsonObjectmain.put("reqHeader", jsonObject);

        JSONObject jsonObject2 = new JSONObject();

        jsonObject2.put("operationCode", "");
        jsonObject2.put("moduleCode", "");
        jsonObject2.put("appCode", "");
        jsonObject2.put("encrKey", "");
        jsonObject2.put("appKey", "");
        jsonObjectmain.put("metaData", jsonObject2);
        return jsonObjectmain;
    }

    public void callLandingPage() {
        Intent intent = new Intent(getApplicationContext(), LandingPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        ActivityCompat.finishAffinity(this);
    }

    public void LogoutUser() {
        Session.setAllInfo(ParentActivity.this, "");
        Session.setUserID(ParentActivity.this, "");
        Session.setLoginStatus(ParentActivity.this, false);
        Session.setEmailId(ParentActivity.this, "");
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        ActivityCompat.finishAffinity(this);
    }
}
