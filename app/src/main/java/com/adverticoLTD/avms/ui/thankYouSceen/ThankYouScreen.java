package com.adverticoLTD.avms.ui.thankYouSceen;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.adverticoLTD.avms.R;
import com.adverticoLTD.avms.baseClasses.BaseActivity;
import com.adverticoLTD.avms.helpers.ConstantClass;

import butterknife.BindView;
import im.delight.android.audio.MusicManager;

public class ThankYouScreen extends BaseActivity {

    @BindView(R.id.txtUserName)
    TextView txtUserName;

    @BindView(R.id.txtThanksMessage)
    TextView txtThanksMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIntentData();

        redirectToDashboard();

    }

    private void getIntentData() {
        String userName = getIntent().getStringExtra(ConstantClass.EXTRAA_VIEW_USER_NAME);
        String scanStatus = getIntent().getStringExtra(ConstantClass.EXTRAA_VIEW_SCAN_STATUS);

        txtUserName.setText(userName);
        if (scanStatus.equals(ConstantClass.RESPONSE_DELIVERY_SIGNED_SUCCESS)) {
            txtThanksMessage.setText("Delivery Sign Successfully");
        } else if (scanStatus.equals(ConstantClass.RESPONSE_DELIVERY_SUCCESS)) {
            txtThanksMessage.setText("Delivery Email Sent Successfully");
        } else {
            txtThanksMessage.setText(scanStatus.equals(ConstantClass.RESPONSE_SUCCESS_SIGN_IN)
                    ? getString(R.string.sign_in_message) : getString(R.string.sign_out_message));
        }


        playVoiceOverMessage(scanStatus);
    }

    private void playVoiceOverMessage(String scanStatus) {

        if (scanStatus.equals(ConstantClass.RESPONSE_SUCCESS_SIGN_IN)) {
            MusicManager.getInstance().play(ThankYouScreen.this, R.raw.fivesquidjamilsigningin);
        } else if (scanStatus.equals(ConstantClass.RESPONSE_SUCCESS_SIGN_OUT)) {
            MusicManager.getInstance().play(ThankYouScreen.this, R.raw.fivesquidjamilsigningout);
        }


    }

    private void redirectToDashboard() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, ConstantClass.REDIRECTION_INTERVAL);
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_thank_you_screen;
    }

    @Override
    public void onClick(View view) {

    }
}
