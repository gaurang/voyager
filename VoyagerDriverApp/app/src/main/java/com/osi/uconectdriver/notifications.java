package com.osi.uconectdriver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Brij on 10-03-2016.
 */
public class notifications extends AppCompatActivity implements View.OnClickListener{
    private TextView header;
    private ImageView ic_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notifications);
        header=(TextView) findViewById(R.id.headername);
        header.setText("NOTIFICATION");
        ic_back=(ImageView) findViewById(R.id.ic_back);
        ic_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
