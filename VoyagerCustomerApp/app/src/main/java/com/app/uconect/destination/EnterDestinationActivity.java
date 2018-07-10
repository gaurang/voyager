package com.app.uconect.destination;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.uconect.ParentActivity;
import com.app.uconect.R;
import com.app.uconect.Util.PreferenceHelper;
import com.app.uconect.activities.Constants;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by atul on 21/4/16.
 */
public class EnterDestinationActivity extends ParentActivity implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener,AdapterView.OnItemClickListener{

    public static String TAG="ACTIVITY";
    protected GoogleApiClient mGoogleApiClient;
    private EnterDestinationAutocompleteAdapter mAdapter;
    private AutoCompleteTextView mAutocompleteView,autocomplete_places;
    private PreferenceHelper preferenceHelper;
    JSONObject jsonObject,jsonObject1;
    private ListView listview;
    private String work,destination;
    private TextView headername,txtAddHome,txtAddWork;
    private ImageView ic_back;
    private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);
    private LinearLayout lenearOffice,lenearHome;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
    Set<String> set=new HashSet<>();
    ArrayList<String> arrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_destination);
        BindView(null,savedInstanceState);

    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        super.BindView(view, savedInstanceState);
        preferenceHelper=new PreferenceHelper(this);
        destination=getIntent().getStringExtra("DESTINATION");
        headername = (TextView) findViewById(R.id.headername);
        lenearHome=(LinearLayout)findViewById(R.id.lenearHome);
        lenearHome.setOnClickListener(this);
        lenearOffice=(LinearLayout)findViewById(R.id.lenearOffice);
        lenearOffice.setOnClickListener(this);
        txtAddHome=(TextView)findViewById(R.id.txtAddHome);
        txtAddWork=(TextView)findViewById(R.id.txtAddWork);
        if(preferenceHelper.getString("Work")!=null){
            try{
                lenearOffice.setVisibility(View.VISIBLE);
                jsonObject=new JSONObject(preferenceHelper.getString("Work"));
                txtAddWork.setText(jsonObject.getString(Constants.ADDRESS));
            }catch (Exception e){
                lenearOffice.setVisibility(View.GONE);
            }
        }else{
            lenearOffice.setVisibility(View.GONE);
        }
        if(preferenceHelper.getString("Home")!=null){
            try{
                lenearHome.setVisibility(View.VISIBLE);
                jsonObject1=new JSONObject(preferenceHelper.getString("Home"));
                txtAddHome.setText(jsonObject1.getString(Constants.ADDRESS));
            }catch (Exception e){
                lenearHome.setVisibility(View.GONE);
            }
        }else{
            lenearHome.setVisibility(View.GONE);
        }

        ic_back=(ImageView)findViewById(R.id.ic_back);
        ic_back.setOnClickListener(this);
        headername.setText("Enter Destination");
        preferenceHelper=new PreferenceHelper(this);
        progressdialog = new ProgressDialogView(EnterDestinationActivity.this, "Please wait..");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.autocomplete_places);
        mAutocompleteView.setHint("Enter your Destination");
        listview=(ListView)findViewById(R.id.listview);
        listview.setOnItemClickListener(this);
        if(destination!=null){
            mAutocompleteView.setText(destination+"");
        }
//        if(preferenceHelper.getArrayList("SELECTEDVALUE")!=null){
//            List list=new ArrayList(preferenceHelper.getArrayList("SELECTEDVALUE"));
//        }
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);
        mAdapter = new EnterDestinationAutocompleteAdapter(this, mGoogleApiClient,null);
        mAutocompleteView.setAdapter(mAdapter);
    }

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
            listview.setAdapter(mAdapter);

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
            arrayList.add(places.get(0).getId());
            set.addAll(arrayList);
            preferenceHelper.addArrayList("SELECTEDVALUE",set);
            DestinationPlaces destinationPlaces=new DestinationPlaces();
            destinationPlaces.setName(places.get(0).getName() + "");
            destinationPlaces.setAddress(places.get(0).getAddress() + "");
            destinationPlaces.setLatitude(places.get(0).getLatLng().latitude);
            destinationPlaces.setLongitude(places.get(0).getLatLng().longitude);
            ArrayList<DestinationPlaces> arrayList=new ArrayList<>();
            arrayList.add(destinationPlaces);
            Intent intent=new Intent();
            Bundle bundle=new Bundle();
            bundle.putSerializable("LIST",arrayList);
            intent.putExtras(bundle);

            setResult(RESULT_OK, intent);
            finish();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ic_back:
                onBackPressed();
                break;
            case R.id.lenearHome:
                mAutocompleteView.setText(txtAddHome.getText().toString()+"");
                break;
            case R.id.lenearOffice:
                mAutocompleteView.setText(txtAddWork.getText().toString()+"");
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        final AutocompletePrediction item = mAdapter.getItem(position);
        mAutocompleteView.setText(item.getPrimaryText(STYLE_BOLD)+""+item.getSecondaryText(STYLE_BOLD));
        final String placeId = item.getPlaceId();
        final CharSequence primaryText = item.getPrimaryText(null);
        Log.i(TAG, "Autocomplete item selected: " + primaryText);
        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                .getPlaceById(mGoogleApiClient, placeId);
        placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
    }
}
