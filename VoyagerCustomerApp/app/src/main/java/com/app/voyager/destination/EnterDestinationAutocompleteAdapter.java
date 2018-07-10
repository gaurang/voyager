package com.app.voyager.destination;

import android.content.Context;
import android.graphics.Typeface;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.app.voyager.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by atul on 21/4/16.
 */
public class EnterDestinationAutocompleteAdapter extends ArrayAdapter<AutocompletePrediction> implements Filterable {

    private static final String TAG = "EnterDestinationAutocompleteAdapter";
    private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);

    private ArrayList<AutocompletePrediction> mResultList;

    private GoogleApiClient mGoogleApiClient;

    private LatLngBounds mBounds;

    private AutocompleteFilter mPlaceFilter;
    Context context;
    public EnterDestinationAutocompleteAdapter(Context context, GoogleApiClient googleApiClient,AutocompleteFilter filter) {
        super(context,R.layout.destination_row_item,0);
        mGoogleApiClient = googleApiClient;
//        mBounds = bounds;
        this.context=context;
        mPlaceFilter = filter;
    }

    public void setBounds(LatLngBounds bounds) {
        mBounds = bounds;
    }

    @Override
    public int getCount() {
        if(mResultList!=null){
            return mResultList.size();
        }else{
            return 0;
        }

    }

    @Override
    public AutocompletePrediction getItem(int position) {
        return mResultList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView == null) {
            // This a new view we inflate the new layout
            viewHolder=new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getApplicationContext().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.destination_row_item,
                    parent, false);
            viewHolder.txtName=(TextView)convertView.findViewById(R.id.txtName);
            viewHolder.txtAddress=(TextView)convertView.findViewById(R.id.txtAddress);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();

        }
//        View row = super.getView(position, convertView, parent);
        AutocompletePrediction item = getItem(position);
        viewHolder.txtName.setText(item.getPrimaryText(STYLE_BOLD));
        viewHolder.txtAddress.setText(item.getSecondaryText(STYLE_BOLD));
//        TextView textView1 = (TextView) row.findViewById(android.R.id.text1);
//        TextView textView2 = (TextView) row.findViewById(android.R.id.text2);
//        textView1.setText(item.getPrimaryText(STYLE_BOLD));
//        textView2.setText(item.getSecondaryText(STYLE_BOLD));

        return convertView;
    }

    public static class ViewHolder{
        TextView txtName,txtAddress;
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null) {
                    mResultList = getAutocomplete(constraint);
                    if (mResultList != null) {
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {

                if (resultValue instanceof AutocompletePrediction) {
                    return ((AutocompletePrediction) resultValue).getFullText(null);
                } else {
                    return super.convertResultToString(resultValue);
                }
            }
        };
    }
    private ArrayList<AutocompletePrediction> getAutocomplete(CharSequence constraint) {
        if (mGoogleApiClient.isConnected()) {
            PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi
                            .getAutocompletePredictions(mGoogleApiClient, constraint.toString(),
                                    mBounds, mPlaceFilter);

            AutocompletePredictionBuffer autocompletePredictions = results
                    .await(60, TimeUnit.SECONDS);

            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                Toast.makeText(getContext(), "Error contacting API: " + status.toString(),
                        Toast.LENGTH_SHORT).show();

                autocompletePredictions.release();
                return null;
            }

            return DataBufferUtils.freezeAndClose(autocompletePredictions);
        }

        return null;
    }


}
