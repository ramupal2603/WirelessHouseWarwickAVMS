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
    public static final String sendDeliveryEmail = "sendDeliveryemail";
    public static final String getDeliveryListing = "getCourierdeliverylist";
    public static final String deliverySignin = "deliverySignin";
    public static final String GET_SITE_LIST = "getSitelist";
    public static final String getSignedStaffList = "getSignedstafflist";
    public static final String insertPatientVisitor = "insertPatientvisitor";

    // Device Type
    public static final String DEVICE_TYPE_TAB = "1";
    public static final String DEVICE_TYPE_MOBILE = "2";


    //Message TYPE
    public static final String MSG_TYPE_MARQUEE = "1";
    public static final String MSG_TYPE_DISCLAIMER = "2";

    //Induction PDF
    public static String[] pdfURL = {"https://adverticoavms.co.uk/Cathedralsquare/Uploads/settings/CathedralSquare.pdf",};

    //BaseUrl of Repository
   // public static String BASE_URL = "http://adverticoavms.co.uk/Demo/Webservice/";
    public static String BASE_URL = "https://adverticoavms.co.uk/Cardinalhouse/Webservice/";


}
