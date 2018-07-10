package com.app.voyager;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by Brij on 02-03-2016.
 */
public class trackride1 extends AppCompatActivity implements View.OnClickListener {
    private Button cancelride;
    private Button stop;
    private Button cancel;
    private Button contact;
    private Button confirm;
    private Button confirm1;
    private ImageView ic_back;
    private TextView header;
    private ImageButton emergency;
    private LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trackride1);
        cancelride=(Button) findViewById(R.id.cancelride);
        stop=(Button) findViewById(R.id.stop);
        contact=(Button) findViewById(R.id.contact);
        cancelride.setOnClickListener(this);
        header=(TextView) findViewById(R.id.headername);
        header.setText("TRACK RIDE");
        ic_back=(ImageView)findViewById(R.id.ic_back);
        ic_back.setOnClickListener(this);
        emergency=(ImageButton) findViewById(R.id.imageButton);
        layout=(LinearLayout) findViewById(R.id.onlinemeter1);
        layout.setOnClickListener(this);
        emergency.setOnClickListener(this);
        contact.setOnClickListener(this);
        stop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == cancelride)
        {
            initiatePopupWindow();
        }
        else if(v == stop)
        {
            Intent startActivity = new Intent(trackride1.this, Rating.class);
            startActivity(startActivity);
        }
        else if (v == contact)
        {
            Intent startActivity = new Intent(trackride1.this,contact.class);
            startActivity(startActivity);
        }
        else if(v == emergency)
        {
            Intent startActivity = new Intent(trackride1.this,emergency.class);
            startActivity(startActivity);
        }
        else if(v == layout)
        {
            Intent startActivity = new Intent(trackride1.this,onlinemeter.class);
            startActivity(startActivity);
        }
        else if(v == ic_back)
        {
            Intent startActivity = new Intent(trackride1.this,trackride.class);
            startActivity(startActivity);
        }
    }
    private PopupWindow pwindo;

    private void initiatePopupWindow() {
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) trackride1.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.cancel_popup, (ViewGroup) findViewById(R.id.popup_element));
            pwindo = new PopupWindow(layout, 300, 370, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
            pwindo.update(0, 0, 1050, 600);
            cancel = (Button) layout.findViewById(R.id.cancel);
            confirm = (Button) layout.findViewById(R.id.confirm);
            confirm.setOnClickListener(this);
            cancel.setOnClickListener(cancel_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            pwindo.dismiss();
        }
    };
}
