package com.app.uconect.Dataset;

import java.io.Serializable;

/**
 * Created by osigroups on 1/24/2016.
 */
public class EmergencyContactData implements Serializable {
    public int contactId = 0;
    public String customerId = "", eContactName = "";
    public String eContactNumber = "";
    public  int trackStatus=0;
}
