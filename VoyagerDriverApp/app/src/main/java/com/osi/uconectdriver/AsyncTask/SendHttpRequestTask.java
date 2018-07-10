package com.osi.uconectdriver.AsyncTask;//package com.osi.uconectdriver.AsyncTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.osi.uconectdriver.LandingPageActivity;
import com.osi.uconectdriver.ParentActivity;
import com.osi.uconectdriver.Util.Session;
import com.osi.uconectdriver.Util.Util;
import com.osi.uconectdriver.interfaces.AsyncInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by ASHISH on 11/21/2015.
 */
public class SendHttpRequestTask extends AsyncTask<String, Void, String> implements AsyncInterface {
    String dataresponce;
    String url;
    String mparams = "";
    Handler replyTo;
    String Imagepath;
    ParentActivity parentActivity;

    public SendHttpRequestTask(Handler reply, ParentActivity parentActivity) {
        replyTo = reply;
        this.parentActivity = parentActivity;
    }

    @Override
    protected String doInBackground(String... params) {
        url = params[0];
        Imagepath = params[1];


        try {
            HttpClient client = new HttpClient(url);
            client.connectForMultipart();
//                country-gender
            JSONObject jsonObject_main = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject_main = parentActivity.getCommontHeaderParams();
            jsonObject.put("custometid", Session.getUserID((LandingPageActivity) parentActivity));
            jsonObject.put("custometid", jsonObject_main + "");

            jsonObject_main.put("body", jsonObject);
            client.addFormPart("data", jsonObject_main + "");
            client.addFormPart("custometid", jsonObject_main + "");

//                multipart.addFilePart("file", new File(Environment.getExternalStorageDirectory().toString() + "/UConectprofile.png"));
//                multipart.addFilePart("video", new File(Environment.getExternalStorageDirectory().toString() + "/UConectprofile.mp4"));
            if (new File(Imagepath).exists())
                client.addFilePart("file", new File(Imagepath).getName() + "", Util.read((new File(Imagepath))));

            client.finishMultipart();
            dataresponce = client.getResponse();
            PrintMessage(dataresponce);
        } catch (Throwable t) {
            PrintMessage(t.getMessage());
            t.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(String data) {
        PrintMessage(dataresponce);
        Parseresponce(dataresponce);
    }

    public void PrintMessage(String messge) {
        System.out.println("###Call123 " + messge);
    }

    private void Parseresponce(String Responce) {
        if (Responce != null) {
            try {
                JSONObject jsonObject = new JSONObject(Responce);
                if (jsonObject.getInt("code") == 1 || jsonObject.getInt("code") == 2) {
                    if (!url.equals(FROMGENERATETOKEN)) {
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
                        dataBundle.putInt("code", FROMGENERATETOKEN);
                        dataBundle.putBoolean("flag", true);
                        dataBundle.putString("responce", Responce);
                        Message msg = Message.obtain();
                        msg.setData(dataBundle);
                        replyTo.sendMessage(msg);

                    }
                } else if (jsonObject.getInt("code") == 5) {
                    Bundle dataBundle = new Bundle();
                    dataBundle.putString("msg",
                            jsonObject.getString("message"));
                    dataBundle.putString("mExtraParam", mparams + "");
                    dataBundle.putInt("code", jsonObject.getInt("code"));
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
                    dataBundle.putInt("code", jsonObject.getInt("code"));
                    dataBundle.putString("responce", Responce);
                    Message msg = Message.obtain();
                    msg.setData(dataBundle);
                    replyTo.sendMessage(msg);

                }
            } catch (JSONException e) {

                Bundle dataBundle = new Bundle();
                dataBundle.putString("msg", e.getMessage());
                // dataBundle.putString("mExtraParam", mExtraParam);
                dataBundle.putInt("code", UNEXPECTEDERRROR);
                dataBundle.putBoolean("flag", false);
                dataBundle.putString("responce", Responce);
                Message msg = Message.obtain();
                msg.setData(dataBundle);
                replyTo.sendMessage(msg);

            }

        } else {
            Bundle dataBundle = new Bundle();
            dataBundle
                    .putString("msg",
                            "UConect could not fetch data as the server has stopped responding.");
            //    dataBundle.putString("mExtraParam", mExtraParam);
            dataBundle.putBoolean("flag", false);
            dataBundle.putInt("code", UNEXPECTEDERRROR);
            dataBundle.putString("responce", Responce);
            Message msg = Message.obtain();
            msg.setData(dataBundle);
            replyTo.sendMessage(msg);
        }

    }
}
