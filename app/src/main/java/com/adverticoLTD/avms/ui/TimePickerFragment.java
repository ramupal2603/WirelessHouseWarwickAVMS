package com.adverticoLTD.avms.ui;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.widget.TimePicker;

import com.adverticoLTD.avms.helpers.ConstantClass;
import com.adverticoLTD.avms.interfaces.OnTimeSelectedListener;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements
        TimePickerDialog.OnTimeSetListener {

    OnTimeSelectedListener onTimeSelectedListener;
    int requestCode;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onTimeSelectedListener = (OnTimeSelectedListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int hourOfTheDay = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Bundle bundle = getArguments();
        if (bundle != null) {
            requestCode = bundle.getInt(ConstantClass.REQUEST_TIME_PICKER, 0);
        }


        // Create a new instance of DatePickerDialog and return it

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hourOfTheDay, minute, false);
        return timePickerDialog;


    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        onTimeSelectedListener.onTimeSelectedListener(hourOfDay, minute, requestCode);
    }
}
