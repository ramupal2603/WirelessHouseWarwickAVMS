package com.adverticoLTD.avms.keyLogSolution.network;

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
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {


    @Multipart
    @POST(WebApiHelper.UPLOAD_SIGNATURE)
    Call<UploadFileResponseModel> uploadFile(@Part MultipartBody.Part signature1);


    @GET(WebApiHelper.STAFF_LIST)
    Call<StaffListResponseModel> getStaffList();


    @POST(WebApiHelper.SIGN_OUT)
    Call<KeySignOutResponseModel> keySignout(@Body KeySignOutRequestModel requestModel);


    @POST(WebApiHelper.SIGN_IN)
    Call<KeySigninResponseModel> keySignIn(@Body KeySignInRequestModel requestModel);

    @GET(WebApiHelper.KEY_LIST_SIGN_OUT)
    Call<KeyResponseModel> getKeyList();

    @GET(WebApiHelper.KEY_LIST_SIGN_IN)
    Call<KeyResponseModel> getKeySignInList();

}
