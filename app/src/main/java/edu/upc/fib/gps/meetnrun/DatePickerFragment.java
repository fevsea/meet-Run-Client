package edu.upc.fib.gps.meetnrun;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import java.util.Date;

public class DatePickerFragment extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;
    private Integer year;
    private Integer month;
    private Integer day;

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    public void setValues(Integer year, Integer month, Integer day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        if (this.year == null) {
            final Calendar c;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                c = Calendar.getInstance();
                this.year = c.get(Calendar.YEAR);
                this.month = c.get(Calendar.MONTH);
                this.day = c.get(Calendar.DAY_OF_MONTH);
            }
            else {
                Date d=new Date();
                this.year=d.getYear();
                this.month=d.getMonth();
                this.day=d.getDay();
            }
        }
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), listener, this.year, this.month, this.day);
    }
}

