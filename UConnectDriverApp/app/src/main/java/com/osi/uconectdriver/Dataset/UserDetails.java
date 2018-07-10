package com.osi.uconectdriver.Dataset;

import java.io.Serializable;

/**
 * Created by ASHISH on 11/6/2015.
 */
public class UserDetails implements Serializable {
   
    public int active=0;
    public int regId=0;
    public String customerId = "", email = "", mobile = "",fname="", lname = "", mname = "", password = "", pin = "",status="",deleteFlag="";
    public String countryCode="",rewardPoints = "",referralCode="",profilePicURL="",authKey="",signInVia="",createDate="",modifiedDate="",message="",paymentList="";
}
