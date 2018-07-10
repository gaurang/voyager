package com.app.uconect;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.uconect.AsyncTask.CallAPI;
import com.app.uconect.Dataset.NotificationData;
import com.app.uconect.RecyclerviewAdapter.NotificationsAdapter;
import com.app.uconect.Util.Session;
import com.app.uconect.Util.Util;
import com.app.uconect.dialogs.ProgressDialogView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by shadab.s on 14-01-2016.
 */
public class NotificationsActivity extends ParentActivity implements View.OnClickListener {

    TextView headername;
    ImageView ic_back,cancel;
    NotificationsAdapter notificationsAdapter;
    ArrayList<NotificationData> NotificationList = new ArrayList<>();
    RecyclerView mrecycler_score;
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificastions);
        webview=(WebView) findViewById(R.id.webview);
        BindView(null, savedInstanceState);
    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        super.BindView(view, savedInstanceState);
        headername = (TextView) findViewById(R.id.headername);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        cancel = (ImageView) findViewById(R.id.cancel);

        try {
            JSONObject jsonObject_main = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject_main = getCommontHeaderParams();
            jsonObject.put("customerId", Session.getUserID(NotificationsActivity.this));
            jsonObject_main.put("body", jsonObject);
            CallNotification(jsonObject_main);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mrecycler_score = (RecyclerView) findViewById(R.id.recyclerview_list);
        mrecycler_score.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                NotificationsActivity.this);
        mrecycler_score.setLayoutManager(mLayoutManager);
//        for (int i = 0; i < 15; i++) {
//            NotificationData notificationData = new NotificationData();
//            notificationData.id = i;
//            notificationData.Title = "The decision to send out notifications is made in the service check and host check logic.";
//            notificationData.Description = "The decision to send out notifications is made in the service check and host check logic. The calculations for whether a notification is to be sent are only triggered when processing a host or service check corresponding to that notification; they are not triggered simply because the <notification_interval> has passed since a previous notification was sent. Host and service notifications occur in the following instances";
//            Historylist.add(notificationData);
//        }
//
//        notificationsAdapter = new NotificationsAdapter(Historylist, NotificationsActivity.this);
//        mrecycler_score.setAdapter(notificationsAdapter);
        headername.setText("NOTIFICATIONS");
        SetOnclicklistener();
    }

    @Override
    public void SetOnclicklistener() {
        super.SetOnclicklistener();
        ic_back.setOnClickListener(this);

    }
    @Override
    public void onBackPressed() {
        callLandingPage();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ic_back:
               onBackPressed();
                break;
        }

    }
    private void CallNotification(JSONObject params) {
        if (Util.isNetworkConnected(NotificationsActivity.this)) {
            try {


                new CallAPI(NOTIFICATION, "NOTIFICATION", params, NotificationsActivity.this, GetNotification_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(NotificationsActivity.this, getString(R.string.nointernetmessage));
        }
    }

    private Bitmap b;
    Handler GetNotification_Handler = new Handler() {
        public void handleMessage(Message msg) {
            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    try {
                        JSONArray jsonArray = new JSONArray(msg.getData().getString("responce"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            {

//                                NotificationData notificationData = new NotificationData();
//                                notificationData.Textmessage = jsonObject.getString("textMsg");
//                                NotificationList.add(notificationData);

                                RelativeLayout noti=(RelativeLayout)findViewById(R.id.noti);
                                noti.bringToFront();
                                cancel.bringToFront();
                                cancel.setVisibility(View.VISIBLE);
                                webview.setVisibility(View.VISIBLE);
                                webview.loadUrl(jsonObject.getString("textMsg"));
                                webview.setWebViewClient(new WebViewClient() {
                                    public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
                                        viewx.loadUrl(urlx);
                                        return false;
                                    }
                                });
//                                notificationsAdapter = new NotificationsAdapter(NotificationList,NotificationsActivity.this);
//                                mrecycler_score.setAdapter(notificationsAdapter);
                            }
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(NotificationsActivity.this)) {
                        CallSessionID(GetNotification_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {

                    }
                } else {

                }
            } else {

            }


        }
    };
    public void setNotifications(String textMsg)
    {
                                RelativeLayout noti=(RelativeLayout)findViewById(R.id.noti);
                                noti.bringToFront();
                                cancel.bringToFront();
                                cancel.setVisibility(View.VISIBLE);
                                webview.setVisibility(View.VISIBLE);
                                webview.loadUrl(textMsg);
                                webview.setWebViewClient(new WebViewClient() {
                                    public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
                                        viewx.loadUrl(urlx);
                                        return false;
                                    }
                                });
                                Log.i("aaaaaaaaaaa", "vbbbbccccccccccccc" + textMsg);
    }
    public Bitmap setImage(String s) {
        webview.loadUrl(s);


        webview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                Picture picture = view.capturePicture();
                b = Bitmap.createBitmap(250,
                        300, Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(b);
                picture.draw(c);
                FileOutputStream fos = null;
                try {

                    fos = new FileOutputStream("mnt/sdcard/yahoo.jpg");
                    if (fos != null) {
                        b.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                        fos.close();
                    }
                } catch (Exception e) {

                }
                webview.setVisibility(View.GONE);


            }
        });
        return b;
    }
}
