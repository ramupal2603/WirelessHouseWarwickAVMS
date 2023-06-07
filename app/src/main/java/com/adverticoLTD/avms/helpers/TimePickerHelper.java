package com.adverticoLTD.avms.helpers;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;

import com.adverticoLTD.avms.ui.TimePickerFragment;

public class TimePickerHelper {

    private FragmentManager supportFragmentManager;
    private Activity activity;
    private int requestCode;

    public TimePickerHelper(Activity activity, FragmentManager supportFragmentManager, int requestCode) {
        this.activity = activity;
        this.supportFragmentManager = supportFragmentManager;
        this.requestCode = requestCode;
    }

    public void show() {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putInt(ConstantClass.REQUEST_TIME_PICKER, requestCode);
        timePickerFragment.setArguments(args);

        timePickerFragment.show(supportFragmentManager, "datePicker");
    }

}
