package com.app.voyager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.voyager.Util.Session;

/**
 * Created by E6420 on 16-06-2016.
 */
public class contact extends AppCompatActivity implements View.OnClickListener {
    private ImageView ic_back;
    private TextView header;
    private TextView name;
    private ImageButton callcop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        ic_back.setOnClickListener(this);
        header = (TextView) findViewById(R.id.headername);
        header.setText("CONTACT");
        name = (TextView) findViewById(R.id.name);
        name.setText(Session.getName(contact.this));
        callcop = (ImageButton) findViewById(R.id.callcop);
        callcop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == callcop) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:09819895193"));
            startActivity(callIntent);
        } else {
            Intent intent = new Intent(contact.this, trackride.class);
            startActivity(intent);
        }
    }
}
