package com.osi.voyagerdriver.AsyncTask;//package com.osi.voyagerdriver.AsyncTask;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//
//import com.osi.voyagerdriver.dataset.AskQuestionData;
//import com.osi.voyagerdriver.interfaces.AsyncInterface;
//import com.osi.voyagerdriver.util.Util;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//
///**
// * Created by ASHISH on 11/21/2015.
// */
//public class SendHttpRequestTask_AskQuestion extends AsyncTask<String, Void, String> implements AsyncInterface {
//    String dataresponce;
//    String url;
//    String mparams = "";
//    Handler replyTo;
//
//    AskQuestionData askQuestionData;
//
//    public SendHttpRequestTask_AskQuestion(Handler reply, AskQuestionData askQuestionData) {
//        replyTo = reply;
//        this.askQuestionData = askQuestionData;
//    }
//
//    @Override
//    protected String doInBackground(String... params) {
//        url = params[0];
//        try {
//            HttpClient client = new HttpClient(url);
//            client.connectForMultipart();
//            client.addFormPart("token", askQuestionData.token);
//            client.addFormPart("user_id", askQuestionData.userid);
//            client.addFormPart("question", askQuestionData.question);
//            client.addFormPart("option1", askQuestionData.option1);
//            client.addFormPart("option2", askQuestionData.option2);
//            client.addFormPart("option3", askQuestionData.option3);
//            client.addFormPart("option4", askQuestionData.option4);
//
//            client.addFormPart("tags", askQuestionData.tags);
//            client.addFormPart("answer_type", askQuestionData.answer_type + "");
//            client.addFormPart("question_type", askQuestionData.question_type + "");
//            client.addFormPart("release_type", askQuestionData.release_type + "");
//
//            client.addFormPart("latitude", askQuestionData.latitude);
//            client.addFormPart("longitude", askQuestionData.longitude);
//            client.addFormPart("range", askQuestionData.range + "");
//
//
//            if (new File(askQuestionData.image).exists())
//                client.addFilePart("image", new File(askQuestionData.image).getName() + "", Util.read((new File(askQuestionData.image))));
//            System.out.println("image  " + askQuestionData.image);
//            client.finishMultipart();
//            dataresponce = client.getResponse();
//            PrintMessage(dataresponce);
//        } catch (Throwable t) {
//            PrintMessage(t.getMessage());
//            t.printStackTrace();
//        }
//
//        return null;
//    }
//
//    @Override
//    protected void onPreExecute() {
//
//    }
//
//    @Override
//    protected void onPostExecute(String data) {
//        PrintMessage(dataresponce);
//        Parseresponce(dataresponce);
//    }
//
//    public void PrintMessage(String messge) {
//        System.out.println("###Call123 " + messge);
//    }
//
//    private void Parseresponce(String Responce) {
//        if (Responce != null) {
//            try {
//                JSONObject jsonObject = new JSONObject(Responce);
//                if (jsonObject.getInt("code") == 1 || jsonObject.getInt("code") == 2) {
//                    if (!url.equals(FROMGENERATETOKEN)) {
//                        Bundle dataBundle = new Bundle();
//                        dataBundle.putString("msg",
//                                jsonObject.getString("message"));
//                        dataBundle.putString("mExtraParam", mparams + "");
//                        dataBundle
//                                .putInt("code", SUCCESS);
//                        dataBundle.putBoolean("flag", true);
//                        dataBundle.putString("responce", Responce);
//                        Message msg = Message.obtain();
//                        msg.setData(dataBundle);
//                        replyTo.sendMessage(msg);
//                    } else {
//                        Bundle dataBundle = new Bundle();
//                        dataBundle.putString("msg",
//                                jsonObject.getString("message"));
//                        dataBundle.putString("mExtraParam", mparams + "");
//                        dataBundle.putInt("code", FROMGENERATETOKEN);
//                        dataBundle.putBoolean("flag", true);
//                        dataBundle.putString("responce", Responce);
//                        Message msg = Message.obtain();
//                        msg.setData(dataBundle);
//                        replyTo.sendMessage(msg);
//
//                    }
//                } else if (jsonObject.getInt("code") == 5) {
//                    Bundle dataBundle = new Bundle();
//                    dataBundle.putString("msg",
//                            jsonObject.getString("message"));
//                    dataBundle.putString("mExtraParam", mparams + "");
//                    dataBundle.putInt("code", jsonObject.getInt("code"));
//                    dataBundle.putBoolean("flag", true);
//                    dataBundle.putString("responce", Responce);
//                    Message msg = Message.obtain();
//                    msg.setData(dataBundle);
//                    replyTo.sendMessage(msg);
//
//                } else {
//                    Bundle dataBundle = new Bundle();
//                    dataBundle.putString("msg",
//                            jsonObject.getString("message"));
//                    dataBundle.putString("mExtraParam", mparams + "");
//                    dataBundle.putBoolean("flag", false);
//                    dataBundle.putInt("code", jsonObject.getInt("code"));
//                    dataBundle.putString("responce", Responce);
//                    Message msg = Message.obtain();
//                    msg.setData(dataBundle);
//                    replyTo.sendMessage(msg);
//
//                }
//            } catch (JSONException e) {
//
//                Bundle dataBundle = new Bundle();
//                dataBundle.putString("msg", e.getMessage());
//                // dataBundle.putString("mExtraParam", mExtraParam);
//                dataBundle.putInt("code", UNEXPECTEDERRROR);
//                dataBundle.putBoolean("flag", false);
//                dataBundle.putString("responce", Responce);
//                Message msg = Message.obtain();
//                msg.setData(dataBundle);
//                replyTo.sendMessage(msg);
//
//            }
//
//        } else {
//            Bundle dataBundle = new Bundle();
//            dataBundle
//                    .putString("msg",
//                            "Voyager could not fetch data as the server has stopped responding.");
//            //    dataBundle.putString("mExtraParam", mExtraParam);
//            dataBundle.putBoolean("flag", false);
//            dataBundle.putInt("code", UNEXPECTEDERRROR);
//            dataBundle.putString("responce", Responce);
//            Message msg = Message.obtain();
//            msg.setData(dataBundle);
//            replyTo.sendMessage(msg);
//        }
//
//    }
//}
