package edu.upc.fib.gps.meetnrun.views;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.upc.fib.gps.meetnrun.views.fragments.DatePickerFragment;
import edu.upc.fib.gps.meetnrun.R;
import edu.upc.fib.gps.meetnrun.views.fragments.TimePickerFragment;

/**
 * Created by Javier on 14/10/2017.
 */

public class CreateMeetingActivity extends FragmentActivity {
    private Integer year, month, day, hour2, minute;

    private GoogleMap maps;
    private LatLng myLocation;
    private Marker myMarker;
    EditText name;
    EditText date;
    EditText hour;
    EditText level;
    EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_meeting);
        name = (EditText) findViewById(R.id.name);
        date = (EditText) findViewById(R.id.date);
        hour = (EditText) findViewById(R.id.hour);
        level = (EditText) findViewById(R.id.level);
        description = (EditText) findViewById(R.id.description);
        Button dateButton = findViewById(R.id.pickDate);
        dateButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        Button hourButton = findViewById(R.id.pickHour);
        hourButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        /*
        MapFragment mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);
        
        Location location = new Location();
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        myLocation = new LatLng(longitude, latitude);

        MapFragment mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapView, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync((OnMapReadyCallback) this);*/

        this.setTitle("Create Meeting");
    }

    public void create (View view){
        String Name = name.getText().toString();
        String Date = date.getText().toString();
        int Level = Integer.parseInt(level.getText().toString());
        String Hour = hour.getText().toString();
        String Description = description.toString();
        String Latitude = String.valueOf(myLocation.latitude);
        String Longitude = String.valueOf(myLocation.longitude);
        if (Name.isEmpty() || Date.isEmpty() || Hour.isEmpty() || Latitude.isEmpty() || Longitude.isEmpty()){
            Toast.makeText(this, "@string/emptyCreate", Toast.LENGTH_SHORT).show();
        }
        else if(Name.length()>=100) Toast.makeText(this,"@string/bigName", Toast.LENGTH_SHORT).show();
        else if(Description.length()>=500) Toast.makeText(this, "@string/bigDescription", Toast.LENGTH_SHORT).show();
        else{
            //DB stuff
            Toast.makeText(this,"Meeting name: "+Name+", Date:"+Date+", Hour: "+Hour+", Level: "+Level+", Description: "+Description, Toast.LENGTH_SHORT);
        }
    }

    public void onMapReady(GoogleMap map) {
      maps = map;
      // Add some markers to the map, and add a data object to each marker.
      myMarker = maps.addMarker(new MarkerOptions()
                       .position(myLocation)
                       .title("@string/location")
                       .draggable(true));
      myMarker.setTag(0);
      // Set a listener for marker click
      maps.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);
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

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create:
                create(view);
                break;
        }
    }
    
}