package com.adverticoLTD.avms.keyLogSolution.ui.welcomeScreen;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;


import com.adverticoLTD.avms.R;
import com.adverticoLTD.avms.keyLogSolution.baseClasses.BaseActivity;
import com.adverticoLTD.avms.keyLogSolution.ui.signInKeyScreen.SignInKeyActivity;
import com.adverticoLTD.avms.keyLogSolution.ui.signoutKeyScreen.KeySignOutActivity;

import butterknife.BindView;

public class WelcomeActivity extends BaseActivity {

    private final int PERMISSION_WRITE_EXTERNAL_STORAGE = 10001;

    @BindView(R.id.loutSignKeyIn)
    LinearLayout loutSignKeyIn;
    @BindView(R.id.loutSignKeyOut)
    LinearLayout loutSignKeyOut;

    @BindView(R.id.backLayout)
    LinearLayout backLayout;


    private boolean isPermitWriteStorage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loutSignKeyIn.setOnClickListener(this);
        loutSignKeyOut.setOnClickListener(this);
        backLayout.setOnClickListener(this);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_welcome;
    }

    @Override
    public void onClick(View v) {

        if (v == loutSignKeyIn) {
            signInKeyAction();
        } else if (v == loutSignKeyOut) {
            signOutKeyAction();
        } else if (v == backLayout) {
            finish();
        }
    }

    private void signOutKeyAction() {
        startActivity(new Intent(WelcomeActivity.this, KeySignOutActivity.class));
    }

    private void signInKeyAction() {
        startActivity(new Intent(WelcomeActivity.this, SignInKeyActivity.class));
    }
}
