package com.adverticoLTD.avms.ui.normalVisitorScreen;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.adverticoLTD.avms.BuildConfig;
import com.adverticoLTD.avms.MyApplication;
import com.adverticoLTD.avms.R;
import com.adverticoLTD.avms.baseClasses.BaseActivity;
import com.adverticoLTD.avms.data.companies.CompanyListDataModel;
import com.adverticoLTD.avms.data.companies.CompanyListResponseModel;
import com.adverticoLTD.avms.data.normalVisitor.NormalVisitorRequestModel;
import com.adverticoLTD.avms.data.normalVisitor.NormalVisitorRequestParamModel;
import com.adverticoLTD.avms.data.normalVisitor.NormalVisitorResponseModel;
import com.adverticoLTD.avms.data.stafflist.StaffListRequestModel;
import com.adverticoLTD.avms.data.stafflist.StaffListRequestParamModel;
import com.adverticoLTD.avms.data.stafflist.StaffListResponseDataModel;
import com.adverticoLTD.avms.data.stafflist.StaffListResponseModel;
import com.adverticoLTD.avms.helpers.ConstantClass;
import com.adverticoLTD.avms.helpers.StringUtils;
import com.adverticoLTD.avms.jobQueue.PrintBadgeJob;
import com.adverticoLTD.avms.network.RetrofitClient;
import com.adverticoLTD.avms.network.RetrofitInterface;
import com.adverticoLTD.avms.ui.Utils;
import com.adverticoLTD.avms.ui.thankYouSceen.ThankYouScreen;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NormalVisitorScreen extends BaseActivity {

    @BindView(R.id.edtFirstName)
    EditText edtFirstName;

    @BindView(R.id.edtSurName)
    EditText edtSurName;

    @BindView(R.id.edtOrganization)
    EditText edtOrganization;

    @BindView(R.id.edtCarRegistration)
    EditText edtCarRegistration;

    @BindView(R.id.edtStaff)
    EditText edtStaff;

    @BindView(R.id.loutCompanyView)
    LinearLayout loutCompanyView;

    @BindView(R.id.edtCompany)
    EditText edtCompany;

    @BindView(R.id.loutStaffView)
    LinearLayout loutStaffView;

    @BindView(R.id.imgSignIn)
    ImageView imgSignIn;

    @BindView(R.id.next)
    TextView txtNext;

    ArrayList<CompanyListDataModel> arrCompaniesList = new ArrayList<>();
    ArrayList<StaffListResponseDataModel> arrStaffList = new ArrayList<>();
    private String selectedCompanyID = "-1";
    private String selectedStaffID = "-1";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        edtStaff.setOnClickListener(this::onClick);
        loutStaffView.setOnClickListener(this::onClick);
        edtCompany.setOnClickListener(this::onClick);
        loutCompanyView.setOnClickListener(this::onClick);
        imgSignIn.setOnClickListener(this::onClick);
        txtNext.setOnClickListener(this::onClick);

    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_normal_visitor;
    }

    @Override
    public void onClick(View view) {
        if (view == loutCompanyView || view==edtCompany) {
            getCompaniesList();
        }

        if (view == loutStaffView || view==edtStaff) {
            if (!selectedCompanyID.equals("-1")) {
                getStaffList();
            } else {
                showToastMessage(getString(R.string.error_company_select_msg));
            }

        }

        if (view == imgSignIn) {
            showDisclaimerDialog();
        }

        if (view == txtNext) {
            hideKeyBoard();
            shakeView(loutCompanyView);
        }
    }

    public void showDisclaimerDialog() {
        final Dialog dialog = new Dialog(NormalVisitorScreen.this);
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
        } else if (StringUtils.checkEmptyEditText(edtOrganization)) {
            showAlertDialog(getContext(), getString(R.string.error_organization));
        } else if (StringUtils.checkEmptyEditText(edtCompany)) {
            showAlertDialog(getContext(), getString(R.string.error_company));
        } else if (StringUtils.checkEmptyEditText(edtStaff)) {
            showAlertDialog(getContext(), getString(R.string.error_staff));
        } else {
            callInsertNormalVisitor();
        }

    }

    private void callInsertNormalVisitor() {
        showProgressBar();
        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.insertNormalVisitor(getNormalVisitorRequestData()).enqueue(new Callback<NormalVisitorResponseModel>() {
            @Override
            public void onResponse(Call<NormalVisitorResponseModel> call, Response<NormalVisitorResponseModel> response) {
                if (response.isSuccessful()) {
                    NormalVisitorResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS)) {

                        Bitmap qrCodeImage = generateQrCodeData(responseModel.getData().getVisitor_id());

                        boolean isRegularVisitor = responseModel.getData().getVisitor_id().endsWith("@5");

                        Bitmap badgeImage = generatePrintData(responseModel.getData().getName(),
                                responseModel.getData().getVisitor_organization(),
                                responseModel.getData().getStaff_name(),
                                responseModel.getData().getCompany_name(), isRegularVisitor, false,
                                qrCodeImage,false);

                        String bitmapString = Utils.BitMapToString(badgeImage);

                        if (BuildConfig.allowBadgePrint) {
                            MyApplication.getInstance().getMainJobManager().addJobInBackground(
                                    new PrintBadgeJob(bitmapString, getWorkPath()));
                        } else {
                            previewPrintBadge(badgeImage);
                        }


                        Intent intent = new Intent(NormalVisitorScreen.this, ThankYouScreen.class);
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
            public void onFailure(Call<NormalVisitorResponseModel> call, Throwable t) {
                t.printStackTrace();
                hideProgressBar();
            }
        });
    }

    private NormalVisitorRequestModel getNormalVisitorRequestData() {
        NormalVisitorRequestModel requestModel = new NormalVisitorRequestModel();
        NormalVisitorRequestParamModel normalVisitorRequestParamModel = new NormalVisitorRequestParamModel();
        normalVisitorRequestParamModel.setCompany_id(selectedCompanyID);
        normalVisitorRequestParamModel.setEmail("");
        normalVisitorRequestParamModel.setFirst_name(edtFirstName.getText().toString().trim());
        normalVisitorRequestParamModel.setSur_name(edtSurName.getText().toString().trim());
        normalVisitorRequestParamModel.setVehicle_registration(edtCarRegistration.getText().toString().trim());
        normalVisitorRequestParamModel.setVisitor_organization(edtOrganization.getText().toString().trim());
        normalVisitorRequestParamModel.setStaff_id(selectedStaffID);
        requestModel.setParam(normalVisitorRequestParamModel);
        return requestModel;
    }

    private void getStaffList() {
        showProgressBar();

        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.getStaffList(getStaffRequest()).enqueue(new Callback<StaffListResponseModel>() {
            @Override
            public void onResponse(Call<StaffListResponseModel> call, Response<StaffListResponseModel> response) {
                if (response.isSuccessful()) {
                    StaffListResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS)) {
                        arrStaffList.clear();
                        arrStaffList.addAll(responseModel.getData());
                        staffListDialog();
                    }
                } else {
                    showToastMessage(getString(R.string.error_something_went_wrong));
                }
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<StaffListResponseModel> call, Throwable t) {
                t.printStackTrace();
                hideProgressBar();
                showToastMessage(getString(R.string.error_something_went_wrong));
            }
        });

    }

    private StaffListRequestModel getStaffRequest() {
        StaffListRequestModel requestModel = new StaffListRequestModel();
        StaffListRequestParamModel paramModel = new StaffListRequestParamModel();
        paramModel.setCompany_id(selectedCompanyID);
        requestModel.setParam(paramModel);
        return requestModel;
    }

    private void getCompaniesList() {
        showProgressBar();

        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.getCompanies().enqueue(new Callback<CompanyListResponseModel>() {
            @Override
            public void onResponse(Call<CompanyListResponseModel> call, Response<CompanyListResponseModel> response) {
                if (response != null) {
                    CompanyListResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS)) {
                        arrCompaniesList = new ArrayList<>();
                        arrCompaniesList.addAll(responseModel.getData());
                        companyListDialog();
                    }
                } else {
                    showToastMessage(getString(R.string.error_something_went_wrong));
                }
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<CompanyListResponseModel> call, Throwable t) {
                t.printStackTrace();
                hideProgressBar();
                showToastMessage(getString(R.string.error_something_went_wrong));
            }
        });
    }

    void companyListDialog() {
        final Dialog dialog = new Dialog(NormalVisitorScreen.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.searchview);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ListView lv = dialog.findViewById(R.id.listView1);
        TextView header = dialog.findViewById(R.id.header);
        header.setText("Select Company");
        Button btn = dialog.findViewById(R.id.cancel);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(NormalVisitorScreen.this, android.R.layout.select_dialog_singlechoice);


        for (int i = 0; i < arrCompaniesList.size(); i++) {
            arrayAdapter.add(arrCompaniesList.get(i).getName());
        }

        lv.setAdapter(arrayAdapter);
        //SEARCH
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String strName = parent.getItemAtPosition(position).toString();

                for (int i = 0; i < arrCompaniesList.size(); i++) {
                    if (arrCompaniesList.get(i).getName().equalsIgnoreCase(strName)) {
                        edtCompany.setText(arrCompaniesList.get(i).getName());
                        selectedCompanyID = arrCompaniesList.get(i).getId();
                        break;
                    }
                }
                shakeView(loutStaffView);
                dialog.dismiss();
            }
        });

        EditText sv = (EditText) dialog.findViewById(R.id.search);
        sv.setHint("Search Name or scroll down");
        sv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrayAdapter.getFilter().filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //BUTTON
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                dialog.dismiss();
            }
        });
        dialog.show();

    }

    void staffListDialog() {
        final Dialog dialog = new Dialog(NormalVisitorScreen.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.searchview);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ListView lv = dialog.findViewById(R.id.listView1);
        TextView header = dialog.findViewById(R.id.header);
        header.setText("Select Staff");
        Button btn = dialog.findViewById(R.id.cancel);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(NormalVisitorScreen.this, android.R.layout.select_dialog_singlechoice);


        for (int i = 0; i < arrStaffList.size(); i++) {
            arrayAdapter.add(arrStaffList.get(i).getName());
        }

        lv.setAdapter(arrayAdapter);
        //SEARCH
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String strName = parent.getItemAtPosition(position).toString();

                for (int i = 0; i < arrStaffList.size(); i++) {
                    if (arrStaffList.get(i).getName().equalsIgnoreCase(strName)) {
                        edtStaff.setText(arrStaffList.get(i).getName());
                        selectedStaffID = arrStaffList.get(i).getId();
                        break;
                    }
                }
                dialog.dismiss();
            }
        });

        EditText sv = (EditText) dialog.findViewById(R.id.search);
        sv.setHint("Search Name or scroll down");
        sv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrayAdapter.getFilter().filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //BUTTON
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
