package com.adverticoLTD.avms.ui.siteSelectionView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.adverticoLTD.avms.BuildConfig;
import com.adverticoLTD.avms.R;
import com.adverticoLTD.avms.baseClasses.BaseActivity;
import com.adverticoLTD.avms.brotherPrinter.PrinterSettingsActivity;
import com.adverticoLTD.avms.data.siteList.SiteListResponseDataModel;
import com.adverticoLTD.avms.data.siteList.SiteListResponseModel;
import com.adverticoLTD.avms.helpers.ConstantClass;
import com.adverticoLTD.avms.helpers.PreferenceKeys;
import com.adverticoLTD.avms.network.RetrofitClient;
import com.adverticoLTD.avms.network.RetrofitInterface;
import com.adverticoLTD.avms.ui.dashboardScreen.DashboardActivity;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiteSelectionViewActivity extends BaseActivity {

    @BindView(R.id.edtSiteName)
    EditText edtSiteName;

    @BindView(R.id.btnNext)
    Button btnNext;

    String selectedSiteId = "0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnNext.setOnClickListener(this);
        edtSiteName.setOnClickListener(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_site_selection_view;
    }

    @Override
    public void onClick(View view) {
        if (view == btnNext) {
            _checkValidSiteSelected();
        } else if (view == edtSiteName) {
            _getSiteDetails();
        }
    }

    private void _getSiteDetails() {

        showProgressBar();

        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.getSiteList().enqueue(new Callback<SiteListResponseModel>() {
            @Override
            public void onResponse(Call<SiteListResponseModel> call, Response<SiteListResponseModel> response) {
                hideProgressBar();
                if (response.isSuccessful()) {
                    SiteListResponseModel responseModel = response.body();
                    if (responseModel != null) {
                        if (responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS)) {
                            ArrayList<SiteListResponseDataModel> arrSiteList =
                                    new ArrayList<>(responseModel.getData());

                            if (!arrSiteList.isEmpty()) {
                                siteSelectionListDialog(arrSiteList);
                            }

                        } else {
                            showNoSitesMessage();
                        }
                    } else {
                        showErrorMessage();
                    }

                } else {
                    showErrorMessage();
                }
            }

            @Override
            public void onFailure(Call<SiteListResponseModel> call, Throwable t) {

            }
        });


    }

    void siteSelectionListDialog(ArrayList<SiteListResponseDataModel> arrSiteList) {
        final Dialog dialog = new Dialog(SiteSelectionViewActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.site_search_view);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ListView lv = (ListView) dialog.findViewById(R.id.listView1);
        TextView header = (TextView) dialog.findViewById(R.id.header);
        header.setText("Select Site");
        Button btn = (Button) dialog.findViewById(R.id.cancel);
        Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
        //CREATE AND SET ADAPTER TO LISTVIEW

        HashMap<String, Boolean> selectedHashMap = new HashMap<String, Boolean>();


        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                SiteSelectionViewActivity.this,
                android.R.layout.simple_list_item_single_choice);


        List<String> siteSelectionList = new ArrayList<String>();

        for (int i = 0; i < arrSiteList.size(); i++) {
            siteSelectionList.add(arrSiteList.get(i).getName());
        }

        MyAdapter myAdapter = new MyAdapter(getContext(), siteSelectionList, selectedHashMap);
        lv.setAdapter(myAdapter);
//        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        EditText sv = (EditText) dialog.findViewById(R.id.search);
        sv.setHint("Select Site");
        sv.setVisibility(View.GONE);
        sv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myAdapter.getFilter().filter(s.toString());

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

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (String name : selectedHashMap.keySet()) {
                    String key = name.toString();
                    String value = selectedHashMap.get(name).toString();
                    edtSiteName.setText(key.toString());
                    break;
                }

                for (int i = 0; i < arrSiteList.size(); i++) {
                    if (edtSiteName.getText().toString().equalsIgnoreCase(arrSiteList.get(i).getName())) {
                        selectedSiteId = arrSiteList.get(i).getId();
                        break;
                    }
                }

                dialog.dismiss();
            }
        });

        dialog.show();

    }



    private void _checkValidSiteSelected() {
        if (!selectedSiteId.equals("0")) {
            Prefs.putString(PreferenceKeys.SITE_ID, selectedSiteId);
            if (BuildConfig.allowBadgePrint) {
                startActivity(new Intent(getApplicationContext(), PrinterSettingsActivity.class));
            } else {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            }
            finish();
        } else {
            showToastMessage("Please select site first.");
        }
    }

}
