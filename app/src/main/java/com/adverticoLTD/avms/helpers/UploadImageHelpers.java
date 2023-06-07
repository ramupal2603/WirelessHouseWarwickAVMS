package com.adverticoLTD.avms.helpers;

import android.content.Context;
import android.widget.Toast;

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
        } else {
            Toast.makeText(context, "Please selectProfile Picture", Toast.LENGTH_LONG);
        }
        return body;
    }

}
