package com.app.uconect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.uconect.Dataset.EmergencyContactData;
import com.app.uconect.Util.Session;

/**
 * Created by E6240 on 04-03-2016.
 */
public class emergency extends AppCompatActivity implements View.OnClickListener{
    private Button back;
    private Context activity;
    private Button police;
    private Button emergencycall;
    private Button emergencycall2;
    private TextView callPolice, emergency, emergency2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency);
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        police = (Button) findViewById(R.id.police);
        police.setOnClickListener(this);
        emergencycall= (Button) findViewById(R.id.emergencycall);
        emergencycall.setOnClickListener(this);
        emergencycall2= (Button) findViewById(R.id.emergencycall2);
        emergencycall2.setOnClickListener(this);

        callPolice = (TextView) findViewById(R.id.back);
        callPolice.setOnClickListener(this);
        emergency = (TextView) findViewById(R.id.back);
        emergency.setOnClickListener(this);
        emergency2 = (TextView) findViewById(R.id.back);
        emergency2.setOnClickListener(this);

        if(Session.getEcontact(emergency.this).isEmpty())
        {
            emergencycall.setVisibility(View.GONE);
            emergency.setVisibility(View.GONE);
        }
        if(Session.getEcontact1(emergency.this).isEmpty())
        {
            emergencycall2.setVisibility(View.GONE);
            emergency2.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == back) {
            Intent startActivity = new Intent(emergency.this, trackride1.class);
            startActivity(startActivity);
        }
        else if(v == police || v == callPolice){
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:09819895193"));
            startActivity(callIntent);
        }
        else if(v == emergencycall || v == emergency){
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+ Session.getEcontact(emergency.this)));
            startActivity(callIntent);
        }
        else if(v == emergencycall2 || v == emergency2){
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+ Session.getEcontact1(emergency.this)));
            startActivity(callIntent);
        }
    }
}