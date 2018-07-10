package com.app.voyager.Dataset;

import java.io.Serializable;

/**
 * Created by osigroups on 1/24/2016.
 */
public class AddedPaymentData extends Throwable implements Serializable{
    public int  id = 0,defalt_payment=0;
    public String paymentId = "" ,customerId = "",email="";
    public String gatewayId = "",accountType="",corporateemail="",corporategateway="";
}
