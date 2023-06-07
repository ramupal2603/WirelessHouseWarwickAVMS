package com.adverticoLTD.avms.keyLogSolution.helpers;

import android.content.pm.PackageInfo;

public class FileUtils {
    private static final String rootDir = "/storage/emulated/0/Android/media";
    private static final String folderName = "SignaturePad";

    private static final PackageInfo packageInfo = new PackageInfo();
    private static final String appPackage = packageInfo.packageName;


    public static String getAppMediaDirectory() {
        return rootDir + "/" + appPackage + "/" + folderName;
    }
}
