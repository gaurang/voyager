package com.app.voyager;

/**
 * Created by E6420 on 6/15/2016.
 */


        import android.content.Context;
import android.content.Intent;
        import android.location.Location;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
        import android.widget.Toast;

        import com.app.voyager.AsyncTask.CallAPI;
        import com.app.voyager.Util.Session;
        import com.app.voyager.Util.Util;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.MapFragment;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;

        import org.json.JSONException;
import org.json.JSONObject;

        import java.util.Timer;
        import java.util.TimerTask;

/**
 * Created by Brij on 02-03-2016.
 */
public class trackride extends ParentActivity implements View.OnClickListener {
    private Button cancelride;
    private Button cancel;
    private Button contact;
    private Button confirm;
    private Button confirm1;
    private Button help;
    private TextView header,detail,textView6,textView7;
    private ImageView ic_back,emergency;
    private EditText pin;
    private JSONObject jsonObject;
    private String name;
    private String phone;
    private Double lat;
    private Double lng;
    private Marker m;
    private GoogleMap mGoogleMap;
    Timer timer=new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trackride);
        cancelride = (Button) findViewById(R.id.cancelride);
        contact = (Button) findViewById(R.id.contact);
        help = (Button) findViewById(R.id.help);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        ic_back.setOnClickListener(this);
        header = (TextView) findViewById(R.id.headername);
        textView6 = (TextView) findViewById(R.id.textView6);
        textView7 = (TextView) findViewById(R.id.textView7);
        emergency=(ImageButton) findViewById(R.id.imageButton);
        emergency.setOnClickListener(this);
        detail = (TextView) findViewById(R.id.detail);
        header.setText("TRACK RIDE");
        cancelride.setOnClickListener(this);
        contact.setOnClickListener(this);
        help.setOnClickListener(this);
        try {
            jsonObject = new JSONObject(getIntent().getExtras().getString("allInfo"));
            detail.setText(jsonObject.getString("driverName"));
            textView6.setText(jsonObject.getString("make")+" "+jsonObject.getString("model"));
            textView7.setText(jsonObject.getString("carNumber"));
            lat= Double.valueOf(jsonObject.getString("lat"));
            lng= Double.valueOf(jsonObject.getString("lng"));
            phone=jsonObject.getString("phone");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LatLng latLng = new LatLng(lat, lng);
        mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(
                R.id.map)).getMap();
        // Showing the current location in Google Map
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        m = mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.my_car)));
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                track();

            }

        }, 0, 5000);

    }

    private void track() {
        JSONObject params = new JSONObject();
        try {

            JSONObject jsonObject = new JSONObject();
            params = getCommontHeaderParams();
            jsonObject.put("customerId", Session.getUserID(trackride.this));
            jsonObject.put("bookingId",Session.getBookingId(this));
            params.put("body", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Util.isNetworkConnected(trackride.this)) {
            try {
                new CallAPI(TRACKRIDE, "TRACKRIDE", params, trackride.this, GetTrackRide_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Util.ShowToast(trackride.this, getString(R.string.nointernetmessage));
        }
    }

    private Toast toast;
    Handler GetTrackRide_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    try {
                        JSONObject jsonobject = new JSONObject(msg.getData().getString("responce"));
                        Location prevLoc = new Location("service Provider");
                        prevLoc.setLatitude(lat);
                        prevLoc.setLongitude(lng);
                        Location newLoc = new Location("service Provider");
                        newLoc.setLatitude(Double.valueOf(jsonobject.getString("lat")));
                        newLoc.setLongitude(Double.valueOf(jsonobject.getString("lng")));
                        float bearing = prevLoc.bearingTo(newLoc);
                        m.setPosition(new LatLng(Double.valueOf(jsonobject.getString("lat")), Double.valueOf(jsonobject.getString("lng"))));
                        m.setRotation(bearing);
                        m.setFlat(true);
                        lat=Double.valueOf(jsonobject.getString("lat"));
                        lng=Double.valueOf(jsonobject.getString("lng"));
                        //m.setPosition(new LatLng(Double.valueOf(jsonobject.getString("lat")), Double.valueOf(jsonobject.getString("lng"))));
                        if(jsonobject.getString("status").equals("COM"))
                        {
                            timer.cancel();
                            Intent intent = new Intent(trackride.this, Rating.class);
                            intent.putExtra("allInfo", msg.getData().getString("responce"));
                            finish();
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(trackride.this)) {
                        CallSessionID(GetTrackRide_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {

                    }
                } else {

                }
            } else {

            }


        }
    };
    @Override
    public void onClick(View v) {
        if (v == cancelride) {
            initiatePopupWindow();
      }
//        else if (v == start) {
//            initiatePopupWindow1();
//        }
        else if (v == contact){
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phone));
            startActivity(callIntent);
        }else if (v == help) {
            Intent startActivity = new Intent(trackride.this, HelpActivity.class);
            startActivity(startActivity);
        } else if (v == confirm) {
            Intent startActivity = new Intent(trackride.this, LandingPageActivity.class);
            startActivity(startActivity);
        }
        else if(v == emergency)
        {
            Intent startActivity = new Intent(trackride.this,emergency.class);
            startActivity(startActivity);
        }
        else if (v == ic_back) {
            Intent startActivity = new Intent(trackride.this,LandingPageActivity.class);
            startActivity(startActivity);
        }
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


};

