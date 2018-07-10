package com.app.uconect.interfaces;

public interface myUrls {

    //String WEB_SERVER_URL = "http://49.248.144.198";
    //String WEB_SERVER_URL = "http://192.168.1.105";
    String WEB_SERVER_POST = ":8080";
    String WEB_SERVER_URL= "http://jbossews-sidhvi.rhcloud.com";
    String WEB_SERVER_BASE_PATH = "/uc/api/";
    // Local
    //String ServerName = "http://192.168.0.4:8080/uconnect/api/";
    //Live

    String PICBASEURL = WEB_SERVER_URL + "/uc";
    //String ServerName = WEB_SERVER_URL +WEB_SERVER_POST+ WEB_SERVER_BASE_PATH;
    String ServerName = WEB_SERVER_URL + WEB_SERVER_BASE_PATH;
    String GenerateCSRFToken = ServerName + "generate-token";
    String REGISTEREAPI = ServerName + "register";
    String UPDATEPROFILE = ServerName + "saveProfile";
    String UPLOADPROFILEPIC = ServerName + "uploadPicFile";
    String GETPROFILEPIC = ServerName + "getPic";
    String GETDRIVERPIC = ServerName + "getDrPic";
    String GETMAP = ServerName + "getMap";
    //    String UPLOADPROFILEPIC = ServerName + "uploadPic";
    String VERIFYMOBILE = ServerName + "verifyOTP";
    String RESENDOTP = ServerName + "resentOTP";
    String FORGOTPASSWORD = ServerName + "forgotPwOTP";
    String ADDPAYMENTUSER = ServerName + "register/customer";
    String ADDPAYMENT = ServerName + "payment";
    String DELETEPAYMENT = ServerName + "delPayment";
    String GETPAYMENTUSER = ServerName + "getPayments";
    String ADDEMERGENCYCONTACT = ServerName + "addEContact";
    String GETEMERGENCYCONTACT = ServerName + "getEContact";
    String DELETEEMERGENCYCONTACT = ServerName + "delEContact";
    String ADDFAVOURITEPLACES = ServerName + "addPref";
    String GETFAVOURITEPLACES = ServerName + "getPref";
    String DELFAVOURITEPLACES = ServerName + "delPref";
    String CHANGEPASSWORDS = ServerName + "cp";
    String ADDUPDATEACCOUNTPROFILE = ServerName + "addUpdateAccount";
    String VERIFYEMAILID = ServerName + "verifyEmailId";
    String PaymentTPROFILE = ServerName + "getAccounts";
    String APPLYPROMOCODE = ServerName + "applyPromo";
    String GETHELP = ServerName + "getHelp";
    String GETHISTORY = ServerName + "getBookingHistory";
    String GETBOOKINGHISTORY = ServerName + "getBookingDetails";
    String LOGINAPI = ServerName + "signin";
    String GETDATAFORCARS = ServerName + "getCSE";
    String RIDEESTAMATE = ServerName + "rideEstimate";
    String FARECARD = ServerName + "fareCard";
    String CONFIG = ServerName + "config";
    String CHANGEDEFAULTACCOUNT = ServerName + "changeDefaultAccount";
    String CONFIRM = ServerName + "book";
    String TRACKRIDE=ServerName + "trackBooking";
    String STARTUPCHECK=ServerName + "startupCheck";
    String CPOTP=ServerName + "cpOTP";
    String CUSTOMERRATING=ServerName + "customerRating";
    String NOTIFICATION=ServerName + "notification";
    String ABOUTUS=ServerName + "aboutus";
}
