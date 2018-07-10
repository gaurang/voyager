package com.osi.uconectdriver;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.vision.barcode.Barcode;
import com.osi.uconectdriver.AsyncTask.CallAPI;
import com.osi.uconectdriver.Util.Session;
import com.osi.uconectdriver.Util.Util;
import com.osi.uconectdriver.dialogs.ProgressDialogView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by Brij on 02-03-2016.
 */
public class trackride extends ParentActivity implements View.OnClickListener {
    private Button cancelride;
    private Button start;
    private Button cancel;
    private Button contact;
    private Button confirm;
    private Button confirm1;
    private Button help,navigate;
    private TextView header, detail, textView6, textView7;
    private ImageView ic_back;
    private EditText pin;
    private String bookingId;
    private GoogleMap map;
    private double longitude;
    private double latitude;
    double prelatitude;
    double prelongitude;
    private String result_in_kms;
    private double lat;
    private double lon;
    private MapFragment fm;
    private LocationManager locationManager;
    private Criteria criteria;
    private String provider;
    private LatLng latLng;
    double distance = 0.0;
    Timer timer = new Timer();
    double a = 0.0;
    private double distance1 = 0.0;
    private double result = 0.0;
    PolylineOptions polylineOptions;
    ArrayList<LatLng> markerPoints;
    private String mobile;
    private JSONObject jsonObject;
    private String name;
    Barcode.GeoPoint point1, point2;
    LocationManager locManager;
    Drawable drawable;
    Document document;
    GMapV2GetRouteDirection v2GetRouteDirection;
    LatLng fromPosition;
    LatLng toPosition;
    GoogleMap mGoogleMap;
    MarkerOptions markerOptions;
    Location location ;
    private Double sourceLatitude,sourceLongitude;
    private JSONObject jsonObject1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trackride);
        cancelride = (Button) findViewById(R.id.cancelride);
        start = (Button) findViewById(R.id.start);
        contact = (Button) findViewById(R.id.contact);
        navigate = (Button) findViewById(R.id.navigate);
        help = (Button) findViewById(R.id.help);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        ic_back.setOnClickListener(this);
        header = (TextView) findViewById(R.id.headername);
        detail = (TextView) findViewById(R.id.detail);
        textView6 = (TextView) findViewById(R.id.textView6);
        textView7 = (TextView) findViewById(R.id.textView7);
        header.setText("TRACK RIDE");
        cancelride.setOnClickListener(this);
        contact.setOnClickListener(this);
        help.setOnClickListener(this);
        navigate.setOnClickListener(this);
        start.setOnClickListener(this);

        try {
            jsonObject = new JSONObject(getIntent().getExtras().getString("allInfo"));
            jsonObject1 = new JSONObject(getIntent().getExtras().getString("allInfo1"));
            detail.setText(jsonObject.getString("fname") + " " + jsonObject.getString("lname"));
            name = jsonObject.getString("fname") + jsonObject.getString("lname");
            sourceLatitude= Double.valueOf(jsonObject1.getString("sourceLatitude"));
            sourceLongitude= Double.valueOf(jsonObject1.getString("sourceLongitude"));
            textView6.setText(jsonObject1.getString("destPlace"));
            Log.d("Brij","Brij"+jsonObject);
            Log.d("AAAAAAAAAaaaaaaaaaaaa","AAAAAAa"+jsonObject1);
            mobile = jsonObject.getString("mobile");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        v2GetRouteDirection = new GMapV2GetRouteDirection();
        mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(
                R.id.map)).getMap();

        // Enabling MyLocation in Google Map
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
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
        mGoogleMap.setTrafficEnabled(true);
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(20));
        markerOptions = new MarkerOptions();
        latitude= Double.parseDouble(Session.getLatitude(trackride.this));
        longitude= Double.parseDouble(Session.getLongitude(trackride.this));
        LatLng latLng = new LatLng(latitude, longitude);

        // Showing the current location in Google Map
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        fromPosition = new LatLng(latitude, longitude);
        toPosition = new LatLng(sourceLatitude, sourceLongitude);
        GetRouteTask getRoute = new GetRouteTask();
        getRoute.execute();
    }
    private class GetRouteTask extends AsyncTask<String, Void, String> {

        private ProgressDialog Dialog;
        String response = "";
        @Override
        protected void onPreExecute() {
            Dialog = new ProgressDialog(trackride.this);
            Dialog.setMessage("Loading route...");
            Dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            //Get All Route values
            document = v2GetRouteDirection.getDocument(fromPosition, toPosition, GMapV2GetRouteDirection.MODE_DRIVING);
            response = "Success";
            return response;

        }

        @Override
        protected void onPostExecute(String result) {
            mGoogleMap.clear();
            if(response.equalsIgnoreCase("Success")){
                ArrayList<LatLng> directionPoint = v2GetRouteDirection.getDirection(document);
                PolylineOptions rectLine = new PolylineOptions().width(10).color(
                        Color.BLUE);

                for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
                }
                // Adding route on the map
                mGoogleMap.addPolyline(rectLine);
                markerOptions.position(toPosition);
                markerOptions.position(fromPosition);
                markerOptions.draggable(true);
                mGoogleMap.addMarker(markerOptions);

            }

            Dialog.dismiss();
        }
    }



    @Override
    public void onClick(View v) {
        if (v == cancelride) {
            initiatePopupWindow();
        } else if (v == start) {
            initiatePopupWindow1();
        } else if (v == contact) {
            Intent startActivity = new Intent(trackride.this, contact.class);
            startActivity.putExtra("mobile", mobile);
            startActivity.putExtra("name",name);
            startActivity(startActivity);
        }else if (v == help) {
            Intent startActivity = new Intent(com.osi.uconectdriver.trackride.this,HelpActivity.class);
            startActivity(startActivity);
        } else if (v == confirm) {
            Intent startActivity = new Intent(trackride.this, LandingPageActivity.class);
            startActivity(startActivity);
        } else if (v == confirm1) {
           getRideStart();
//            Intent startActivity = new Intent(com.osi.uconectdriver.trackride.this,trackride1.class);
//            startActivity(startActivity);
        }else if(v == ic_back){
            finish();
        }else if(v == navigate){
//            final Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://maps.google.com/maps?" + "saddr="+ latitude + "," + longitude + "&daddr=" + 11.723512 + "," + 78.466287));
//            intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
//            startActivity(intent);
            Uri gmmIntentUri = Uri.parse("google.navigation:q=Taronga+Zoo,+Sydney+Australia");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);

        }
    }


    private void getRideStart() {
        if (pin.getText().toString().trim().length() == 0) {
            Util.ShowToast(trackride.this, "Please enter code");
        } else {
            try {
                JSONObject jsonObject_main = new JSONObject();
                JSONObject jsonObject = new JSONObject();
                jsonObject_main = getCommontHeaderParams();
                jsonObject.put("driverId", Session.getUserID(trackride.this));
                jsonObject.put("bookingId", Session.getBookingId(trackride.this));
                jsonObject.put("pin", pin.getText().toString().trim());
                jsonObject_main.put("body", jsonObject);
                CallAPI1(jsonObject_main);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void CallAPI1(JSONObject params) {
        if (Util.isNetworkConnected(trackride.this)) {
            try {
                new CallAPI(GETSTARTRIDE, "GETDRIVERDETAILS", params, trackride.this, GetStartRide_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(trackride.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetStartRide_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    JSONObject jsonObject2= null;
                    try {
                        jsonObject2 = new JSONObject(msg.getData().getString("responce"));
                        Intent intent = new Intent(trackride.this, trackride1.class);
                        intent.putExtra("mobile", mobile);
                        intent.putExtra("name", name);
                        intent.putExtra("rideId",jsonObject2.getString("rideId"));
                        intent.putExtra("allInfo", String.valueOf(new JSONObject(getIntent().getExtras().getString("allInfo"))));
                        intent.putExtra("allInfo1", String.valueOf(new JSONObject(getIntent().getExtras().getString("allInfo1"))));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // Session.setAllInfo(trackride.this,msg.getData().getString("responce"));

                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPI1(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(trackride.this)) {
                        CallSessionID(GetStartRide_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        Util.ShowToast(trackride.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    Util.ShowToast(trackride.this, msg.getData().getString("msg"));
                }
            } else {
                Util.ShowToast(trackride.this, msg.getData().getString("msg"));
            }
        }
    };
    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();  // optional depending on your needs
    }
        private PopupWindow pwindo;

        private void initiatePopupWindow() {
            try {
// We need to get the instance of the LayoutInflater
                LayoutInflater inflater = (LayoutInflater) trackride.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.cancel_popup, (ViewGroup) findViewById(R.id.popup_element));
                pwindo = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
                //pwindo.update(0, 0, 1050, 600);
                cancel = (Button) layout.findViewById(R.id.cancel);
                confirm = (Button) layout.findViewById(R.id.confirm);
                confirm.setOnClickListener(this);
                cancel.setOnClickListener(cancel_button_click_listener);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
            public void onClick(View v) {
                pwindo.dismiss();
            }
        };


        private void initiatePopupWindow1() {
            try {
// We need to get the instance of the LayoutInflater
                LayoutInflater inflater = (LayoutInflater) trackride.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.popup_start, (ViewGroup) findViewById(R.id.popup_element1));
                pwindo = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
           //     pwindo.update(0, 0, 1050, 500);
                cancel = (Button) layout.findViewById(R.id.cancel);
                confirm1 = (Button) layout.findViewById(R.id.confirm1);
                pin=(EditText) layout.findViewById(R.id.pin);
                confirm1.setOnClickListener(this);
                cancel.setOnClickListener(cancel_button_click_listener);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

};

