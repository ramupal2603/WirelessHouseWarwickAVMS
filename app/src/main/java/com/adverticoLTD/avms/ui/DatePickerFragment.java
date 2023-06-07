package com.adverticoLTD.avms.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.widget.DatePicker;

import com.adverticoLTD.avms.helpers.ConstantClass;
import com.adverticoLTD.avms.interfaces.OnDateSelectedListener;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    OnDateSelectedListener onDateSelectedListener;
    int requestCode;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onDateSelectedListener = (OnDateSelectedListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Bundle bundle = getArguments();
        if (bundle != null) {
            requestCode = bundle.getInt(ConstantClass.REQUEST_DATE_PICKER, 0);
        }


        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return datePickerDialog;


    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        onDateSelectedListener.onDateSelectedListener(year, month, day, requestCode);
    }
}