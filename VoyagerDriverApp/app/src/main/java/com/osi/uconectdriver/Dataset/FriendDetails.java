package com.osi.uconectdriver.Dataset;

import java.io.Serializable;

/**
 * Created by ASHISH on 11/6/2015.
 */
public class FriendDetails implements Serializable {
    public int user_id = 0;
    public String CONTACTID = "";
    public int active=0;public String imageuri;

    public int is_friend=0;
    public String first_name = "", last_name = "",  mobilenumber="";
}
