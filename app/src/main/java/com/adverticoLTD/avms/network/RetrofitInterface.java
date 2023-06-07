package com.adverticoLTD.avms.network;


import com.adverticoLTD.avms.data.companies.CompanyListResponseModel;
import com.adverticoLTD.avms.data.disclaimerMessage.DisclaimerMessageResponseModel;
import com.adverticoLTD.avms.data.disclaimerMessage.DisclaimerRequestModel;
import com.adverticoLTD.avms.data.existingContractor.ExistingContractorRequestModel;
import com.adverticoLTD.avms.data.existingContractor.ExistingContractorResponseModel;
import com.adverticoLTD.avms.data.getSignedInRecords.SignedInRecordResponseModel;
import com.adverticoLTD.avms.data.getSignedInRecords.SignedInRecordsParamModel;
import com.adverticoLTD.avms.data.getSignedInRecords.SignedInRecordsRequestModel;
import com.adverticoLTD.avms.data.normalContractor.NormalContractorRequestModel;
import com.adverticoLTD.avms.data.normalContractor.NormalContractorResponseModel;
import com.adverticoLTD.avms.data.normalVisitor.NormalVisitorRequestModel;
import com.adverticoLTD.avms.data.normalVisitor.NormalVisitorResponseModel;
import com.adverticoLTD.avms.data.scanQrCode.ScanQrCodeRequestModel;
import com.adverticoLTD.avms.data.scanQrCode.ScanQrCodeResponseModel;
import com.adverticoLTD.avms.data.stafflist.StaffListRequestModel;
import com.adverticoLTD.avms.data.stafflist.StaffListResponseModel;
import com.adverticoLTD.avms.network.utils.WebApiHelper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST(WebApiHelper.getDisclaimerMessage)
    Call<DisclaimerMessageResponseModel> getDisclaimerMessage(@Body DisclaimerRequestModel requestModel);

    @POST(WebApiHelper.scanQrCodeSignInOut)
    Call<ScanQrCodeResponseModel> scanQrCode(@Body ScanQrCodeRequestModel requestModel);

    @GET(WebApiHelper.getCompanies)
    Call<CompanyListResponseModel> getCompanies();

    @POST(WebApiHelper.getStaffList)
    Call<StaffListResponseModel> getStaffList(@Body StaffListRequestModel requestModel);

    @POST(WebApiHelper.insertUser)
    Call<NormalContractorResponseModel> insertUser(@Body NormalContractorRequestModel requestModel);


    @POST(WebApiHelper.insertNormalVisitor)
    Call<NormalVisitorResponseModel> insertNormalVisitor(@Body NormalVisitorRequestModel requestModel);

    @POST(WebApiHelper.signInUser)
    Call<ExistingContractorResponseModel> existingContractorSignIn(@Body ExistingContractorRequestModel requestModel);

    @POST(WebApiHelper.getSignedInRecords)
    Call<SignedInRecordResponseModel> getSignedInRecords(@Body SignedInRecordsRequestModel requestModel);


}
