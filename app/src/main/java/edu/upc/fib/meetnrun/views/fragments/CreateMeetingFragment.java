package edu.upc.fib.meetnrun.views.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ScrollView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.persistence.GenericController;

import static android.R.layout.simple_spinner_item;
import static edu.upc.fib.meetnrun.R.id.scrollView;
import static edu.upc.fib.meetnrun.R.id.spinner;


public class CreateMeetingFragment extends Fragment implements OnMapReadyCallback {
    private Integer year, month, day, hour2, minute;

    private View view;
    private GoogleMap maps;
    private LatLng myLocation;
    private Marker myMarker;
    private final static String[] kind = {"@string/public_meeting","@string/private_meeting"};
    EditText name;
    EditText date;
    EditText hour;
    EditText level;
    EditText description;

    Integer Id;
    String Name;
    String Description;
    Boolean Public;
    Integer Level;
    String Date;
    String Latitude;
    String Longitude;
    ScrollView sV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_meeting, container, false);
        this.view = view;

        name = (EditText) view.findViewById(R.id.name);
        date = (EditText) view.findViewById(R.id.date);
        hour = (EditText) view.findViewById(R.id.hour);
        level = (EditText) view.findViewById(R.id.level);
        description = (EditText) view.findViewById(R.id.description);
        Button dateButton = (Button) view.findViewById(R.id.pickDate);
        dateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        Button hourButton = (Button) view.findViewById(R.id.pickHour);
        hourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });
        sV=(ScrollView) view.findViewById(R.id.scrollView);

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        Spinner spin = (Spinner) view.findViewById(R.id.spinner);
        spin.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        spin.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, kind));

        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.map, mMapFragment).commit();
        mMapFragment.getMapAsync(this);
        return view;
    }


    private void showTimePickerDialog() {
        final EditText timeText = (EditText) view.findViewById(R.id.hour);
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
        timePickerFragment.show(getFragmentManager(), "timePicker");
    }

    private void showDatePickerDialog() {
        final EditText dateText = (EditText) view.findViewById(R.id.date);
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
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }


    public void create (CreateMeetingFragment view){
        Name = name.getText().toString();
        Date = date.getText().toString();
        Level = Integer.parseInt(level.getText().toString());
        String Hour = hour.getText().toString();
        Description = description.getText().toString();
        Date=Date+','+Hour;
        Latitude=String.valueOf(41.388576);
        Longitude=String.valueOf(2.112840);
        Public=Boolean.TRUE;
        Id=26102017;

        if (Name.isEmpty() || Date.isEmpty() || Hour.isEmpty() /*|| Latitude.isEmpty() || Longitude.isEmpty()*/){
            Toast.makeText(this.getContext(), "@string/emptyCreate", Toast.LENGTH_SHORT).show();
        }
        else if(Name.length()>=100) Toast.makeText(this.getContext(),"@string/bigName", Toast.LENGTH_SHORT).show();
        else if(Description.length()>=500) Toast.makeText(this.getContext(), "@string/bigDescription", Toast.LENGTH_SHORT).show();
        else{
            //DB stuff
            Toast.makeText(this.getContext(),"Meeting name: "+Name+", Date:"+Date+", Hour: "+Hour+", Level: "+Level+", Description: "+Description, Toast.LENGTH_SHORT).show();
            create_meeting();
            this.getActivity().finish();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_meeting_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.done_button) {
            create(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLocationPicker() {
        int PLACE_PICKER_REQUEST = 1;
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        }
        catch (GooglePlayServicesRepairableException |GooglePlayServicesNotAvailableException e) {
            GooglePlayServicesUtil.getErrorDialog(GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()), getActivity(), PLACE_PICKER_REQUEST);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.maps = map;
        LatLng location = new LatLng(41.388576, 2.112840);
        myMarker = map.addMarker(new MarkerOptions().position(location).title("Meeting"));
        moveMapCameraAndMarker(location);
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                sV.requestDisallowInterceptTouchEvent(true);
            }
        });
    }

    private void moveMapCameraAndMarker(LatLng location) {
        CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(location, 15);
        maps.moveCamera(camera);
        myMarker.remove();
        myMarker = maps.addMarker(new MarkerOptions().position(location).title("Meeting"));
    }

    private void create_meeting(){
        new newMeeting().execute();
    }

    private class newMeeting extends AsyncTask<String,String,String> {
        Meeting m;
        @Override
        protected String doInBackground(String... strings){
            try {
                m= GenericController.getInstance().createMeetingPublic(Name,Description,Public,Level,Date,Latitude,Longitude);
            } catch (ParamsException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s){
            getActivity().finish();
            super.onPostExecute(s);
        }
    }

}
