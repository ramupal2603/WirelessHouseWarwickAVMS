package com.adverticoLTD.avms.keyLogSolution.baseClasses;


import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.adverticoLTD.avms.R;
import com.adverticoLTD.avms.data.acesstoken.AccessTokenResponseModel;
import com.adverticoLTD.avms.data.disclaimerMessage.DisclaimerMessageResponseDataModel;
import com.adverticoLTD.avms.data.disclaimerMessage.DisclaimerMessageResponseModel;
import com.adverticoLTD.avms.data.disclaimerMessage.DisclaimerRequestModel;
import com.adverticoLTD.avms.data.disclaimerMessage.DisclaimerRequestParamModel;
import com.adverticoLTD.avms.helpers.ConstantClass;
import com.adverticoLTD.avms.helpers.DateTimeUtils;
import com.adverticoLTD.avms.helpers.PreferenceKeys;
import com.adverticoLTD.avms.keyLogSolution.customClasses.ProgressLoader;
import com.adverticoLTD.avms.network.RetrofitClient;
import com.adverticoLTD.avms.network.RetrofitInterface;
import com.adverticoLTD.avms.network.utils.WebApiHelper;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    Unbinder unbinder = null;


    private ProgressLoader loader;

    @Nullable
    @BindView(R.id.txtMarquee)
    TextView txtMarquee;

    String disclaimerMessage = "";
    String marqueeMessage = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        unbinder = ButterKnife.bind(this);

        getAccessKeyToken();

        if (marqueeMessage.isEmpty()) {
            getDisclaimerMessage();
        } else {
            setDisclaimerMessage("");
        }
    }

    public void getAccessKeyToken() {
        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.getTokenAccesskey(Prefs.getString(PreferenceKeys.PREF_ACCESS_TOKEN, ""),
                DateTimeUtils.getCurrentDateHeader()).enqueue(new Callback<AccessTokenResponseModel>() {
            @Override
            public void onResponse(Call<AccessTokenResponseModel> call, Response<AccessTokenResponseModel> response) {
                if (response.isSuccessful()) {
                    AccessTokenResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().toString().equals(ConstantClass.RESPONSE_SUCCESS)) {

                        Prefs.putString(PreferenceKeys.PREF_ACCESS_TOKEN, responseModel.getData().getApiTokenKey());

                    }
                } else if (response.code() == ConstantClass.RESPONSE_UNAUTHORIZED
                        || response.code() == ConstantClass.RESPONSE_UNAUTHORIZED_FOR) {

                    Prefs.putString(PreferenceKeys.PREF_ACCESS_TOKEN, "");
                    getAccessKeyToken();

                }
            }

            @Override
            public void onFailure(Call<AccessTokenResponseModel> call, Throwable t) {
                t.printStackTrace();
                showToastMessage(getString(R.string.error_something_went_wrong));
            }
        });

    }

    private void getDisclaimerMessage() {
        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.getDisclaimerMessage(Prefs.getString(PreferenceKeys.PREF_ACCESS_TOKEN, ""),
                DateTimeUtils.getCurrentDateHeader(), getDisclaimerRequest()).enqueue(new Callback<DisclaimerMessageResponseModel>() {
            @Override
            public void onResponse(Call<DisclaimerMessageResponseModel> call, Response<DisclaimerMessageResponseModel> response) {
                if (response.isSuccessful()) {
                    DisclaimerMessageResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS)) {
                        ArrayList<DisclaimerMessageResponseDataModel> arrDisclaimerData = responseModel.getData();
                        if (!arrDisclaimerData.isEmpty()) {
//                            disclaimerMessage = responseModel.getData().get(0).getMessage();
                            setDisclaimerMessage(responseModel.getData().get(0).getMessage());
                        }
                    }

                } else if (response.code() == ConstantClass.RESPONSE_UNAUTHORIZED
                        || response.code() == ConstantClass.RESPONSE_UNAUTHORIZED_FOR) {
                    getAccessKeyToken();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getDisclaimerMessage();

                }

            }

            @Override
            public void onFailure(Call<DisclaimerMessageResponseModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private DisclaimerRequestModel getDisclaimerRequest() {

        DisclaimerRequestModel requestModel = new DisclaimerRequestModel();
        DisclaimerRequestParamModel requestParamModel = new DisclaimerRequestParamModel();
        requestParamModel.setType(WebApiHelper.MSG_TYPE_MARQUEE);
        requestModel.setParam(requestParamModel);

        return requestModel;
    }

    private void setDisclaimerMessage(String message) {
        for (int i = 0; i < 4; i++) {
            disclaimerMessage = disclaimerMessage + " | " + message;
        }

        if (txtMarquee != null) {
            txtMarquee.setText(disclaimerMessage);
        }

        if (txtMarquee != null) {
            txtMarquee.setSelected(true);
        }
    }

    protected abstract int getLayoutResource();

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    public void showToastMessage(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    public void printLogMessage(String tag, String errorMessage) {
        Log.e(tag, errorMessage);
    }

    public void showProgressBar() {

        //Check if Activity is null then close activity.
        if (getActivity() == null) {
            return;
        } else {
            //If loader instance is null then re-create object.
            if (loader == null) {
                loader = new ProgressLoader(getActivity());
            }

            //If progress bar is not showing then show progress bar.
            if (!loader.isShowing()) {
                loader.show();
            }
        }

    }

    public void hideProgressBar() {

        if (loader != null && loader.isShowing()) {
            loader.dismiss();
        }
    }

    public BaseActivity getActivity() {
        return BaseActivity.this;
    }

}

