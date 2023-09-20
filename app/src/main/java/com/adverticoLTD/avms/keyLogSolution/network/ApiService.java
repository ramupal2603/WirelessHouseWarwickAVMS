package com.adverticoLTD.avms.keyLogSolution.network;

import com.adverticoLTD.avms.data.companies.CompanyRequestModel;
import com.adverticoLTD.avms.keyLogSolution.data.keyRefList.KeyResponseModel;
import com.adverticoLTD.avms.keyLogSolution.data.signIn.KeySignInRequestModel;
import com.adverticoLTD.avms.keyLogSolution.data.signIn.KeySigninResponseModel;
import com.adverticoLTD.avms.keyLogSolution.data.signOut.KeySignOutRequestModel;
import com.adverticoLTD.avms.keyLogSolution.data.signOut.KeySignOutResponseModel;
import com.adverticoLTD.avms.keyLogSolution.data.staffList.StaffListResponseModel;
import com.adverticoLTD.avms.keyLogSolution.data.upload.UploadFileResponseModel;
import com.adverticoLTD.avms.keyLogSolution.network.utils.WebApiHelper;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {


    @Multipart
    @POST(WebApiHelper.UPLOAD_SIGNATURE)
    Call<UploadFileResponseModel> uploadFile(@Part MultipartBody.Part signature1);

    @Multipart
    @POST(WebApiHelper.UPLOAD_DELIVERY_SIGNATURE)
    Call<UploadFileResponseModel> uploadDeliveryFile(@Part MultipartBody.Part signature1);


    @GET(WebApiHelper.STAFF_LIST)
    Call<StaffListResponseModel> getStaffList(@Header("Accesskey") String basicToken,
                                              @Header("Accesskeydate") String dateTime,@Body CompanyRequestModel requestModel);


    @POST(WebApiHelper.SIGN_OUT)
    Call<KeySignOutResponseModel> keySignout(@Header("Accesskey") String basicToken,
                                             @Header("Accesskeydate") String dateTime,
                                             @Body KeySignOutRequestModel requestModel);


    @POST(WebApiHelper.SIGN_IN)
    Call<KeySigninResponseModel> keySignIn(@Header("Accesskey") String basicToken,
                                           @Header("Accesskeydate") String dateTime,
                                           @Body KeySignInRequestModel requestModel);

    @GET(WebApiHelper.KEY_LIST_SIGN_OUT)
    Call<KeyResponseModel> getKeyList(@Header("Accesskey") String basicToken,
                                      @Header("Accesskeydate") String dateTime,
                                      @Body CompanyRequestModel requestModel);

    @GET(WebApiHelper.KEY_LIST_SIGN_IN)
    Call<KeyResponseModel> getKeySignInList(@Header("Accesskey") String basicToken,
                                            @Header("Accesskeydate") String dateTime,
                                            @Body CompanyRequestModel requestModel);

}
