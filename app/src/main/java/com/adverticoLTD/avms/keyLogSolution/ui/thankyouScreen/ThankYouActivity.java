package com.adverticoLTD.avms.keyLogSolution.ui.thankyouScreen;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;


import com.adverticoLTD.avms.R;
import com.adverticoLTD.avms.keyLogSolution.baseClasses.BaseActivity;
import com.adverticoLTD.avms.keyLogSolution.helpers.ConstantClass;

import butterknife.BindView;

public class ThankYouActivity extends BaseActivity {

    @BindView(R.id.txtThanksView)
    TextView txtThanksView;

    @BindView(R.id.txtThanksViewDesc)
    TextView txtThanksViewDesc;


    String signingOutMsg = "Thank You for signing out the key.";

    String signingInMsg = "Thank You for signing in the key.";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIntentData();

        redirectToHomePage();

    }

    private void getIntentData() {

        if (getIntent().getStringExtra(ConstantClass.EXTRA_IS_FROM).equals(ConstantClass.SIGN_OUT)) {
            txtThanksViewDesc.setVisibility(View.VISIBLE);
            txtThanksViewDesc.setText("Please ensure the key is signed back in after you have finished.");
            txtThanksView.setText(signingOutMsg);
        } else {
            txtThanksViewDesc.setVisibility(View.GONE);
            txtThanksView.setText(signingInMsg);
        }

    }

    private void redirectToHomePage() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, ConstantClass.THANK_YOU_REDIRECTION__INTERVAL);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_thanks_screen;
    }

    @Override
    public void onClick(View v) {

    }
}
