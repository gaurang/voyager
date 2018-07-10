package com.app.voyager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.voyager.Dataset.UserDetails;
import com.app.voyager.Util.Session;

/**
 * Created by shadab.s on 14-01-2016.
 */
public class FreeridesActivity extends ParentActivity implements View.OnClickListener {

    TextView headername;
    ImageView ic_back;
    Button invite;
    TextView invitecode;
    UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freerides);
        userDetails = Session.GetUserInformation(Session.getAllInfo(FreeridesActivity.this));
        BindView(null, savedInstanceState);
    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        super.BindView(view, savedInstanceState);
        headername = (TextView) findViewById(R.id.headername);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        invite = (Button) findViewById(R.id.btn_invite);
        invitecode = (TextView) findViewById(R.id.invitecode);
        invitecode.setText(userDetails.referralCode + "");
        headername.setText("FREE RIDES");
        SetOnclicklistener();
    }

    @Override
    public void SetOnclicklistener() {
        super.SetOnclicklistener();
        ic_back.setOnClickListener(this);
        invite.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ic_back:
              onBackPressed();
                break;
            case R.id.btn_invite:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Use my Voyager promo code, " + userDetails.referralCode);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
        }


    }
    @Override
    public void onBackPressed() {
        callLandingPage();
    }

}
