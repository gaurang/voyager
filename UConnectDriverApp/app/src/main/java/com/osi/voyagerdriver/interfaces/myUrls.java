package com.osi.voyagerdriver.interfaces;

public interface myUrls {

    //String WEB_SERVER_URL = "http://49.248.144.198";
    String WEB_SERVER_URL = "http://35.188.64.29";
    //String WEB_SERVER_URL= "http://jbossews-sidhvi.rhcloud.com";
    String WEB_SERVER_POST = ":8080";
    String WEB_SERVER_BASE_PATH = "/voyager" + "/api/cse/";
    // Local
    //String ServerName = "http://192.168.0.4:8080/uconnect/api/";
    //Live

    String PICBASEURL = WEB_SERVER_URL + "/uc";
    //String ServerName = WEB_SERVER_URL + WEB_SERVER_POST + WEB_SERVER_BASE_PATH;
    String ServerName = WEB_SERVER_URL + WEB_SERVER_BASE_PATH;
    //////////////////////////////////////////////
    String LOGINAPI = ServerName + "signin";
    String GOONLINEMODE = ServerName + "dlocOn";
    String GOOFFILNEMODE = ServerName + "dlocOff";
    String SENDLOCATIONFORSHOW = ServerName + "dloc";
    //////////////////////////////////////////////
    String GenerateCSRFToken = ServerName + "generate-token";
    String REGISTEREAPI = ServerName + "register";
    String UPDATEPROFILE = ServerName + "saveProfile";
    String UPLOADPROFILEPIC = ServerName + "uploadPicFile";
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
    String CHANGEPASSWORDS = ServerName + "cp";
    String ADDUPDATEACCOUNTPROFILE = ServerName + "addUpdateAccount";
    String PaymentTPROFILE = ServerName + "getAccounts";
    String APPLYPROMOCODE = ServerName + "applyPromo";
    String GETHELP = ServerName + "getHelp";
    String GETHISTORY = ServerName + "getBookingHistory";
    String GETHISTORYDETAILS = ServerName + "getBookingDetails";
    String GETDRIVERDETAILS=ServerName + "getDriverDetails";
    String GETSTARTRIDE=ServerName + "rideStart";
    String BOOKINGACCEPTREJECT= ServerName+"bookingAcceptReject";
    String GETSTOPRIDE= ServerName + "rideEnd";
    String DRIVERRATING= ServerName + "driverRating";
    String CHANGEVEHICLE= ServerName + "getVehicleList";
    String ABOUTUS= ServerName + "aboutus";
    String GETPROFILEPIC = ServerName + "getDrPic";
    String GETMAP = ServerName + "getMap";
    String GETPIC = ServerName + "getPic";

}
