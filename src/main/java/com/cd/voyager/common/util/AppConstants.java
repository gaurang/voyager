package com.cd.voyager.common.util;

public class AppConstants {
	
    public static final Integer USER_TYPE_DRIVER = 1;
    
    
    

    public static final String DRIVERTYPE_TAXI = "A";
    
    public static final String DRIVERTYPE_PRIVATE = "B";
    
    public static final String DRIVERTYPE_HIRE = "C";

    public static final String CONFIG_DELEMETER = "-";
    
    
    //Mobile status code
    public static final String GLOBAL_SUCCESS = "200";
    public static final String GLOBAL_REDIRACTION = "300";
    public static final String GLOBAL_PAGE_ERROR = "400";
    public static final String GLOBAL_SERVER_ERROR = "500";
    public static final String GLOBAL_FAIL = "450";
    public static final String GLOBAL_SESSION_TIMEOUT = "440";
    
    public static final String GLOBAL_PAGE_NOT_FOUND = "404";
    public static final String ERROR_DUPLICATE_ENRTY = "400";
    
    public static final String ERROR_MISSING_INPUT = "420";
    public static final String ERROR_INVALID_INPUT = "420";
    
    public static final String ERROR_INVALID_SERVICE_CALL = "400";
    
    public static final String ERROR_INVALID_OTP = "420";
    
    //resource bundle Message code
    //
    //public static final String OTP_TPL = "";


    //Status
    public static final String STATUS_ACTIVE = "A";	
    public static final String STATUS_INACTIVE = "I";
    public static final String STATUS_LOCKED = "L";
    public static final String STATUS_RIDING = "R";
    public static final String STATUS_ACCEPT = "ACC";
    public static final String STATUS_REJECT = "REJ";
    
    //Booking Status
    public static final String STATUS_BOOKED = "BKD";	
    public static final String STATUS_CANCELLED = "CAN";
    public static final String STATUS_REJECTED = "REJ";
    public static final String STATUS_NOT_AVAILABLE = "NA";
    public static final String STATUS_CONFIRMED = "CON";
    public static final String STATUS_COMPLETE = "COM";
    public static final String STATUS_STARTED = "STR";
    
    
    //deletflag
    public static final Integer DELETEFLAG_DELETED = 1;
    public static final Integer DELETEFLAG_ALIVE = 0;
    
    //Gen Status
    public static final Integer STATUS_GLOBAL_ON = 1;
    public static final Integer STATUS_GLOBAL_OFF  = 0;
    
    //Account Type
    
    public static final String ACCOUNT_TYPE_CORPORATE = "C";
    public static final String ACCOUNT_TYPE_PERSONAL = "P";
    
    
    //SignUp
    public static final Integer CUSTOMER_CORP_EMAIL_SIGNUP_FAIL = -1;
    public static 	 Integer CUSTOMER_SIGNUP_SUCCESS = 1;
    

    public static final String LOGIN_WITH_FACEBOOK = "F";
    public static final String LOGIN_WITH_GOOGLE = "G";

    
    
    public static final String REFERAL_CODE_FORMAT = "UC";
    public static final String REFERAL_DRIVER_FORMAT = "UD";
    public static final String REFERAL_CORPORATE_FORMAT = "UB";
    
    public static final String REF_CODE_APPLIED = "APP";	
    public static final String REF_CODE_EXP = "EXP";
    public static final String REF_CODE_ENCASHED = "ENC";

    public static final String PROMO_TYPE_REF = "REF";
    public static final String PROMO_TYPE_CPN = "CPN";
    public static final String PROMO_REWARD_DISCOUNT = "DISC";
    public static final String PROMO_REWARD_CASHBACK = "CASH";

        
    public static final Integer INVALID_REFERAL_CODE = -1;
    public static final Integer INVALID_PROMO_CODE = 0;
    public static final Integer DUPLICATE_PROMO_CODE = -2;

    
    public static final Integer VALID_ENTRY = 1;
    public static final Integer INVALID_ENTRY = -1;
    public static final Integer NO_ROW_UPDATE = 0;

   
    public static final Integer REF_CODE_DEFAULT = 0;

    
    //Applied Rules
    
    public static final String PROMOCODE_APPLIED_RULE_ONCE = "ONCE";
    public static final String PROMOCODE_APPLIED_RULE_DAILY = "DAILY";
    public static final String PROMOCODE_APPLIED_RULE_WEEKLY = "WEEKLY";
    public static final String PROMOCODE_APPLIED_RULE_MONTHLY = "MONTHLY";
    public static final String PROMOCODE_APPLIED_RULE_YEARLY = "YEARLY";
    
    public static final Integer NO_FILTER = -1;
    
    
    //
    public static final String GATEWAY_PAYWAY = "PAYWAY";
    public static final String GATEWAY_PAYPAL = "PAYPAL";
    
    //
    public static final String ASSET_ACTIVE = "A";
    public static final String ASSET_INACTIVE = "IA";
    
    //
    public static final String PAYMENT_PENDING = "PEND";
    
}
