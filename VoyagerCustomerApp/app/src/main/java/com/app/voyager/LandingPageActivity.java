package com.app.voyager;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.voyager.AsyncTask.CallAPI;
import com.app.voyager.Dataset.BoonkingData;
import com.app.voyager.Dataset.CarLocationsData;
import com.app.voyager.Dataset.CarServiceData;
import com.app.voyager.Dataset.CarSubServiceData;
import com.app.voyager.Dataset.GData;
import com.app.voyager.Dataset.LocationDetailsData;
import com.app.voyager.Dataset.UserDetails;
import com.app.voyager.Util.Methods;
import com.app.voyager.Util.Session;
import com.app.voyager.Util.Util;
import com.app.voyager.activities.EnterSourceActivity;
import com.app.voyager.destination.DestinationPlaces;
import com.app.voyager.destination.EnterDestinationActivity;
import com.app.voyager.dialogs.ProgressDialogView;
import com.app.voyager.fragments.LandingStepRide1;
import com.app.voyager.fragments.LandingStepRide2;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LandingPageActivity extends ParentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    public BoonkingData boonkingData = new BoonkingData();
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000;
    private static final long FASTEST_INTERVAL = 1000;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    ArrayList<String> sourceAddressArrayList=new ArrayList<>();
    ArrayList<String> destinationAddressArrayList=new ArrayList<>();
    public GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    boolean ALLPERMISSIONGRANTERd = false;
    private TextView markerText;
    private LatLng center;
    private LinearLayout markerLayout;
    private Geocoder geocoder;
    private List<Address> addresses;
    private TextView Address;
    private TextView textView1_destination;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    public LocationDetailsData SourceData = new LocationDetailsData();
    public LocationDetailsData DestinationData = new LocationDetailsData();
    public ArrayList<CarServiceData> GetCarData;
    public ArrayList<CarSubServiceData> carSubServiceDatas=new ArrayList<>();
    LinearLayout locationMarker;
    TextView tv_timeduration;
    public int SelectedTab = 0;
    //TextView edt_username, edt_emailid;
    UserDetails userDetails;
    WebView webview;
   // Map<String, Marker> markerMap =  new ConcurrentHashMap<String, Marker>();
    ArrayList<Marker> mList = new ArrayList<Marker>();
    ArrayList<Marker> mListAll = new ArrayList<Marker>();


    public static boolean isLandingPageActivityOpen = false;              //added to stop API call when activity not visible by samson 1-4-2016
    LocationChangeListner locationChangeListner;
    private LandingStepRide1 parentactivity;
    private Button nav_about;


    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        super.BindView(view, savedInstanceState);
        userDetails = Session.GetUserInformation(Session.getAllInfo(LandingPageActivity.this));
        card_view_destinatio = (CardView) findViewById(R.id.card_view_destinatio);

        startUpCheck();

        progressdialog = new ProgressDialogView(LandingPageActivity.this, "Please wait...!");
        progressdialog.show();
        card_view_source = (CardView) findViewById(R.id.card_view_source);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        try {
            String a = Session.getConfig(LandingPageActivity.this);
            //   GetCarData = Session.GetCarData(new JSONObject("{\"TAXI\":{\"masterAttr\":\"SERVICE_TYPE\",\"attrName\":\"TAXI\",\"attrValue1\":\"TX\",\"attrValue2\":null,\"type\":null,\"level\":\"1\",\"unit\":null,\"subAttributes\":{\"TAXI\":{\"masterAttr\":\"TAXI\",\"attrName\":\"TAXI\",\"attrValue1\":\"TXI\",\"attrValue2\":\"\",\"type\":\"SERVICE_TYPE\",\"level\":\"2\",\"unit\":\"\",\"subAttributes\":null},\"MAXI_TAXI\":{\"masterAttr\":\"TAXI\",\"attrName\":\"MAXI_TAXI\",\"attrValue1\":\"MTX\",\"attrValue2\":\"\",\"type\":\"SERVICE_TYPE\",\"level\":\"2\",\"unit\":\"\",\"subAttributes\":null}}},\"PRIVATE\":{\"masterAttr\":\"SERVICE_TYPE\",\"attrName\":\"PRIVATE\",\"attrValue1\":\"PT\",\"attrValue2\":null,\"type\":null,\"level\":\"1\",\"unit\":null,\"subAttributes\":{\"MUV\":{\"masterAttr\":\"PRIVATE\",\"attrName\":\"MUV\",\"attrValue1\":\"MUV\",\"attrValue2\":\"\",\"type\":\"SERVICE_TYPE\",\"level\":\"2\",\"unit\":\"\",\"subAttributes\":null},\"SUV\":{\"masterAttr\":\"PRIVATE\",\"attrName\":\"SUV\",\"attrValue1\":\"SUV\",\"attrValue2\":\"\",\"type\":\"SERVICE_TYPE\",\"level\":\"2\",\"unit\":\"\",\"subAttributes\":null},\"HUTCH_BACK\":{\"masterAttr\":\"PRIVATE\",\"attrName\":\"HUTCH_BACK\",\"attrValue1\":\"HTB\",\"attrValue2\":\"\",\"type\":\"SERVICE_TYPE\",\"level\":\"2\",\"unit\":\"\",\"subAttributes\":null},\"SEDAN\":{\"masterAttr\":\"PRIVATE\",\"attrName\":\"SEDAN\",\"attrValue1\":\"SED\",\"attrValue2\":\"\",\"type\":\"SERVICE_TYPE\",\"level\":\"2\",\"unit\":\"\",\"subAttributes\":null}}}}"));
            JSONObject b=new JSONObject(a);
            GetCarData = Session.GetCarData(b.getJSONObject("SERVICE_TYPE"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        try {
//            JSONObject jsonObject_main = new JSONObject();
//            JSONObject jsonObject = new JSONObject();
//            jsonObject_main = getCommontHeaderParams();
//            jsonObject_main.put("body", jsonObject);
//            CallAPI(jsonObject_main);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        markerLayout = (LinearLayout) findViewById(R.id.locationMarker);
        nav_payment = (Button) findViewById(R.id.nav_payment);
        nav_history = (Button) findViewById(R.id.nav_history);
        nav_freeride = (Button) findViewById(R.id.nav_freeride);
        nav_promotion = (Button) findViewById(R.id.nav_promotion);
        nav_notification = (Button) findViewById(R.id.nav_notification);
        nav_about = (Button) findViewById(R.id.nav_about);
        nav_help = (Button) findViewById(R.id.nav_help);
        nav_setting = (Button) findViewById(R.id.nav_setting);
        tv_timeduration = (TextView) findViewById(R.id.tv_timeduration);
        markerText = (TextView) findViewById(R.id.locationMarkertext);
        locationMarker = (LinearLayout) findViewById(R.id.locationMarker);
        Address = (TextView) findViewById(R.id.adressText);
        Address.setOnClickListener(this);

        textView1_destination = (TextView) findViewById(R.id.textView1_destination);
        textView1_destination.setHint("Enter your destination");
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        LinearLayout navigationView = (LinearLayout) findViewById(R.id.nav_view);
        int width = getResources().getDisplayMetrics().widthPixels;
        Log.d("width ", width + "");
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = width;
        navigationView.setLayoutParams(params);
        SetOnclicklistener();
        PrintMessage("Email id " + Session.getEmailId(LandingPageActivity.this));

        FragmentManagement(LANDINGPAGESTEP1, REPLACE,
                null, true, TAG_LANDINGPAGESTEP1);
    }




    private void startUpCheck() {
        try {
            JSONObject jsonObject_main = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject_main = getCommontHeaderParams();
            jsonObject.put("customerId", Session.getUserID(LandingPageActivity.this));
            jsonObject_main.put("body", jsonObject);
            CallStartUp(jsonObject_main);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void CallStartUp(JSONObject params) {
        if (Util.isNetworkConnected(LandingPageActivity.this)) {
            try {
                new CallAPI(STARTUPCHECK, "STARTUPCHECK", params, LandingPageActivity.this, GetRating_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(LandingPageActivity.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetRating_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    try {
                        JSONObject jsonObject = new JSONObject(msg.getData().getString("responce"));
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("pendingRating"));
                        JSONArray jsonArray1 = new JSONArray(jsonObject.getString("trackBooking"));
                        Log.i("aaaaaaaa","aaaaaaaaaaaa"+jsonArray1.length());
                        if (jsonArray.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                Session.setBookingId(LandingPageActivity.this, jsonObject1.getString("bookingId"));
                                Intent startActivity = new Intent(LandingPageActivity.this, Rating.class);
                                startActivity.putExtra("rideTotalAmt", jsonObject1.getString("rideTotalAmt"));
                                startActivity.putExtra("currency", jsonObject1.getString("currency"));
                                startActivity(startActivity);
                                finish();
                            }
                        }else if (jsonArray1.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                Session.setBookingId(LandingPageActivity.this, jsonObject1.getString("bookingId"));
                                Intent startActivity = new Intent(LandingPageActivity.this, trackride.class);
                                startActivity.putExtra("allInfo",msg.getData().getString("trackBooking"));
                                startActivity(startActivity);
                                finish();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallStartUp(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(LandingPageActivity.this)) {
                        CallSessionID(GetRating_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        Util.ShowToast(LandingPageActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    Util.ShowToast(LandingPageActivity.this, msg.getData().getString("msg"));
                }

            }
        }
    };



//    public void CallAPI(JSONObject params) {
//        if (Util.isNetworkConnected(LandingPageActivity.this)) {
//            try {
//                if (progressdialog.isShowing())
//                    progressdialog.dismiss();
//                progressdialog.show();
//                new CallAPI(CONFIG1, "CONFIG", params, LandingPageActivity.this, Get_Handler, true);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else {
//            progressdialog.dismissanimation(ProgressDialogView.ERROR);
//            Util.ShowToast(LandingPageActivity.this, getString(R.string.nointernetmessage));
//        }
//    }
//
//    Handler Get_Handler = new Handler() {
//        public void handleMessage(Message msg) {
//
//            PrintMessage("Handler " + msg.getData().toString());
//            if (msg.getData().getBoolean("flag")) {
//                if (msg.getData().getInt("code") == SUCCESS) {
//                    Session.setAllInfo(LandingPageActivity.this, msg.getData().getString("responce"));
//                    try {
//                        JSONObject jsonObject = new JSONObject(msg.getData().getString("responce"));
//                        Log.i("asdfg","asdfg"+jsonObject);
//                        //GetCarData = Session.GetCarData(new JSONObject(String.valueOf(jsonObject)));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Util.ShowToast(LandingPageActivity.this, e.getMessage());
//                    }
//                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
//                    ParseSessionDetails(msg.getData().getString("responce"));
//                    try {
//                        CallAPI(new JSONObject(msg.getData()
//                                .getString("mExtraParam")));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
//                    if (Util.isNetworkConnected(LandingPageActivity.this)) {
//                        CallSessionID(GetDetails_Handler, msg.getData()
//                                .getString("mExtraParam"));
//                    } else {
//                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
//                        Util.ShowToast(LandingPageActivity.this, getString(R.string.nointernetmessage));
//                    }
//                } else {
//                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
//                    Util.ShowToast(LandingPageActivity.this, msg.getData().getString("msg"));
//                }
//            } else {
//                progressdialog.dismissanimation(ProgressDialogView.ERROR);
//                Util.ShowToast(LandingPageActivity.this, msg.getData().getString("msg"));
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_drawer);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getBaseContext());
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are
            // not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
                    requestCode);
            dialog.show();

        } else {

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
            mLocationRequest.setInterval(GData.UPDATE_INTERVAL_IN_MILLISECONDS);

            // Use high accuracy
            mLocationRequest
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            // Set the interval ceiling to one minute
            mLocationRequest
                    .setFastestInterval(GData.FAST_INTERVAL_CEILING_IN_MILLISECONDS);
            BindView(null, savedInstanceState);
        }

    }

    @Override
    public void SetOnclicklistener() {
        super.SetOnclicklistener();
        //  navigationView.setNavigationItemSelectedListener(this);


        card_view_destinatio.setOnClickListener(this);
        card_view_source.setOnClickListener(this);
        Address.setOnClickListener(this);
        nav_payment.setOnClickListener(this);
        nav_history.setOnClickListener(this);
        nav_freeride.setOnClickListener(this);
        nav_promotion.setOnClickListener(this);
        nav_notification.setOnClickListener(this);
        nav_help.setOnClickListener(this);
        nav_setting.setOnClickListener(this);
        nav_about.setOnClickListener(this);
        updateUI();
        FragmentManagement(LANDINGPAGESTEP1, REPLACE,
                null, true, TAG_LANDINGPAGESTEP1);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    long timeinterval = 0;

    protected void startLocationUpdates() {
        if (ALLPERMISSIONGRANTERd) {


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

            if (locationChangeListner==null)
            {
                locationChangeListner = new LocationChangeListner();
            }

            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, /*new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            Log.d(TAG, "Firing onLocationChanged..............................................");
                            mCurrentLocation = location;
                            if (timeinterval == 0) {
                                updateUI();
                                timeinterval = (int) (System.currentTimeMillis() / 1000) % 60;
                                //   CallAPIFetchDataLocation();
                            }
                            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                            Log.d(TAG, "Location update started ..............: " + timeinterval);
                        }
                    }*/locationChangeListner);
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
            Log.d(TAG, "UI update initiated ............." + mCurrentLocation.getLatitude() + "  ---- " + mCurrentLocation.getLongitude());

            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());


            stupMap(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), "");
//            Address.setText("At Time: " + mLastUpdateTime + "\n" +
//                    "Latitude: " + lat + "\n" +
//                    "Longitude: " + lng + "\n" +
//                    "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" +
//                    "Provider: " + mCurrentLocation.getProvider());
            progressdialog.dismiss();
            ALLPERMISSIONGRANTERd = false;


        } else {
            ALLPERMISSIONGRANTERd = true;
            Log.d(TAG, "location is null ...............");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();

    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected())

            if (locationChangeListner != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient,locationChangeListner /*new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {

                        }
                    }*/);
            }
        Log.d(TAG, "Location update stopped .......................");
    }

    @Override
    public void onResume() {
        super.onResume();
        //  CheckEnableGPS();
        if (mGoogleApiClient.isConnected()) {
            ALLPERMISSIONGRANTERd = true;
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");

        }

        isLandingPageActivityOpen = true;  //added on 1-4-201
        CallAPIFetchDataLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
        isLandingPageActivityOpen = false;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    double lat = 0.000100;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }

    private void stupMap(final double lat, final double lng, final String name) {

        if (mGoogleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
//            mGoogleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
//                    .getMap();
            mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
            // Check if we were successful in obtaining the map.


        }
        if (mGoogleMap != null) {
            try {
                LatLng latLong;
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.setTrafficEnabled(true);
                latLong = new LatLng(lat, lng);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLong).zoom(15f).tilt(70).build();
                mGoogleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
                if (CarLocationsDataList.size() == 0)
                    mGoogleMap.clear();
                mGoogleMap.setOnCameraChangeListener(new OnCameraChangeListener() {

                    @Override
                    public void onCameraChange(CameraPosition arg0) {
                        // TODO Auto-generated method stub
                        if (isenable) {
                            center = mGoogleMap.getCameraPosition().target;
                            markerText.setText(" Set your Location ");
                            markerLayout.setVisibility(View.VISIBLE);
                            if (name.length() == 0) {
                                try {
                                    new GetLocationAsync(center.latitude, center.longitude)
                                            .execute();

                                } catch (Exception e) {
                                    PrintMessage("Exception " + e.getMessage());
                                }
                            } else {
                                try {
                                    SourceData.latitude = lat;
                                    SourceData.longitude = lng;
                                    Address.setText(name + " ");
                                    SourceData.Placename = name;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });

                markerLayout.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        try {

                            LatLng latLng1 = new LatLng(center.latitude,
                                    center.longitude);

                            Marker m = mGoogleMap.addMarker(new MarkerOptions()
                                    .position(latLng1)
                                    .title(" Set your Location ")
                                    .snippet("")
                                    .icon(BitmapDescriptorFactory
                                            .fromResource(R.drawable.add_marker)));
                            m.setDraggable(true);

                            markerLayout.setVisibility(View.GONE);
                        } catch (Exception e) {
                        }

                    }
                });
                CallAPIFetchDataLocation();
            } catch (Exception e) {
                PrintMessage("Exception 2 " + e.getMessage());
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.adressText:
                Intent sourceIntent1=new Intent(this, EnterSourceActivity.class);
                sourceIntent1.putExtra("SOURCE",Address.getText().toString()+"");
                startActivityForResult(sourceIntent1,REQUEST_CODE_AUTOCOMPLETEFORSOURCE);
                break;
            case R.id.card_view_destinatio:
                Intent intent=new Intent(this, EnterDestinationActivity.class);
                intent.putExtra("DESTINATION",textView1_destination.getText().toString()+"");
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETEFORDESTINATION);
                break;
            case R.id.card_view_source:
                Intent sourceIntent=new Intent(this, EnterSourceActivity.class);
                sourceIntent.putExtra("SOURCE",Address.getText().toString()+"");
                startActivityForResult(sourceIntent,REQUEST_CODE_AUTOCOMPLETEFORSOURCE);
//                openAutocompleteActivity(REQUEST_CODE_AUTOCOMPLETEFORSOURCE,"SOURCE");
                break;
            case R.id.iv_menu:
//                Log.d("CLICK123 ", "CLICK");
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                drawer.closeDrawer(GravityCompat.START);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {

                    drawer.openDrawer(drawer);
                }

                break;
        }
        onNavigationItemSelected(view.getId());
    }


    private class GetLocationAsync extends AsyncTask<String, Void, String> {

        // boolean duplicateResponse;
        double x, y;
        StringBuilder str;

        public GetLocationAsync(double latitude, double longitude) {
            // TODO Auto-generated constructor stub
            x = latitude;
            y = longitude;
        }

        @Override
        protected void onPreExecute() {
            Methods.showProgressDialog(LandingPageActivity.this,"Please wait..");
            Address.setHint(" Getting location ");
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                geocoder = new Geocoder(LandingPageActivity.this, Locale.ENGLISH);
                addresses = geocoder.getFromLocation(x, y, 1);
                str = new StringBuilder();
                try {
                    if (geocoder.isPresent() && addresses.size() > 0) {
                        Address returnAddress = addresses.get(0);
                        String localityString = returnAddress.getLocality();
                        String city = returnAddress.getCountryName();
                        String region_code = returnAddress.getCountryCode();
                        String zipcode = returnAddress.getPostalCode();
                        str.append(localityString + "");
                        str.append(city + "" + region_code + "");
                        str.append(zipcode + "");
                    } else {
                    }
                } catch (IndexOutOfBoundsException e) {
                    Log.e("tag", e.getMessage());
                }
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            return str + "";
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.print("result " + result);
            Methods.closeProgressDialog();
            try {
                SourceData.latitude = mCurrentLocation.getLatitude();
                SourceData.longitude = mCurrentLocation.getLongitude();
                Address.setText(addresses.get(0).getAddressLine(0)
                        + addresses.get(0).getAddressLine(1) + " ");
                SourceData.Placename = addresses.get(0).getAddressLine(0)
                        + addresses.get(0).getAddressLine(1) + " ";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }


    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (fragmentmanager != null
                    && fragmentmanager.getBackStackEntryCount() > 1) {
                super.onBackPressed();
            } else {
                finish();
            }
        }

    }

    Button nav_payment, nav_history, nav_freeride, nav_promotion, nav_notification, nav_help, nav_setting;

    public boolean onNavigationItemSelected(int id) {
        // Handle navigation view item clicks here.


        if (id == R.id.nav_payment) {
            Intent intent = new Intent(LandingPageActivity.this, PaymentAlreadyAddedActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_history) {
            Intent intent = new Intent(LandingPageActivity.this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_freeride) {
            Intent intent = new Intent(LandingPageActivity.this, FreeridesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_promotion) {
            Intent intent = new Intent(LandingPageActivity.this, PromotionActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_notification) {
            Intent intent = new Intent(LandingPageActivity.this, NotificationsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(LandingPageActivity.this, HelpActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(LandingPageActivity.this, SettingActivity.class);
            startActivity(intent);
        }else if(id == R.id.nav_about)
        {
            Intent intent = new Intent(LandingPageActivity.this, AboutUs.class);
            startActivity(intent);
        }


        drawer.closeDrawer(Gravity.LEFT);
        return true;
    }


    public void UpdateMarker(Marker marker, double oldlat, double oldlong, double newlat, double newlong) {
        Log.d("marker data", marker + "  --  " + oldlat + "  --  " + oldlong + "  --  " + newlat + "  --  " + newlong);
        Location prevLoc = new Location("service Provider");
        prevLoc.setLatitude(oldlat);
        prevLoc.setLongitude(oldlong);
        Location newLoc = new Location("service Provider");
        newLoc.setLatitude(newlat);
        newLoc.setLongitude(newlong);
        float bearing = prevLoc.bearingTo(newLoc);
        marker.setPosition(new LatLng(newlat, newlong));
              marker.setRotation(bearing);
        marker.setFlat(true);
        oldlong = newlong;

    }

    // ArrayList<CarLocationsData> AllCabsList = new ArrayList<>();
    ArrayList<CarLocationsData> CarLocationsDataList = new ArrayList<>();
    // double[] addtempdata = {0.0, 0.021, 0.0713, 0.064, 0.099, 0.05677, 0.012, 0.02, 0.03, 0.83};

    public void AddMarker() {
//        AllCabsList.addAll(CarLocationsDataList);


        mList = new ArrayList<>();

        Log.i("markerrrrrrrrrrrrr", "aaaaaaa" + CarLocationsDataList.size());
        for (int i = 0; i < CarLocationsDataList.size(); i++) {
            Marker m;
            if (CarLocationsDataList.get(i).old_latitude == 0) {
                m = mGoogleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(CarLocationsDataList.get(i).latitude, CarLocationsDataList.get(i).longitude))
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.my_car)));
                Log.i("markerrrrrrrrrrrrr", "aaaaaaa" + i + "------------" + CarLocationsDataList.get(i).driverId);
                CarLocationsData Data = new CarLocationsData();
                Data.driverId = CarLocationsDataList.get(i).driverId;
                Data.CountryName = CarLocationsDataList.get(i).CountryName;
                Data.latitude = CarLocationsDataList.get(i).latitude;
                Data.longitude = CarLocationsDataList.get(i).longitude;
                Data.old_latitude = CarLocationsDataList.get(i).old_latitude;
                Data.old_longitude = CarLocationsDataList.get(i).old_longitude;
                Data.Type = CarLocationsDataList.get(i).Type;
                Data.distance = CarLocationsDataList.get(i).distance;
                Data.marker = m;
                Data.isnew = CarLocationsDataList.get(i).isnew;
                CarLocationsDataList.get(i).marker = m;
                Log.d("logsmarker", CarLocationsDataList.get(i).marker + "   " + Data.marker);
                CarLocationsDataList.set(i, Data);
                Log.d("logsmarker", CarLocationsDataList.get(i).marker + "   " + Data.marker);
                mList.add(m);
                mListAll.add(CarLocationsDataList.get(i).marker);
            } else {
                Log.d("logsmarker", CarLocationsDataList.get(i).marker + "");
                UpdateMarker(CarLocationsDataList.get(i).marker, CarLocationsDataList.get(i).old_latitude, CarLocationsDataList.get(i).old_longitude, CarLocationsDataList.get(i).latitude, CarLocationsDataList.get(i).longitude);
                mList.add(CarLocationsDataList.get(i).marker);
                mListAll.add(CarLocationsDataList.get(i).marker);
            }
        }

        for(int j=0;j<mListAll.size();j++){
           if(!mList.contains(mListAll.get(j))){
               mListAll.get(j).remove();
           }
        }
        mListAll.retainAll(mList);
    }

    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentmanager;

    public void FragmentManagement(int fragmentno, int Add_Replace,
                                   Bundle arguments, boolean addtobackstack, String FragmentTag) {
        if ((fragmentmanager == null || fragmentmanager
                .findFragmentByTag(FragmentTag) == null)) {
            /*
             * initialize fragments
			 */
            fragmentmanager = getSupportFragmentManager();
            if (fragmentno == LANDINGPAGESTEP1) {
                fragmentmanager.popBackStack(null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);

                LandingStepRide1 LandingStepRide1 = new LandingStepRide1();
                fragment = LandingStepRide1;
            } else if (fragmentno == LANDINGPAGESTEP2) {
                fragmentmanager.popBackStack(null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
                LandingStepRide2 LandingStepRide2 = new LandingStepRide2();
                fragment = LandingStepRide2;
            }


            if (arguments != null)
                fragment.setArguments(arguments);

            fragmentTransaction = fragmentmanager.beginTransaction();
            // fragmentTransaction.setCustomAnimations(R.anim.fadein,
            // R.anim.fade_out);

			/*
             * Add Or Replace Fragments
			 */

            if (Add_Replace == REPLACE)
                fragmentTransaction.replace(R.id.fragmentcontainer, fragment,
                        FragmentTag);
            else
                fragmentTransaction.add(R.id.fragmentcontainer, fragment,
                        FragmentTag);

			/*
             * If we want to maintain hirarchy
			 */

            if (addtobackstack) {
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();

        } else {
        }
    }

    CardView card_view_destinatio, card_view_source;
    boolean isenable;

    public void DisableMapFunctionality(boolean isenable) {
        this.isenable = isenable;
        textView1_destination.setHint("Enter your destination");
        if (!isenable) {
            card_view_destinatio.setVisibility(View.VISIBLE);
            card_view_source.setClickable(false);

        } else {
            card_view_destinatio.setVisibility(View.GONE);
            card_view_source.setClickable(true);
            CallAPIFetchDataLocation();
        }
        //Toast.makeText(LandingPageActivity.this," "+isenable,Toast.LENGTH_SHORT).show();
        // mGoogleMap.getUiSettings().setZoomControlsEnabled(isenable);
    }

    private void openAutocompleteActivity(int requestcode,String type) {
        try {
            int PLACE_PICKER_REQUEST = 1;
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS).build();
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(typeFilter)
                    .build(this);

            startActivityForResult(intent, requestcode);
        } catch (GooglePlayServicesRepairableException e) {

            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            loadSavedLines();
        }
    }
    public void loadSavedLines() {

        SharedPreferences settings = getSharedPreferences("data", 0);
        int size = settings.getInt("size", 0);

        if (size != 0) {
            String[] history = new String[size];

            for (int i = 0; i < 5; i++) {
                history[i] = settings.getString(String.valueOf(i + 1), "empty");
                Log.v("HISTORY","HISTORY:::"+history[i]);
            }

            AutoCompleteTextView autoComplete = (AutoCompleteTextView) findViewById(R.id.place_autocomplete_prediction_primary_text);
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.content_landing_page2, history);
            autoComplete.setAdapter(adapter);
            autoComplete.setText(size);
        }
    }
    public void checkThanSaveLine(String new_line, AutoCompleteTextView autoComplete) {
        SharedPreferences settings = getSharedPreferences("data", 0);
        int size = settings.getInt("size", 0);
        String[] history = new String[size+1];
        boolean repeat = false;
        for (int i = 0; i < size; i++) {
            history[i] = settings.getString(String.valueOf(i+1),"empty");
            if (history[i].equals(new_line.toString())) {
                repeat = true;
            }
        }
        if (repeat == false) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(String.valueOf(size+1), new_line.toString() );
            editor.putInt("size", size+1);
            editor.commit();
            history[size] = new_line.toString();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(LandingPageActivity.this,R.layout.content_landing_page2, history);
            autoComplete.setAdapter(adapter);
        }
    }

    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    private static final int REQUEST_CODE_AUTOCOMPLETEFORSOURCE = 1;
    public static final int REQUEST_CODE_AUTOCOMPLETEFORDESTINATION = 2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETEFORDESTINATION) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Bundle bundle=data.getExtras();
                ArrayList<DestinationPlaces> places=(ArrayList)bundle.getSerializable("LIST");
                //Methods.toastShort("NAME:::" + places.get(0).getName(), this);
                String Placedetails = places.get(0).getName()+""+places.get(0).getAddress() + "";
                textView1_destination.setText(Placedetails);
                Log.i(TAG, "Place Selected: Placedetails " + Placedetails);
                DestinationData.Placename = Placedetails;
                DestinationData.latitude = places.get(0).getLatitude();
                DestinationData.longitude = places.get(0).getLongitude();
                isenable = false;

            }
        } else if (requestCode == REQUEST_CODE_AUTOCOMPLETEFORSOURCE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle=data.getExtras();
                ArrayList<DestinationPlaces> places=(ArrayList)bundle.getSerializable("LIST");
                //Methods.toastShort("NAME:::" + places.get(0).getName(), this);
                String Placedetails = places.get(0).getAddress() + "";
                Address.setText(places.get(0).getName()+""+places.get(0).getAddress());
                LatLng latLng=new LatLng(places.get(0).getLatitude(),places.get(0).getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng).zoom(15f).tilt(70).build();
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
                SourceData.Placename = places.get(0).getName();
                SourceData.latitude = places.get(0).getLatitude();
                SourceData.longitude = places.get(0).getLongitude();
                stupMap(places.get(0).getLatitude(), places.get(0).getLongitude(), Placedetails);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
    }

    /**
     * Helper method to format information about a place nicely.
     */
    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {

        return Html.fromHtml(res.getString(R.string.locationdetect, name, id, address, phoneNumber,
                websiteUri));

    }

    public void CallAPIFetchDataLocation() {
        if (!isLandingPageActivityOpen)         //added by samson 1-4-2016
            return;
        JSONObject params = new JSONObject();
        try {

            JSONObject jsonObject = new JSONObject();
            params = getCommontHeaderParams();
            jsonObject.put("customerId", Session.getUserID(LandingPageActivity.this));
            jsonObject.put("lat", SourceData.latitude);
            jsonObject.put("lng", SourceData.longitude);
            jsonObject.put("place", "");
            jsonObject.put("area", "");
            jsonObject.put("serviceType", GetCarData.get(SelectedTab).attrName);
            jsonObject.put("vehicleType", Session.getVehicleType(LandingPageActivity.this));
            params.put("body", jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Util.isNetworkConnected(LandingPageActivity.this)) {
            try {

                new CallAPI(GETDATAFORCARS, "GETDATAFORCARS", params, LandingPageActivity.this, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private JSONObject jsonObject1;
    private int count=0;
    private String duration;

    public String getDuration(){
        return this.duration;
    }
    Handler GetDetails_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {

                    try {
                        ArrayList<CarLocationsData> currentCarLocationsDataList = new ArrayList<>();
                        JSONArray jsonarray = new JSONArray(msg.getData().getString("responce"));
                        Session.setCabCount(LandingPageActivity.this, String.valueOf(jsonarray !=null ? jsonarray.length():"0"));
                       // if (jsonarray.length() > 0) {
                            Log.d("jsonarray ", jsonarray.length() + "");
                            if(jsonarray.length() == 0)
                            {
                                //Session.setTime(LandingPageActivity.this,"0");
                                Log.i("fragment","fragmentttttttt"+(fragment instanceof LandingStepRide1));
                                if (fragment instanceof LandingStepRide1){
                                 //   getGoogleData(jsonObject1.getString("lat"), jsonObject1.getString("lng"));
                                    LandingStepRide1 landingStepRide1 = (LandingStepRide1) fragment;
                                    landingStepRide1.setTVTimeDurationText("No Cabs");
                                    landingStepRide1.setBookNow(false);
                                }
                            }

                            for (int i = 0; i < jsonarray.length(); i++) {
                                jsonObject1 = jsonarray.getJSONObject(i);
                                CarLocationsData Data = new CarLocationsData();
                                Data.driverId = jsonObject1.getInt("driverId");
                                Data.distance = jsonObject1.getInt("distance");

                                if(i ==0) {

                                    if (fragment instanceof LandingStepRide1){
                                        getGoogleData(jsonObject1.getString("lat"), jsonObject1.getString("lng"));
                                        LandingStepRide1 landingStepRide1 = (LandingStepRide1) fragment;
                                        landingStepRide1.setTVTimeDurationText(duration);
                                        landingStepRide1.setBookNow(true);
                                    }
                                }

                                Log.i("sessionn","Sessionsss"+Session.getTime(LandingPageActivity.this));
                                Data.CountryName = "";
                                Data.latitude = jsonObject1.getDouble("lat");
                                Data.longitude = jsonObject1.getDouble("lng");
                                Data.Type = 0;
                                boolean isaadded = false;
                                count = 1;
                                isaddede:
                                for (int j = 0; j < CarLocationsDataList.size(); j++) {
                                    if (!CarLocationsDataList.get(j).isnew && CarLocationsDataList.get(j).driverId == Data.driverId) {
                                        isaadded = true;
                                        break isaddede;
                                    }
                                                                    }
                                if (isaadded) {
                                    //lat += 0.000100;
                                    Data.driverId = CarLocationsDataList.get(i).driverId;
                                    Data.CountryName = CarLocationsDataList.get(i).CountryName;
                                    Data.latitude = jsonObject1.getDouble("lat");
                                    Data.longitude = jsonObject1.getDouble("lng");
//                                Data.latitude = SourceData.latitude + lat;
//                                Data.longitude = SourceData.longitude;
                                    Data.Type = CarLocationsDataList.get(i).Type;
                                    Data.distance = CarLocationsDataList.get(i).distance;
                                    Data.marker = CarLocationsDataList.get(i).marker;
                                    Data.isnew = CarLocationsDataList.get(i).isnew;
                                    Data.old_latitude = CarLocationsDataList.get(i).latitude;
                                    Data.old_longitude = CarLocationsDataList.get(i).longitude;
                                    CarLocationsDataList.set(i, Data);
                                } else {
                                    Data.isnew = false;
                                    CarLocationsDataList.add(Data);
                                }
                                currentCarLocationsDataList.add(Data);
                            }
                        Log.i("currentCarLocations","="+currentCarLocationsDataList.size());
                        Log.i("CarLocationsDataList", "====" + CarLocationsDataList.size());
                        CarLocationsDataList.retainAll(currentCarLocationsDataList);
                        Log.i("currentCarLocations", "=" + currentCarLocationsDataList.size());
                        Log.i("CarLocationsDataList", "====" + CarLocationsDataList.size());
                        AddMarker();

                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPIFetchDataLocation();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(LandingPageActivity.this)) {
                        CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {

                    }
                } else {

                }
            } else {

            }
            if (isenable)
            {
                final Handler handler = new Handler();      //delay added by Samson 1-4-2016
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        CallAPIFetchDataLocation();
                    }
                }, 5000);
            }

        }
    };

    public void getGoogleData(String lat, String lng ){

            ArrayList args = new ArrayList();
            String url = "http://maps.google.com/maps/api/directions/json?origin="
                    + SourceData.latitude + "," + SourceData.longitude + "&destination=" + lat
                    + "," + lng + "&sensor=false&units=metric";
            Log.i("====================","aaaaaaaaaaaaaa"+url);
            HttpResponse responseText = null;
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost(url);
                responseText = httpClient.execute(httpPost, localContext);
                InputStream is = responseText.getEntity().getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;
                while( (line = br.readLine()) != null){
                    sb.append(line);
                }
                JSONObject jsonObject = new JSONObject(sb.toString());
                // routesArray contains ALL routes
                JSONArray routesArray = jsonObject.getJSONArray("routes");
                // Grab the first route
                JSONObject route = routesArray.getJSONObject(0);
                // Take all legs from the route
                JSONArray legs = route.getJSONArray("legs");
                // Grab first leg
                JSONObject leg = legs.getJSONObject(0);

                JSONObject durationObject = leg.getJSONObject("duration");
                duration = durationObject.getString("text");
                // parentactivity.setTVTimeDurationText();

            }
            catch (Exception e) {
                e.printStackTrace();
            }

            Session.setTime(LandingPageActivity.this,duration);


    }
    public class LocationChangeListner implements LocationListener { //new class added by Samson 1-4-2016

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "Firing onLocationChanged..............................................");
            mCurrentLocation = location;
            if (timeinterval == 0) {
                updateUI();
                timeinterval = (int) (System.currentTimeMillis() / 1000) % 60;
                //   CallAPIFetchDataLocation();
            }
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            Log.d(TAG, "Location update started ..............: " + timeinterval);

        }
    }
//    mGoogleMap.getUiSettings().setScrollGesturesEnabled(false);
}