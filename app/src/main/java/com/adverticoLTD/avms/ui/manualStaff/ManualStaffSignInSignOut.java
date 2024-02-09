package com.adverticoLTD.avms.ui.manualStaff;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.adverticoLTD.avms.BuildConfig;
import com.adverticoLTD.avms.MyApplication;
import com.adverticoLTD.avms.R;
import com.adverticoLTD.avms.baseClasses.BaseActivity;
import com.adverticoLTD.avms.data.getStaffList.StaffSignParamModel;
import com.adverticoLTD.avms.data.getStaffList.StaffSignRequestModel;
import com.adverticoLTD.avms.data.getStaffList.StaffSignResponseModel;
import com.adverticoLTD.avms.data.nameList.NameListDataModel;
import com.adverticoLTD.avms.data.scanQrCode.ScanQrCodeRequestModel;
import com.adverticoLTD.avms.data.scanQrCode.ScanQrCodeRequestParamModel;
import com.adverticoLTD.avms.data.scanQrCode.ScanQrCodeResponseDataModel;
import com.adverticoLTD.avms.data.scanQrCode.ScanQrCodeResponseModel;
import com.adverticoLTD.avms.helpers.ConstantClass;
import com.adverticoLTD.avms.helpers.DateTimeUtils;
import com.adverticoLTD.avms.helpers.PreferenceKeys;
import com.adverticoLTD.avms.jobQueue.PrintBadgeJob;
import com.adverticoLTD.avms.network.RetrofitClient;
import com.adverticoLTD.avms.network.RetrofitInterface;
import com.adverticoLTD.avms.network.utils.WebApiHelper;
import com.adverticoLTD.avms.ui.Utils;
import com.adverticoLTD.avms.ui.dashboardScreen.DashboardActivity;
import com.adverticoLTD.avms.ui.thankYouSceen.ThankYouScreen;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManualStaffSignInSignOut extends BaseActivity {

    @BindView(R.id.edtStaffName)
    AutoCompleteTextView edtStaffName;

    @BindView(R.id.imgBack)
    ImageView imgBack;

    @BindView(R.id.loutImgSignIn)
    LinearLayout loutImgSignIn;

    @BindView(R.id.loutImgSignOut)
    LinearLayout loutImgSignOut;

    ArrayList<NameListDataModel> arrStaffList = new ArrayList<NameListDataModel>();
    String selectedStaffID = "";
    String staffStatus = ConstantClass.STAFF_ALL;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_staff_manual_sign_in_out;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIntentData();

        registerClickListener();

        getStaffRecords();

        setUpDataAdapter();
    }

    private void getStaffRecords() {

        showProgressBar();

        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.getSigneStaffList(Prefs.getString(PreferenceKeys.PREF_ACCESS_TOKEN, ""),
                DateTimeUtils.getCurrentDateHeader(), getStaffSelectionRequest()).enqueue(new Callback<StaffSignResponseModel>() {
            @Override
            public void onResponse(Call<StaffSignResponseModel> call, Response<StaffSignResponseModel> response) {
                hideProgressBar();
                if (response.isSuccessful()) {
                    StaffSignResponseModel responseModel = response.body();
                    if (responseModel != null) {
                        if (responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS)) {
                            arrStaffList.clear();
                            arrStaffList.addAll(responseModel.getData());
                            setUpDataAdapter();
                        } else {
                            showErrorMessage();
                        }
                    } else {
                        showErrorMessage();
                    }

                } else {
                    showErrorMessage();
                }
            }

            @Override
            public void onFailure(Call<StaffSignResponseModel> call, Throwable t) {

            }
        });

    }

    private StaffSignRequestModel getStaffSelectionRequest() {


        StaffSignRequestModel requestModel = new StaffSignRequestModel();
        StaffSignParamModel staffSignParamModel = new StaffSignParamModel();
        staffSignParamModel.setStaff_status(staffStatus);
        requestModel.setParam(staffSignParamModel);
        return requestModel;
    }

    private void setUpDataAdapter() {
        edtStaffName.setThreshold(2);
        NameListAdapter nameListAdapter = new NameListAdapter(this, arrStaffList);
        edtStaffName.setAdapter(nameListAdapter);
    }

    private void registerClickListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loutImgSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSignInOutMethodForManual(selectedStaffID);
            }
        });

        loutImgSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSignInOutMethodForManual(selectedStaffID);
            }
        });

        edtStaffName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                hideKeyBoard();
                for (int i = 0; i < arrStaffList.size(); i++) {
                    if (arrStaffList.get(i).getName().toLowerCase().equals
                            (edtStaffName.getText().toString().toLowerCase().trim())) {
                        selectedStaffID = arrStaffList.get(i).getId() + "@" + arrStaffList.get(i).getUser_type();
                        break;
                    }
                }
            }
        });
    }

    private void callSignInOutMethodForManual(String selectedStaffID) {
        showProgressBar();

        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.scanQrCode(Prefs.getString(PreferenceKeys.PREF_ACCESS_TOKEN, ""),
                DateTimeUtils.getCurrentDateHeader(), getScanQrCodeRequest(selectedStaffID)).enqueue(new Callback<ScanQrCodeResponseModel>() {
            @Override
            public void onResponse(Call<ScanQrCodeResponseModel> call, Response<ScanQrCodeResponseModel> response) {
                if (response.isSuccessful()) {
                    ScanQrCodeResponseModel responseModel = response.body();
                    if (responseModel != null) {
                        if (responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS_SIGN_IN)) {


                            ScanQrCodeResponseDataModel responseDataModel = responseModel.getData();



                            boolean isStaff = responseDataModel.getVisitor_id().endsWith("@3");

                            Intent intent = new Intent(ManualStaffSignInSignOut.this, ThankYouScreen.class);
                            intent.putExtra(ConstantClass.EXTRAA_VIEW_USER_NAME, responseModel.getData().getName());
                            intent.putExtra(ConstantClass.EXTRAA_VIEW_SCAN_STATUS, responseModel.getStatus());
                            startActivity(intent);
                            setResult(RESULT_OK);
                            finish();

                        } else if (responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS_SIGN_OUT)) {

                            Intent intent = new Intent(ManualStaffSignInSignOut.this, ThankYouScreen.class);
                            intent.putExtra(ConstantClass.EXTRAA_VIEW_USER_NAME, responseModel.getData().getName());
                            intent.putExtra(ConstantClass.EXTRAA_VIEW_SCAN_STATUS, responseModel.getStatus());
                            startActivity(intent);
                            setResult(RESULT_OK);
                            finish();

                        } else {
                            showToastMessage(getString(R.string.error_msg_scan_qr_code));
                        }


                    }
                }else if (response.code() == ConstantClass.RESPONSE_UNAUTHORIZED
                        || response.code() == ConstantClass.RESPONSE_UNAUTHORIZED_FOR) {
                    getAccessKeyToken();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    callSignInOutMethodForManual(selectedStaffID);

                }
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<ScanQrCodeResponseModel> call, Throwable t) {
                t.printStackTrace();
                hideProgressBar();
            }
        });
    }

    private ScanQrCodeRequestModel getScanQrCodeRequest(String scannedID) {
        ScanQrCodeRequestModel requestModel = new ScanQrCodeRequestModel();
        ScanQrCodeRequestParamModel paramModel = new ScanQrCodeRequestParamModel();
        paramModel.setDevice_type(WebApiHelper.DEVICE_TYPE_TAB);
        paramModel.setUser_id(scannedID);
        requestModel.setParam(paramModel);
        return requestModel;
    }


    private void getIntentData() {
        int actionType = getIntent().getIntExtra(ConstantClass.EXTRAA_SIGN_IN_OUT, 0);
        if (actionType == ConstantClass.REQUEST_SIGN_IN) {
            loutImgSignIn.setVisibility(View.VISIBLE);
            loutImgSignOut.setVisibility(View.GONE);
            staffStatus = ConstantClass.STAFF_SIGNED_OUT;
        } else if (actionType == ConstantClass.REQUEST_SIGN_OUT) {
            loutImgSignIn.setVisibility(View.GONE);
            loutImgSignOut.setVisibility(View.VISIBLE);
            staffStatus = ConstantClass.STAFF_SIGNED_IN;
        }
    }
}
