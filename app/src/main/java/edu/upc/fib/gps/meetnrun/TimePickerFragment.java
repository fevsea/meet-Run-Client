package edu.upc.fib.gps.meetnrun;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.icu.util.TimeUnit;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

/**
 * Created by guillemcastro on 15/10/2017.
 */

public class TimePickerFragment extends DialogFragment {

    private TimePickerDialog.OnTimeSetListener listener;
    private Integer hour;
    private Integer minute;

    public void setListener(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }

    public void setValues(Integer hour, Integer minute) {
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (this.hour == null) {
            final Calendar c = Calendar.getInstance();
            this.hour = c.get(Calendar.HOUR_OF_DAY);
            this.minute = c.get(Calendar.MINUTE);
        }

        return new TimePickerDialog(getActivity(), listener, this.hour, this.minute, DateFormat.is24HourFormat(getActivity()));

    }

}
