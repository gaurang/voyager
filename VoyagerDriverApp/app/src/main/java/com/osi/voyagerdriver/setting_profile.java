package com.osi.voyagerdriver;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by Brij on 05-03-2016.
 */
public class setting_profile extends Fragment {
        private Button change;
        private Button change1;
        private LinearLayout layout1;
        private LinearLayout layout2;
        private ViewPager viewPager;
        private Button logout;
        private EditText oldpassword;
        private EditText newpassword;
        private EditText confirmpassword;


        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.profile, container, false);
            viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
            return rootView;
        }
//        private void changePassword() {
//            if (oldpassword.getText().toString().length() == 0) {
//                Util.ShowToast(setting_profile.this, "Enter old password");
//            } else if (newpassword.getText().toString().length() == 0) {
//                Util.ShowToast(setting_profile.this, "Enter new password");
//            } else if (confirmpassword.getText().toString().length() == 0) {
//                Util.ShowToast(setting_profile.this, "Enter confirm Password");
//            } else if (!confirmpassword.getText().toString().equals(newpassword.getText().toString())) {
//                Util.ShowToast(setting_profile.this, "Password mismatch");
//            } else {
//                try {
//                    JSONObject jsonObject_main = new JSONObject();
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject_main = getCommontHeaderParams();
//
//                    jsonObject.put("customerId", Session.getUserID(setting_profile.this));
//                    jsonObject.put("oldPassword", oldpassword.getText().toString().trim());
//                    jsonObject.put("newPassword", newpassword.getText().toString().trim());
//                    jsonObject_main.put("body", jsonObject);
//                    CallAPI(jsonObject_main);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        public void CallAPI(JSONObject params) {
//            if (Util.isNetworkConnected(setting_profile.this)) {
//                try {
//
//                    new CallAPI(CHANGEPASSWORDS, "CHANGEPASSWORDS", params, setting_profile.this, GetDetails_Handler, true);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Util.ShowToast(setting_profile.this, getString(R.string.nointernetmessage));
//            }
//        }
//
//        Handler GetDetails_Handler = new Handler() {
//            public void handleMessage(Message msg) {
//
//                PrintMessage("Handler " + msg.getData().toString());
//                if (msg.getData().getBoolean("flag")) {
//                    if (msg.getData().getInt("code") == SUCCESS) {
//                        Util.ShowToast(setting_profile.this, "Password change successfully");
//                        finish();
//
//                    } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
//                        ParseSessionDetails(msg.getData().getString("responce"));
//                        try {
//                            CallAPI(new JSONObject(msg.getData()
//                                    .getString("mExtraParam")));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
//                        if (Util.isNetworkConnected(setting_profile.this)) {
//                            CallSessionID(GetDetails_Handler, msg.getData()
//                                    .getString("mExtraParam"));
//                        } else {
//                            Util.ShowToast(setting_profile.this, getString(R.string.nointernetmessage));
//                        }
//                    } else {
//                        Util.ShowToast(setting_profile.this, msg.getData().getString("msg"));
//                    }
//                } else {
//                    Util.ShowToast(setting_profile.this, msg.getData().getString("msg"));
//                }
//            }
//        };
    }