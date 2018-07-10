package com.osi.voyagerdriver;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.osi.voyagerdriver.adapter.PagerAdapter2;


/**
 * Created by Brij on 05-03-2016.
 */
public class EarningActivity extends AppCompatActivity implements View.OnClickListener{
    private Button week;
    private ImageView ic_back;
    private Button all;
    private ViewPager viewPager;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earnings);
        viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter2 adapter = new PagerAdapter2
                (getSupportFragmentManager(), 2);
        viewPager.setAdapter(adapter);
        week=(Button)findViewById(R.id.week);
        all=(Button)findViewById(R.id.all);
        ic_back=(ImageView) findViewById(R.id.ic_back);
        ic_back.setOnClickListener(this);
        week.setOnClickListener(this);
        all.setOnClickListener(this);
        textView=(TextView)findViewById(R.id.headername);
        textView.setText("EARNINGS");
    }

    @Override
    public void onClick(View v) {
        if(v == week)
        {
            viewPager.setCurrentItem(0);
            week.setBackgroundResource(R.drawable.yellow_shape);
            all.setBackgroundResource(R.drawable.whiteshape);
        }
        if(v == all)
        {
            viewPager.setCurrentItem(1);
            all.setBackgroundResource(R.drawable.yellow_shape);
            week.setBackgroundResource(R.drawable.whiteshape);
        }else if(v == ic_back)
        {
            finish();
        }
    }
}