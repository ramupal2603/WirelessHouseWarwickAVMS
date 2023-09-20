package com.adverticoLTD.avms.ui.manualDashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.adverticoLTD.avms.R;
import com.adverticoLTD.avms.baseClasses.BaseActivity;
import com.adverticoLTD.avms.helpers.ConstantClass;
import com.adverticoLTD.avms.keyLogSolution.ui.welcomeScreen.WelcomeActivity;
import com.adverticoLTD.avms.ui.contractorView.contractorTypeScreen.ContractorTypeActivity;
import com.adverticoLTD.avms.ui.deliveries.DeliverySelectionScreen;
import com.adverticoLTD.avms.ui.manualStaff.SelectionActivity;
import com.adverticoLTD.avms.ui.normalVisitorScreen.NormalVisitorScreen;
import com.adverticoLTD.avms.ui.patientVisitScreen.PatientVisitScreen;
import com.adverticoLTD.avms.ui.userSelection.UserTypeActivity;

import butterknife.BindView;

public class ManualDashboardActivity extends BaseActivity {

    @BindView(R.id.loutHomeScreen)
    LinearLayout loutHomeScreen;

    @BindView(R.id.loutVisitorScreen)
    LinearLayout loutVisitorScreen;

    @BindView(R.id.loutContractorScreen)
    LinearLayout loutContractorScreen;

    @BindView(R.id.loutKeyLog)
    LinearLayout loutKeyLog;

    @BindView(R.id.loutSignOut)
    LinearLayout loutSignOut;

    @BindView(R.id.loutDeliveries)
    LinearLayout loutDeliveries;

    @BindView(R.id.loutStaffScreen)
    LinearLayout loutStaffScreen;

    @BindView(R.id.loutPatientVisit)
    LinearLayout loutPatientVisit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loutHomeScreen.setOnClickListener(this::onClick);
        loutVisitorScreen.setOnClickListener(this::onClick);
        loutContractorScreen.setOnClickListener(this::onClick);
        loutKeyLog.setOnClickListener(this::onClick);
        loutSignOut.setOnClickListener(this::onClick);
        loutDeliveries.setOnClickListener(this::onClick);
        loutStaffScreen.setOnClickListener(this::onClick);
        loutPatientVisit.setOnClickListener(this::onClick);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_manual_dashboard;
    }

    @Override
    public void onClick(View view) {
        if (view == loutHomeScreen) {
            finish();
        }

        if (view == loutVisitorScreen) {
            Intent intent = new Intent(ManualDashboardActivity.this, NormalVisitorScreen.class);
            startActivityForResult(intent, ConstantClass.REQUEST_NORMAL_VISITOR);
        }

        if (view == loutContractorScreen) {
            Intent intent = new Intent(ManualDashboardActivity.this, ContractorTypeActivity.class);
            startActivityForResult(intent, ConstantClass.REQUEST_NORMAL_CONTRACTOR);
        }
        if (view == loutKeyLog) {
            Intent intent = new Intent(ManualDashboardActivity.this, WelcomeActivity.class);
            startActivityForResult(intent, ConstantClass.REQUEST_NORMAL_CONTRACTOR);
        }
        if (view == loutSignOut) {
            Intent intent = new Intent(ManualDashboardActivity.this, UserTypeActivity.class);
            startActivityForResult(intent, ConstantClass.REQUEST_NORMAL_CONTRACTOR);
        }
        if (view == loutDeliveries) {
            Intent intent = new Intent(ManualDashboardActivity.this, DeliverySelectionScreen.class);
            startActivityForResult(intent, ConstantClass.REQUEST_NORMAL_VISITOR);
        }
        if (view == loutStaffScreen) {
            Intent intent = new Intent(ManualDashboardActivity.this, SelectionActivity.class);
            startActivity(intent);
        }
        if (view == loutPatientVisit) {
            Intent intent = new Intent(ManualDashboardActivity.this, PatientVisitScreen.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantClass.REQUEST_NORMAL_VISITOR && resultCode == RESULT_OK) {
            finish();
        } else if (requestCode == ConstantClass.REQUEST_NORMAL_CONTRACTOR && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        } else if (requestCode == ConstantClass.REQUEST_NORMAL_STAFF && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
