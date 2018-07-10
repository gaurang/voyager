package com.app.voyager.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.voyager.Dataset.TerrifPlans;
import com.app.voyager.LandingPageActivity;
import com.app.voyager.R;

import java.util.ArrayList;

/**
 * Created by osigroups on 1/14/2016.
 */
public class RateCardDialog extends Dialog implements DialogInterface.OnClickListener {
    LandingPageActivity landingPageActivity;
    ArrayList<TerrifPlans> terrifPlanses;
    TextView vehicalType;
    TextView tarrif_name, tarrif_bookingfee, tarrif_baserate, tarrif_distancerate, tarrif_waittime, tv_cancel;
    LinearLayout trafiaddedcontainer;
    String vehicalTypetxt = "";

    public RateCardDialog(LandingPageActivity context, ArrayList<TerrifPlans> terrifPlanses, String vehicalTypetxt) {
        super(context, R.style.Progressdialogthem);
        landingPageActivity = context;
        this.terrifPlanses = terrifPlanses;
        this.terrifPlanses = terrifPlanses;
        setContentView(R.layout.dialog_ratecard);
        trafiaddedcontainer = (LinearLayout) findViewById(R.id.trafiaddedcontainer);
        trafiaddedcontainer.removeAllViews();
        vehicalType = (TextView) findViewById(R.id.vehicalType);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        vehicalType.setText(vehicalTypetxt);
        Log.d("terrifPlanses ", terrifPlanses.size() + "");
        for (int i = 0; i < terrifPlanses.size(); i++) {
            View view = context.getLayoutInflater().inflate(R.layout.ratecard_container, null);
            tarrif_name = (TextView) view.findViewById(R.id.tarrif_name);

            tarrif_bookingfee = (TextView) view.findViewById(R.id.tarrif_bookingfee);
            tarrif_baserate = (TextView) view.findViewById(R.id.tarrif_baserate);
            tarrif_distancerate = (TextView) view.findViewById(R.id.tarrif_distancerate);
            tarrif_waittime = (TextView) view.findViewById(R.id.tarrif_waittime);
            tarrif_name.setText(terrifPlanses.get(i).attrName + " (" + terrifPlanses.get(i).attrValue1 + " : " + terrifPlanses.get(i).attrValue2 + " )");
            tarrif_bookingfee.setText("Booking fee\n" + terrifPlanses.get(i).UNIT_BASE_FARE + " " + terrifPlanses.get(i).BASE_FARE);
            tarrif_baserate.setText("Base rate\n" + terrifPlanses.get(i).UNIT_BASE_FARE + " " + terrifPlanses.get(i).BASE_FARE);
            tarrif_distancerate.setText("Distance Rate\n" + terrifPlanses.get(i).UNIT_BASE_KM_RATE + " " + terrifPlanses.get(i).BASE_KM_RATE+"/km");
            tarrif_waittime.setText("Wait Charge\n" + terrifPlanses.get(i).UNIT_BASE_WAIT_CHRG + " " + terrifPlanses.get(i).BASE_WAIT_CHRG+"/min");
            trafiaddedcontainer.addView(view);

            Log.d("values ", terrifPlanses.get(i).BASE_WAIT_TIME + " " + terrifPlanses.get(i).BASE_WAIT_UNIT + " " + terrifPlanses.get(i).BASE_WAIT_CHRG + " " + terrifPlanses.get(i).BASE_FARE + " ");

        }
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
