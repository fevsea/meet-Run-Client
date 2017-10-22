package edu.upc.fib.gps.meetnrun;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Javier on 14/10/2017.
 */

public class create_meeting extends FragmentActivity {
    private Integer year, month, day, hour2, minute;
    private EditText name = (EditText) findViewById(R.id.name);
    private EditText date = (EditText) findViewById(R.id.date);
    private EditText hour = (EditText) findViewById(R.id.hour);
    private EditText level = (EditText) findViewById(R.id.level);
    private EditText description = (EditText) findViewById(R.id.description);
    private GoogleMap maps;
    private LatLng myLocation;
    private Marker myMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_meeting);

        Button dateButton = findViewById(R.id.pickDate);
        dateButton.setOnClickListener((View.OnClickListener) this);
        Button hourButton = findViewById(R.id.pickHour);
        hourButton.setOnClickListener((View.OnClickListener) this);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        myLocation = new LatLng(longitude, latitude);

        MapFragment mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapView, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync((OnMapReadyCallback) this);

        this.setTitle("Edit Meeting");
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
            case R.id.pickDate:
                showDatePickerDialog();
                break;
            case R.id.pickHour:
                showTimePickerDialog();
                break;
            case R.id.create:
                create(view);
                break;
        }
    }
    
}