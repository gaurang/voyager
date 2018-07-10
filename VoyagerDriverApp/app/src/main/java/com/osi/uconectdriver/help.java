package com.osi.uconectdriver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Brij on 04-03-2016.
 */
public class help extends AppCompatActivity implements View.OnClickListener {
    private TextView header;
    private ImageView ic_back;
    private Button pick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        pick = (Button) findViewById(R.id.pick);
        pick.setOnClickListener(this);
        header = (TextView) findViewById(R.id.headername);
        header.setText("HELP");
        ic_back = (ImageView) findViewById(R.id.ic_back);
        ic_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ic_back) {
            Intent startActivity = new Intent(help.this, trackride.class);
            startActivity(startActivity);
        }
    }
}
