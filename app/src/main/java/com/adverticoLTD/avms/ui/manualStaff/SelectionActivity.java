package com.adverticoLTD.avms.ui.manualStaff;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;


import com.adverticoLTD.avms.R;
import com.adverticoLTD.avms.baseClasses.BaseActivity;
import com.adverticoLTD.avms.helpers.ConstantClass;

import butterknife.BindView;

public class SelectionActivity extends BaseActivity {

    @BindView(R.id.txtSignIn)
    TextView txtSignIn;

    @BindView(R.id.txtSignOut)
    TextView txtSignOut;

    @BindView(R.id.imgBack)
    ImageView imgBack;

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
        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ManualStaffSignInSignOut.class);
                intent.putExtra(ConstantClass.EXTRAA_SIGN_IN_OUT, ConstantClass.REQUEST_SIGN_IN);
                startActivity(intent);
                finish();
            }
        });
        txtSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ManualStaffSignInSignOut.class);
                intent.putExtra(ConstantClass.EXTRAA_SIGN_IN_OUT, ConstantClass.REQUEST_SIGN_OUT);
                startActivity(intent);
                finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
