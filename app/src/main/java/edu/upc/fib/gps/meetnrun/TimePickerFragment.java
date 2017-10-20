package edu.upc.fib.gps.meetnrun;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.icu.util.TimeUnit;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import java.util.Date;

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
            final Calendar c;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                c = Calendar.getInstance();
                this.hour = c.get(Calendar.HOUR_OF_DAY);
                this.minute = c.get(Calendar.MINUTE);
            }
            else {
                Date d=new Date();
                this.hour = d.getHours();
                this.minute = d.getMinutes();
            }
        }

        return new TimePickerDialog(getActivity(), listener, this.hour, this.minute, DateFormat.is24HourFormat(getActivity()));

    }

}