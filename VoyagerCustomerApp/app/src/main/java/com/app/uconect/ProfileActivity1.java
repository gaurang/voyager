package com.app.uconect;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.uconect.AsyncTask.CallAPI;
import com.app.uconect.Dataset.EmergencyContactData;
import com.app.uconect.RecyclerviewAdapter.EmergencycontactAdapter;
import com.app.uconect.Util.Methods;
import com.app.uconect.Util.NetworkHelper;
import com.app.uconect.Util.Session;
import com.app.uconect.Util.Util;
import com.app.uconect.adapter.PagerAdapter1;
import com.app.uconect.dialogs.ProgressDialogView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Brij on 05-03-2016.
 */
public class ProfileActivity1 extends ParentActivity implements View.OnClickListener{
    private Button emergency,change,profile,change1,back,addcontact;
    private EditText fname,lname,email,oldpassword,newpassword,confirmpassword,mobile;
    private ImageView ic_back;
    private LinearLayout layout1,layout2,messagelayout;
    private ViewPager viewPager;
    private TextView header,edit,update;
    EmergencycontactAdapter emergencycontactAdapter;
    int CONTACT = 1;
    private static final String GETDRIVERDETAILS1 = "http://192.168.1.114:8080/uc/api/cse/getDriverDetails";
    private static final String UPDATEDETAILS = "http://192.168.1.114:8080/uc/api/cse/updateDriver";
    private static final String ADDEMERGENCYCONTACT1 = "http://192.168.1.106:8080/uc/api/cse/addDEContact";
    private static final String GETEMERGENCYCONTACT1= "http://192.168.1.106:8080/uc/api/cse/getDEContact";
    RecyclerView mrecycler_score;
    private JSONObject params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_1);
        progressdialog = new ProgressDialogView(ProfileActivity1.this, "Please wait..");
        progressdialog.show();
        viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter1 adapter = new PagerAdapter1
                (getSupportFragmentManager(), 2);
        viewPager.setCurrentItem(0);
        header=(TextView)findViewById(R.id.headername);
        header.setText("SETTINGS");
        viewPager.setAdapter(adapter);
        profile=(Button)findViewById(R.id.profile);
        emergency=(Button)findViewById(R.id.emergency);
        messagelayout = (LinearLayout) findViewById(R.id.messagelayout);
        ic_back=(ImageView)findViewById(R.id.ic_back);
        ic_back.setOnClickListener(this);
        profile.setOnClickListener(this);
        emergency.setOnClickListener(this);
            if(NetworkHelper.isOnline(this)){
                getDetail();
            }else{
                Methods.toastShort("Please check your internet connection..",this);
            }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profile:
                viewPager.setCurrentItem(0);
                profile.setBackgroundResource(R.drawable.yellow_shape);
                emergency.setBackgroundResource(R.drawable.whiteshape);
                getProfile();
                break;
            case R.id.emergency:
                viewPager.setCurrentItem(1);
                emergency.setBackgroundResource(R.drawable.yellow_shape);
                profile.setBackgroundResource(R.drawable.whiteshape);
                getemergency();
                break;
            case R.id.ic_back:
                Intent intent=new Intent(ProfileActivity1.this,SettingActivity.class);
                startActivity(intent);
                break;
        }
    }



    private void getProfile() {
        change = (Button) findViewById(R.id.changepassword);
        change1 = (Button) findViewById(R.id.changepassword1);
        update=(Button)findViewById(R.id.update);
        oldpassword = (EditText) findViewById(R.id.oldpasswpord);
        newpassword = (EditText) findViewById(R.id.newpassword);
        confirmpassword = (EditText) findViewById(R.id.confirmpassword);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        back=(Button) findViewById(R.id.back);
        edit=(TextView) findViewById(R.id.edit);
        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        email = (EditText) findViewById(R.id.email);
        mobile=(EditText)findViewById(R.id.mobile);
        edit.setVisibility(View.VISIBLE);
        change.setVisibility(View.VISIBLE);
        update.setVisibility(View.GONE);
        change.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);
            }
        });
        change1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(NetworkHelper.isOnline(ProfileActivity1.this)){
                    progressdialog = new ProgressDialogView(ProfileActivity1.this, "Please wait..");
                    progressdialog.show();
                    changePassword();
                }else {
                    Methods.toastShort("Please check your internet connection..",ProfileActivity1.this);

                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                layout2.setVisibility(View.GONE);
                layout1.setVisibility(View.VISIBLE);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile.setEnabled(true);
                fname.setEnabled(true);
                lname.setEnabled(true);
                edit.setVisibility(View.GONE);
                update.setVisibility(View.VISIBLE);
                change.setVisibility(View.GONE);
                update.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(NetworkHelper.isOnline(ProfileActivity1.this)){
                            progressdialog = new ProgressDialogView(ProfileActivity1.this, "Please wait..");
                            progressdialog.show();
                            getUpdate();
                        }else {
                            Methods.toastShort("Please check your internet connection..",ProfileActivity1.this);

                        }

                    }
                });
            }
        });
    }

    private void changePassword() {
        if (oldpassword.getText().toString().length() == 0) {
            Util.ShowToast(ProfileActivity1.this, "Enter old password");
        } else if (newpassword.getText().toString().length() == 0) {
            Util.ShowToast(ProfileActivity1.this, "Enter new password");
        } else if (confirmpassword.getText().toString().length() == 0) {
            Util.ShowToast(ProfileActivity1.this, "Enter confirm Password");
        } else if (oldpassword.getText().toString().equals(newpassword.getText().toString())) {
            Util.ShowToast(ProfileActivity1.this, "Enter different password");
        }else if (!confirmpassword.getText().toString().equals(newpassword.getText().toString())) {
            Util.ShowToast(ProfileActivity1.this, "Password mismatch");
        }else {
            try {
                JSONObject jsonObject_main = new JSONObject();
                JSONObject jsonObject = new JSONObject();
                jsonObject_main = getCommontHeaderParams();
                jsonObject.put("customerId", Session.getUserID(ProfileActivity1.this));
                jsonObject.put("oldpassword", oldpassword.getText().toString().trim());
                jsonObject.put("newpasswword", newpassword.getText().toString().trim());
                jsonObject_main.put("body", jsonObject);
                CallAPI(jsonObject_main);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void CallAPI(JSONObject params) {
        if (Util.isNetworkConnected(ProfileActivity1.this)) {
            try {
                new CallAPI(CHANGEPASSWORDS, "CHANGEPASSWORD", params, ProfileActivity1.this, Change_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(ProfileActivity1.this, getString(R.string.nointernetmessage));
        }
    }

    Handler Change_Handler = new Handler() {
        public void handleMessage(Message msg) {
            progressdialog.dismiss();
            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    Util.ShowToast(ProfileActivity1.this, "Password change successfully");
                    layout2.setVisibility(View.GONE);
                    layout1.setVisibility(View.VISIBLE);
                    finish();

                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(ProfileActivity1.this)) {
                        CallSessionID(Change_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        Util.ShowToast(ProfileActivity1.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    Util.ShowToast(ProfileActivity1.this, msg.getData().getString("msg"));
                }
            } else {
                Util.ShowToast(ProfileActivity1.this, msg.getData().getString("msg"));
            }
        }
    };
    private void getemergency() {
        addcontact = (Button) findViewById(R.id.addcontact);
        messagelayout = (LinearLayout) findViewById(R.id.messagelayout);
        mrecycler_score = (RecyclerView) findViewById(R.id.recyclerview_list);
        mrecycler_score.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                ProfileActivity1.this);
        mrecycler_score.setLayoutManager(mLayoutManager);
        SetOnclicklistener();
        addcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DataList.size() < 2) {
                    final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
                    Intent intentPickContact = new Intent(Intent.ACTION_PICK, uriContact);
                    startActivityForResult(intentPickContact, CONTACT);
                } else {
                    Util.ShowToast(ProfileActivity1.this, "You can add Maximum 2 conatact");
                }
            }
        });
    }
    @Override
    public void SetOnclicklistener() {
        ic_back.setOnClickListener(this);
        addcontact.setOnClickListener(this);
        try {
            JSONObject jsonObject_main = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject_main = getCommontHeaderParams();
            jsonObject.put("customerId", Session.getUserID(ProfileActivity1.this));
            jsonObject_main.put("body", jsonObject);
            CallGETEEMERGENCY(jsonObject_main);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    String phone;
    String name;

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        // TODO Auto-generated method stub
        if (resultCode == RESULT_OK) {
            if (requestCode == CONTACT) {
                Uri returnUri = data.getData();
                Cursor cursor = getContentResolver().query(returnUri, null, null, null, null);

                if (cursor.moveToNext()) {
                    int columnIndex_ID = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                    String contactID = cursor.getString(columnIndex_ID);

                    int columnIndex_HASPHONENUMBER = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                    String stringHasPhoneNumber = cursor.getString(columnIndex_HASPHONENUMBER);

                    if (stringHasPhoneNumber.equalsIgnoreCase("1")) {
                        Cursor cursorNum = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactID,
                                null,
                                null);

                        //Get the first phone number
                        if (cursorNum.moveToNext()) {
                            int columnIndex_number = cursorNum.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            int columnIndex_name = cursorNum.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                            phone = cursorNum.getString(columnIndex_number);
                            name = cursorNum.getString(columnIndex_name);

                        }

                        boolean isfound = false;
                        for (int i = 0; i < DataList.size(); i++) {
                            if (DataList.get(i).eContactNumber.contains(phone.replace("-", "").replace(" ", ""))) {
                                isfound = true;
                                break;
                            }
                        }
                        Log.d("Conatct ", phone + "  \n  " + name + "   " + isfound);
                        if (!isfound) {
                            try {
                                JSONObject jsonObject_main = new JSONObject();
                                JSONObject jsonObject = new JSONObject();
                                jsonObject_main = getCommontHeaderParams();
                                jsonObject.put("eContactNumber", phone.replace("-", "").replace(" ", ""));
                                jsonObject.put("favName", name);
                                jsonObject.put("trackStatus", 0);
                                jsonObject.put("eContactName", name);

                                jsonObject.put("customerId", Session.getUserID(ProfileActivity1.this));
                                jsonObject_main.put("body", jsonObject);
                                CallADDEMERGENCYAPI(jsonObject_main);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Util.ShowToast(ProfileActivity1.this, "Already added");
                        }
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "NO data!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    public void CallADDEMERGENCYAPI(JSONObject params) {
        if (Util.isNetworkConnected(ProfileActivity1.this)) {
            try {

                new CallAPI(ADDEMERGENCYCONTACT, "ADDEMERGENCYCONTACT", params, ProfileActivity1.this, GetResend_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(ProfileActivity1.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetResend_Handler = new Handler() {
        public void handleMessage(Message msg) {
            progressdialog.dismiss();
            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {

                    Util.ShowToast(ProfileActivity1.this, msg.getData().getString("msg"));
                    try {
                        JSONObject jsonObject_main = new JSONObject();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject_main = getCommontHeaderParams();
                        jsonObject.put("customerId", Session.getUserID(ProfileActivity1.this));
                        jsonObject_main.put("body", jsonObject);
                        CallGETEEMERGENCY(jsonObject_main);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallADDEMERGENCYAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(ProfileActivity1.this)) {
                        CallSessionID(GetResend_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        Util.ShowToast(ProfileActivity1.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    Util.ShowToast(ProfileActivity1.this, msg.getData().getString("msg"));
                }
            } else {
                Util.ShowToast(ProfileActivity1.this, msg.getData().getString("msg"));

            }
        }
    };

    public void CallGETEEMERGENCY(JSONObject params) {
        if (Util.isNetworkConnected(ProfileActivity1.this)) {
            try {
                progressdialog = new ProgressDialogView(ProfileActivity1.this, "Please wait..");
                progressdialog.show();
                new CallAPI(GETEMERGENCYCONTACT, "GETEMERGENCYCONTACT", params, ProfileActivity1.this, GetPlaces_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Util.ShowToast(ProfileActivity1.this, getString(R.string.nointernetmessage));
        }
    }

    ArrayList<EmergencyContactData> DataList = new ArrayList<>();

    Handler GetPlaces_Handler = new Handler() {
        public void handleMessage(Message msg) {
                progressdialog.dismiss();
            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    DataList = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(msg.getData().getString("responce"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            EmergencyContactData emergencyContactData = new EmergencyContactData();
                            emergencyContactData.contactId = jsonObject.getInt("contactId");
                            emergencyContactData.trackStatus = jsonObject.getInt("trackStatus");
                            emergencyContactData.customerId = jsonObject.getString("customerId");
                            emergencyContactData.eContactName = jsonObject.getString("eContactName");
                            emergencyContactData.eContactNumber = jsonObject.getString("eContactNumber");
                            DataList.add(emergencyContactData);
                        }

                        if (DataList.size() > 0) {
                            messagelayout.setVisibility(View.GONE);
                            //emergencycontactAdapter = new EmergencycontactAdapter(DataList, ProfileActivity1.this);
                            mrecycler_score.setAdapter(emergencycontactAdapter);
                        } else {
                            messagelayout.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        messagelayout.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }
                    //   Util.ShowToast(AddFavorPlacesActivity.this, msg.getData().getString("responce"));

                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallGETEEMERGENCY(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(ProfileActivity1.this)) {
                        CallSessionID(GetPlaces_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        Util.ShowToast(ProfileActivity1.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    Util.ShowToast(ProfileActivity1.this, msg.getData().getString("msg"));
                }
            } else {
                Util.ShowToast(ProfileActivity1.this, msg.getData().getString("msg"));

            }
        }
    };
    private void getDetail() {
        try {
            JSONObject jsonObject_main = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject_main = getCommontHeaderParams();
            jsonObject.put("customerId", Session.getUserID(ProfileActivity1.this));
            jsonObject_main.put("body", jsonObject);

            CallAPI1(jsonObject_main);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void CallAPI1(JSONObject params) {
        if (Util.isNetworkConnected(ProfileActivity1.this)) {
            try {

                new CallAPI(UPDATEPROFILE, "UPDATEDETAILS", params, ProfileActivity1.this, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(ProfileActivity1.this, getString(R.string.nointernetmessage));
        }
    }
    Handler GetDetails_Handler = new Handler() {
        public void handleMessage(Message msg) {
            progressdialog.dismiss();
                    if(viewPager.getCurrentItem()==0){
                        getProfile();
                    }else{
                        getemergency();
                    }
            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    // Session.setAllInfo(ProfileActivity1.this,msg.getData().getString("responce"));
                    //Intent intent = new Intent(ProfileActivity1.this, LandingPageActivity.class);
                    try {
                        fname = (EditText) findViewById(R.id.fname);
                        lname = (EditText) findViewById(R.id.lname);
                        email = (EditText) findViewById(R.id.email);
                        mobile=(EditText) findViewById(R.id.mobile);
                        mobile.setEnabled(false);
                        fname.setEnabled(false);
                        lname.setEnabled(false);
                        email.setEnabled(false);
                        JSONObject jsonObject = new JSONObject(msg.getData().getString("responce"));
                        fname.setText(jsonObject.getString("fname"));
                        lname.setText(jsonObject.getString("lname"));
                        email.setText(jsonObject.getString("email"));
                        mobile.setText(jsonObject.getString("mobile"));
//                        intent.putExtra("mobile", jsonObject.getString("mobile"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    //startActivity(intent);
                    //    finish();
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(ProfileActivity1.this)) {
                        CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        Util.ShowToast(ProfileActivity1.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    Util.ShowToast(ProfileActivity1.this, msg.getData().getString("msg"));
                }
            } else {
                Util.ShowToast(ProfileActivity1.this, msg.getData().getString("msg"));
            }
        }
    };
    private void getUpdate() {
        mobile = (EditText) findViewById(R.id.mobile);
        if (mobile.getText().toString().trim().length() == 0) {
            Util.ShowToast(ProfileActivity1.this, "Enter Mobile Number");
        } else {
            try {
                JSONObject jsonObject_main = new JSONObject();
                JSONObject jsonObject = new JSONObject();
                jsonObject_main = getCommontHeaderParams();
                jsonObject.put("customerId", Session.getUserID(ProfileActivity1.this));
                jsonObject.put("mobile", mobile.getText().toString().trim());
                jsonObject_main.put("body", jsonObject);
                CallAPI2(jsonObject_main);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void CallAPI2(JSONObject params) {
        if (Util.isNetworkConnected(ProfileActivity1.this)) {
            try {
                new CallAPI(UPDATEPROFILE, "UPDATEDETAILS", params, ProfileActivity1.this, Update_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(ProfileActivity1.this, getString(R.string.nointernetmessage));
        }
    }

    Handler Update_Handler = new Handler() {
        public void handleMessage(Message msg) {
            progressdialog.dismiss();
            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    Util.ShowToast(ProfileActivity1.this, "Mobile number changed successfully");
                    update.setVisibility(View.GONE);
                    getDetail();
                    change.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.VISIBLE);

                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(ProfileActivity1.this)) {
                        CallSessionID(Update_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        Util.ShowToast(ProfileActivity1.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    Util.ShowToast(ProfileActivity1.this, msg.getData().getString("msg"));
                }
            } else {
                Util.ShowToast(ProfileActivity1.this, msg.getData().getString("msg"));
            }
        }
    };
}
