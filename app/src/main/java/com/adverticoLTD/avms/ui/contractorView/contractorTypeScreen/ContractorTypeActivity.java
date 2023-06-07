package com.adverticoLTD.avms.ui.contractorView.contractorTypeScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.adverticoLTD.avms.R;
import com.adverticoLTD.avms.baseClasses.BaseActivity;
import com.adverticoLTD.avms.helpers.ConstantClass;
import com.adverticoLTD.avms.ui.contractorView.existingContractorScreen.ExistingContractorActivity;
import com.adverticoLTD.avms.ui.contractorView.newContractorScreen.NewContractorActivity;

import butterknife.BindView;

public class ContractorTypeActivity extends BaseActivity {

    @BindView(R.id.txtVisitor)
    TextView txtExistingContractor;

    @BindView(R.id.txtNewContractor)
    TextView txtNewContractor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        txtExistingContractor.setOnClickListener(this::onClick);
        txtNewContractor.setOnClickListener(this::onClick);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_contractor_selection;
    }

    @Override
    public void onClick(View view) {
        if (view == txtNewContractor) {
            Intent intent = new Intent(ContractorTypeActivity.this, NewContractorActivity.class);
            startActivityForResult(intent, ConstantClass.REQUEST_NORMAL_CONTRACTOR);
        }

        if (view == txtExistingContractor) {
            Intent intent = new Intent(ContractorTypeActivity.this, ExistingContractorActivity.class);
            startActivityForResult(intent, ConstantClass.REQUEST_NORMAL_CONTRACTOR);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantClass.REQUEST_NORMAL_CONTRACTOR && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();

        }
    }
}
