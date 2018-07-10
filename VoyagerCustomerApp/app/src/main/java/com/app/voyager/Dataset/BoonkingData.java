package com.app.voyager.Dataset;

import java.math.BigDecimal;

/**
 * Created by osigroups on 3/13/2016.
 */
public class BoonkingData {
    public Integer bookingId;
    public Integer customerId;
    public Integer corpCustId;
    public String sourceLatitude;
    public String sourceLongitude;
    public String destLatitude;
    public String destLongitude;
    public String accountType;
    public String paymentId;
    public String vehicleType;
    public String vehicleName="";
    public Integer promoId;
    public String cancellationReason;
    public BigDecimal cancellationFee;
}
