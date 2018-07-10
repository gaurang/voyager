package com.app.uconect;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.uconect.AsyncTask.CallAPI;
import com.app.uconect.Dataset.EmergencyContactData;
import com.app.uconect.RecyclerviewAdapter.EmergencycontactAdapter;
import com.app.uconect.Util.Session;
import com.app.uconect.Util.Util;
import com.app.uconect.dialogs.ProgressDialogView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shadab.s on 14-01-2016.
 */
public class EmergencyContactActivity extends ParentActivity implements View.OnClickListener {

    TextView headername;
    ImageView ic_back;
    Button addcontact;
    LinearLayout messagelayout;
    EmergencycontactAdapter emergencycontactAdapter;


    RecyclerView mrecycler_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_contact);
        progressdialog = new ProgressDialogView(EmergencyContactActivity.this, "");
        BindView(null, savedInstanceState);
    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {

        super.BindView(view, savedInstanceState);
        headername = (TextView) findViewById(R.id.headername);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        addcontact = (Button) findViewById(R.id.addcontact);
        messagelayout = (LinearLayout) findViewById(R.id.messagelayout);
        headername.setText("EMERGENCY CONTACT");
        mrecycler_score = (RecyclerView) findViewById(R.id.recyclerview_list);
        mrecycler_score.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                EmergencyContactActivity.this);
        mrecycler_score.setLayoutManager(mLayoutManager);

        SetOnclicklistener();

    }


    @Override
    public void SetOnclicklistener() {
        ic_back.setOnClickListener(this);
        addcontact.setOnClickListener(this);
        try {
            JSONObject jsonObject_main = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject_main = getCommontHeaderParams();
            jsonObject.put("customerId", Session.getUserID(EmergencyContactActivity.this));
            jsonObject_main.put("body", jsonObject);
            CallGETEEMERGENCY(jsonObject_main);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    int CONTACT = 1;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                finish();
                break;
            case R.id.addcontact:
//                Intent intent = new Intent(EmergencyContactActivity.this, ContactActivity.class);
//                startActivity(intent);
                if (DataList.size() < 4) {
                    final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
                    Intent intentPickContact = new Intent(Intent.ACTION_PICK, uriContact);
                    startActivityForResult(intentPickContact, CONTACT);
                } else {
                    Util.ShowToast(EmergencyContactActivity.this, "You can add Maximum 4 conatact");
                }
                break;
        }
    }

    String phone;
    String name;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

                                jsonObject.put("customerId", Session.getUserID(EmergencyContactActivity.this));
                                jsonObject_main.put("body", jsonObject);
                                CallADDEMERGENCYAPI(jsonObject_main);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Util.ShowToast(EmergencyContactActivity.this, "Already added");
                        }
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "NO data!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void CallADDEMERGENCYAPI(JSONObject params) {
        if (Util.isNetworkConnected(EmergencyContactActivity.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(ADDEMERGENCYCONTACT, "ADDEMERGENCYCONTACT", params, EmergencyContactActivity.this, GetResend_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(EmergencyContactActivity.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetResend_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {

                    progressdialog.dismissanimation(ProgressDialogView.ERROR);

                    Util.ShowToast(EmergencyContactActivity.this, msg.getData().getString("msg"));
                    try {
                        JSONObject jsonObject_main = new JSONObject();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject_main = getCommontHeaderParams();
                        jsonObject.put("customerId", Session.getUserID(EmergencyContactActivity.this));
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
                    if (Util.isNetworkConnected(EmergencyContactActivity.this)) {
                        CallSessionID(GetResend_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(EmergencyContactActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(EmergencyContactActivity.this, msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(EmergencyContactActivity.this, msg.getData().getString("msg"));

            }
        }
    };

    public void CallGETEEMERGENCY(JSONObject params) {
        if (Util.isNetworkConnected(EmergencyContactActivity.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(GETEMERGENCYCONTACT, "GETEMERGENCYCONTACT", params, EmergencyContactActivity.this, GetPlaces_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(EmergencyContactActivity.this, getString(R.string.nointernetmessage));
        }
    }

    ArrayList<EmergencyContactData> DataList = new ArrayList<>();

    Handler GetPlaces_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    DataList = new ArrayList<>();
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
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
//                            emergencycontactAdapter = new EmergencycontactAdapter(DataList,getAc,EmergencyContactActivity.this);
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
                    if (Util.isNetworkConnected(EmergencyContactActivity.this)) {
                        CallSessionID(GetPlaces_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(EmergencyContactActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(EmergencyContactActivity.this, msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(EmergencyContactActivity.this, msg.getData().getString("msg"));

            }
        }
    };
}
