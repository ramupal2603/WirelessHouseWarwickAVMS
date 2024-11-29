package com.adverticoLTD.avms.network;


import com.adverticoLTD.avms.data.acesstoken.AccessTokenResponseModel;
import com.adverticoLTD.avms.data.companies.CompanyListResponseModel;
import com.adverticoLTD.avms.data.contractorStatus.ContractorRequestModel;
import com.adverticoLTD.avms.data.contractorStatus.ContractorResponseModel;
import com.adverticoLTD.avms.data.delivery.DeliveryListingResponseModel;
import com.adverticoLTD.avms.data.delivery.DeliveryRequestModel;
import com.adverticoLTD.avms.data.delivery.DeliveryResponseModel;
import com.adverticoLTD.avms.data.delivery.DeliverySignInRequestModel;
import com.adverticoLTD.avms.data.disclaimerMessage.DisclaimerMessageResponseModel;
import com.adverticoLTD.avms.data.disclaimerMessage.DisclaimerRequestModel;
import com.adverticoLTD.avms.data.existingContractor.ExistingContractorRequestModel;
import com.adverticoLTD.avms.data.existingContractor.ExistingContractorResponseModel;
import com.adverticoLTD.avms.data.getSignedInRecords.SignedInRecordResponseModel;
import com.adverticoLTD.avms.data.getSignedInRecords.SignedInRecordsRequestModel;
import com.adverticoLTD.avms.data.getStaffList.StaffSignRequestModel;
import com.adverticoLTD.avms.data.getStaffList.StaffSignResponseModel;
import com.adverticoLTD.avms.data.normalContractor.NormalContractorRequestModel;
import com.adverticoLTD.avms.data.normalContractor.NormalContractorResponseModel;
import com.adverticoLTD.avms.data.normalVisitor.NormalVisitorRequestModel;
import com.adverticoLTD.avms.data.normalVisitor.NormalVisitorResponseModel;
import com.adverticoLTD.avms.data.patientVisitor.PatientVisitorRequestModel;
import com.adverticoLTD.avms.data.patientVisitor.PatientVisitorResponseModel;
import com.adverticoLTD.avms.data.scanQrCode.ScanQrCodeRequestModel;
import com.adverticoLTD.avms.data.scanQrCode.ScanQrCodeResponseModel;
import com.adverticoLTD.avms.data.siteList.SiteListResponseModel;
import com.adverticoLTD.avms.data.stafflist.StaffListRequestModel;
import com.adverticoLTD.avms.data.stafflist.StaffListResponseModel;
import com.adverticoLTD.avms.network.utils.WebApiHelper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RetrofitInterface {


    @GET(WebApiHelper.getTokenAccesskey)
    Call<AccessTokenResponseModel> getTokenAccesskey(@Header("Accesskey") String basicToken,
                                                     @Header("Accesskeydate") String dateTime);


    @POST(WebApiHelper.getDisclaimerMessage)
    Call<DisclaimerMessageResponseModel> getDisclaimerMessage(@Header("Accesskey") String basicToken,
                                                              @Header("Accesskeydate") String dateTime,
                                                              @Body DisclaimerRequestModel requestModel);

    @POST(WebApiHelper.scanQrCodeSignInOut)
    Call<ScanQrCodeResponseModel> scanQrCode(@Header("Accesskey") String basicToken,
                                             @Header("Accesskeydate") String dateTime,
                                             @Body ScanQrCodeRequestModel requestModel);

    @GET(WebApiHelper.getCompanies)
    Call<CompanyListResponseModel> getCompanies(@Header("Accesskey") String basicToken,
                                                @Header("Accesskeydate") String dateTime);

    @POST(WebApiHelper.getStaffList)
    Call<StaffListResponseModel> getStaffList(@Header("Accesskey") String basicToken,
                                              @Header("Accesskeydate") String dateTime,
                                              @Body StaffListRequestModel requestModel);

    @POST(WebApiHelper.insertUser)
    Call<NormalContractorResponseModel> insertUser(@Header("Accesskey") String basicToken,
                                                   @Header("Accesskeydate") String dateTime,
                                                   @Body NormalContractorRequestModel requestModel);


    @POST(WebApiHelper.insertNormalVisitor)
    Call<NormalVisitorResponseModel> insertNormalVisitor(@Header("Accesskey") String basicToken,
                                                         @Header("Accesskeydate") String dateTime,
                                                         @Body NormalVisitorRequestModel requestModel);

    @POST(WebApiHelper.signInUser)
    Call<ExistingContractorResponseModel> existingContractorSignIn(@Header("Accesskey") String basicToken,
                                                                   @Header("Accesskeydate") String dateTime,
                                                                   @Body ExistingContractorRequestModel requestModel);

    @POST(WebApiHelper.getSignedInRecords)
    Call<SignedInRecordResponseModel> getSignedInRecords(@Header("Accesskey") String basicToken,
                                                         @Header("Accesskeydate") String dateTime,
                                                         @Body SignedInRecordsRequestModel requestModel);

    @POST(WebApiHelper.getDeliveryListing)
    Call<DeliveryListingResponseModel> getDeliveryListing(@Header("Accesskey") String basicToken,
                                                          @Header("Accesskeydate") String dateTime);

    @POST(WebApiHelper.sendDeliveryEmail)
    Call<DeliveryResponseModel> sendDeliveryEmail(@Header("Accesskey") String basicToken,
                                                  @Header("Accesskeydate") String dateTime,
                                                  @Body DeliveryRequestModel requestModel);

    @POST(WebApiHelper.deliverySignin)
    Call<DeliveryResponseModel> deliverySignIn(@Header("Accesskey") String basicToken,
                                               @Header("Accesskeydate") String dateTime,
                                               @Body DeliverySignInRequestModel requestModel);

    @POST(WebApiHelper.getSignedStaffList)
    Call<StaffSignResponseModel> getSigneStaffList(@Header("Accesskey") String basicToken,
                                                   @Header("Accesskeydate") String dateTime,
                                                   @Body StaffSignRequestModel requestModel);

    @GET(WebApiHelper.GET_SITE_LIST)
    Call<SiteListResponseModel> getSiteList();


    @POST(WebApiHelper.insertPatientVisitor)
    Call<PatientVisitorResponseModel> insertPatientVisitor(@Header("Accesskey") String basicToken,
                                                           @Header("Accesskeydate") String dateTime,
                                                           @Body PatientVisitorRequestModel requestModel);
    @POST(WebApiHelper.getContractorStatus)
    Call<ContractorResponseModel> getContractorStatus(@Body ContractorRequestModel requestModel,
                                                      @Header("Accesskey") String basicToken,
                                                      @Header("Accesskeydate") String dateTime);

}
