package com.app.voyager.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.voyager.Dataset.CarSubServiceData;
import com.app.voyager.LandingPageActivity;
import com.app.voyager.R;
import com.app.voyager.Util.Session;
import com.app.voyager.slidedatetimepicker.SlideDateTimeListener;
import com.app.voyager.slidedatetimepicker.SlideDateTimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class LandingStepRide1 extends MainFragment implements View.OnClickListener {
    LandingPageActivity parentactivity;
    TextView book_now;
    LinearLayout cartypemainlayoput;
    //    ArrayList<CarServiceData> carServiceTaxi = new ArrayList<>();
//    ArrayList<CarServiceData> carServicePrivate = new ArrayList<>();
//    String TAXI = "{ \"TAXI\":[ {\"id\":\"Txi\", \"name\":\"Taxi\", \"minduration\":\"1\" },{\"id\":\"mtx\", \"name\":\"Maxi Taxi\", \"minduration\":\"5\" } ] }";
//    String PRIVATE = "{ \"PRIVATE\":[ {\"id\":\"SED\", \"name\":\"Sedan\", \"minduration\":\"1\" },{\"id\":\"SUV\", \"name\":\"SUV\", \"minduration\":\"7\" } ] }";
    Button btn_taxi[];


    @Override
    public void onCreate(Bundle savedInstanceState) {
        parentactivity = (LandingPageActivity) getActivity();
        Log.d("getdata", Session.getAllInfo(parentactivity));
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentactivity.DisableMapFunctionality(true);
        View view = inflater.inflate(R.layout.fragment_landingpage1, container,
                false);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //view.setOnClickListener(this);

        book_now = (TextView) view.findViewById(R.id.book_now);
        LinearLayout service_maintype = (LinearLayout) view.findViewById(R.id.service_maintype);
        btn_taxi = new Button[parentactivity.GetCarData.size()];
        service_maintype.removeAllViews();
        for (int i = 0; i < parentactivity.GetCarData.size(); i++) {
            View view1 = parentactivity.getLayoutInflater().inflate(R.layout.landingpage1_cservicetype, null);
            btn_taxi[i] = (Button) view1.findViewById(R.id.btn_taxi);
            if (i == 0)
                btn_taxi[0].setBackgroundResource(R.drawable.left_selected_innetsmall);
            else
                btn_taxi[i].setBackgroundResource(R.drawable.left_unselected_innetsmall);
            btn_taxi[i].setText(parentactivity.GetCarData.get(i).attrName);
            parentactivity.boonkingData.vehicleType = parentactivity.GetCarData.get(0).attrValue1;
            parentactivity.boonkingData.vehicleName = parentactivity.GetCarData.get(0).attrName;
            btn_taxi[i].setId(i);
            btn_taxi[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    parentactivity.SelectedTab = v.getId();
                    parentactivity.boonkingData.vehicleType = parentactivity.GetCarData.get(parentactivity.SelectedTab).attrValue1;
                    parentactivity.boonkingData.vehicleName = parentactivity.GetCarData.get(parentactivity.SelectedTab).attrName;
                    for (int i = 0; i < parentactivity.GetCarData.size(); i++) {
                        btn_taxi[i].setBackgroundResource(R.drawable.right_unselected_innetsmall);
                    }
                    btn_taxi[parentactivity.SelectedTab].setBackgroundResource(R.drawable.left_selected_innetsmall);
                    addTaxiTye(parentactivity.GetCarData.get(parentactivity.SelectedTab).carSubServiceDatas);
                }
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.weight = 1.0f;
            Button button = new Button(parentactivity);
            view1.setLayoutParams(params);

            service_maintype.addView(view1);
        }

        book_now.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                parentactivity.FragmentManagement(LANDINGPAGESTEP2, REPLACE,
                        null, true, TAG_LANDINGPAGESTEP2);
            }
        });
        TextView book_later = (TextView) view.findViewById(R.id.book_later);
        book_later.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SlideDateTimePicker slideDateTimePicker = new SlideDateTimePicker.Builder(parentactivity.getSupportFragmentManager()).build();
                slideDateTimePicker
                        .setListener(listener);
                slideDateTimePicker.setInitialDate(new Date());
                slideDateTimePicker.setMinDate(new Date());
                //.setMaxDate(maxDate)
                //.setIs24HourTime(true)
                //.setTheme(SlideDateTimePicker.HOLO_DARK)
                //.setIndicatorColor(Color.parseColor("#990000"))

                slideDateTimePicker.show();
            }
        });
        cartypemainlayoput = (LinearLayout) view.findViewById(R.id.cartypemainlayoput);
        addTaxiTye(parentactivity.GetCarData.get(parentactivity.SelectedTab).carSubServiceDatas);
    }

    ImageView car_image[];
    TextView tv_timeduration[];
    Integer currentClicked ;

    public void setTVTimeDurationText(String value){
        tv_timeduration[currentClicked].setText(value);
    }
    public void setBookNow(boolean isEnabled){
         book_now.setClickable(isEnabled);
    }

    public void addTaxiTye(final ArrayList<CarSubServiceData> carService) {
        cartypemainlayoput.removeAllViews();
        car_image = new ImageView[carService.size()];
        tv_timeduration = new TextView[carService.size()];
        for (int i = 0; i < carService.size(); i++) {
            final View view1 = getActivity().getLayoutInflater().inflate(R.layout.cartype_layout, null);
            TextView tv_cartype = (TextView) view1.findViewById(R.id.tv_cartype);
            tv_timeduration[i] = (TextView) view1.findViewById(R.id.tv_timeduration);
            car_image[i] = (ImageView) view1.findViewById(R.id.car_image);
            tv_cartype.setText(carService.get(i).attrName);
            if (i == 0) {
                tv_timeduration[0].setText("");
                parentactivity.CallAPIFetchDataLocation();
                car_image[0].setBackgroundResource(R.drawable.taxi2);
                parentactivity.boonkingData.vehicleType = parentactivity.GetCarData.get(parentactivity.SelectedTab).carSubServiceDatas.get(0).attrValue1;
                Session.setVehicleType(getContext(), parentactivity.boonkingData.vehicleType);
                currentClicked = 0;


            }


            view1.setId(i);
            view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_timeduration[view1.getId()].setText("");
                    parentactivity.CallAPIFetchDataLocation();
                    for (int i = 0; i < carService.size(); i++) {
                        //car_image[i].setBackgroundResource(R.drawable.car_hover);
                        car_image[i].setImageResource(R.drawable.taxi1);
                        tv_timeduration[i].setText("");

                    }
                    currentClicked = view1.getId();
                    parentactivity.boonkingData.vehicleType = parentactivity.GetCarData.get(parentactivity.SelectedTab).carSubServiceDatas.get(view.getId()).attrValue1;
                    Session.setVehicleType(getContext(),parentactivity.boonkingData.vehicleType );
                    //car_image[view.getId()].setBackgroundResource(R.drawable.car_bg);
                    car_image[view.getId()].setImageResource(R.drawable.taxi2);



                }
            });
            cartypemainlayoput.addView(view1);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void getStatusConnection(boolean isConnected) {
    }

    public void PrintMessage(String Error) {
        Log.d("########Call123 ", Error);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        final int animatorId = (enter) ? R.anim.push_up_in
                : R.anim.push_up_out;
        Animation anim = AnimationUtils.loadAnimation(parentactivity,
                animatorId);

        return anim;
    }

    @Override
    public void onClick(View view) {

    }

    private SimpleDateFormat mFormatter = new SimpleDateFormat("MMMM dd yyyy hh:mm aa");


    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
            Toast.makeText(parentactivity,
                    mFormatter.format(date), Toast.LENGTH_SHORT).show();
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel() {
            Toast.makeText(parentactivity,
                    "Canceled", Toast.LENGTH_SHORT).show();
        }
    };

}
