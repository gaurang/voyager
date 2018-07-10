package com.app.uconect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.uconect.preferencetabs.PreferenceTabActivity;
import com.app.uconect.preferencetabs.ProfileTabActivity;

/*
 * Created by shadab.s on 14-01-2016.
 */
public class SettingActivity extends ParentActivity implements View.OnClickListener {
    Button btn_emergency, logout;

    TextView personeldetail;

    Button editpro, business, personal;
    TextView headername;
    ImageView ic_back;
    ImageView profile;
    ImageView preferrence;
    Button ly_addfav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        BindView(null, savedInstanceState);
    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        super.BindView(view, savedInstanceState);
        //btn_emergency = (Button) findViewById(R.id.btn_emergency);
        //personeldetail = (TextView) findViewById(R.id.personeldetail);
        headername = (TextView) findViewById(R.id.headername);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        profile = (ImageView) findViewById(R.id.profile);
        preferrence = (ImageView) findViewById(R.id.preferrence);
        logout = (Button) findViewById(R.id.logout);
        headername.setText("Settings");
        SetOnclicklistener();
    }

    @Override
    public void SetOnclicklistener() {
        super.SetOnclicklistener();
//        btn_emergency.setOnClickListener(this);
//        personeldetail.setOnClickListener(this);
//        editpro.setOnClickListener(this);
//        business.setOnClickListener(this);
//        personal.setOnClickListener(this);
//        ly_addfav.setOnClickListener(this);
        profile.setOnClickListener(this);
        preferrence.setOnClickListener(this);
        ic_back.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile:
                Intent intent = new Intent(SettingActivity.this, ProfileTabActivity.class);
                startActivity(intent);
                break;
            case R.id.preferrence:
                intent = new Intent(SettingActivity.this, PreferenceTabActivity.class);
                startActivity(intent);
                break;
            case R.id.ic_back:
                onBackPressed();
                break;
            case R.id.logout:
                LogoutUser();
                break;
//            case R.id.personeldetail:
//                intent = new Intent(SettingActivity.this, ProfileActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.business:
//                intent = new Intent(SettingActivity.this, BusinessActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.personal:
//                intent = new Intent(SettingActivity.this, PersonalActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.candiname:
//                intent = new Intent(SettingActivity.this, ProfileActivity.class);
//                startActivity(intent);
//                break;
        }
    }

    @Override
    public void onBackPressed() {
        callLandingPage();
    }

}
