package com.adverticoLTD.avms.ui.deliveries;

import android.Manifest;
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

public class DeliverySelectionScreen extends BaseActivity {

    private final int PERMISSION_WRITE_EXTERNAL_STORAGE = 10001;

    @BindView(R.id.loutNotifyDelivery)
    LinearLayout loutNotifyDelivery;
    @BindView(R.id.loutSignDelivery)
    LinearLayout loutSignDelivery;

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

        loutSignDelivery.setOnClickListener(this);
        loutNotifyDelivery.setOnClickListener(this);
        backLayout.setOnClickListener(this);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_delivery_selection;
    }

    @Override
    public void onClick(View v) {

        if (v == loutNotifyDelivery) {
            notifyDeliveryAction();
        } else if (v == loutSignDelivery) {
            signDeliveryAction();
        } else if (v == backLayout) {
            finish();
        }
    }

    private void signDeliveryAction() {
        startActivity(new Intent(DeliverySelectionScreen.this, DeliveryListingActivity.class));
        finish();
    }

    private void notifyDeliveryAction() {
        startActivity(new Intent(DeliverySelectionScreen.this, DeliveriesActivity.class));
        finish();
    }
}
