package com.adverticoLTD.avms.ui.patientVisitScreen;

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
import com.adverticoLTD.avms.data.companies.CompanyListDataModel;
import com.adverticoLTD.avms.data.patientVisitor.PatientVisitorRequestModel;
import com.adverticoLTD.avms.data.patientVisitor.PatientVisitorRequestParamModel;
import com.adverticoLTD.avms.data.patientVisitor.PatientVisitorResponseModel;
import com.adverticoLTD.avms.data.stafflist.StaffListResponseDataModel;
import com.adverticoLTD.avms.helpers.ConstantClass;
import com.adverticoLTD.avms.helpers.DateTimeUtils;
import com.adverticoLTD.avms.helpers.PreferenceKeys;
import com.adverticoLTD.avms.helpers.StringUtils;
import com.adverticoLTD.avms.jobQueue.PrintBadgeJob;
import com.adverticoLTD.avms.network.RetrofitClient;
import com.adverticoLTD.avms.network.RetrofitInterface;
import com.adverticoLTD.avms.ui.Utils;
import com.adverticoLTD.avms.ui.thankYouSceen.ThankYouScreen;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientVisitScreen extends BaseActivity {

    @BindView(R.id.edtFirstName)
    EditText edtFirstName;

    @BindView(R.id.edtSurName)
    EditText edtSurName;

    @BindView(R.id.edtHereToVisit)
    EditText edtHereToVisit;



    @BindView(R.id.loutImgSignIn)
    LinearLayout loutImgSignIn;


    ArrayList<CompanyListDataModel> arrCompaniesList = new ArrayList<>();
    ArrayList<StaffListResponseDataModel> arrStaffList = new ArrayList<>();
    private String selectedCompanyID = "-1";
    private String selectedStaffID = "-1";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loutImgSignIn.setOnClickListener(this::onClick);
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_patient_visitor;
    }

    @Override
    public void onClick(View view) {
        if (view == loutImgSignIn) {
            showDisclaimerDialog();
        }
    }

    public void showDisclaimerDialog() {
        final Dialog dialog = new Dialog(PatientVisitScreen.this);
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
                validateNormalVisitorSignIn();
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

    private void validateNormalVisitorSignIn() {

        if (StringUtils.checkEmptyEditText(edtFirstName)) {
            showAlertDialog(getContext(), getString(R.string.error_first_name));
        } else if (StringUtils.checkEmptyEditText(edtSurName)) {
            showAlertDialog(getContext(), getString(R.string.error_surname));
        } else if (StringUtils.checkEmptyEditText(edtHereToVisit)) {
            showAlertDialog(getContext(), getString(R.string.error_here_to_visit));
        } else {
            callInsertPatientVisitor();
        }

    }

    private void callInsertPatientVisitor() {


        showProgressBar();
        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.insertPatientVisitor(Prefs.getString(PreferenceKeys.PREF_ACCESS_TOKEN, ""),
                DateTimeUtils.getCurrentDateHeader(), getPatientVisitorRequestData()).enqueue(new Callback<PatientVisitorResponseModel>() {
            @Override
            public void onResponse(Call<PatientVisitorResponseModel> call, Response<PatientVisitorResponseModel> response) {
                if (response.isSuccessful()) {
                    PatientVisitorResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS)) {

                        Bitmap qrCodeImage = generateQrCodeData(responseModel.getData().getVisitor_id());


                        Bitmap badgeImage = generatePrintData(responseModel.getData().getName(),
                                "",
                                responseModel.getData().getHere_to_visit(),
                                "Patient Visit", false, false,
                                qrCodeImage, false, true);

                        String bitmapString = Utils.BitMapToString(badgeImage);

                        if (BuildConfig.allowBadgePrint) {
                            MyApplication.getInstance().getMainJobManager().addJobInBackground(
                                    new PrintBadgeJob(bitmapString, getWorkPath()));
                        } else {
                            previewPrintBadge(badgeImage);
                        }


                        Intent intent = new Intent(PatientVisitScreen.this, ThankYouScreen.class);
                        intent.putExtra(ConstantClass.EXTRAA_VIEW_USER_NAME, responseModel.getData().getName());
                        intent.putExtra(ConstantClass.EXTRAA_VIEW_SCAN_STATUS, responseModel.getStatus());
                        startActivity(intent);
                        setResult(RESULT_OK);
                        finish();

                    } else {
                        showAlertDialog(getContext(), getResources().getString(R.string.error_already_signed_in));
                    }
                }
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<PatientVisitorResponseModel> call, Throwable t) {
                t.printStackTrace();
                hideProgressBar();
            }
        });
    }

    private PatientVisitorRequestModel getPatientVisitorRequestData() {
        PatientVisitorRequestModel requestModel = new PatientVisitorRequestModel();

        String siteID = Prefs.getString(PreferenceKeys.SITE_ID, "0");
        PatientVisitorRequestParamModel requestParamModel = new PatientVisitorRequestParamModel();
        requestParamModel.setFirst_name(edtFirstName.getText().toString().trim());
        requestParamModel.setSur_name(edtSurName.getText().toString().trim());
        requestParamModel.setHere_to_visit(edtHereToVisit.getText().toString().trim());
        requestParamModel.setVehicle_registration("N/A");
        requestParamModel.setSite_id(siteID);
        requestModel.setParam(requestParamModel);

        return requestModel;
    }


}
