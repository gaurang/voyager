package com.app.voyager.fragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.app.voyager.R;
import com.app.voyager.adapter.PlacesAutoCompleteAdapter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

/**
 * Created by atul on 18/4/16.
 */
public class SourceFragment extends AppCompatActivity implements PlaceSelectionListener {

    PlacesAutoCompleteAdapter dataAdapter;
    EditText edtEnterLocation;
    ListView lvAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_source);
        lvAddress = (ListView)findViewById(R.id.lvAddress);
        edtEnterLocation=(EditText) findViewById(R.id.edtEnterLocation);
        dataAdapter = new PlacesAutoCompleteAdapter(this, R.layout.layout_textview);

            // Assign adapter to ListView


        //enables filtering for the contents of the given ListView
        lvAddress.setTextFilterEnabled(true);

        edtEnterLocation.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                dataAdapter.getFilter().filter(s.toString());
                lvAddress.setAdapter(dataAdapter);
            }
        });

    }


    @Override
    public void onPlaceSelected(Place place) {

    }

    @Override
    public void onError(Status status) {

    }
}
