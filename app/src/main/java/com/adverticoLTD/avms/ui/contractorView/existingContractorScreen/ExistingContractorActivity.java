package com.adverticoLTD.avms.ui.contractorView.existingContractorScreen;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.adverticoLTD.avms.BuildConfig;
import com.adverticoLTD.avms.MyApplication;
import com.adverticoLTD.avms.R;
import com.adverticoLTD.avms.baseClasses.BaseActivity;
import com.adverticoLTD.avms.data.contractorStatus.ContractorRequestModel;
import com.adverticoLTD.avms.data.contractorStatus.ContractorRequestParamModel;
import com.adverticoLTD.avms.data.contractorStatus.ContractorResponseModel;
import com.adverticoLTD.avms.data.existingContractor.ExistingContractorRequestModel;
import com.adverticoLTD.avms.data.existingContractor.ExistingContractorRequestParamModel;
import com.adverticoLTD.avms.data.existingContractor.ExistingContractorResponseModel;
import com.adverticoLTD.avms.helpers.ConstantClass;
import com.adverticoLTD.avms.helpers.DateTimeUtils;
import com.adverticoLTD.avms.helpers.PreferenceKeys;
import com.adverticoLTD.avms.helpers.StringUtils;
import com.adverticoLTD.avms.jobQueue.PrintBadgeJob;
import com.adverticoLTD.avms.network.RetrofitClient;
import com.adverticoLTD.avms.network.RetrofitInterface;
import com.adverticoLTD.avms.ui.Utils;
import com.adverticoLTD.avms.ui.contractorView.newContractorScreen.WebViewPDFActivity;
import com.adverticoLTD.avms.ui.thankYouSceen.ThankYouScreen;
import com.pixplicity.easyprefs.library.Prefs;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExistingContractorActivity extends BaseActivity {

    @BindView(R.id.edtContractorId)
    EditText edtContractorId;

    @BindView(R.id.loutImgSignIn)
    LinearLayout loutImgSignIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loutImgSignIn.setOnClickListener(this::onClick);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_existing_contractor;
    }

    @Override
    public void onClick(View view) {

        if (view == loutImgSignIn) {
            hideKeyBoard();
            edtContractorId.clearFocus();
            showDisclaimerDialog();
        }
    }

    public void showDisclaimerDialog() {
        final Dialog dialog = new Dialog(ExistingContractorActivity.this);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_disclaimer);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        TextView dis_txt = dialog.findViewById(R.id.dis_txt);
        dis_txt.setText(Html.fromHtml(gdprMessage));

        LinearLayout accept = dialog.findViewById(R.id.accept);
        ImageView close_btn = dialog.findViewById(R.id.close_btn);

        // if decline button is clicked, close the custom dialog
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateExistingContractorSignIn();
                dialog.dismiss();
            }
        });
        LinearLayout declineButton = dialog.findViewById(R.id.decline);

        // if decline button is clicked, close the custom dialog
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void validateExistingContractorSignIn() {

        if (StringUtils.checkEmptyEditText(edtContractorId)) {
            showAlertDialog(getContext(), getResources().getString(R.string.error_contractor_id));
        } else {
            contractorStatus(edtContractorId.getText().toString().trim());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantClass.REQUEST_NORMAL_CONTRACTOR && resultCode == RESULT_OK) {
            if (data != null) {
                String descriptionOfWork = data.getStringExtra(ConstantClass.EXTRAA_DESCRIPTION_WORK);
                String imagePath = data.getStringExtra(ConstantClass.EXTRAA_SIGNATURE);
                siginInExistingContractorWithID(descriptionOfWork, imagePath);
            }
        }
    }

    private void contractorStatus(String userID) {

        showProgressBar();

        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.getContractorStatus(getcontractorStatus(userID),
                Prefs.getString(PreferenceKeys.PREF_ACCESS_TOKEN, ""), DateTimeUtils.getCurrentDateHeader()).enqueue(new Callback<ContractorResponseModel>() {
            @Override
            public void onResponse(Call<ContractorResponseModel> call, Response<ContractorResponseModel> response) {
                if (response.isSuccessful()) {
                    ContractorResponseModel responseModel = response.body();
                    if (responseModel != null) {
                        if (responseModel.getStatus().equals("1")) {
                            // Call for SignOut
                            showAlertDialog(getContext(), getResources().getString(R.string.error_already_signed_in_contractor));

                        } else if (responseModel.getStatus().equals("0")) {

                            // Call for SignIn
                            Intent contractorIntent = new Intent(ExistingContractorActivity.this, WebViewPDFActivity.class);
                            contractorIntent.putExtra(ConstantClass.EXTRAA_VIEW_USER_NAME, responseModel.getContractor_name());
                            startActivityForResult(contractorIntent, ConstantClass.REQUEST_NORMAL_CONTRACTOR);

                        } else {
                            showToastMessage(getString(R.string.error_something_went_wrong));
                        }


                    }
                } else if (response.code() == ConstantClass.RESPONSE_UNAUTHORIZED) {
                    getAccessKeyToken();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    contractorStatus(userID);

                }
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<ContractorResponseModel> call, Throwable t) {
                t.printStackTrace();
                hideProgressBar();
            }
        });
    }

    private ContractorRequestModel getcontractorStatus(String userID) {
        ContractorRequestModel requestModel = new ContractorRequestModel();
        ContractorRequestParamModel paramModel = new ContractorRequestParamModel();
        paramModel.setUser_id(userID + "@4");
        requestModel.setParam(paramModel);
        return requestModel;
    }

    private void siginInExistingContractorWithID(String descriptionOfWork, String imagePath) {
        showProgressBar();
        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.existingContractorSignIn(Prefs.getString(PreferenceKeys.PREF_ACCESS_TOKEN, ""),
                DateTimeUtils.getCurrentDateHeader(), getExistingContractorRequest(descriptionOfWork, imagePath)).enqueue(new Callback<ExistingContractorResponseModel>() {
            @Override
            public void onResponse(Call<ExistingContractorResponseModel> call, Response<ExistingContractorResponseModel> response) {
                if (response.isSuccessful()) {
                    ExistingContractorResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS)) {
                        Bitmap qrCodeImage = generateQrCodeData(responseModel.getData().getVisitor_id());

                        Bitmap badgeImage = generatePrintData(responseModel.getData().getName(),
                                responseModel.getData().getVisitor_organization(),
                                responseModel.getData().getStaff_name(),
                                responseModel.getData().getCompany_name(),
                                false, true, qrCodeImage, false, false);

                        String bitmapString = Utils.BitMapToString(badgeImage);

                        if (BuildConfig.allowBadgePrint) {
                            MyApplication.getInstance().getMainJobManager().addJobInBackground(
                                    new PrintBadgeJob(bitmapString, getWorkPath()));
                        } else {
                            previewPrintBadge(badgeImage);
                        }


                        Intent intent = new Intent(ExistingContractorActivity.this, ThankYouScreen.class);
                        intent.putExtra(ConstantClass.EXTRAA_VIEW_USER_NAME, responseModel.getData().getName());
                        intent.putExtra(ConstantClass.EXTRAA_VIEW_SCAN_STATUS, responseModel.getStatus());
                        startActivity(intent);
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        showAlertDialog(getContext(), getResources().getString(R.string.error_already_signed_in_contractor));
                    }

                } else if (response.code() == ConstantClass.RESPONSE_UNAUTHORIZED
                        || response.code() == ConstantClass.RESPONSE_UNAUTHORIZED_FOR) {
                    getAccessKeyToken();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    siginInExistingContractorWithID(descriptionOfWork, imagePath);

                }
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<ExistingContractorResponseModel> call, Throwable t) {
                t.printStackTrace();
                showErrorMessage();
                hideProgressBar();
            }
        });
    }

    private ExistingContractorRequestModel getExistingContractorRequest(String descriptionOfWork, String imagePath) {
        ExistingContractorRequestModel requestModel = new ExistingContractorRequestModel();
        ExistingContractorRequestParamModel paramModel = new ExistingContractorRequestParamModel();
        paramModel.setVisitor_id(edtContractorId.getText().toString().trim());
        paramModel.setDescription(descriptionOfWork);
        paramModel.setSignature(imagePath);
        requestModel.setParam(paramModel);
        return requestModel;
    }
}
