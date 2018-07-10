package com.osi.voyagerdriver.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.osi.voyagerdriver.Dataset.UserDetails;

import org.json.JSONException;
import org.json.JSONObject;

public class Session {

    private static String bookingId;

    public static String getTokenID(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        return pref.getString("TokenID", "");
    }

    public static void setTokenID(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
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
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        return pref.getString("ImagePath", "http://UConectDriver.com");
    }

    public static void setImagePath(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("ImagePath", Data);
        lock_editor.commit();
    }

    // Store All Login Information
    public static String getAllInfo(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        return pref.getString("AllInfo", "");
    }

    public static void setAllInfo(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("AllInfo", Data);
        lock_editor.commit();
    }

    // Login Status
    public static boolean getLoginStatus(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        return pref.getBoolean("LoginStatus", false);
    }

    public static void setLoginStatus(Context context, boolean flag) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putBoolean("LoginStatus", flag);
        lock_editor.commit();
    }
// Location Tracking Status -> isonline Or offline

    public static int getIsOnlioneStatus(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        return pref.getInt("IsOnlione", 0);
    }

    public static void setIsOnlioneStatus(Context context, int flag) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putInt("IsOnlione", flag);
        lock_editor.commit();
    }


    // FaceBook Friend Status
    public static boolean getFBfriendStatus(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        return pref.getBoolean("FBfriend", false);
    }


    public static void setFBfriendStatus(Context context, boolean flag) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putBoolean("FBfriend", flag);
        lock_editor.commit();
    }

    public static void setGoogleplusfriendStatus(Context context, boolean flag) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putBoolean("Googleplusfriend", flag);
        lock_editor.commit();
    }

    // FaceBook Friend Status
    public static boolean getGoogleplusfriendStatus(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        return pref.getBoolean("Googleplusfriend", false);
    }

    public static String getUserID(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        return pref.getString("UserID", "1");
    }

    public static void setUserID(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("UserID", Data);
        lock_editor.commit();
    }

    public static String getEmailId(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        return pref.getString("EmailId", "1");
    }

    public static void setEmailId(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("EmailId", Data);
        lock_editor.commit();
    }


    public static String getServiceType(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        return pref.getString("serviceType", "1");
    }

    public static void setServiceType(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("serviceType", Data);
        lock_editor.commit();
    }
    public static String getVehicleType(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        return pref.getString("vehicleType", "1");
    }

    public static void setVehicleType(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("vehicleType", Data);
        lock_editor.commit();
    }


    public static String getPushwooshToken(Context context) {
        SharedPreferences pref = null;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        return pref.getString("PushwooshToken", "");
    }

    public static void setPushwooshToken(Context context, String token) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("PushwooshToken", token);
        lock_editor.commit();
    }

    public static String getLongitude(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        return pref.getString("Longitude", "");
    }

    public static void setLongitude(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("Longitude", Data);
        lock_editor.commit();
    }

    public static String getLatitude(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        return pref.getString("Latitude", "");
    }

    public static void setLatitude(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
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
                userDetails.paymentList = jsonObject1.getString("modifiedDate");

        } catch (JSONException e) {

            e.printStackTrace();
        }
        return userDetails;

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

    public static void setDefaultVehicleId( Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("defaultVehicleId", Data);
        lock_editor.commit();
    }
    public static String getDefaultVehicleId(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        return pref.getString("defaultVehicleId", "");
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

    public static void setIsOnline( Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("online", Data);
        lock_editor.commit();
    }
    public static String getIsOnline(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        return pref.getString("online", "");
    }
    public static String getDefaultVehicle(Context context) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        return pref.getString("defaultVehicle", "1");
    }

    public static void setDefaultVehicle(Context context, String Data) {
        SharedPreferences pref;
        SharedPreferences.Editor lock_editor;

        pref = context.getSharedPreferences("UConectDriver", Context.MODE_PRIVATE);
        lock_editor = pref.edit();
        lock_editor.putString("defaultVehicle", Data);
        lock_editor.commit();
    }
}