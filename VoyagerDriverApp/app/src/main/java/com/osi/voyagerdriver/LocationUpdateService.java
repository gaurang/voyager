package com.osi.voyagerdriver;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.osi.voyagerdriver.AsyncTask.CallAPI;
import com.osi.voyagerdriver.Util.Session;
import com.osi.voyagerdriver.Util.Util;
import com.osi.voyagerdriver.interfaces.AsyncInterface;
import com.osi.voyagerdriver.interfaces.MyInterface;
import com.osi.voyagerdriver.interfaces.myUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class LocationUpdateService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, AsyncInterface, MyInterface, myUrls {
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 1000;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    private List<Address> addresses;

    // UI elements
    public LocationUpdateService() {
    }

    @Override
    public void onCreate() {
        Log.d("Call123 ", "onCreate");
        super.onCreate();
        int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getBaseContext());
        Log.i("aaaaaaaaaaaaaaa", "dlocccccccccccccccc" + status);
        if (status == ConnectionResult.SUCCESS) { // Google Play Services are
            // not available


            // Google Play Services are available
            createLocationRequest();
            // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(AppIndex.API).build();
            mGoogleApiClient.connect();

            // Getting reference to the SupportMapFragment
            // Create a new global location parameters object
            mLocationRequest = LocationRequest.create();
            /*
             * Set the update interval
             */
            mLocationRequest.setInterval(INTERVAL);

            // Use high accuracy
            mLocationRequest
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            // Set the interval ceiling to one minute
            mLocationRequest
                    .setFastestInterval(FASTEST_INTERVAL);
            if (mGoogleApiClient.isConnected()) {
                startLocationUpdates();
                Log.d(TAG, "Location update start .....................");
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Call123 ", "onStartCommand");

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("Call123 ", "onDestroy");
        stopLocationUpdates();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    long timeinterval = 0;

    protected void startLocationUpdates() {
        {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            Log.d(TAG, "Firing onLocationChanged..............................................");
                            mCurrentLocation = location;
                            updateUI();
                            timeinterval = (int) (System.currentTimeMillis() / 1000) % 60;
                            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                            Log.d(TAG, "Location update started ..............: " + timeinterval);
                        }
                    });
            Log.d(TAG, "Location update started ..............: " + mLastUpdateTime);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    private void updateUI() {
        Log.d(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());
            try {
                JSONObject jsonObject_main = new JSONObject();
                JSONObject jsonObject = new JSONObject();
                //jsonObject_main = getCommontHeaderParams();
                Session.setLatitude(LocationUpdateService.this, lat);
                Session.setLongitude(LocationUpdateService.this, lng);
                jsonObject.put("driverId",Session.getUserID(LocationUpdateService.this));
                jsonObject.put("lat", lat + "");
                jsonObject.put("lng", lng + "");
                jsonObject.put("place", "");
                jsonObject.put("subArea", "");
                jsonObject.put("area", "");
                jsonObject.put("countryCode", "");
                jsonObject.put("zip", "");
                jsonObject.put("zoneCode", "");
                jsonObject.put("vehicleType", Session.getVehicleType(LocationUpdateService.this));
                jsonObject.put("serviceType", Session.getServiceType(LocationUpdateService.this));
                jsonObject.put("vehicleId", Session.getDefaultVehicleId(LocationUpdateService.this));
                jsonObject.put("gcmRegId",Session.getToken(LocationUpdateService.this));
                jsonObject_main.put("body", jsonObject);

                CallAPI(jsonObject_main);

            } catch (JSONException e) {
                e.printStackTrace();
            }
//            Address.setText("At Time: " + mLastUpdateTime + "\n" +
//                    "Latitude: " + lat + "\n" +
//                    "Longitude: " + lng + "\n" +
//                    "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" +
//                    "Provider: " + mCurrentLocation.getProvider());
        } else {
            Log.d(TAG, "location is null ...............");
        }
    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                        }
                    });
            mGoogleApiClient.disconnect();
        }
        Log.d(TAG, "Location update stopped.......................");
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    CallAPI callAPI;

    public void CallAPI(JSONObject params) {
//        Intent intent = new Intent(LoginActivity.this, LandingPageActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
        if (callAPI == null && Util.isNetworkConnected(LocationUpdateService.this) && Session.getIsOnlioneStatus(LocationUpdateService.this) == LandingPageActivity.ONLINE) {
            try {

                callAPI = new CallAPI(SENDLOCATIONFORSHOW, "SENDLOCATIONFORSHOW", params, LocationUpdateService.this, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void PrintMessage(String Message) {
        Log.d(TAG, Message);
    }

    Handler GetDetails_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    callAPI = null;
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(LocationUpdateService.this)) {
                        CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                        callAPI = null;
                    } else {
                        callAPI = null;
                    }
                } else {
                    callAPI = null;
                }
            } else {
                callAPI = null;
            }
        }
    };

    @Override
    public void BindView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void SetOnclicklistener() {

    }

    public void CallSessionID(Handler GetDetails_Handler, String jsonObject) {
        if (Util.isNetworkConnected(LocationUpdateService.this)) {
            try {
//                progressdialog.show();
                new CallAPI(GenerateCSRFToken, "GenerateCSRFToken", new JSONObject(), LocationUpdateService.this, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void ParseSessionDetails(String Data) {
    }
}
