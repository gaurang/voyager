package com.osi.voyagerdriver;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.osi.voyagerdriver.Util.Session;
import com.osi.voyagerdriver.Util.Util;

import java.util.ArrayList;
import java.util.List;

public class SplashScreenActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

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
                intent.putExtra("key","0");
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashScreenActivity.this, Launch_activity.class);
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
    String REQUESTPERMISSINALLOW[] = {
            //Manifest.permission.MAPS_RECEIVE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS,
            //Manifest.permission.USE_CREDENTIALS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            //Manifest.permission.AUTHENTICATE_ACCOUNTS
            };

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
        if (checkPlayServices()) {

            // Building the GoogleApi client

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

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }
}
