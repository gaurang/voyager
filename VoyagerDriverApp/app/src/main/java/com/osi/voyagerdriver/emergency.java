package com.osi.voyagerdriver;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Brij on 04-03-2016.
 */
public class emergency extends AppCompatActivity implements View.OnClickListener{
    private Button back;
    private Context activity;
    private Button police;
    private Button emergencycall;

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
    }

    @Override
    public void onClick(View v) {
        if(v == back) {
            finish();
        }
        else if(v == police){
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:09819895193"));
            startActivity(callIntent);
        }
        else if(v == emergencycall){
            Intent startActivity = new Intent(emergency.this, trackride1.class);
            startActivity(startActivity);
        }
    }
}