package com.adverticoLTD.avms.network.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtils {

    private static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
