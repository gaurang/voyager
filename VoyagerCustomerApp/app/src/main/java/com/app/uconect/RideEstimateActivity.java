package com.app.uconect;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.uconect.Dataset.LocationDetailsData;
import com.app.uconect.Util.Util;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.CameraPosition;

import org.json.JSONException;
import org.json.JSONObject;

public class RideEstimateActivity extends ParentActivity implements View.OnClickListener {
    TextView headername;
    ImageView ic_back;
    TextView textView1_destination, textView1_source,fareestmate;
    Button confirm;
    JSONObject jsonObject;
    String Dataset = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riding_estimate);
        try {
            jsonObject = new JSONObject(getIntent().getStringExtra("Data"));
            Dataset = " $ " + jsonObject.getString("minFare") + " - " + jsonObject.getString("maxFare");
        } catch (JSONException e) {
            Util.ShowToast(RideEstimateActivity.this, e.getMessage() + "");
            finish();
            e.printStackTrace();
        }
        BindView(null, savedInstanceState);
    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        headername = (TextView) findViewById(R.id.headername);
        fareestmate = (TextView) findViewById(R.id.fareestmate);
        textView1_destination = (TextView) findViewById(R.id.textView1_destination);
        textView1_source = (TextView) findViewById(R.id.textView1_source);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        confirm = (Button) findViewById(R.id.confirm);
        headername.setText("RIDE ESTIMATE");
        fareestmate.setText(Dataset);
        SetOnclicklistener();
        super.BindView(view, savedInstanceState);
    }

    @Override
    public void SetOnclicklistener() {
        ic_back.setOnClickListener(this);
        confirm.setOnClickListener(this);
        textView1_destination.setOnClickListener(this);
        textView1_source.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                finish();
                break;
            case R.id.confirm:
                finish();
                break;
            case R.id.textView1_destination:
                openAutocompleteActivity(REQUEST_CODE_AUTOCOMPLETEFORDESTINATION);

                break;
            case R.id.textView1_source:
                openAutocompleteActivity(REQUEST_CODE_AUTOCOMPLETEFORSOURCE);
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
    private static final int REQUEST_CODE_AUTOCOMPLETEFORSOURCE = 1;
    private static final int REQUEST_CODE_AUTOCOMPLETEFORDESTINATION = 2;
    LocationDetailsData SourceData = new LocationDetailsData();
    LocationDetailsData DestinationData = new LocationDetailsData();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETEFORDESTINATION) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place Selected: " + place.getName());


                // Format the place's details and display them in the TextView.
                String Placedetails = place.getName() + " " + place.getAddress() + "";
                textView1_destination.setText(Placedetails);
                Log.i(TAG, "Place Selected: Placedetails " + Placedetails);
                DestinationData.Placename = Placedetails;
                DestinationData.latitude = place.getLatLng().latitude;
                DestinationData.longitude = place.getLatLng().longitude;
//                mGoogleMap.addMarker(new MarkerOptions()
//                        .position(place.getLatLng())
//                        .icon(BitmapDescriptorFactory
//                                .fromResource(R.drawable.destination_marker)));


            }
        } else if (requestCode == REQUEST_CODE_AUTOCOMPLETEFORSOURCE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place Selected: " + place.getName());

                // Format the place's details and display them in the TextView.
                textView1_source.setText(formatPlaceDetails(getResources(), place.getName(),
                        place.getId(), place.getAddress(), place.getPhoneNumber(),
                        place.getWebsiteUri()));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(place.getLatLng()).zoom(15f).tilt(70).build();

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

                String Placedetails = formatPlaceDetails(getResources(), place.getName(),
                        place.getId(), place.getAddress(), place.getPhoneNumber(),
                        place.getWebsiteUri()) + "";
                SourceData.Placename = Placedetails;
                SourceData.latitude = place.getLatLng().latitude;
                SourceData.longitude = place.getLatLng().longitude;
//                // Display attributions if required.
//                CharSequence attributions = place.getAttributions();
//                if (!TextUtils.isEmpty(attributions)) {
//                    mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));
//                } else {
//                    mPlaceAttribution.setText("");
//                }
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

        return Html.fromHtml(res.getString(R.string.app_name, name, id, address, phoneNumber,
                websiteUri));

    }

}
