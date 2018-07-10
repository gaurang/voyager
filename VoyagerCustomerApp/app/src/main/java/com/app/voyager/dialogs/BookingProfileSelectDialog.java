package com.app.voyager.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.app.voyager.Dataset.AddedPaymentData;
import com.app.voyager.Dataset.BoonkingData;
import com.app.voyager.LandingPageActivity;
import com.app.voyager.R;
import com.app.voyager.interfaces.AsyncInterface;
import com.app.voyager.interfaces.myUrls;

import java.util.ArrayList;

/**
 * Created by osigroups on 1/14/2016.
 */
public class BookingProfileSelectDialog extends Dialog implements DialogInterface.OnClickListener, myUrls, AsyncInterface {
    LandingPageActivity parentactivity;
    Button tv_cancel, tv_done;
    RadioButton rdbtn_Buisness, rdbtn_Personal;
    RadioGroup rdbtn_group;
    ArrayList<AddedPaymentData> addedPaymentList;
    BoonkingData boonkingData;

    public BookingProfileSelectDialog(LandingPageActivity context, ArrayList<AddedPaymentData> addedPayment, BoonkingData boonkingData) {
        super(context, R.style.Progressdialogthem);
        parentactivity = context;
        this.addedPaymentList = addedPayment;
        this.boonkingData = boonkingData;
        setContentView(R.layout.dialog_bookingprofileselect);

        tv_cancel = (Button) findViewById(R.id.cancel);
        tv_done = (Button) findViewById(R.id.done);
        rdbtn_Buisness = (RadioButton) findViewById(R.id.rdbtn_Buisness);
        rdbtn_Personal = (RadioButton) findViewById(R.id.rdbtn_Personal);
        rdbtn_Personal.setVisibility(View.GONE);
        rdbtn_Buisness.setVisibility(View.GONE);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        for (int i = 0; i < addedPaymentList.size(); i++) {
            if (addedPaymentList.get(i).accountType.equalsIgnoreCase("C")) {
                if (context.boonkingData.accountType != null && context.boonkingData.accountType.equalsIgnoreCase("C")) {
                    rdbtn_Buisness.setChecked(true);
                }
                rdbtn_Buisness.setVisibility(View.VISIBLE);
            } else if (addedPaymentList.get(i).accountType.equalsIgnoreCase("P")) {
                if (context.boonkingData.accountType != null && context.boonkingData.accountType.equalsIgnoreCase("P")) {
                    rdbtn_Personal.setChecked(true);
                }
                rdbtn_Personal.setVisibility(View.VISIBLE);
            }
        }
        rdbtn_group = (RadioGroup) findViewById(R.id.rdbtn_group);
        rdbtn_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = rdbtn_group.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton radioSexButton = (RadioButton) findViewById(selectedId);


                parentactivity.boonkingData.accountType = (String) radioSexButton.getTag();
            }

        });

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }


}