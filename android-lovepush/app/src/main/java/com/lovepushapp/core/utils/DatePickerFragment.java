package com.lovepushapp.core.utils;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;



@SuppressLint("ValidFragment")
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    DatePickerFragment.SelectedDate getString;

    @SuppressLint("ValidFragment")
    public DatePickerFragment(DatePickerFragment.SelectedDate getString) {
        this.getString = getString;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        getString.onDate(day, month, year);
    }

    public interface SelectedDate {
        public void onDate(int day, int month, int year);
    }
}



