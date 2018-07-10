package com.app.voyager.Util;

import android.graphics.Typeface;

import com.app.voyager.Dataset.LoginDetails;


/**
 * Created by ASHISH on 11/16/2015.
 */
public class Constants {
    public static Typeface Tv_Kgp_medium;
    public static Typeface KozGoPr6N_Bold;
    public static LoginDetails loginDetails = new LoginDetails();
    public static final String ip="192.168.1.114";
    private static final String BASE_URL="http://"+ip+":8080/uc/";
    private static final String GETDRIVERDETAILS1 = BASE_URL+"api/cse/getDriverDetails";
    private static final String UPDATEDETAILS = BASE_URL+"api/cse/updateDriver";
    private static final String ADDEMERGENCYCONTACT1 = BASE_URL+"api/cse/addDEContact";
    private static final String GETEMERGENCYCONTACT1= BASE_URL+"api/cse/getDEContact";
}
