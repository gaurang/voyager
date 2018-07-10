package com.osi.uconectdriver;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.osi.uconectdriver.AsyncTask.CallAPI;
import com.osi.uconectdriver.Util.Session;
import com.osi.uconectdriver.Util.Util;
import com.osi.uconectdriver.dialogs.ProgressDialogView;

import org.json.JSONException;
import org.json.JSONObject;

public class Ratings extends ParentActivity implements View.OnClickListener {
    private Button submit;
    private EditText comment;
    private ImageView rate;
    private TextView problem;
    private TextView experience;
    private TextView important,amount,text;
    private TextView thankyou;
    private ImageButton star1;
    private ImageButton star2;
    private ImageButton star3;
    private ImageButton star4;
    private ImageButton star5;
    private ImageView ic_back;
    private TextView header;
    private int rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        star1 = (ImageButton) findViewById(R.id.Star1);
        star2 = (ImageButton) findViewById(R.id.Star2);
        star3 = (ImageButton) findViewById(R.id.Star3);
        star4 = (ImageButton) findViewById(R.id.Star4);
        star5 = (ImageButton) findViewById(R.id.Star5);
        rate = (ImageView) findViewById(R.id.rate);
        submit = (Button) findViewById(R.id.submit);
        comment=(EditText) findViewById(R.id.comment);
        problem = (TextView) findViewById(R.id.problem);
        amount = (TextView) findViewById(R.id.amount);
        text = (TextView) findViewById(R.id.text);
        amount.setText(getIntent().getExtras().getString("rideTotalAmt")+" "+getIntent().getExtras().getString("currency"));
        experience=(TextView) findViewById(R.id.experience);
        important=(TextView) findViewById(R.id.important);
        thankyou=(TextView) findViewById(R.id.thankyou);
        ic_back=(ImageView) findViewById(R.id.ic_back);
        ic_back.setOnClickListener(this);
        header=(TextView) findViewById(R.id.headername);
        header.setText("RATINGS");
        star1.setOnClickListener(this);
        star2.setOnClickListener(this);
        star3.setOnClickListener(this);
        star4.setOnClickListener(this);
        star5.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        experience.setVisibility(View.GONE);
        important.setVisibility(View.GONE);
        thankyou.setVisibility(View.GONE);
        amount.setVisibility(View.GONE);
        text.setVisibility(View.GONE);
        submit.setVisibility(View.VISIBLE);
        if (v == star1) {
            star1.setBackgroundResource(R.drawable.stars1);
            star2.setBackgroundResource(R.drawable.stars2);
            star3.setBackgroundResource(R.drawable.stars2);
            star4.setBackgroundResource(R.drawable.stars2);
            star5.setBackgroundResource(R.drawable.stars2);
            rate.setImageResource(R.drawable.star1);
            problem.setVisibility(View.VISIBLE);
            comment.setVisibility(View.VISIBLE);
            rating=1;
        } else if (v == star2) {
            star1.setBackgroundResource(R.drawable.stars1);
            star2.setBackgroundResource(R.drawable.stars1);
            star3.setBackgroundResource(R.drawable.stars2);
            star4.setBackgroundResource(R.drawable.stars2);
            star5.setBackgroundResource(R.drawable.stars2);
            rate.setImageResource(R.drawable.star2);
            problem.setVisibility(View.VISIBLE);
            comment.setVisibility(View.VISIBLE);
            rating=2;
        } else if (v == star3) {
            star1.setBackgroundResource(R.drawable.stars1);
            star2.setBackgroundResource(R.drawable.stars1);
            star3.setBackgroundResource(R.drawable.stars1);
            star4.setBackgroundResource(R.drawable.stars2);
            star5.setBackgroundResource(R.drawable.stars2);
            rate.setImageResource(R.drawable.star3);
            problem.setVisibility(View.VISIBLE);
            comment.setVisibility(View.VISIBLE);
            rating=3;
        } else if (v == star4) {
            star1.setBackgroundResource(R.drawable.stars1);
            star2.setBackgroundResource(R.drawable.stars1);
            star3.setBackgroundResource(R.drawable.stars1);
            star4.setBackgroundResource(R.drawable.stars1);
            star5.setBackgroundResource(R.drawable.stars2);
            rate.setImageResource(R.drawable.star4);
            problem.setVisibility(View.GONE);
            comment.setVisibility(View.GONE);
            rating=4;
        } else if (v == star5) {
            star1.setBackgroundResource(R.drawable.stars1);
            star2.setBackgroundResource(R.drawable.stars1);
            star3.setBackgroundResource(R.drawable.stars1);
            star4.setBackgroundResource(R.drawable.stars1);
            star5.setBackgroundResource(R.drawable.stars1);
            rate.setImageResource(R.drawable.star5);
            problem.setVisibility(View.GONE);
            comment.setVisibility(View.GONE);
            rating=5;
        } else if (v == submit) {
            rate();
        }

    }

    private void rate() {
        try {
            JSONObject jsonObject_main = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject_main = getCommontHeaderParams();
            jsonObject.put("driverId", Session.getUserID(Ratings.this));
            jsonObject.put("bookingId", Session.getBookingId(Ratings.this));
            jsonObject.put("custRating", rating);
            jsonObject.put("feedback", comment.getText().toString().trim());
            jsonObject_main.put("body", jsonObject);
            CallAPIRATINGS(jsonObject_main);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void CallAPIRATINGS(JSONObject params) {
        if (Util.isNetworkConnected(Ratings.this)) {
            try {
                new CallAPI(DRIVERRATING, "DRIVERRATING", params, Ratings.this, GetRating_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(Ratings.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetRating_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {

                        Intent intent = new Intent(Ratings.this, LandingPageActivity.class);
                        intent.putExtra("key", "0");
                        Session.setBookingId(Ratings.this,"");
                        startActivity(intent);
                    finish();

                }
            }else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPIRATINGS(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(Ratings.this)) {
                        CallSessionID(GetRating_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        Util.ShowToast(Ratings.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    Util.ShowToast(Ratings.this, msg.getData().getString("msg"));
                }

            }
    };
}
