package com.app.voyager.Dataset;

import java.io.Serializable;

/**
 * Created by osigroups on 1/25/2016.
 */
// public int 0-PENDING , 1-CANCEL, 2- SUCCESS
public class HistoryData implements Serializable{
    public String bookingId ="";
    public String customerId = "";
    public String sourceLatitude = "";
    public String sourceLongitude = "";
    public String destLatitude = "";
    public String paymentId = "";
    public String vehicleType = "";
    //cancl-> C -> success -> A
       public String status = "";
    public String createDate = "";
    public String modifiedDate = "";
    public String corpCustId = "";
    public String srcPlace = "";
    public String destPlace = "";
    public String finalFare = "";
    public String driverName = "";
    public String driverPhoto = "";
    public String accountType = "";


    public String car="";
}
