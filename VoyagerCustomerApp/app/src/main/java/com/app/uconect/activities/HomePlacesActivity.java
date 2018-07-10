package com.app.uconect.activities;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.uconect.AsyncTask.CallAPI;
import com.app.uconect.ParentActivity;
import com.app.uconect.R;
import com.app.uconect.Util.Methods;
import com.app.uconect.Util.PreferenceHelper;
import com.app.uconect.Util.Session;
import com.app.uconect.Util.Util;
import com.app.uconect.dialogs.ProgressDialogView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by atul on 21/4/16.
 */
public class HomePlacesActivity extends ParentActivity implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener{

    public static String TAG="ACTIVITY";
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private AutoCompleteTextView mAutocompleteView;
    JSONObject jsonObject;
    private PreferenceHelper preferenceHelper;
    String home,homeAddress;
    private Button remove;
    private TextView home1;

    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
    TextView headername;
    ImageView ic_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_places);
        BindView(null, savedInstanceState);

    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        super.BindView(view, savedInstanceState);
        headername = (TextView) findViewById(R.id.headername);
        home1=(TextView) findViewById(R.id.textView4);
        ic_back=(ImageView)findViewById(R.id.ic_back);
        ic_back.setOnClickListener(this);
        remove=(Button) findViewById(R.id.removeHome);
        headername.setText("Add Home");
        preferenceHelper=new PreferenceHelper(this);
        progressdialog = new ProgressDialogView(HomePlacesActivity.this, "Please wait..");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.autocomplete_places);
        homeAddress=getIntent().getStringExtra("HOMEADDRESS");
        if(homeAddress.toString().length()!= 0){
            home1.setVisibility(View.VISIBLE);
            home1.setText(""+homeAddress);
            remove.setVisibility(View.VISIBLE);
            remove.setOnClickListener(this);
        }
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_GREATER_SYDNEY,
                null);
        mAutocompleteView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ic_back:
                finish();
                break;
            case R.id.removeHome:
                remove();

        }
    }

    private void remove() {
        try {
//                home=places.get(0).getName().toString()+""+places.get(0).getAddress().toString()+"";
            JSONObject jsonObject_main = new JSONObject();
            jsonObject = new JSONObject();
            jsonObject_main = getCommontHeaderParams();
            jsonObject.put("customerId", Session.getUserID(HomePlacesActivity.this));
            jsonObject.put(Constants.FAVLABLE, "Home");
           // jsonObject.put("favName",home1.getText().toString().trim());
            jsonObject_main.put(Constants.BODY, jsonObject);
            Log.i("dda","aaaaa"+jsonObject_main);
            CallDELPLACESAPI(jsonObject_main);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void CallDELPLACESAPI(JSONObject params) {
        if (Util.isNetworkConnected(HomePlacesActivity.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(DELFAVOURITEPLACES, "DELFAVOURITEPLACES", params, HomePlacesActivity.this, GetDel_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(HomePlacesActivity.this, getString(R.string.nointernetmessage));
        }
    }
    Handler GetDel_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {

                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    try {
                        JSONArray jsonArray = new JSONArray(msg.getData().getString("responce"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (jsonObject.getString("favLabel").equals("Home")) {
                                Methods.toastShort("Added to home successfully", HomePlacesActivity.this);
                                jsonObject.put(Constants.ADDRESS, home);
                                preferenceHelper.addString("Home", "" + jsonObject);
                                home1.setText("");
                                remove.setVisibility(View.GONE);
                            } else {
//                                textView1_office.setText(jsonObject.getString("favName"));
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // Util.ShowToast(AddFavorPlacesActivity.this, msg.getData().getString("responce"));

                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallADDPLACESAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(HomePlacesActivity.this)) {
                        CallSessionID(GetResend_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(HomePlacesActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(HomePlacesActivity.this, msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(HomePlacesActivity.this, msg.getData().getString("msg"));

            }
        }
    };
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);
            Log.i(TAG, "Autocomplete item selected: " + primaryText);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            try {
//                home=places.get(0).getName().toString()+""+places.get(0).getAddress().toString()+"";
                JSONObject jsonObject_main = new JSONObject();
                jsonObject = new JSONObject();
                jsonObject_main = getCommontHeaderParams();
                jsonObject.put(Constants.LATITUDE, places.get(0).getLatLng().latitude);
                jsonObject.put(Constants.LONGITUDE,places.get(0).getLatLng().longitude);
                jsonObject.put(Constants.FAVLABLE, "Home");
               jsonObject.put(Constants.FAVNAME, formatPlaceDetails(getResources(), places.get(0).getName(),
                        "", places.get(0).getAddress(), "",null));
                home=places.get(0).getName()+","+places.get(0).getAddress();
                jsonObject.put(Constants.CUSTOMER_ID, Session.getUserID(HomePlacesActivity.this));
                jsonObject_main.put(Constants.BODY, jsonObject);
                Log.i("dda","aaaaa"+jsonObject_main);
                CallADDPLACESAPI(jsonObject_main);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            places.release();
        }
    };
    public void CallADDPLACESAPI(JSONObject params) {
        if (Util.isNetworkConnected(HomePlacesActivity.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(ADDFAVOURITEPLACES, "ADDFAVOURITEPLACES", params, HomePlacesActivity.this, GetResend_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(HomePlacesActivity.this, getString(R.string.nointernetmessage));
        }
    }
    Handler GetResend_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {

                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    try {
                        JSONArray jsonArray = new JSONArray(msg.getData().getString("responce"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (jsonObject.getString("favLabel").equals("Home")) {
                                Methods.toastShort("Added to home successfully", HomePlacesActivity.this);
                                jsonObject.put(Constants.ADDRESS, home);
                                preferenceHelper.addString("Home", "" + jsonObject);
                                String a = jsonObject.getString("favName");
                                int n=a.indexOf("s:");
                                int b=a.indexOf("Phone");
                                home1.setText(a.toString().trim().substring(n + 2, b));
                                remove.setVisibility(View.VISIBLE);
                            } else {
//                                textView1_office.setText(jsonObject.getString("favName"));
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // Util.ShowToast(AddFavorPlacesActivity.this, msg.getData().getString("responce"));

                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallADDPLACESAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(HomePlacesActivity.this)) {
                        CallSessionID(GetResend_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(HomePlacesActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(HomePlacesActivity.this, msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(HomePlacesActivity.this, msg.getData().getString("msg"));

            }
        }
    };

    public JSONObject getCommontHeaderParams() throws JSONException {
        String device_id = "";
        try {

            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            device_id = tm.getDeviceId();

        } catch (Exception ex) {
        }
        JSONObject jsonObjectmain = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deviceId", Settings.System.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID));
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

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

}

