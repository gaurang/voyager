package com.osi.voyagerdriver.AsyncTask;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.osi.voyagerdriver.globalClass.AppController;
import com.osi.voyagerdriver.interfaces.AsyncInterface;
import com.osi.voyagerdriver.interfaces.myUrls;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by ASHISH on 11/3/2015.
 */
public class CallAPI implements AsyncInterface, myUrls {
    String URL;
    String Tag;
    JSONObject mparams;
    Context context;
    Handler replyTo;
    boolean iscancelprevois = false;
    int Methodetype = Request.Method.POST;
    private static RequestQueue queue;
    public CallAPI(String URL, String Tag, JSONObject params, Context context, Handler replyTo, boolean iscancelprevois) throws JSONException {
        this.URL = URL.trim();
        this.Tag = Tag;
        // params.put("token", Session.getTokenID(context));
        this.mparams = params;
        this.context = context;
        this.replyTo = replyTo;
        this.iscancelprevois = iscancelprevois;
        executecode();
    }

    public CallAPI(String URL, String Tag, JSONObject params, Context context, Handler replyTo) throws JSONException {
        this.URL = URL.trim();
        this.Tag = Tag;
        //     params.put("token", Session.getTokenID(context));
        this.mparams = params;
        this.context = context;
        this.replyTo = replyTo;
        executecode();
    }

    public CallAPI(String URL, String Tag, JSONObject params, Context context, Handler replyTo, int Methodetype) throws JSONException {
        this.URL = URL.trim();
        this.Tag = Tag;
        this.Methodetype = Methodetype;
        //     params.put("token", Session.getTokenID(context));
        this.mparams = params;
        this.context = context;
        this.replyTo = replyTo;
        executecode();
    }



    public void executecode() throws JSONException {  // Tag used to cancel the request

        String tag_json_obj = Tag;

        if(queue == null) {
            queue = Volley.newRequestQueue(context);
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Methodetype,
                URL, mparams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Parseresponce(response + "", mparams);
                        System.out.println("Call123 In Async On Responce " + Methodetype + "   " + URL + " " + response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Parseresponce(null, mparams);
                Log.d("JSON Parser", "");
                System.out.println("Call123 In Async On Responce Error: " + Methodetype + "   " + URL + " " + error.getMessage());

            }
        });
        if (Tag.equals("GenerateCSRFToken")) {

            queue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }
        if (iscancelprevois) {
            queue.cancelAll(Tag);
        }
        queue.add(jsonObjReq);

        //      AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void Cancelrequest() {
        AppController.getInstance().cancelPendingRequests(Tag);
    }

    private void Parseresponce(String ResponceData, JSONObject mExtraParam) {
        if (ResponceData != null) {
            try {
                JSONObject jsonObjectdata = new JSONObject(ResponceData);
                JSONObject jsonObject = jsonObjectdata.getJSONObject("header");
                String Responce = jsonObjectdata.getString("body");
                if (jsonObject.getInt("statusCode") == SUCCESS || jsonObject.getInt("statusCode") == SUCCESSAlt) {
                    if (!URL.equals(GenerateCSRFToken)) {
                        Bundle dataBundle = new Bundle();
                        dataBundle.putString("msg",
                                jsonObject.getString("message"));
                        dataBundle.putString("mExtraParam", mparams + "");
                        dataBundle
                                .putInt("code", SUCCESS);
                        dataBundle.putBoolean("flag", true);
                        dataBundle.putString("responce", Responce);
                        Message msg = Message.obtain();
                        msg.setData(dataBundle);
                        replyTo.sendMessage(msg);
                    } else {
                        Bundle dataBundle = new Bundle();
                        dataBundle.putString("msg",
                                jsonObject.getString("message"));
                        dataBundle.putString("mExtraParam", mparams + "");
                        dataBundle.putInt("statusCode", FROMGENERATETOKEN);
                        dataBundle.putBoolean("flag", true);
                        dataBundle.putString("responce", Responce);
                        Message msg = Message.obtain();
                        msg.setData(dataBundle);
                        replyTo.sendMessage(msg);

                    }
                } else if (jsonObject.getInt("statusCode") == SESSIONEXPIRE) {
                    Bundle dataBundle = new Bundle();
                    dataBundle.putString("msg",
                            jsonObject.getString("message"));
                    dataBundle.putString("mExtraParam", mparams + "");
                    dataBundle.putInt("code", jsonObject.getInt("statusCode"));
                    dataBundle.putBoolean("flag", true);
                    dataBundle.putString("responce", Responce);
                    Message msg = Message.obtain();
                    msg.setData(dataBundle);
                    replyTo.sendMessage(msg);

                } else {
                    Bundle dataBundle = new Bundle();
                    dataBundle.putString("msg",
                            jsonObject.getString("message"));
                    dataBundle.putString("mExtraParam", mparams + "");
                    dataBundle.putBoolean("flag", false);
                    dataBundle.putInt("code", jsonObject.getInt("statusCode"));
                    dataBundle.putString("responce", Responce);
                    Message msg = Message.obtain();
                    msg.setData(dataBundle);
                    replyTo.sendMessage(msg);

                }
            } catch (JSONException e) {

                Bundle dataBundle = new Bundle();
                dataBundle.putString("msg", e.getMessage());
                dataBundle.putString("mExtraParam", mparams + "");
                dataBundle.putInt("code", UNEXPECTEDERRROR);
                dataBundle.putBoolean("flag", false);
                dataBundle.putString("responce", "");
                Message msg = Message.obtain();
                msg.setData(dataBundle);
                replyTo.sendMessage(msg);

            }

        } else {
            Bundle dataBundle = new Bundle();
            dataBundle
                    .putString("msg",
                            "Voyager could not fetch data as the server has stopped responding.");
            dataBundle.putString("mExtraParam", mparams + "");
            dataBundle.putBoolean("flag", false);
            dataBundle.putInt("code", UNEXPECTEDERRROR);
            dataBundle.putString("responce", "");
            Message msg = Message.obtain();
            msg.setData(dataBundle);
            replyTo.sendMessage(msg);
        }

    }
//    Handler GetDetails_Handler = new Handler() {
//        public void handleMessage(Message msg) {
//            PrintMessage(msg.getData().toString());
//
//            if (msg.getData().getBoolean("flag")) {
//                if (msg.getData().getInt("code") == SUCCESS) {
//
//                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
//                    ParseSessionDetails(msg.getData().getString("responce"));
//                    CallGetDetails();
//                } else if (msg.getData().getInt("code") ==SESSIONEXPIRE) {
//                    if (Util.isNetworkConnected(LoginScreen.this)) {
////                        CallSessionID(GetDetails_Handler, msg.getData()
////                                .getString("mExtraParam"));
//
//                    } else {
//
//                    }
//                } else {
//
//                }
//            } else {
//
//            }
//
//        }
//
//    };
}
