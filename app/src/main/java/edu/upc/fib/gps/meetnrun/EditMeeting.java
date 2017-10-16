package edu.upc.fib.gps.meetnrun;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EditMeeting extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    private Integer year, month, day, hour, minute;
    private LatLng location = new LatLng(46.5, 45.6);
    private GoogleMap map;
    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_meeting);

        Button changeDateButton = (Button) findViewById(R.id.change_date_button);
        changeDateButton.setOnClickListener(this);
        Button changeTimeButton = (Button) findViewById(R.id.change_time_button);
        changeTimeButton.setOnClickListener(this);

        MapFragment mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();

        //SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        mMapFragment.getMapAsync(this);

        this.setTitle("Edit Meeting");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_date_button:
                showDatePickerDialog();
                break;
            case R.id.change_time_button:
                showTimePickerDialog();
                break;
        }

    }

    private void showDatePickerDialog() {
        final EditText dateText = (EditText) findViewById(R.id.meeting_date);
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yearSet, int monthSet, int daySet) {
                // +1 because january is zero
                year = yearSet;
                month = monthSet +1;
                day = daySet;
                final String selectedDate = day + "/" + (month+1) + "/" + year;
                dateText.setText(selectedDate);
            }
        });
        datePickerFragment.setValues(year, month, day);
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void showTimePickerDialog() {
        final EditText timeText = (EditText) findViewById(R.id.meeting_time);
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourSet, int minuteSet) {
                hour = hourSet;
                minute = minuteSet;
                final String selectedTime = hour + ":" + minute;
                timeText.setText(selectedTime);
            }
        });
        timePickerFragment.setValues(hour, minute);
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        map.addMarker(new MarkerOptions().position(location).title("Meeting"));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }
    }
}
