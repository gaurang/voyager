package com.app.voyager.tabfragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.voyager.AsyncTask.CallAPI;
import com.app.voyager.Dataset.EmergencyContactData;
import com.app.voyager.ParentActivity;
import com.app.voyager.R;
import com.app.voyager.RecyclerviewAdapter.EmergencycontactAdapter;
import com.app.voyager.Util.Session;
import com.app.voyager.Util.Util;
import com.app.voyager.dialogs.ProgressDialogView;
import com.app.voyager.interfaces.AsyncInterface;
import com.app.voyager.interfaces.myUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by atul on 26/4/16.
 */
public class EmergencyContactFragment extends Fragment implements View.OnClickListener,myUrls,AsyncInterface {

    Button addcontact;
    String phone;
    String name;
    int CONTACT = 1;
    ProgressDialogView progressdialog;
    LinearLayout messagelayout;
    EmergencycontactAdapter emergencycontactAdapter;
    RecyclerView mrecycler_score;
    ArrayList<EmergencyContactData> DataList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_emergency_contacts,container,false);
        initComponent(view);
        try {
            JSONObject jsonObject_main = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject_main = ((ParentActivity)getActivity()).getCommontHeaderParams();
            jsonObject.put("customerId", Session.getUserID(getActivity()));
            jsonObject_main.put("body", jsonObject);
            CallGETEEMERGENCY(jsonObject_main);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    private void initComponent(View view) {
        addcontact = (Button)view.findViewById(R.id.addcontact);
        addcontact.setOnClickListener(this);
        messagelayout = (LinearLayout)view.findViewById(R.id.messagelayout);
        mrecycler_score = (RecyclerView)view.findViewById(R.id.recyclerview_list);
        mrecycler_score.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mrecycler_score.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addcontact:
                if (DataList.size() < 2) {
                    final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
                    Intent intentPickContact = new Intent(Intent.ACTION_PICK, uriContact);
                    startActivityForResult(intentPickContact, CONTACT);
                } else {
                    Util.ShowToast(getActivity(), "You can add Maximum 2 conatact");
                }
                break;
        }
    }
    public void CallGETEEMERGENCY(JSONObject params) {
        if (Util.isNetworkConnected(getActivity())) {
            try {
                progressdialog = new ProgressDialogView(getActivity(), "Please wait..");
                progressdialog.show();
                new CallAPI(GETEMERGENCYCONTACT, "GETEMERGENCYCONTACT", params, getActivity(), GetPlaces_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Util.ShowToast(getActivity(), getString(R.string.nointernetmessage));
        }
    }

    Handler GetPlaces_Handler = new Handler() {
        public void handleMessage(Message msg) {
            progressdialog.dismiss();
//            PrintMessage("Handler " + msg.getData().toString());
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
                            if(Session.getEcontact(getContext()).isEmpty())
                            {
                                Session.setEcontact(getContext(), jsonObject.getString("eContactNumber"));
                            }
                            else
                            {
                                Session.setEcontact1(getContext(), jsonObject.getString("eContactNumber"));
                            }
                            DataList.add(emergencyContactData);
                        }

                        if (DataList.size() > 0) {
                            messagelayout.setVisibility(View.GONE);
                            emergencycontactAdapter = new EmergencycontactAdapter(DataList,getActivity(),EmergencyContactFragment.this);
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
                    ((ParentActivity)getActivity()).ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallGETEEMERGENCY(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(getActivity())) {
                        ((ParentActivity)getActivity()).CallSessionID(GetPlaces_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        Util.ShowToast(getActivity(), getString(R.string.nointernetmessage));
                    }
                } else {
                    Util.ShowToast(getActivity(), msg.getData().getString("msg"));
                }
            } else {
                Util.ShowToast(getActivity(), msg.getData().getString("msg"));

            }
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == CONTACT) {
                Uri returnUri = data.getData();
                Cursor cursor = getActivity().getContentResolver().query(returnUri, null, null, null, null);

                if (cursor.moveToNext()) {
                    int columnIndex_ID = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                    String contactID = cursor.getString(columnIndex_ID);

                    int columnIndex_HASPHONENUMBER = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                    String stringHasPhoneNumber = cursor.getString(columnIndex_HASPHONENUMBER);

                    if (stringHasPhoneNumber.equalsIgnoreCase("1")) {
                        Cursor cursorNum = getActivity().getContentResolver().query(
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
                                jsonObject_main = ((ParentActivity)getActivity()).getCommontHeaderParams();
                                jsonObject.put("eContactNumber", phone.replace("-", "").replace(" ", ""));
                                jsonObject.put("favName", name);
                                jsonObject.put("trackStatus", 0);
                                jsonObject.put("eContactName", name);

                                jsonObject.put("customerId", Session.getUserID(getActivity()));
                                jsonObject_main.put("body", jsonObject);
                                CallADDEMERGENCYAPI(jsonObject_main);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Util.ShowToast(getActivity(), "Already added");
                        }
                    }


                } else {
                    Toast.makeText(getActivity(), "NO data!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    public void CallADDEMERGENCYAPI(JSONObject params) {
        if (Util.isNetworkConnected(getActivity())) {
            try {

                new CallAPI(ADDEMERGENCYCONTACT, "ADDEMERGENCYCONTACT", params, getActivity(), GetResend_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(getActivity(), getString(R.string.nointernetmessage));
        }
    }
    Handler GetResend_Handler = new Handler() {
        public void handleMessage(Message msg) {
            progressdialog.dismiss();
//            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {

                    Util.ShowToast(getActivity(), msg.getData().getString("msg"));
                    try {
                        JSONObject jsonObject_main = new JSONObject();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject_main = ((ParentActivity)getActivity()).getCommontHeaderParams();
                        jsonObject.put("customerId", Session.getUserID(getActivity()));
                        jsonObject_main.put("body", jsonObject);
                        CallGETEEMERGENCY(jsonObject_main);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ((ParentActivity)getActivity()).ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallADDEMERGENCYAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(getActivity())) {
                        ((ParentActivity)getActivity()).CallSessionID(GetResend_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        Util.ShowToast(getActivity(), getString(R.string.nointernetmessage));
                    }
                } else {
                    Util.ShowToast(getActivity(), msg.getData().getString("msg"));
                }
            } else {
                Util.ShowToast(getActivity(), msg.getData().getString("msg"));

            }
        }
    };

}
