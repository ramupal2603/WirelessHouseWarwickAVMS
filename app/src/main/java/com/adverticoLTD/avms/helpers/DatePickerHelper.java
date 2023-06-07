package com.adverticoLTD.avms.helpers;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;

import com.adverticoLTD.avms.ui.DatePickerFragment;

public class DatePickerHelper {

    private FragmentManager supportFragmentManager;
    private Activity activity;
    private int requestCode;

    public DatePickerHelper(Activity activity, FragmentManager supportFragmentManager, int requestCode) {
        this.activity = activity;
        this.supportFragmentManager = supportFragmentManager;
        this.requestCode = requestCode;
    }

    public void show() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt(ConstantClass.REQUEST_DATE_PICKER, requestCode);
        datePickerFragment.setArguments(args);

        datePickerFragment.show(supportFragmentManager, "datePicker");
    }
}
