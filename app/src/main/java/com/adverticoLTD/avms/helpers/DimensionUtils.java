package com.adverticoLTD.avms.helpers;

import android.app.Activity;
import android.util.TypedValue;

public class DimensionUtils {


    public static float getFloatedTextSize(int textSizeInSp, Activity activity) {

        float scaledSizeInPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                textSizeInSp, activity.getResources().getDisplayMetrics());
        return scaledSizeInPixels;
    }
}
