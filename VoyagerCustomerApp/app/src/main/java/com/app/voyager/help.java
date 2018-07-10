package com.app.voyager;

/**
 * Created by Brij on 06-02-2016.
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.app.voyager.AsyncTask.CallAPI;
import com.app.voyager.Dataset.HelpData;
import com.app.voyager.RecyclerviewAdapter.HelpAdapter;
import com.app.voyager.Util.Util;
import com.app.voyager.dialogs.ProgressDialogView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class help extends ParentActivity implements View.OnClickListener {

    RecyclerView mrecycler_score;
    HelpAdapter helpAdapter;
    ArrayList<HelpData> Historylist = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        progressdialog = new ProgressDialogView(help.this, "");
        BindView(null, savedInstanceState);
        //getData();
    }

    public void BindView(View view, Bundle savedInstanceState) {
        super.BindView(view, savedInstanceState);
        mrecycler_score = (RecyclerView) findViewById(R.id.recycler_view);
        mrecycler_score.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(help.this);
        mrecycler_score.setLayoutManager(mLayoutManager);
        SetOnclicklistener();
    }

    @Override
    public void SetOnclicklistener() {
        super.SetOnclicklistener();
        try {
            JSONObject jsonObject_main = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject_main = getCommontHeaderParams();
            jsonObject.put("customerId", 4);
            jsonObject_main.put("body", jsonObject);
            CallAPI(jsonObject_main);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void CallAPI(JSONObject params) {
        if (Util.isNetworkConnected(help.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(GETHELP, "GETHELP", params, help.this, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(help.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetDetails_Handler = new Handler() {
        public void handleMessage(Message msg) {
            Log.i("aaaaaaaaa", "aaaaa" + msg.getData().getString("responce"));
            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);

                    try {
                        JSONArray jsonArray = new JSONArray(msg.getData().getString("responce"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            HelpData helpData = new HelpData();
                            helpData.supportId = jsonObject.getInt("supportId");
                            helpData.supportType = jsonObject.getString("supportType");
                            helpData.supportQuestion = jsonObject.getString("supportQuestion");
                            helpData.description = jsonObject.getString("description");
                            helpData.supportFor = jsonObject.getString("supportFor");
                            Historylist.add(helpData);
                        }
                        helpAdapter = new HelpAdapter(Historylist, help.this);
                        mrecycler_score.setAdapter(helpAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(help.this)) {
                        CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(help.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(help.this, msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(help.this, msg.getData().getString("msg"));
            }
        }
    };


//    private void getData() {
//        int t = 1;
//        if (t==0) {
//            Toast.makeText(this, "Please enter an id", Toast.LENGTH_LONG).show();
//            return;
//        }
//        loading = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);
//
//        String url = Config.DATA_URL;
//
//        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                loading.dismiss();
//                showJSON(response);
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(help.this, error.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }

//    private void showJSON(String response) {
//
//        try {
//            JSONObject jsonObject = new JSONObject(response);
//            Log.i("SSSS","aaaaaaa"+jsonObject);
//            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
//            String avgData = result.get(0).toString();
//            String quest = null;
//            String desc = null;
//            for (int i = 0; i < result.length(); i++) {
//
//                JSONObject jsonObj = result.getJSONObject(i);
//
//                String id = jsonObj.getString("supportid");
//                quest = jsonObj.getString("supportQuestion");
//                desc = jsonObj.getString("description");
//            }
//            help context=this;
//            Intent intent = new Intent(context, help1.class);
//            intent.putExtra("supportQuestion",quest);
//            intent.putExtra("description",desc);
//            startActivity(intent);
//            }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onClick(View v) {
    }

}
