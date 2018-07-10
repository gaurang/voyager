package com.app.uconect.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.uconect.Dataset.SpinnerModel;
import com.app.uconect.R;

import java.util.ArrayList;

/**
 * Created by osigroups on 1/11/2016.
 */
public class Countrycode_adapter extends ArrayAdapter<String> {

    private Activity activity;
    private ArrayList data;

    SpinnerModel tempValues = null;
    LayoutInflater inflater;

    /*************
     * CustomAdapter Constructor
     *****************/
    public Countrycode_adapter(
            Activity activitySpinner,
            int textViewResourceId,
            ArrayList objects

    ) {
        super(activitySpinner, textViewResourceId, objects);

        /********** Take passed values **********/
        activity = activitySpinner;
        data = objects;


        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.spinner_countrycode, parent, false);

        /***** Get each Model object from Arraylist ********/
        tempValues = null;
        tempValues = (SpinnerModel) data.get(position);

        TextView label = (TextView) row.findViewById(R.id.tvLanguage);

        ImageView companyLogo = (ImageView) row.findViewById(R.id.imgLanguage);

        {
            companyLogo.setImageResource(tempValues.Image);

            // Set values for spinner each row
            label.setText(tempValues.CountryName);


        }

        return row;
    }
}