package com.adverticoLTD.avms.ui.userSelection;

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
import com.adverticoLTD.avms.ui.manualDashboard.ManualSignInOutView;
import com.adverticoLTD.avms.ui.manualStaff.ManualStaffSignInSignOut;

import butterknife.BindView;

public class UserTypeActivity extends BaseActivity {

    @BindView(R.id.txtVisitor)
    TextView txtVisitor;

    @BindView(R.id.txtNewContractor)
    TextView txtNewContractor;

    @BindView(R.id.txtStaffSignOut)
    TextView txtStaffSignOut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        txtVisitor.setOnClickListener(this::onClick);
        txtNewContractor.setOnClickListener(this::onClick);
        txtStaffSignOut.setOnClickListener(this::onClick);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_user_type_selection;
    }

    @Override
    public void onClick(View view) {
        if (view == txtNewContractor) {
            Intent intent = new Intent(UserTypeActivity.this, ManualSignInOutView.class);
            intent.putExtra(ConstantClass.EXTRAA_USER_TYPE,ConstantClass.CONTRACTOR_TYPE);
            startActivityForResult(intent, ConstantClass.REQUEST_NORMAL_CONTRACTOR);
        }

        if (view == txtVisitor) {
            Intent intent = new Intent(UserTypeActivity.this, ManualSignInOutView.class);
            intent.putExtra(ConstantClass.EXTRAA_USER_TYPE,ConstantClass.VISITOR_TYPE);
            startActivityForResult(intent, ConstantClass.REQUEST_NORMAL_CONTRACTOR);
        }

        if (view == txtStaffSignOut) {
            Intent intent = new Intent(UserTypeActivity.this, ManualStaffSignInSignOut.class);
            intent.putExtra(ConstantClass.EXTRAA_SIGN_IN_OUT, ConstantClass.REQUEST_SIGN_OUT);
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
