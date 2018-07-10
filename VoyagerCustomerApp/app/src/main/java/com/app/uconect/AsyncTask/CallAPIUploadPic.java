package com.app.uconect.AsyncTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.uconect.Util.Session;
import com.app.uconect.globalClass.AppController;
import com.app.uconect.interfaces.AsyncInterface;
import com.app.uconect.interfaces.myUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by ASHISH on 11/3/2015.
 */
public class CallAPIUploadPic implements AsyncInterface, myUrls {
    String URL;
    String Tag;
    JSONObject mparams;
    Context context;
    Handler replyTo;
    boolean iscancelprevois = false;
    int Methodetype = Request.Method.POST;


    public CallAPIUploadPic(String URL, String Tag, JSONObject params, Context context, Handler replyTo, Bitmap bitmap) throws JSONException {
        this.URL = URL.trim();
        this.Tag = Tag;
        this.Methodetype = Methodetype;
        params.put("token", Session.getTokenID(context));
        this.mparams = params;
        this.context = context;
        this.replyTo = replyTo;
        this.bitmap = bitmap;
        CallAPiData();
    }

    public void executecode() throws JSONException {  // Tag used to cancel the request

        String tag_json_obj = Tag;
        RequestQueue queue = Volley.newRequestQueue(context);

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
                            "Uconect could not fetch data as the server has stopped responding.");
            dataBundle.putString("mExtraParam", mparams + "");
            dataBundle.putBoolean("flag", false);
            dataBundle.putInt("code", UNEXPECTEDERRROR);
            dataBundle.putString("responce", "");
            Message msg = Message.obtain();
            msg.setData(dataBundle);
            replyTo.sendMessage(msg);
        }

    }

    String mimeType;
    DataOutputStream dos = null;
    String lineEnd = "\r\n";
    String boundary = "apiclient-" + System.currentTimeMillis();
    String twoHyphens = "--";
    int bytesRead, bytesAvailable, bufferSize;
    byte[] buffer;
    int maxBufferSize = 1024 * 1024;
    Bitmap bitmap;

    public void CallAPiData() {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        final byte[] bitmapData = byteArrayOutputStream.toByteArray();


        //  mimeType = "multipart/mixed;boundary=" + boundary;
        mimeType = "multipart/form-data;boundary=" + boundary;

        BaseVolleyRequest baseVolleyRequest = new BaseVolleyRequest(1, URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                System.out.println("Call123 In Async On Responce " + new String(response.data) + "   " + response.statusCode + " " + response.toString());
                Parseresponce(new String(response.data) + "", mparams);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Parseresponce(null, mparams);
                System.out.println("Call123 In Async On Responce Error: " + Methodetype + "   " + URL + " " + error.getMessage());

            }
        }) {
//            @Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  params = new HashMap<>();
//                // the POST parameters:
//                params.put("body", mparams+"");
//
//                return params;
//            }

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String>  params = new HashMap<>();
//                // the POST parameters:
//                params.put("body", mparams+"");
//
//                return params;
//            }

            @Override
            public String getBodyContentType() {
                return mimeType;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                dos = new DataOutputStream(bos);
                try {
                    String picname = System.currentTimeMillis() + "" + new Random().nextInt(1505525) + ".png";
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
                            + picname + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);


                    ByteArrayInputStream fileInputStream = new ByteArrayInputStream(bitmapData);
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }

                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    return bos.toByteArray();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bitmapData;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);

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
        queue.add(baseVolleyRequest);


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
