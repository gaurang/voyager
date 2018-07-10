package com.osi.uconectdriver;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Brij on 04-03-2016.
 */
public class contact extends AppCompatActivity implements View.OnClickListener{
    private ImageView ic_back;
    private TextView header;
    private TextView name;
    private ImageButton call;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        ic_back.setOnClickListener(this);
        header = (TextView) findViewById(R.id.headername);
        header.setText("CONTACT");
        name = (TextView) findViewById(R.id.name);
        call = (ImageButton) findViewById(R.id.call);
        call.setOnClickListener(this);
        name.setText(getIntent().getExtras().getString("name"));
        mobile=getIntent().getExtras().getString("mobile");
    }

    @Override
    public void onClick(View v) {
        if (v == call) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+mobile));
            startActivity(callIntent);
        } else {
            finish();
        }
    }
}


