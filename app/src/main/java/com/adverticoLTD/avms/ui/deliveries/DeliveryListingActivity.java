package com.adverticoLTD.avms.ui.deliveries;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adverticoLTD.avms.R;
import com.adverticoLTD.avms.baseClasses.BaseActivity;
import com.adverticoLTD.avms.data.delivery.DeliveryListDataModel;
import com.adverticoLTD.avms.data.delivery.DeliveryListingResponseModel;
import com.adverticoLTD.avms.data.delivery.DeliveryParamModel;
import com.adverticoLTD.avms.data.delivery.DeliveryRequestModel;
import com.adverticoLTD.avms.data.delivery.DeliveryResponseModel;
import com.adverticoLTD.avms.data.delivery.DeliverySignInParamModel;
import com.adverticoLTD.avms.data.delivery.DeliverySignInRequestModel;
import com.adverticoLTD.avms.helpers.DateTimeUtils;
import com.adverticoLTD.avms.helpers.PreferenceKeys;
import com.adverticoLTD.avms.interfaces.OnItemClick;
import com.adverticoLTD.avms.keyLogSolution.data.upload.UploadFileResponseModel;
import com.adverticoLTD.avms.keyLogSolution.helpers.ConstantClass;
import com.adverticoLTD.avms.keyLogSolution.network.ApiService;
import com.adverticoLTD.avms.keyLogSolution.network.utils.UploadImageHelpers;
import com.adverticoLTD.avms.keyLogSolution.ui.signInKeyScreen.SignInKeyActivity;
import com.adverticoLTD.avms.keyLogSolution.ui.signatureView.SignatureViewActivity;
import com.adverticoLTD.avms.network.RetrofitClient;
import com.adverticoLTD.avms.network.RetrofitInterface;
import com.adverticoLTD.avms.ui.thankYouSceen.ThankYouScreen;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DeliveryListingActivity extends BaseActivity implements OnItemClick {


    @BindView(R.id.loutResultView)
    LinearLayout loutResultView;

    @BindView(R.id.rcvFireEvacuationList)
    RecyclerView rcvFireEvacuationList;

    @BindView(R.id.txtVisitorListEmptyView)
    TextView txtVisitorListEmptyView;


    ArrayList<DeliveryListDataModel> arrTLTVisitorList;
    DeliveryAdapter deliveryAdapter;
    private static final int REQUEST_SIGN_IN_SIGNATURE = 1001;
    private File file1;
    private String logIDToUpdate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpRecyclerView();

        getDeliveryListing();

    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_delivery_listing_view;
    }

    @Override
    public void onClick(View v) {

    }

    private void setUpRecyclerView() {
        rcvFireEvacuationList.setLayoutManager(new LinearLayoutManager(DeliveryListingActivity.this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    protected void getDeliveryListing() {
        showProgressBar();

        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        Call<DeliveryListingResponseModel> call = retrofitInterface.getDeliveryListing(Prefs.getString(PreferenceKeys.PREF_ACCESS_TOKEN, ""),
                DateTimeUtils.getCurrentDateHeader());
        call.enqueue(new Callback<DeliveryListingResponseModel>() {
            @Override
            public void onResponse(Call<DeliveryListingResponseModel> call, Response<DeliveryListingResponseModel> response) {

                if (response.isSuccessful()) {

                    DeliveryListingResponseModel responseModel = response.body();
                    if (responseModel != null) {
                        if (responseModel.getStatus().equals("1")) {
                            arrTLTVisitorList = new ArrayList<>();
                            arrTLTVisitorList.addAll(responseModel.getData());
                            setTLTVisitorDataAdapter(arrTLTVisitorList);

                            loutResultView.setVisibility(View.VISIBLE);
                            txtVisitorListEmptyView.setVisibility(View.GONE);

                        } else {
                            loutResultView.setVisibility(View.GONE);
                            txtVisitorListEmptyView.setVisibility(View.VISIBLE);

                        }
                    }

                } else {
                    showToastMessage("Unable to get List");
                }

                hideProgressBar();
            }

            @Override
            public void onFailure(Call<DeliveryListingResponseModel> call, Throwable t) {
                hideProgressBar();
                showToastMessage("" + t.getLocalizedMessage());
            }
        });
    }

    private void setTLTVisitorDataAdapter(ArrayList<DeliveryListDataModel> arrTLTVisitorList) {
        deliveryAdapter = new DeliveryAdapter(DeliveryListingActivity.this, arrTLTVisitorList,DeliveryListingActivity.this);
        rcvFireEvacuationList.setAdapter(deliveryAdapter);
    }


    @Override
    public void OnRecordClickListener(String logID, int index) {
        if (!logID.isEmpty()) {
            logIDToUpdate=logID;
            Intent intent = new Intent(DeliveryListingActivity.this, SignatureViewActivity.class);
            startActivityForResult(intent, REQUEST_SIGN_IN_SIGNATURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGN_IN_SIGNATURE && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra("filePath");

            //loads the file
            File file = new File(filePath);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            file1 = new File(filePath);
            uploadfile(file1,"1");
        }

    }

    private void uploadfile(File signature, String fileNo) {

        showProgressBar();

        MultipartBody.Part body = UploadImageHelpers.uploadImage(DeliveryListingActivity.this, signature,
                "signature");

        ApiService apiService = com.adverticoLTD.avms.keyLogSolution.network.RetrofitClient.getRetrofit().create(ApiService.class);
        Call<UploadFileResponseModel> call = apiService.uploadDeliveryFile(body);
        call.enqueue(new Callback<UploadFileResponseModel>() {
            @Override
            public void onResponse(Call<UploadFileResponseModel> call, Response<UploadFileResponseModel> response) {

                hideProgressBar();

                if (response.isSuccessful()) {
                    UploadFileResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCES)) {
                        if (fileNo.equals("1")) {
                            String signature1 = responseModel.getSignature_name();
                            callDeliveryUpdate(logIDToUpdate,signature1);
                        }

                    }
                } else if (response.code() == com.adverticoLTD.avms.helpers.ConstantClass.RESPONSE_UNAUTHORIZED
                        || response.code() == com.adverticoLTD.avms.helpers.ConstantClass.RESPONSE_UNAUTHORIZED_FOR) {
                    getAccessKeyToken();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    uploadfile(signature, fileNo);

                } else {
                    showToastMessage(getResources().getString(R.string.response_error_msg));
                }
            }

            @Override
            public void onFailure(Call<UploadFileResponseModel> call, Throwable t) {
                hideProgressBar();
                printLogMessage("uploadFailed", t.getMessage());
            }
        });
    }


    private void callDeliveryUpdate(String logId,String signature) {
        showProgressBar();
        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.deliverySignIn(Prefs.getString(PreferenceKeys.PREF_ACCESS_TOKEN, ""),
                DateTimeUtils.getCurrentDateHeader(), getDeliveryEmailRequestModel(logId,signature)).enqueue(new Callback<DeliveryResponseModel>() {
            @Override
            public void onResponse(Call<DeliveryResponseModel> call, Response<DeliveryResponseModel> response) {
                if (response.isSuccessful()) {
                    DeliveryResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().equals(com.adverticoLTD.avms.helpers.ConstantClass.RESPONSE_SUCCESS)) {

                        Intent intent = new Intent(DeliveryListingActivity.this, ThankYouScreen.class);
                        intent.putExtra(com.adverticoLTD.avms.helpers.ConstantClass.EXTRAA_VIEW_USER_NAME, "");
                        intent.putExtra(com.adverticoLTD.avms.helpers.ConstantClass.EXTRAA_VIEW_SCAN_STATUS, com.adverticoLTD.avms.helpers.ConstantClass.RESPONSE_DELIVERY_SUCCESS);
                        startActivity(intent);
                        setResult(RESULT_OK);
                        finish();

                    } else {
                        showAlertDialog(getContext(), getResources().getString(R.string.error_already_signed_in));
                    }
                } else if (response.code() == com.adverticoLTD.avms.helpers.ConstantClass.RESPONSE_UNAUTHORIZED
                        || response.code() == com.adverticoLTD.avms.helpers.ConstantClass.RESPONSE_UNAUTHORIZED_FOR) {
                    getAccessKeyToken();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    callDeliveryUpdate(logId,signature);

                }
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<DeliveryResponseModel> call, Throwable t) {
                t.printStackTrace();
                hideProgressBar();
            }
        });
    }

    private DeliverySignInRequestModel getDeliveryEmailRequestModel(String logId,String signature) {

        DeliverySignInRequestModel deliveryRequestModel = new DeliverySignInRequestModel();

        DeliverySignInParamModel deliveryParamModel = new DeliverySignInParamModel();
        deliveryParamModel.setDelivery_signature(signature);
        deliveryParamModel.setKey_log_id(logId);
        deliveryRequestModel.setParam(deliveryParamModel);

        return deliveryRequestModel;
    }

}
