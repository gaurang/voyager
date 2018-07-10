package com.app.voyager.tabfragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.voyager.R;
import com.app.voyager.Util.PreferenceHelper;
import com.app.voyager.activities.Constants;
import com.app.voyager.activities.HomePlacesActivity;
import com.app.voyager.activities.WorkPlacesActivity;

import org.json.JSONObject;

/**
 * Created by atul on 14/4/16.
 */
public class FavoritesFragment extends Fragment implements View.OnClickListener{

    private LinearLayout llhome,llWork;
    PreferenceHelper preferenceHelper;
    private TextView textView1_home,textView1_office;
    JSONObject jsonObject,jsonObject1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_favorite_places,container,false);
        preferenceHelper=new PreferenceHelper(getActivity());
        textView1_home=(TextView)view.findViewById(R.id.textView1_home);
        textView1_office=(TextView)view.findViewById(R.id.textView1_office);
        llhome=(LinearLayout)view.findViewById(R.id.llhome);
        llWork=(LinearLayout)view.findViewById(R.id.llWork);
        llhome.setOnClickListener(this);
        llWork.setOnClickListener(this);
        if(preferenceHelper.getString("Work")!=null){
            try {
                jsonObject = new JSONObject(preferenceHelper.getString("Work"));
                textView1_office.setText(jsonObject.getString(Constants.ADDRESS));
            }catch (Exception e){

            }
        }
        if(preferenceHelper.getString("Home")!=null){
            try{
                jsonObject1=new JSONObject(preferenceHelper.getString("Home"));
                textView1_home.setText(jsonObject1.getString(Constants.ADDRESS));
            }catch (Exception e){
            }
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(preferenceHelper.getString("Work")!=null){
            try {
                jsonObject = new JSONObject(preferenceHelper.getString("Work"));
                textView1_office.setText(jsonObject.getString(Constants.ADDRESS));
            }catch (Exception e){

            }
        }
        if(preferenceHelper.getString("Home")!=null){
            try{
                jsonObject1=new JSONObject(preferenceHelper.getString("Home"));
                textView1_home.setText(jsonObject1.getString(Constants.ADDRESS));
            }catch (Exception e){
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llhome:
                Intent intent=new Intent(getActivity(), HomePlacesActivity.class);
                intent.putExtra("HOMEADDRESS",textView1_home.getText().toString()+"");
                startActivity(intent);
                break;
            case R.id.llWork:
                Intent intent1=new Intent(getActivity(), WorkPlacesActivity.class);
                intent1.putExtra("WORKADDRESS",textView1_office.getText().toString()+"");
                startActivity(intent1);
                break;
        }
    }
}