package com.osi.uconectdriver;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.osi.uconectdriver.AsyncTask.CallAPI;
import com.osi.uconectdriver.Dataset.ChangeVehicleData;
import com.osi.uconectdriver.Util.Session;
import com.osi.uconectdriver.Util.Util;
import com.osi.uconectdriver.dialogs.ProgressDialogView;

import org.json.JSONException;
import org.json.JSONObject;

public class LandingPageActivity extends ParentActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,LocationListener {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1;
    private static final String PROPERTY_REG_ID = "";
    public static int ONLINE = 1, OFFLINE = 0, BREAK = 2;
    private Button offline;
    private TextView textView4;
    private Button change;
    private Spinner spinner;
    private Object arg1;
    TextView statustext;
    private TextView earnings;
    private TextView rides;
    private TextView referral;
    private TextView documents;
    private TextView notification;
    private TextView help;
    private TextView settings;
    private TextView about,clock;
    private FrameLayout map1;
    private LinearLayout ride;
    private LinearLayout ride1;
    private Button accept,reject,confirm,online;
    Intent intent;
    private CountDownTimer countDownTimer;
    private boolean timerHasStarted = false;
    Toolbar mActionBarToolbar;
    Location mCurrentLocation;
    ChangeVehicleData changeVehicleData;
    private GoogleMap googleMap;
    Context applicationContext;
    private Context context;
    private final long startTime = 16 * 1000;
    private final long interval = 1 * 1000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private double longitude;
    private double latitude;
    private Button ok,change1;
    private PopupWindow pwindo;
    private String bookingId;
    private EditText reason;
    private String key;
    int SLECTEDOPTION = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        //Initializing our broadcast receiver
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
            Intent itent = new Intent(this, GcmIntentService.class);
            Log.i("gggccccccmmmmmmmm","gggggggcccccccccccmmmmmm");
            startService(itent);
        }


        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("");
        progressdialog = new ProgressDialogView(LandingPageActivity.this, "");
        setSupportActionBar(mActionBarToolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mActionBarToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        int width = getResources().getDisplayMetrics().widthPixels;
        Log.d("width ", width + "");
        toggle.syncState();
        earnings = (TextView) findViewById(R.id.earnings);
        rides = (TextView) findViewById(R.id.rides);
        referral = (TextView) findViewById(R.id.referral);
        documents = (TextView) findViewById(R.id.documents);
        notification = (TextView) findViewById(R.id.notification);
        help = (TextView) findViewById(R.id.help);
        settings = (TextView) findViewById(R.id.settings);
        about = (TextView) findViewById(R.id.about);
        map1 = (FrameLayout) findViewById(R.id.map1);
        ride = (LinearLayout) findViewById(R.id.ride);
        ride1 = (LinearLayout) findViewById(R.id.ride1);
        accept = (Button) findViewById(R.id.accept);
        reject = (Button) findViewById(R.id.reject);
        online = (Button) findViewById(R.id.online);
        clock = (TextView) findViewById(R.id.clock);
        change = (Button) findViewById(R.id.change);
        change.setOnClickListener(this);

        countDownTimer = new MyCountDownTimer(startTime, interval);
        clock.setText(clock.getText() + String.valueOf(startTime / 1000));
        accept.setOnClickListener(this);
        reject.setOnClickListener(this);
        earnings.setOnClickListener(this);
        rides.setOnClickListener(this);
        online.setOnClickListener(this);
        referral.setOnClickListener(this);
        documents.setOnClickListener(this);
        notification.setOnClickListener(this);
        help.setOnClickListener(this);
        settings.setOnClickListener(this);
        about.setOnClickListener(this);
        //spinner = (Spinner) findViewById(R.id.spinner);
        changeVehicleData = (ChangeVehicleData) getIntent().getSerializableExtra("data");
        textView4 = (TextView) findViewById(R.id.textView4);
        textView4.setText(Session.getDefaultVehicle(this));
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = width;
        navigationView.setLayoutParams(params);
//        statustext = (TextView) findViewById(R.id.statustext);
//
//        statustext.setOnClickListener(this);
        getLocation();
        Log.i("aaaaaaaaaaa", "sssssssssssss" + Session.getIsOnline(LandingPageActivity.this));
        if (getIntent().getExtras().getString("key").equals("1")) {
            String message = getIntent().getExtras().getString("message");
            try {
                JSONObject jsonObject = (new JSONObject(message));
                Session.setBookingId(LandingPageActivity.this, jsonObject.getString("bookingId"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!timerHasStarted) {
                countDownTimer.start();
                timerHasStarted = true;
            } else {
                countDownTimer.start();
                timerHasStarted = true;
            }
            map1.setVisibility(View.GONE);
            ride.setVisibility(View.VISIBLE);
            ride1.setVisibility(View.GONE);
            textView4.setVisibility(View.GONE);
            online.setText("GO OFFLINE");
            online.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.go_offline, 0, 0, 0);
            setStatus(Session.getIsOnlioneStatus(LandingPageActivity.this));
            SLECTEDOPTION = 1;
        }
        else if (Session.getIsOnline(LandingPageActivity.this).equals("online")) {
            map1.setVisibility(View.VISIBLE);
            ride.setVisibility(View.GONE);
            ride1.setVisibility(View.GONE);
            textView4.setVisibility(View.VISIBLE);
            online.setText("GO OFFLINE");
            online.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.go_offline, 0, 0, 0);
            setStatus(Session.getIsOnlioneStatus(LandingPageActivity.this));
            SLECTEDOPTION = 1;
        }

                setStatus(Session.getIsOnlioneStatus(LandingPageActivity.this));
            }


    //Registering receiver on activity resume
    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GcmIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GcmIntentService.REGISTRATION_ERROR));
    }


    //Unregistering receiver on activity paused
    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }


    private void getLocation() {
        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else { // Google Play Services are available


            // Getting reference to the SupportMapFragment of activity_main.xml
            MapFragment fm = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

            // Getting GoogleMap object from the fragment
            googleMap = fm.getMap();

            // Enabling MyLocation Layer of Google Map
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
            googleMap.setMyLocationEnabled(true);
            Log.i("status", "statusssssssssssss");
            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Getting Current Location
            Location location = locationManager.getLastKnownLocation(provider);
            Log.i("aaaaaaaaaaaaaahh","asdasda"+location);

            if (location != null) {
                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(provider, 20000, 0, (LocationListener) this);
        }
    }

    public void onLocationChanged(Location location) {


        // Getting latitude of the current location
        latitude = location.getLatitude();
        Log.i("bbbbbbbbbb","asdasda"+latitude);
        // Getting longitude of the current location
        longitude = location.getLongitude();
        Log.i("cccccccccccc","asdasda"+longitude);
        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Showing the current location in Google Map
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        // Setting latitude and longitude in the TextView tv_location

    }


    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.settings) {
            intent = new Intent(LandingPageActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.rides) {
            intent = new Intent(LandingPageActivity.this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.earnings) {
            intent = new Intent(LandingPageActivity.this, EarningActivity.class);
            startActivity(intent);
        } else if (id == R.id.referral) {
            intent = new Intent(LandingPageActivity.this, PromotionActivity.class);
            startActivity(intent);
        } else if (id == R.id.documents) {
            intent = new Intent(LandingPageActivity.this, DocumentActivity.class);
            startActivity(intent);
        } else if (id == R.id.help) {
            intent = new Intent(LandingPageActivity.this, HelpActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void CallAPI(JSONObject params) {

        if (Util.isNetworkConnected(LandingPageActivity.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                //progressdialog.show();
                if (SLECTEDOPTION == OFFLINE)
                    new CallAPI(GOOFFILNEMODE, "GOONLINEMODE", params, LandingPageActivity.this, GetDetails_Handler, true);
                else if (SLECTEDOPTION == ONLINE) {
                    initiatePopupWindow();
                    new CallAPI(GOONLINEMODE, "GOONLINEMODE", params, LandingPageActivity.this, GetDetails_Handler, true);
                }
                else
                    new CallAPI(GOOFFILNEMODE, "GOOFFILNEMODE", params, LandingPageActivity.this, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(LandingPageActivity.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetDetails_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    if (progressdialog.isShowing())
                        progressdialog.dismiss();
                    Session.setIsOnlioneStatus(LandingPageActivity.this, SLECTEDOPTION);
                    setStatus(Session.getIsOnlioneStatus(LandingPageActivity.this));


                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(LandingPageActivity.this)) {
                        CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(LandingPageActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(LandingPageActivity.this, msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(LandingPageActivity.this, msg.getData().getString("msg"));
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (v == settings) {
            intent = new Intent(LandingPageActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (v == rides) {
            intent = new Intent(LandingPageActivity.this, HistoryActivity.class);
            startActivity(intent);
        } else if (v == earnings) {
            intent = new Intent(LandingPageActivity.this, EarningActivity.class);
            startActivity(intent);
        } else if (v == referral) {
            intent = new Intent(LandingPageActivity.this, PromotionActivity.class);
            startActivity(intent);
        } else if (v == documents) {
            intent = new Intent(LandingPageActivity.this, Document.class);
            startActivity(intent);
        } else if (v == help) {
            intent = new Intent(LandingPageActivity.this, HelpActivity.class);
            startActivity(intent);
        } else if (v == about) {
            intent = new Intent(LandingPageActivity.this, AboutUs.class);
            startActivity(intent);
        } else if (v == notification) {
            intent = new Intent(LandingPageActivity.this, notifications.class);
            startActivity(intent);
        }
        if (v == online) {
            if(Session.getIsOnline(LandingPageActivity.this).equals("offline"))
            {
                SLECTEDOPTION=1;
                online.setText("GO OFFLINE");
                online.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.go_offline, 0, 0, 0);
            }
            else if(Session.getIsOnline(LandingPageActivity.this).equals("online"))
            {
                SLECTEDOPTION=0;
                online.setText("GO ONLINE");
                online.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.go_online, 0, 0, 0);

            }
                        try {
                            JSONObject jsonObject_main = new JSONObject();
                            JSONObject jsonObject = new JSONObject();
                            jsonObject_main = getCommontHeaderParams();
                            //LocationUpdateService ls = new LocationUpdateService() ;]
                            jsonObject.put("driverId", Session.getUserID(LandingPageActivity.this));
                            jsonObject.put("lat", latitude);
                            jsonObject.put("lng", longitude);
                            jsonObject.put("place", "");
                            jsonObject.put("subArea", "");
                            jsonObject.put("area", "");
                            jsonObject.put("countryCode", "");
                            jsonObject.put("zip", "");
                            jsonObject.put("zoneCode", "");
                            jsonObject.put("vehicleId", Session.getDefaultVehicleId(LandingPageActivity.this));
                            jsonObject.put("vehicleType", Session.getVehicleType(LandingPageActivity.this));
                            jsonObject.put("serviceType", Session.getServiceType(LandingPageActivity.this));
                            jsonObject.put("gcmRegId", Session.getToken(LandingPageActivity.this));
                            Log.i("aaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaa" + Session.getToken(LandingPageActivity.this));
                            jsonObject_main.put("body", jsonObject);
                            CallAPI(jsonObject_main);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

        }else if (v == change) {
            Context context = this;
            Intent startActivity = new Intent(context, changevehicle.class);
            startActivity(startActivity);
        } else if (v == accept) {
            try {
                JSONObject jsonObject_main = new JSONObject();
                JSONObject jsonObject = new JSONObject();
                jsonObject_main = getCommontHeaderParams();
                jsonObject.put("driverId", Session.getUserID(LandingPageActivity.this));
                jsonObject.put("bookingId", Session.getBookingId(LandingPageActivity.this));
                jsonObject.put("status","ACC");
                jsonObject_main.put("body", jsonObject);
                CallAPIBOOKINGACCEPTREJECT(jsonObject_main);
                countDownTimer.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(v == ok) {
            pwindo.dismiss();

        }
        else if(v == change1){
            Intent startActivity = new Intent(LandingPageActivity.this, changevehicle.class);
            startActivity(startActivity);
        }
        else if (v == reject) {
            initiatePopupWindow1();
        }
        else if(v == confirm) {
            try {
                if (reason.getText().toString().trim().equals("")) {
                    Util.ShowToast(LandingPageActivity.this, "Please Enter A Reason");
                } else {
                    JSONObject jsonObject_main = new JSONObject();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject_main = getCommontHeaderParams();
                    jsonObject.put("driverId", Session.getUserID(LandingPageActivity.this));
                    jsonObject.put("bookingId", Session.getBookingId(LandingPageActivity.this));
                    jsonObject.put("status", "REJ");
                    jsonObject.put("rejectReason", reason.getText().toString().trim());
                    jsonObject_main.put("body", jsonObject);
                    CallAPIBOOKINGACCEPTREJECT(jsonObject_main);
                    pwindo.dismiss();
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void CallAPIBOOKINGACCEPTREJECT(JSONObject params) {
        if (Util.isNetworkConnected(LandingPageActivity.this)) {
            try {
                new CallAPI(BOOKINGACCEPTREJECT, "BOOKINGACCEPTREJECT", params, LandingPageActivity.this, GetAcceptReject_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(LandingPageActivity.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetAcceptReject_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    // Session.setAllInfo(trackride.this,msg.getData().getString("responce"));
                    //Intent intent = new Intent(trackride.this, LandingPageActivity.class);
                    try {

                        JSONObject jsonObject = new JSONObject(msg.getData().getString("responce"));
                        JSONObject jsonObject1=new JSONObject(String.valueOf(jsonObject.getJSONObject("booking")));
                        if(jsonObject1.getString("status").equals("CON")){
                            intent=new Intent(LandingPageActivity.this,trackride.class);
                            intent.putExtra("allInfo",jsonObject.getString("customer"));
                            intent.putExtra("allInfo1",jsonObject.getString("booking"));
                            startActivity(intent);
                        }else{
                            map1.setVisibility(View.VISIBLE);
                            ride.setVisibility(View.GONE);
                            ride1.setVisibility(View.GONE);
                            textView4.setVisibility(View.VISIBLE);
                            online.setText("GO OFFLINE");
                            online.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.go_offline, 0, 0, 0);
                        }
//                        intent.putExtra("mobile", jsonObject.getString("mobile"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    //startActivity(intent);
                    //    finish();
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPIBOOKINGACCEPTREJECT(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(LandingPageActivity.this)) {
                        CallSessionID(GetAcceptReject_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        Util.ShowToast(LandingPageActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    Util.ShowToast(LandingPageActivity.this, msg.getData().getString("msg"));
                }
            } else {
                Util.ShowToast(LandingPageActivity.this, msg.getData().getString("msg"));
            }
        }
    };


    private void initiatePopupWindow1() {
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) LandingPageActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup_reject, (ViewGroup) findViewById(R.id.popup_element));
            pwindo = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
            reason=(EditText) layout.findViewById(R.id.reason);
            //pwindo.update(0, 0, 1050, 600);
            confirm = (Button) layout.findViewById(R.id.confirm);
            confirm.setOnClickListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setStatus(int status) {

        if (status == OFFLINE) {
            online.setText("GO ONLINE");
            online.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.go_online, 0, 0, 0);
            stopService(new Intent(LandingPageActivity.this, LocationUpdateService.class));
            map1.setVisibility(View.VISIBLE);
            ride.setVisibility(View.GONE);
            ride1.setVisibility(View.GONE);
            textView4.setVisibility(View.VISIBLE);
            Session.setIsOnline(LandingPageActivity.this, "offline");
            change.setEnabled(true);
            change.findFocus();

        } else if (status == ONLINE) {
            startService(new Intent(LandingPageActivity.this, LocationUpdateService.class));
            online.setText("GO OFFLINE");
            online.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.go_offline, 0, 0, 0);
            Session.setIsOnline(LandingPageActivity.this, "online");
            change.setEnabled(false);
            change.clearFocus();
//            intent=new Intent(LandingPageActivity.this,trackride.class);
//            startActivity(intent);

        } else {
            stopService(new Intent(LandingPageActivity.this, LocationUpdateService.class));
            online.setText("Break");
            online.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.break12, 0, 0, 0);
            map1.setVisibility(View.VISIBLE);
            ride.setVisibility(View.GONE);
            ride1.setVisibility(View.GONE);
            textView4.setVisibility(View.VISIBLE);

        }
    }


    private void initiatePopupWindow() {
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) LandingPageActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup_change, (ViewGroup) findViewById(R.id.popup_element));
            pwindo = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
            //pwindo.update(0, 0, 1050, 600);
            ok = (Button) layout.findViewById(R.id.ok);
            change1 = (Button) layout.findViewById(R.id.change1);
            ok.setOnClickListener(this);
            change1.setOnClickListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }
        @Override
        public void onFinish() {
            map1.setVisibility(View.VISIBLE);
            ride.setVisibility(View.GONE);
            ride1.setVisibility(View.GONE);
            textView4.setVisibility(View.VISIBLE);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            clock.setText("" + millisUntilFinished / 1000);

        }
    }


}

