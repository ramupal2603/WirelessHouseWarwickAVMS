package com.adverticoLTD.avms.keyLogSolution.network.utils;

import android.content.Context;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UploadImageHelpers {

    public static MultipartBody.Part uploadImage(Context context, File file, String key) {

        MultipartBody.Part body = null;

        if ((file != null && file.exists())) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file.getAbsoluteFile());
            body = MultipartBody.Part.createFormData(key, file.getName(), requestFile);
        }
        return body;
    }

}
