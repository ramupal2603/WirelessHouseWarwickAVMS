package com.adverticoLTD.avms.keyLogSolution.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.adverticoLTD.avms.keyLogSolution.helpers.ConstantClass;


public class Utils {

    public static boolean isEditTextEmpty(EditText editText) {
        return editText.getText().toString().trim().equals("");
    }

    public static void autoHideKeyboard(final Activity activity) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                hideKeyBoard(activity);
            }
        }, ConstantClass.AUTO_HIDE_KEYBOARD_TIMER);
    }

    public static void hideKeyBoard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
