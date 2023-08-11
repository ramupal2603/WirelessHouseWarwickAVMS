package com.adverticoLTD.avms.ui.deliveries;

import android.app.Dialog;
import android.content.Intent;
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

import com.adverticoLTD.avms.R;
import com.adverticoLTD.avms.baseClasses.BaseActivity;
import com.adverticoLTD.avms.data.companies.CompanyListDataModel;
import com.adverticoLTD.avms.data.companies.CompanyListResponseModel;
import com.adverticoLTD.avms.data.delivery.DeliveryParamModel;
import com.adverticoLTD.avms.data.delivery.DeliveryRequestModel;
import com.adverticoLTD.avms.data.delivery.DeliveryResponseModel;
import com.adverticoLTD.avms.data.stafflist.StaffListRequestModel;
import com.adverticoLTD.avms.data.stafflist.StaffListRequestParamModel;
import com.adverticoLTD.avms.data.stafflist.StaffListResponseDataModel;
import com.adverticoLTD.avms.data.stafflist.StaffListResponseModel;
import com.adverticoLTD.avms.helpers.ConstantClass;
import com.adverticoLTD.avms.helpers.DateTimeUtils;
import com.adverticoLTD.avms.helpers.PreferenceKeys;
import com.adverticoLTD.avms.helpers.StringUtils;
import com.adverticoLTD.avms.network.RetrofitClient;
import com.adverticoLTD.avms.network.RetrofitInterface;
import com.adverticoLTD.avms.ui.thankYouSceen.ThankYouScreen;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveriesActivity extends BaseActivity {


    @BindView(R.id.loutCompanyView)
    LinearLayout loutCompanyView;

    @BindView(R.id.loutStaffView)
    LinearLayout loutStaffView;

    @BindView(R.id.edtCompany)
    EditText edtCompany;

    @BindView(R.id.edtStaff)
    EditText edtStaff;

    @BindView(R.id.edtCourierName)
    EditText edtCourierName;

    @BindView(R.id.imgSignIn)
    ImageView imgSignIn;

    @BindView(R.id.loutSendDeliveryEmail)
    LinearLayout loutSendDeliveryEmail;

    ArrayList<CompanyListDataModel> arrCompaniesList = new ArrayList<>();
    ArrayList<StaffListResponseDataModel> arrStaffList = new ArrayList<>();
    private String selectedCompanyID = "-1";
    private String selectedStaffID = "-1";


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_deliveries;
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        edtStaff.setOnClickListener(this::onClick);
        loutStaffView.setOnClickListener(this::onClick);
        edtCompany.setOnClickListener(this::onClick);
        loutCompanyView.setOnClickListener(this::onClick);
        imgSignIn.setOnClickListener(this::onClick);
        loutSendDeliveryEmail.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {


        if (view == loutCompanyView || view == edtCompany) {
            getCompaniesList();
        }

        if (view == loutStaffView || view == edtStaff) {
            if (!selectedCompanyID.equals("-1")) {
                getStaffList();
            } else {
                showToastMessage(getString(R.string.error_company_select_msg));
            }

        }

        if (view == imgSignIn || view == loutSendDeliveryEmail) {
            validateDeliveriesModule();
        }

    }

    public void showDisclaimerDialog() {
        final Dialog dialog = new Dialog(DeliveriesActivity.this);
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

                validateDeliveriesModule();

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

    private void validateDeliveriesModule() {
        if (StringUtils.checkEmptyEditText(edtCourierName)) {
            showToastMessage("Please Enter Courier Name");
        } else if (selectedCompanyID.equals("-1")) {
            showToastMessage("Please select Company");
        } else if (selectedStaffID.equals("-1")) {
            showToastMessage("Please select Staff");
        } else {
            callSendDeliveryEmailAddress();
        }

    }


    private void getStaffList() {
        showProgressBar();

        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.getStaffList(Prefs.getString(PreferenceKeys.PREF_ACCESS_TOKEN, ""),
                DateTimeUtils.getCurrentDateHeader(), getStaffRequest()).enqueue(new Callback<StaffListResponseModel>() {
            @Override
            public void onResponse(Call<StaffListResponseModel> call, Response<StaffListResponseModel> response) {
                if (response.isSuccessful()) {
                    StaffListResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS)) {
                        arrStaffList.clear();
                        arrStaffList.addAll(responseModel.getData());
                        staffListDialog();
                    }
                } else if (response.code() == ConstantClass.RESPONSE_UNAUTHORIZED
                        || response.code() == ConstantClass.RESPONSE_UNAUTHORIZED_FOR) {
                    getAccessKeyToken();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getStaffList();

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
        apiService.getCompanies(Prefs.getString(PreferenceKeys.PREF_ACCESS_TOKEN, ""),
                DateTimeUtils.getCurrentDateHeader()).enqueue(new Callback<CompanyListResponseModel>() {
            @Override
            public void onResponse(Call<CompanyListResponseModel> call, Response<CompanyListResponseModel> response) {
                if (response.isSuccessful()) {
                    CompanyListResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS)) {
                        arrCompaniesList = new ArrayList<>();
                        arrCompaniesList.addAll(responseModel.getData());
                        companyListDialog();
                    }
                } else if (response.code() == ConstantClass.RESPONSE_UNAUTHORIZED
                        || response.code() == ConstantClass.RESPONSE_UNAUTHORIZED_FOR) {
                    getAccessKeyToken();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getCompaniesList();

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
        final Dialog dialog = new Dialog(DeliveriesActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.searchview);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ListView lv = dialog.findViewById(R.id.listView1);
        TextView header = dialog.findViewById(R.id.header);
        header.setText("Select Company");
        Button btn = dialog.findViewById(R.id.cancel);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(DeliveriesActivity.this, android.R.layout.select_dialog_singlechoice);


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
        final Dialog dialog = new Dialog(DeliveriesActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.searchview);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ListView lv = dialog.findViewById(R.id.listView1);
        TextView header = dialog.findViewById(R.id.header);
        header.setText("Select Staff");
        Button btn = dialog.findViewById(R.id.cancel);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(DeliveriesActivity.this, android.R.layout.select_dialog_singlechoice);


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


    private void callSendDeliveryEmailAddress() {
        showProgressBar();
        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.sendDeliveryEmail(Prefs.getString(PreferenceKeys.PREF_ACCESS_TOKEN, ""),
                DateTimeUtils.getCurrentDateHeader(), getDeliveryEmailRequestModel()).enqueue(new Callback<DeliveryResponseModel>() {
            @Override
            public void onResponse(Call<DeliveryResponseModel> call, Response<DeliveryResponseModel> response) {
                if (response.isSuccessful()) {
                    DeliveryResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS)) {

                        Intent intent = new Intent(DeliveriesActivity.this, ThankYouScreen.class);
                        intent.putExtra(ConstantClass.EXTRAA_VIEW_USER_NAME, "");
                        intent.putExtra(ConstantClass.EXTRAA_VIEW_SCAN_STATUS, ConstantClass.RESPONSE_DELIVERY_SUCCESS);
                        startActivity(intent);
                        setResult(RESULT_OK);
                        finish();

                    } else {
                        showAlertDialog(getContext(), getResources().getString(R.string.error_already_signed_in));
                    }
                } else if (response.code() == ConstantClass.RESPONSE_UNAUTHORIZED
                        || response.code() == ConstantClass.RESPONSE_UNAUTHORIZED_FOR) {
                    getAccessKeyToken();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    callSendDeliveryEmailAddress();

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

    private DeliveryRequestModel getDeliveryEmailRequestModel() {

        DeliveryRequestModel deliveryRequestModel = new DeliveryRequestModel();

        DeliveryParamModel deliveryParamModel = new DeliveryParamModel();
        deliveryParamModel.setCompany_id(selectedCompanyID);
        deliveryParamModel.setStaff_id(selectedStaffID);
        deliveryParamModel.setCourier_name(edtCourierName.getText().toString().trim());
        deliveryRequestModel.setParam(deliveryParamModel);

        return deliveryRequestModel;
    }


}
