package com.osi.voyagerdriver;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.osi.voyagerdriver.AsyncTask.CallAPI;
import com.osi.voyagerdriver.Dataset.HelpData;
import com.osi.voyagerdriver.RecyclerviewAdapter.DocumentAdapter;
import com.osi.voyagerdriver.Util.Session;
import com.osi.voyagerdriver.Util.Util;
import com.osi.voyagerdriver.dialogs.ProgressDialogView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shadab.s on 14-01-2016.
 */
public class DocumentActivity extends ParentActivity implements View.OnClickListener {

    TextView headername;
    ImageView ic_back;
    DocumentAdapter helpAdapter;
    ArrayList<HelpData> Historylist = new ArrayList<>();
    RecyclerView mrecycler_score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        progressdialog = new ProgressDialogView(DocumentActivity.this, "");
        BindView(null, savedInstanceState);
    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        super.BindView(view, savedInstanceState);
        headername = (TextView) findViewById(R.id.headername);
        ic_back = (ImageView) findViewById(R.id.ic_back);


        mrecycler_score = (RecyclerView) findViewById(R.id.recyclerview_list);
        mrecycler_score.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                DocumentActivity.this);
        mrecycler_score.setLayoutManager(mLayoutManager);

        headername.setText("DOCUMENTS");
        SetOnclicklistener();
    }

    @Override
    public void SetOnclicklistener() {
        super.SetOnclicklistener();
        ic_back.setOnClickListener(this);
        try {
            JSONObject jsonObject_main = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject_main = getCommontHeaderParams();
            jsonObject.put("customerId", Session.getUserID(DocumentActivity.this));
            jsonObject_main.put("body", jsonObject);
            CallAPI(jsonObject_main);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ic_back:
                finish();
                break;
        }

    }

    public void CallAPI(JSONObject params) {

//            for (int i = 0; i < 10; i++) {
//
//                HelpData helpData = new HelpData();
//                helpData.supportId = i;
//                helpData.supportType = ("supportType");
//                helpData.supportQuestion = ("supportQuestion");
//                helpData.description = ("description");
//                helpData.supportFor = ("supportFor");
//                Historylist.add(helpData);
//            }
//
//            helpAdapter = new DocumentAdapter(Historylist, DocumentActivity.this);
//            mrecycler_score.setAdapter(helpAdapter);

        if (Util.isNetworkConnected(DocumentActivity.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(GETHELP, "GETHELP", params, DocumentActivity.this, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(DocumentActivity.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetDetails_Handler = new Handler() {
        public void handleMessage(Message msg) {

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

                        helpAdapter = new DocumentAdapter(Historylist, DocumentActivity.this);
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
                    if (Util.isNetworkConnected(DocumentActivity.this)) {
                        CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(DocumentActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(DocumentActivity.this, msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(DocumentActivity.this, msg.getData().getString("msg"));
            }
        }
    };
}
