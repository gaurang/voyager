package com.app.uconect;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.uconect.AsyncTask.CallAPI;
import com.app.uconect.Dataset.LocationDetailsData;
import com.app.uconect.Util.Session;
import com.app.uconect.Util.Util;
import com.app.uconect.dialogs.ProgressDialogView;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddFavorPlacesActivity extends ParentActivity implements View.OnClickListener {
    TextView headername;
    ImageView ic_back;
    TextView textView1_office, textView1_home;
    private String favLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfavplaces);
        progressdialog = new ProgressDialogView(AddFavorPlacesActivity.this, "");
        BindView(null, savedInstanceState);
    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        headername = (TextView) findViewById(R.id.headername);
        textView1_office = (TextView) findViewById(R.id.textView1_office);
        textView1_home = (TextView) findViewById(R.id.textView1_home);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        headername.setText("FAVOURITE PLACES");
        SetOnclicklistener();
        super.BindView(view, savedInstanceState);
    }

    @Override
    public void SetOnclicklistener() {
        ic_back.setOnClickListener(this);
        textView1_office.setOnClickListener(this);
        textView1_home.setOnClickListener(this);
        try {
            JSONObject jsonObject_main = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject_main = getCommontHeaderParams();

            jsonObject.put("customerId", Session.getUserID(AddFavorPlacesActivity.this));
            jsonObject_main.put("body", jsonObject);
            CallGETPLACESAPI(jsonObject_main);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                finish();
                break;
            case R.id.textView1_home:
                favLabel="Home";
                openAutocompleteActivity(REQUEST_CODE_AUTOCOMPLETEFAV);
                break;
            case R.id.textView1_office:
                favLabel="Office";
                openAutocompleteActivity(REQUEST_CODE_AUTOCOMPLETEFAV);

                break;
        }
    }

    private void openAutocompleteActivity(int requestcode) {
        try {

            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
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
        }
    }

    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    private static final int REQUEST_CODE_AUTOCOMPLETEFAV = 2;
    LocationDetailsData SourceData = new LocationDetailsData();
    LocationDetailsData DestinationData = new LocationDetailsData();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Place place = PlaceAutocomplete.getPlace(this, data);
    Log.i(TAG, "Place Selected: " + place.getName());
    String Placedetails = place.getName() + " " + place.getAddress() + "";
    if (requestCode == REQUEST_CODE_AUTOCOMPLETEFAV) {
        if (resultCode == RESULT_OK) {
            // Get the user's selected place from the Intent.
            // Format the place's details and display them in the TextView.
            textView1_home.setText(Placedetails);

        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(this, data);
            Log.e(TAG, "Error: Status = " + status.toString());
        } else if (resultCode == RESULT_CANCELED) {
            // Indicates that the activity closed before a selection was made. For example if
            // the user pressed the back button.
        }
    }else
        {
            // Format the place's details and display them in the TextView.
            textView1_office.setText(Placedetails);
        }
        SourceData.Placename = Placedetails;
                SourceData.latitude = place.getLatLng().latitude;
                SourceData.longitude = place.getLatLng().longitude;
                try {
                    JSONObject jsonObject_main = new JSONObject();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject_main = getCommontHeaderParams();
                    jsonObject.put("latitude", SourceData.latitude);
                    jsonObject.put("longitude", SourceData.longitude);
                    jsonObject.put("favLabel", favLabel);
                    jsonObject.put("favName", SourceData.Placename.replace(" ", " "));
                    jsonObject.put("customerId", Session.getUserID(AddFavorPlacesActivity.this));
                    jsonObject_main.put("body", jsonObject);
                    Log.i("dda","aaaaa"+jsonObject_main);
                    CallADDPLACESAPI(jsonObject_main);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                // Display attributions if required.
//                CharSequence attributions = place.getAttributions();
//                if (!TextUtils.isEmpty(attributions)) {
//                    mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));
//                } else {
//                    mPlaceAttribution.setText("");
//                }
    }


    /**
     * Helper method to format information about a place nicely.
     */
    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {

        return Html.fromHtml(res.getString(R.string.app_name, name, id, address, phoneNumber,
                websiteUri));

    }

    public void CallADDPLACESAPI(JSONObject params) {
        if (Util.isNetworkConnected(AddFavorPlacesActivity.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(ADDFAVOURITEPLACES, "ADDFAVOURITEPLACES", params, AddFavorPlacesActivity.this, GetResend_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(AddFavorPlacesActivity.this, getString(R.string.nointernetmessage));
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
                                textView1_home.setText(jsonObject.getString("favName"));
                            } else {
                                textView1_office.setText(jsonObject.getString("favName"));
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
                    if (Util.isNetworkConnected(AddFavorPlacesActivity.this)) {
                        CallSessionID(GetResend_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(AddFavorPlacesActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(AddFavorPlacesActivity.this, msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(AddFavorPlacesActivity.this, msg.getData().getString("msg"));

            }
        }
    };

    public void CallGETPLACESAPI(JSONObject params) {
        if (Util.isNetworkConnected(AddFavorPlacesActivity.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(GETFAVOURITEPLACES, "GETFAVOURITEPLACES", params, AddFavorPlacesActivity.this, GetPlaces_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(AddFavorPlacesActivity.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetPlaces_Handler = new Handler() {
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
                                textView1_home.setText(jsonObject.getString("favName"));
                            } else {
                                textView1_office.setText(jsonObject.getString("favName"));
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                 //   Util.ShowToast(AddFavorPlacesActivity.this, msg.getData().getString("responce"));

                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallGETPLACESAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(AddFavorPlacesActivity.this)) {
                        CallSessionID(GetPlaces_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(AddFavorPlacesActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(AddFavorPlacesActivity.this, msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(AddFavorPlacesActivity.this, msg.getData().getString("msg"));

            }
        }
    };
}
