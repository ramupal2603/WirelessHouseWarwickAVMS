package com.adverticoLTD.avms.network.utils;

public class WebApiHelper {

    //Method Listing
    public static final String getDisclaimerMessage = "getMessage";
    public static final String scanQrCodeSignInOut = "scanQrcode";
    public static final String getCompanies = "getCompanylist";
    public static final String getStaffList = "getStafflist";
    public static final String insertUser = "insertUser";
    public static final String insertNormalVisitor = "insertNormalvisitor";
    public static final String signInUser = "signinUser";
    public static final String getSignedInRecords = "getSigninrecords";
    public static final String getTokenAccesskey = "getAccesskey";

    // Device Type
    public static final String DEVICE_TYPE_TAB = "1";
    public static final String DEVICE_TYPE_MOBILE = "2";


    //Message TYPE
    public static final String MSG_TYPE_MARQUEE = "1";
    public static final String MSG_TYPE_DISCLAIMER = "2";

    //BaseUrl of Repository
    public static String BASE_URL = "http://adverticoavms.co.uk/Demo/Webservice/";
    //public static String BASE_URL = "https://adverticoavms.co.uk/Bruntwood/Webservice/";


}
