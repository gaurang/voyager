package com.osi.voyagerdriver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Brij on 04-03-2016.
 */
public class bookings extends AppCompatActivity implements View.OnClickListener{
    private TextView header;
    private ImageView ic_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.bookings);
        header=(TextView) findViewById(R.id.headername);
        header.setText("BOOKINGS");
        ic_back=(ImageView) findViewById(R.id.ic_back);
        ic_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent startActivity = new Intent(bookings.this,help.class);
        startActivity(startActivity);
    }
}
