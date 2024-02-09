package com.adverticoLTD.avms.ui.manualDashboard;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.adverticoLTD.avms.R;
import com.adverticoLTD.avms.baseClasses.BaseActivity;
import com.adverticoLTD.avms.data.getSignedInRecords.SignedInRecordResponseModel;
import com.adverticoLTD.avms.data.getSignedInRecords.SignedInRecordsDataModel;
import com.adverticoLTD.avms.data.getSignedInRecords.SignedInRecordsParamModel;
import com.adverticoLTD.avms.data.getSignedInRecords.SignedInRecordsRequestModel;
import com.adverticoLTD.avms.helpers.ConstantClass;
import com.adverticoLTD.avms.helpers.DateTimeUtils;
import com.adverticoLTD.avms.helpers.PreferenceKeys;
import com.adverticoLTD.avms.network.RetrofitClient;
import com.adverticoLTD.avms.network.RetrofitInterface;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManualSignInOutView extends BaseActivity {

    @BindView(R.id.edtVisitorName)
    AutoCompleteTextView edtVisitorName;

    @BindView(R.id.imgBack)
    ImageView imgBack;

    @BindView(R.id.loutImgSignIn)
    LinearLayout loutImgSignIn;

    ArrayList<SignedInRecordsDataModel> arrVisitorList = new ArrayList<SignedInRecordsDataModel>();
    String selectedVisitorID = "";
    private String userType = ConstantClass.VISITOR_TYPE;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_visitor_manual_sign_out;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIntentData();

        registerClickListener();

        getVisitorRecords(userType);

        setUpDataAdapter();

    }

    private void getIntentData() {
        userType = getIntent().getStringExtra(ConstantClass.EXTRAA_USER_TYPE);
    }

    private void getVisitorRecords(String userType) {

        showProgressBar();

        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.getSignedInRecords(Prefs.getString(PreferenceKeys.PREF_ACCESS_TOKEN, ""),
                DateTimeUtils.getCurrentDateHeader(), getRequestModel(userType)).enqueue(new Callback<SignedInRecordResponseModel>() {
            @Override
            public void onResponse(Call<SignedInRecordResponseModel> call, Response<SignedInRecordResponseModel> response) {
                hideProgressBar();
                if (response.isSuccessful()) {
                    SignedInRecordResponseModel responseModel = response.body();
                    if (responseModel != null) {
                        if (responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS)) {
                            arrVisitorList.clear();
                            arrVisitorList.addAll(responseModel.getData());
                            setUpDataAdapter();
                        } else {
                            showErrorMessage();
                        }
                    } else {
                        showErrorMessage();
                    }

                } else if (response.code() == ConstantClass.RESPONSE_UNAUTHORIZED
                        || response.code() == ConstantClass.RESPONSE_UNAUTHORIZED_FOR) {
                    getAccessKeyToken();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getVisitorRecords(userType);

                } else {
                    showErrorMessage();
                }
            }

            @Override
            public void onFailure(Call<SignedInRecordResponseModel> call, Throwable t) {
                t.printStackTrace();
                hideProgressBar();
            }
        });

    }

    private SignedInRecordsRequestModel getRequestModel(String userType) {


        SignedInRecordsRequestModel requestModel = new SignedInRecordsRequestModel();
        SignedInRecordsParamModel paramModel = new SignedInRecordsParamModel();
        paramModel.setVisitor_type_mode(userType);
        requestModel.setParam(paramModel);
        return requestModel;
    }

    private void setUpDataAdapter() {
        edtVisitorName.setThreshold(2);
        VisitorSurnameListAdapter visitorSurnameListAdapter = new VisitorSurnameListAdapter(this, arrVisitorList);
        edtVisitorName.setAdapter(visitorSurnameListAdapter);
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
                scanQrCodeForSignOut(selectedVisitorID);
            }
        });

        edtVisitorName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                hideKeyBoard();
                for (int i = 0; i < arrVisitorList.size(); i++) {
                    String visitorName = String.format("%s %s", arrVisitorList.get(i).getFirst_name(),
                            arrVisitorList.get(i).getSur_name());
                    if (visitorName.toLowerCase().equals
                            (edtVisitorName.getText().toString().toLowerCase().trim())) {
                        selectedVisitorID = arrVisitorList.get(i).getVisitor_id();
                        break;
                    }
                }
            }
        });
    }


}
