package edu.upc.fib.gps.meetnrun;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Javier on 14/10/2017.
 */

public class create_meeting extends Activity{
    private Integer year, month, day, hour2, minute;
    private EditText name=(EditText) findViewById(R.id.name);
    private EditText date=(EditText) findViewById(R.id.date);
    private EditText hour=(EditText) findViewById(R.id.hour);
    private EditText level=(EditText) findViewById(R.id.level);
    private EditText description=(EditText) findViewById(R.id.description);
    private Button dateButton=(Button) findViewById(R.id.pickDate);
    private Button hourButton=(Button) findViewById(R.id.pickHour);
    private GoogleMap maps;

   /* private void updateLabel() {
        String myFormat = "DD/mm/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

        edittext.setText(sdf.format(myCalendar.getTime()));
    } */

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_meeting);
    }

    public void create (View view){
        String Name = name.getText().toString();
        String Date = date.getText().toString();
        int Level = Integer.parseInt(level.getText().toString());
        String Hour = hour.getText().toString();
        String Description = description.toString();
        if (Name.isEmpty() || Date.isEmpty() || Hour.isEmpty()){
            Toast.makeText(this, "@string/emptycreate", Toast.LENGTH_SHORT).show();
        }
        else if(Name.length()>=100) Toast.makeText(this,"@string/bigName", Toast.LENGTH_SHORT).show();
        else if(Description.length()>=500) Toast.makeText(this, "@string/bigDescription", Toast.LENGTH_SHORT).show();
        else{
            //DB stuff
        }
    }

    private void showTimePickerDialog() {
        final EditText timeText = (EditText) findViewById(R.id.hour);
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourSet, int minuteSet) {
                hour2 = hourSet;
                minute = minuteSet;
                final String selectedTime = hour2 + ":" + minute;
                timeText.setText(selectedTime);
            }
        });
        timePickerFragment.setValues(hour2, minute);
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void showDatePickerDialog() {
        final EditText dateText = findViewById(R.id.date);
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yearSet, int monthSet, int daySet) {
                // +1 because january is zero
                year = yearSet;
                month = monthSet +1;
                day = daySet;
                final String selectedDate = day + "/" + month + "/" + year;
                dateText.setText(selectedDate);
            }
        });
        datePickerFragment.setValues(year, month, day);
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pickDate:
                showDatePickerDialog();
                break;
            case R.id.pickHour:
                showTimePickerDialog();
                break;
            case R.id.change_location_button:
                showLocationPicker();
                break;
        }
    }
    
}