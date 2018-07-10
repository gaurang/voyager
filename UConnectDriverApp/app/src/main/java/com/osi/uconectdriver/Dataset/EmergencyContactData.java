package com.osi.uconectdriver.Dataset;

import java.io.Serializable;

/**
 * Created by osigroups on 1/24/2016.
 */
public class EmergencyContactData implements Serializable {
    public int contactId = 0;
    public String driverId = "", eContactName = "";
    public String eContactNumber = "";
    public  int trackStatus=0;
}
