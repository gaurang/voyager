package com.osi.uconectdriver;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.osi.uconectdriver.AsyncTask.CallAPI;
import com.osi.uconectdriver.Util.Session;
import com.osi.uconectdriver.Util.Util;
import com.osi.uconectdriver.dialogs.ProgressDialogView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Brij on 02-03-2016.
 */
public class trackride1 extends ParentActivity implements View.OnClickListener {
    private Button cancelride;
    private Button stop;
    private Button cancel;
    private Button contact;
    private Button confirm;
    private Button confirm1;
    private ImageView ic_back;
    private TextView header;
    private ImageButton emergency;
    private LinearLayout layout;
    private Button tollsubmit;
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
    double distance=0.0;
    Timer timer=new Timer();
    double a=0.0;
    private double distance1=0.0;
    private double result=0.0;
    private String name;
    private String mobile;
    private String rideId;
    private double difference;
    private double lStartTime;
    private String c;
    private long stringdouble;
    private EditText toll;
    private long time;
    private SQLiteDatabase db;
    private EditText extra;
    GMapV2GetRouteDirection v2GetRouteDirection;
    LatLng fromPosition;
    LatLng toPosition;
    GoogleMap mGoogleMap;
    MarkerOptions markerOptions;
    Location location ;
    private Double sourceLatitude,sourceLongitude;
    private JSONObject jsonObject1,jsonObject;
    private double longitude;
    private double latitude;
    Document document;
    private Button navigate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.trackride1);
        lStartTime = System.currentTimeMillis();
        cancelride=(Button) findViewById(R.id.cancelride);
        navigate = (Button) findViewById(R.id.navigate);
        stop=(Button) findViewById(R.id.stop);
        contact=(Button) findViewById(R.id.contact);
        cancelride.setOnClickListener(this);
        header=(TextView) findViewById(R.id.headername);
        header.setText("TRACK RIDE");
        ic_back=(ImageView)findViewById(R.id.ic_back);
        ic_back.setOnClickListener(this);
        emergency=(ImageButton) findViewById(R.id.imageButton);
        layout=(LinearLayout) findViewById(R.id.onlinemeter1);
        layout.setOnClickListener(this);
        emergency.setOnClickListener(this);
        contact.setOnClickListener(this);
        stop.setOnClickListener(this);
        //createDatabase();
        name= (getIntent().getExtras().getString("name"));
        mobile=getIntent().getExtras().getString("mobile");
        rideId=getIntent().getExtras().getString("rideId");
        prelatitude = Double.parseDouble(Session.getLatitude(trackride1.this));
        prelongitude = Double.parseDouble(Session.getLongitude(trackride1.this));
        Log.i("-----------------","aaaaaaaaaaaaa"+prelatitude);
        try {
            jsonObject = new JSONObject(getIntent().getExtras().getString("allInfo"));
            jsonObject1 = new JSONObject(getIntent().getExtras().getString("allInfo1"));
            name = jsonObject.getString("fname") + jsonObject.getString("lname");
            sourceLatitude= Double.valueOf(jsonObject1.getString("destLatitude"));
            sourceLongitude= Double.valueOf(jsonObject1.getString("destLongitude"));
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
        latitude= Double.parseDouble(Session.getLatitude(trackride1.this));
        longitude= Double.parseDouble(Session.getLongitude(trackride1.this));
        LatLng latLng = new LatLng(latitude, longitude);

        // Showing the current location in Google Map
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        fromPosition = new LatLng(latitude, longitude);
        toPosition = new LatLng(sourceLatitude, sourceLongitude);
        GetRouteTask getRoute = new GetRouteTask();
        getRoute.execute();


        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                String c = Session.getLatitude(trackride1.this);
                Log.i("-----------------", "cccccccccccc" + c);
                Log.i("-----------------", "++++++++++++++++++" + prelatitude);
                String d = Session.getLongitude(trackride1.this);
                Log.i("-----------------", "ddddddddddddddd" + d);
                lat = prelatitude;
                lon = prelongitude;
                prelatitude = Double.parseDouble(c);
                prelongitude = Double.parseDouble(d);

                double latA = Math.toRadians(lat);
                double lonA = Math.toRadians(lon);
                double latB = Math.toRadians(prelatitude);
                double lonB = Math.toRadians(prelongitude);
                double cosAng = (Math.cos(latA) * Math.cos(latB) * Math.cos(lonB - lonA)) +
                        (Math.sin(latA) * Math.sin(latB));
                double ang = Math.acos(cosAng);
                distance = ang * 6371 + distance;

                Log.i("distance", "distance" + distance);


                double lat1 = Math.toRadians(lat);
                double lat2 = Math.toRadians(prelatitude);
                double diflat = Math.toRadians(prelatitude - lat);
                double diflon = Math.toRadians(prelongitude - lon);
                double R = 6371000;//meters
                double a = Math.sin(diflat / 2) * Math.sin(diflat / 2) + Math.cos(lat1) + Math.cos(lat2) * Math.sin(diflon / 2) * Math.sin(diflon / 2);
                double b = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                distance1 = distance1 + (R * b);
                Log.i("haversine", "haversine" + distance1);
//                float[] results = new float[1];
//                Location.distanceBetween(latA, lonA,
//                        latB, lonB,
//                        results);
//
//                a = a + Double.parseDouble(String.valueOf(results[0]));
//                Log.i("aaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaa" + a);
                getDistanceOnRoad(lat, lon, prelatitude, prelongitude);


                result = result + Double.parseDouble(result_in_kms);
                //insertIntoDB();
                Log.i("getDistance", "getDistance" + result);
            }

        }, 0, 10000);

    }
    private class GetRouteTask extends AsyncTask<String, Void, String> {

        private ProgressDialog Dialog;
        String response = "";
        @Override
        protected void onPreExecute() {
            Dialog = new ProgressDialog(trackride1.this);
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

    private String getDistanceOnRoad(double latitude, double longitude,
                                     double prelatitute, double prelongitude) {

        String url = "http://maps.google.com/maps/api/directions/xml?origin="
                + latitude + "," + longitude + "&destination=" + prelatitute
                + "," + prelongitude + "&sensor=false&units=metric";
        Log.i("====================","aaaaaaaaaaaaaa"+url);
        String tag[] = { "text" };
        HttpResponse response = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            response = httpClient.execute(httpPost, localContext);
            InputStream is = response.getEntity().getContent();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document doc = builder.parse(is);
            if (doc != null) {
                NodeList nl;
                ArrayList args = new ArrayList();
                for (String s : tag) {
                    nl = doc.getElementsByTagName(s);
                    if (nl.getLength() > 0) {
                        Node node = nl.item(nl.getLength() - 1);
                        args.add(node.getTextContent().substring(0, node.getTextContent().indexOf(" ")));
                        Log.i("====================", "------------" + node.getTextContent().substring(0, node.getTextContent().indexOf(" ")));
                    } else {
                        args.add(" - ");
                    }
                }
                Log.i("====================","aaaaaaaaaaaaaa"+args.get(0));
                result_in_kms = String.format("%s", args.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("====================","aaaaaaaaaaaaaa"+result_in_kms);
        return result_in_kms;
    }


    @Override
    public void onClick(View v) {
        if(v == cancelride)
        {
            initiatePopupWindow();
        }
        else if(v == stop)
        {
            timer.cancel();
            double lEndTime = System.currentTimeMillis();
            difference = lEndTime - lStartTime;
            c=String.format("%.6f", (difference/60000));
            initiatePopupWindow2();
        }
        else if (v == contact)
        {
            Intent startActivity = new Intent(trackride1.this,contact.class);
            startActivity.putExtra("mobile", mobile);
            startActivity.putExtra("name",name);
            startActivity(startActivity);
        }
        else if(v == emergency)
        {
            Intent startActivity = new Intent(trackride1.this,emergency.class);
            startActivity(startActivity);
        }
        else if(v == layout)
        {
            Intent startActivity = new Intent(trackride1.this,onlinemeter.class);
            startActivity(startActivity);
        }
        else if(v == ic_back)
        {
            finish();
        }
        else if(v == tollsubmit)
        {
            Util.ShowToast(trackride1.this,"Total Distance:" +result + " " +c);
            stopRide();
        }
        else if(v == navigate){
//            final Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://maps.google.com/maps?" + "saddr="+ latitude + "," + longitude + "&daddr=" + 11.723512 + "," + 78.466287));
//            intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
//            startActivity(intent);
            Uri gmmIntentUri = Uri.parse("google.navigation:q=Taronga+Zoo,+Sydney+Australia");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);

        }
    }

    private void stopRide() {
        try {
            JSONObject jsonObject_main = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject_main = getCommontHeaderParams();
            jsonObject.put("driverId", Session.getUserID(trackride1.this));
            jsonObject.put("rideId", rideId);
            jsonObject.put("kmTravelled", result);
            jsonObject.put("bookingId", Session.getBookingId(trackride1.this));
            jsonObject.put("tollCharges",toll.getText().toString().trim());
            jsonObject.put("extra",extra.getText().toString().trim());
            jsonObject.put("waitTime", (difference/1000));
            jsonObject_main.put("body", jsonObject);
            CallAPI1(jsonObject_main);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void CallAPI1(JSONObject params) {
        if (Util.isNetworkConnected(trackride1.this)) {
            try {
                new CallAPI(GETSTOPRIDE, "GETSTOPRIDE", params, trackride1.this, GetStopRide_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(trackride1.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetStopRide_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    // Session.setAllInfo(trackride.this,msg.getData().getString("responce"));
                    //Intent intent = new Intent(trackride.this, LandingPageActivity.class);
                    try {

                        JSONObject jsonObject = new JSONObject(msg.getData().getString("responce"));
                        Intent startActivity = new Intent(trackride1.this, Ratings.class);
                        startActivity.putExtra("rideTotalAmt",jsonObject.getString("rideTotalAmt"));
                        startActivity.putExtra("currency",jsonObject.getString("currency"));
                        startActivity(startActivity);
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    //startActivity(intent);
                    //    finish();
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPI1(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(trackride1.this)) {
                        CallSessionID(GetStopRide_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        Util.ShowToast(trackride1.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    Util.ShowToast(trackride1.this, msg.getData().getString("msg"));
                }
            } else {
                Util.ShowToast(trackride1.this, msg.getData().getString("msg"));
            }
        }
    };

    private PopupWindow pwindo;

    private void initiatePopupWindow() {
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) trackride1.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.cancel_popup, (ViewGroup) findViewById(R.id.popup_element));
            pwindo = new PopupWindow(layout, 300, 370, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
            pwindo.update(0, 0, 1050, 600);
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


    private PopupWindow pwindo2;

    private void initiatePopupWindow2() {
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) trackride1.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup_stop, (ViewGroup) findViewById(R.id.popup_element1));
            pwindo2 = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pwindo2.showAtLocation(layout, Gravity.CENTER, 0, 0);
            //pwindo.update(0, 0, 1050, 600);
            toll=(EditText) layout.findViewById(R.id.toll);
            tollsubmit=(Button) layout.findViewById(R.id.tollsubmit);
            extra=(EditText) layout.findViewById(R.id.extra);
            tollsubmit.setOnClickListener(this);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    protected void createDatabase(){
//        db=openOrCreateDatabase("kmTravelled", Context.MODE_PRIVATE, null);
//        db.execSQL("CREATE TABLE IF NOT EXISTS kmTravelled(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name km);");
//    }
//    protected void insertIntoDB(){
//        String query = "INSERT INTO kmTravelled (kmTravelled) VALUES('"+result+"');";
//        db.execSQL(query);
//        Toast.makeText(getApplicationContext(),"Saved Successfully", Toast.LENGTH_SHORT).show();
//    }

}
