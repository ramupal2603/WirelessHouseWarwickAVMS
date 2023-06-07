package com.adverticoLTD.avms;

import android.app.Application;
import android.content.ContextWrapper;
import android.util.Log;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.pixplicity.easyprefs.library.Prefs;



public class MyApplication extends Application {

    private static final String TAG = "MYAPPLICATION";
    private static MyApplication mAppInstance = null;
    private JobManager mainJobManager;

    public static MyApplication getInstance() {
        return mAppInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mAppInstance = this;

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }

    public synchronized JobManager getMainJobManager() {
        if (mainJobManager == null) {
            configureJobManager();
        }
        return mainJobManager;
    }

    private void configureJobManager() {
        try {

            Configuration.Builder builder = new Configuration.Builder(this).consumerKeepAlive(120);//wait 2 minute
            builder.id("0");
            builder.minConsumerCount(0)
                    .maxConsumerCount(1);

            mainJobManager = new JobManager(builder.build());
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }


}
