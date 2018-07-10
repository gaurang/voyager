package com.app.uconect.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.app.uconect.Dataset.CarServiceData;
import com.app.uconect.Dataset.CarSubServiceData;
import com.app.uconect.Dataset.TerrifPlans;
import com.app.uconect.Dataset.UserDetails;
import com.app.uconect.RegisterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Session {

    public static String getTokenID(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        return pref.getString("TokenID", "");
    }

    public static void setTokenID(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("TokenID", Data);
        lock_editor.commit();
    }

    /*
     *
     *
     * STore Image Path To USe in Over aAll App
     */
    public static String getImagePath(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        return pref.getString("ImagePath", "http://UConect.com");
    }

    public static void setImagePath(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("ImagePath", Data);
        lock_editor.commit();
    }

    // Store All Login Information
    public static String getAllInfo(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        return pref.getString("AllInfo", "");
    }

    public static void setAllInfo(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;
        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("AllInfo", Data);
        lock_editor.commit();
    }

    // Login Status
    public static boolean getLoginStatus(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        return pref.getBoolean("LoginStatus", false);
    }

    public static void setLoginStatus(Context context, boolean flag) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putBoolean("LoginStatus", flag);
        lock_editor.commit();
    }

    // Store All Login Information
    public static String getConfig(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        return pref.getString("Config", "");
    }

    public static void setConfig(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;
        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("Config", Data);
        lock_editor.commit();
    }

    // FaceBook Friend Status
    public static boolean getFBfriendStatus(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        return pref.getBoolean("FBfriend", false);
    }


    public static void setFBfriendStatus(Context context, boolean flag) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putBoolean("FBfriend", flag);
        lock_editor.commit();
    }

    public static void setGoogleplusfriendStatus(Context context, boolean flag) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putBoolean("Googleplusfriend", flag);
        lock_editor.commit();
    }

    // FaceBook Friend Status
    public static boolean getGoogleplusfriendStatus(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        return pref.getBoolean("Googleplusfriend", false);
    }

    public static String getUserID(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        return pref.getString("UserID", "1");
    }

    public static void setUserID(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("UserID", Data);
        lock_editor.commit();
    }

    public static String getEmailId(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        return pref.getString("EmailId", "1");
    }

    public static void setEmailId(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("EmailId", Data);
        lock_editor.commit();
    }


    public static String getPushwooshToken(Context context) {
        SharedPreferences pref = null;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        return pref.getString("PushwooshToken", "");
    }

    public static void setPushwooshToken(Context context, String token) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("PushwooshToken", token);
        lock_editor.commit();
    }

    public static String getLongitude(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        return pref.getString("Longitude", "");
    }

    public static void setLongitude(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("Longitude", Data);
        lock_editor.commit();
    }

    public static String getLatitude(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        return pref.getString("Latitude", "");
    }

    public static void setLatitude(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("Latitude", Data);
        lock_editor.commit();
    }

    public static UserDetails GetUserInformation(String Data) {
        UserDetails userDetails = new UserDetails();
        try {
            // JSONObject jsonObject = new JSONObject(Data);
            JSONObject jsonObject1 = new JSONObject(Data);

            if (jsonObject1.has("regId")) userDetails.regId = jsonObject1.getInt("regId");
            userDetails.active = 1;
            if (jsonObject1.has("customerId"))
                userDetails.customerId = jsonObject1.getString("customerId");
            if (jsonObject1.has("email")) userDetails.email = jsonObject1.getString("email");
            if (jsonObject1.has("mobile")) userDetails.mobile = jsonObject1.getString("mobile");
            if (jsonObject1.has("fname")) userDetails.fname = jsonObject1.getString("fname");
            if (jsonObject1.has("lname")) userDetails.lname = jsonObject1.getString("lname");
            if (jsonObject1.has("mname")) userDetails.mname = jsonObject1.getString("mname");
            if (jsonObject1.has("password"))
                userDetails.password = jsonObject1.getString("password");
            if (jsonObject1.has("pin")) userDetails.pin = jsonObject1.getString("pin");
            if (jsonObject1.has("status")) userDetails.status = jsonObject1.getString("status");
            if (jsonObject1.has("deleteFlag"))
                userDetails.deleteFlag = jsonObject1.getString("deleteFlag");
            if (jsonObject1.has("rewardPoints"))
                userDetails.rewardPoints = jsonObject1.getString("rewardPoints");
            if (jsonObject1.has("referralCode"))
                userDetails.referralCode = jsonObject1.getString("referralCode");
            if (jsonObject1.has("profilePicURL"))
                userDetails.profilePicURL = jsonObject1.getString("profilePicURL");
            if (jsonObject1.has("authKey")) userDetails.authKey = jsonObject1.getString("authKey");
            if (jsonObject1.has("signInVia"))
                userDetails.signInVia = jsonObject1.getString("signInVia");
            if (jsonObject1.has("createDate"))
                userDetails.createDate = jsonObject1.getString("createDate");
            if (jsonObject1.has("modifiedDate"))
                userDetails.modifiedDate = jsonObject1.getString("modifiedDate");
            if (jsonObject1.has("message")) userDetails.message = jsonObject1.getString("message");
            if (jsonObject1.has("countryCode"))
                userDetails.countryCode = jsonObject1.getString("countryCode");

            if (jsonObject1.has("paymentList"))
                userDetails.paymentList = jsonObject1.getString("paymentList");

        } catch (JSONException e) {

            e.printStackTrace();
        }
        return userDetails;

    }

    public static ArrayList<TerrifPlans> getTarrifplans(String Data) throws JSONException {
        ArrayList<TerrifPlans> terrifPlanses = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(Data);
        JSONArray jsonArray = jsonObject.getJSONArray("TARIFF");
        for (int i = 0; i < jsonArray.length(); i++) {
            TerrifPlans terrif = new TerrifPlans();
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            terrif.masterAttr = jsonObject1.getString("masterAttr");
            terrif.attrName = jsonObject1.getString("attrName");
            terrif.attrValue1 = jsonObject1.getString("attrValue1");
            terrif.attrValue2 = jsonObject1.getString("attrValue2");
            Log.d("12345678 ", jsonObject1.getJSONObject("subAttributes") + "");
            // getvalue attrValue1
            terrif.BASE_WAIT_TIME = jsonObject1.getJSONObject("subAttributes").getJSONObject("BASE_WAIT_TIME").getString("attrValue1");
            terrif.BASE_WAIT_UNIT = jsonObject1.getJSONObject("subAttributes").getJSONObject("BASE_WAIT_UNIT").getString("attrValue1");
            terrif.BASE_WAIT_CHRG = jsonObject1.getJSONObject("subAttributes").getJSONObject("BASE_WAIT_CHRG").getString("attrValue1");
            //  terrif.BASE_KM = jsonObject1.getJSONObject("subAttributes").getJSONObject("BASE_KM").getString("attrValue1");
            terrif.BASE_FARE = jsonObject1.getJSONObject("subAttributes").getJSONObject("BASE_FARE").getString("attrValue1");
             terrif.BASE_KM_RATE = jsonObject1.getJSONObject("subAttributes").getJSONObject("BASE_KM_FARE").getString("attrValue1");

            terrif.UNIT_BASE_WAIT_TIME = jsonObject1.getJSONObject("subAttributes").getJSONObject("BASE_WAIT_TIME").getString("unit");
            terrif.UNIT_BASE_WAIT_UNIT = jsonObject1.getJSONObject("subAttributes").getJSONObject("BASE_WAIT_UNIT").getString("unit");
            terrif.UNIT_BASE_WAIT_CHRG = jsonObject1.getJSONObject("subAttributes").getJSONObject("BASE_WAIT_CHRG").getString("unit");
            //  terrif.UNIT_BASE_KM = jsonObject1.getJSONObject("subAttributes").getJSONObject("BASE_KM").getString("unit");
            terrif.UNIT_BASE_FARE = jsonObject1.getJSONObject("subAttributes").getJSONObject("BASE_FARE").getString("unit");
            terrif.UNIT_BASE_KM_RATE = jsonObject1.getJSONObject("subAttributes").getJSONObject("BASE_KM_FARE").getString("unit");

          Log.d("values ",terrif.BASE_WAIT_TIME+" "+terrif.BASE_WAIT_UNIT+" "+terrif.BASE_WAIT_CHRG+" "+terrif.BASE_FARE+" ");
            terrifPlanses.add(terrif);
        }
        return terrifPlanses;
    }

    public static ArrayList<CarServiceData> GetCarData(JSONObject SERVICE_TYPE) throws JSONException {
        ArrayList<CarServiceData> carServiceDatas = new ArrayList<>();
        Iterator<?> keys = SERVICE_TYPE.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (SERVICE_TYPE.get(key) instanceof JSONObject) {

                CarServiceData carServiceData = new CarServiceData();
                carServiceData.attrName = ((JSONObject) SERVICE_TYPE.get(key)).getString("attrName");
                carServiceData.attrValue1 = ((JSONObject) SERVICE_TYPE.get(key)).getString("attrValue1");
                //if(((JSONObject)SERVICE_TYPE.get(key)).getString("subAttributes").trim().length() == 0 )
                  //  continue;
                JSONObject jsonObject;
                Log.i("qaaaaaaaaaa","XXXXXXXXXXXXXXXXXXXXXXXXXX" +carServiceData.attrName+carServiceData.attrValue1);
                try{
                    jsonObject = ((JSONObject) SERVICE_TYPE.get(key)).getJSONObject("subAttributes");
                }catch(JSONException e){
                    e.printStackTrace();
                    continue;
                }

                Iterator<?> keys_sub = jsonObject.keys();

                while (keys_sub.hasNext()) {
                    String key_sub = (String) keys_sub.next();
                    if (jsonObject.get(key_sub) instanceof JSONObject) {
                        CarSubServiceData carSubServiceData = new CarSubServiceData();
                        carSubServiceData.masterAttr = ((JSONObject) jsonObject.get(key_sub)).getString("attrName");
                        carSubServiceData.attrName = ((JSONObject) jsonObject.get(key_sub)).getString("attrName");
                        carSubServiceData.attrValue1 = ((JSONObject) jsonObject.get(key_sub)).getString("attrValue1");
                        Log.i("qaaaaaaaaaa", "XXXXXXXXXXXXXXXXXXXXXXXXXX" + ((JSONObject) jsonObject.get(key_sub)).getString("attrValue1"));
                        carServiceData.carSubServiceDatas.add(carSubServiceData);
                    }
                }
                carServiceDatas.add(carServiceData);
//                SERVICE_TYPE.get(key)
            }


        }
        return carServiceDatas;
    }

    public static void setRegID(RegisterActivity context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("regId", Data);
        lock_editor.commit();
    }
    public static String getRegID(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        return pref.getString("regId", "1");
    }
    public static void setCc(RegisterActivity context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("countryCode", Data);
        lock_editor.commit();
    }
    public static String getCc(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        return pref.getString("countryCode", "1");
    }

    public static void setName(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("fname", Data);
        lock_editor.commit();
    }
    public static String getName(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        return pref.getString("fname", "1");
    }

    public static String getPaymentID(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        return pref.getString("paymentId", "1");
    }

    public static void setPaymentId(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("paymentId", Data);
        lock_editor.commit();
    }

    public static String getAccountType(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        return pref.getString("accountType", "1");
    }

    public static void setAccountType(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("accountType", Data);
        lock_editor.commit();
    }

    public static void setEcontact(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("eContactNumber", Data);
        lock_editor.commit();
    }
    public static String getEcontact(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        return pref.getString("eContactNumber", "");
    }

    public static void setEcontact1(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("eContactNumber1", Data);
        lock_editor.commit();
    }
    public static String getEcontact1(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        return pref.getString("eContactNumber1", "");
    }

    public static void setVehicleType(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("eContactNumber1", Data);
        lock_editor.commit();
    }
    public static String getVehicleType(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        return pref.getString("eContactNumber1", "");
    }

    public static void setTime(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("time", Data);
        lock_editor.commit();
    }
    public static String getTime(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConect", Context.MODE_PRIVATE);
        return pref.getString("time", "");
    }

    public static void setBookingId( Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("bookingId", Data);
        lock_editor.commit();
    }
    public static String getBookingId(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        return pref.getString("bookingId", "");
    }
    public static String getToken(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        return pref.getString("Token", "");
    }

    public static void setToken(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("Token", Data);
        lock_editor.commit();
    }
    public static String getCabCount(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        return pref.getString("cabCount", "");
    }

    public static void setCabCount(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("cabCount", Data);
        lock_editor.commit();
    }


    public static String getUnit(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        return pref.getString("unit", "");
    }

    public static void setUnit(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("unit", Data);
        lock_editor.commit();
    }
}
