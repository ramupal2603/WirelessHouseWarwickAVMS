package com.adverticoLTD.avms.ui.splashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.adverticoLTD.avms.BuildConfig;
import com.adverticoLTD.avms.R;
import com.adverticoLTD.avms.brotherPrinter.PrinterSettingsActivity;
import com.adverticoLTD.avms.helpers.ConstantClass;
import com.adverticoLTD.avms.ui.dashboardScreen.DashboardActivity;


public class SplashScreenActivity extends AppCompatActivity {

//    public static boolean isDebugMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        redirectDashboardActivity();
    }

    private void redirectDashboardActivity() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (BuildConfig.allowBadgePrint) {
                    startActivity(new Intent(getApplicationContext(), PrinterSettingsActivity.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                }
                finish();

            }
        }, ConstantClass.REDIRECTION_INTERVAL);
    }
}
