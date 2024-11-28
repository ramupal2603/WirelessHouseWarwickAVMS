package com.adverticoLTD.avms.ui.manualStaff;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;


import com.adverticoLTD.avms.R;
import com.adverticoLTD.avms.baseClasses.BaseActivity;
import com.adverticoLTD.avms.helpers.ConstantClass;

import butterknife.BindView;

public class SelectionActivity extends BaseActivity {

    @BindView(R.id.loutStaffScreen)
    LinearLayout loutStaffScreen;

    @BindView(R.id.loutCoverGuards)
    LinearLayout loutCoverGuards;

    @BindView(R.id.imgBack)
    ImageView imgBack;

    int actionType;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_user_selection;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionType = getIntent().getIntExtra(ConstantClass.EXTRAA_SIGN_IN_OUT, 0);

        loutStaffScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ManualStaffSignInSignOut.class);
                intent.putExtra(ConstantClass.EXTRAA_SIGN_IN_OUT, actionType);
                intent.putExtra(ConstantClass.EXTRAA_USER_TYPE, ConstantClass.CLEANER_TYPE);
                startActivityForResult(intent, ConstantClass.REQUEST_NORMAL_STAFF);


            }
        });
        loutCoverGuards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ManualStaffSignInSignOut.class);
                intent.putExtra(ConstantClass.EXTRAA_SIGN_IN_OUT, actionType);
                intent.putExtra(ConstantClass.EXTRAA_USER_TYPE, ConstantClass.COVER_GUARD_TYPE);
                startActivityForResult(intent, ConstantClass.REQUEST_NORMAL_STAFF);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantClass.REQUEST_NORMAL_STAFF && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
