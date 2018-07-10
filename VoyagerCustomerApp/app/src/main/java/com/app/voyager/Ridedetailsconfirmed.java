package com.app.voyager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by shadab.s on 19-01-2016.
 */
public class Ridedetailsconfirmed extends ParentActivity implements View.OnClickListener {

    TextView headername;
    ImageView ic_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ridedetailsconfirmed);
        BindView(null, savedInstanceState);
    }


    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        super.BindView(view, savedInstanceState);
        headername = (TextView) findViewById(R.id.headername);
        ic_back = (ImageView) findViewById(R.id.ic_back);


        headername.setText("DETAILS");

        SetOnclicklistener();

    }


    @Override
    public void SetOnclicklistener() {
        super.SetOnclicklistener();
        ic_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                finish();
                break;
        }
    }
}
