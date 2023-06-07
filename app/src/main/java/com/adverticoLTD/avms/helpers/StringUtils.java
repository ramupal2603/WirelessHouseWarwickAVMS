package com.adverticoLTD.avms.helpers;

import android.text.InputType;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.ArrayList;

public class StringUtils {

    public static void setEditTextNonEditable(EditText edt) {
        edt.setClickable(true);
        edt.setFocusable(false);
        edt.setInputType(InputType.TYPE_NULL);
    }

    public static boolean checkEmptyEditText(EditText edt) {
        return edt.getText().toString().trim().equals("");
    }

    public static String getOnlyNumbersFromString(String originString) {
        return originString.replaceAll("\\D+", "");
    }

    public static String convertStringListToJson(ArrayList<String> arrList) {
        return new Gson().toJson(arrList).replaceAll("\\[", "").replaceAll("]", "");
    }

    public static String convertIntListToJson(ArrayList<Integer> arrList) {
        return new Gson().toJson(arrList).replaceAll("\\[", "").replaceAll("]", "");
    }


}
