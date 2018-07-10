package com.osi.uconectdriver.Dataset;

import java.io.Serializable;

/**
 * Created by ASHISH on 11/6/2015.
 */
public class LoginDetails implements Serializable {
    public int id = 0;
    public int typelogin=0;//0-gmail,1-fb,2-googleplus
    public String fName = "", lName = "", gender = "", profileUrl = "", emailId = "", facebookId = "", googleId = "",username="",age="",password="",country="",city="",Status="Hey i am using Questtag!";
    public String interestedTag = "";
}
