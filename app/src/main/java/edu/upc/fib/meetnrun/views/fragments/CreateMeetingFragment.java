package edu.upc.fib.meetnrun.views.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ScrollView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;


import edu.upc.fib.meetnrun.views.MeetingFriendsActivity;
import static android.app.Activity.RESULT_OK;


public class CreateMeetingFragment extends Fragment implements OnMapReadyCallback, CompoundButton.OnCheckedChangeListener {
    private Integer year, month, day, hour2, minute;

    Meeting m;
    private boolean friends;
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
    EditText location;

    String Name;
    String Description;
    Boolean Public;
    Integer Level;
    String Date;
    String Latitude;
    String Longitude;
    ScrollView sV;
    Switch publicMeeting;

    private IMeetingAdapter meetingAdapter;

    Geocoder geocoder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        meetingAdapter = CurrentSession.getInstance().getMeetingAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_meeting, container, false);
        this.view = view;


        myLocation = new LatLng(41.388576, 2.112840);
        friends=false;
        name = view.findViewById(R.id.name);
        date = view.findViewById(R.id.date);
        hour = view.findViewById(R.id.hour);
        level = view.findViewById(R.id.level);
        location = view.findViewById(R.id.meetingLocation);
        description = view.findViewById(R.id.description);
        Button dateButton = view.findViewById(R.id.pickDate);
        dateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        Button hourButton = view.findViewById(R.id.pickHour);
        hourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });
        sV= view.findViewById(R.id.scrollView);

        Button pickLocation= view.findViewById(R.id.pickLocation);
        pickLocation.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                showLocationPicker();
            }
        });
        Button createButton= view.findViewById(R.id.create);
        createButton.setOnClickListener(new View.OnClickListener (){

            @Override
            public void onClick(View view) {
                create();
            }
        });

        FloatingActionButton fab =
                getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        publicMeeting = view.findViewById(R.id.switch_create);
        publicMeeting.setOnCheckedChangeListener(this);
        publicMeeting.setText(R.string.private_meeting);
        Public=false;

        geocoder = new Geocoder(this.getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(myLocation.latitude, myLocation.longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                location.setText(strReturnedAddress.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.mapView, mMapFragment).commit();
        mMapFragment.getMapAsync(this);
        return view;
    }


    private void showTimePickerDialog() {
        final EditText timeText = view.findViewById(R.id.hour);
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourSet, int minuteSet) {
                String resHour=String.valueOf(hourSet);
                String resMin=String.valueOf(minuteSet);
                hour2=hourSet;
                minute=minuteSet;
                if (minuteSet<10) resMin="0"+resMin;
                if (hourSet<10) resHour="0"+resHour;
                final String selectedTime = resHour + ":" + resMin;
                timeText.setText(selectedTime);
            }
        });
        timePickerFragment.setValues(hour2, minute);
        timePickerFragment.show(getFragmentManager(), "timePicker");
    }

    private void showDatePickerDialog() {
        final EditText dateText = view.findViewById(R.id.date);
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yearSet, int monthSet, int daySet) {
                // +1 because january is zero
                year = yearSet;
                month = monthSet +1;
                day = daySet;
                final String selectedDate = year + "/" + month + "/" + day;
                dateText.setText(selectedDate);
            }
        });
        datePickerFragment.setValues(year, month, day);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }


    private void create(){
        Name = name.getText().toString();
        Date = date.getText().toString();

        String Hour = hour.getText().toString();
        Description = description.getText().toString();
        Latitude= String.valueOf(myLocation.latitude);
        Latitude = Latitude.substring(0,Math.min(Latitude.length(),10));
        Longitude=String.valueOf(myLocation.longitude);
        Longitude= Longitude.substring(0,Math.min(Longitude.length(),10));
        String hourTxt,minuteTxt;
        String yearTxt,monthTxt,dayTxt;
        if (hour2 < 10) hourTxt = "0"+hour2;
        else hourTxt = String.valueOf(hour2);
        if (minute < 10) minuteTxt = "0"+minute;
        else minuteTxt = String.valueOf(minute);
        yearTxt = String.valueOf(year);
        if (month < 10) monthTxt = "0"+month;
        else monthTxt = String.valueOf(month);
        if (day < 10) dayTxt = "0"+day;
        else dayTxt = String.valueOf(day);
        Date=yearTxt+"-"+monthTxt+"-"+dayTxt+"T"+hourTxt+":"+minuteTxt+":00Z";
        Level = Integer.parseInt(level.getText().toString());


        if (Name.isEmpty() || Date.isEmpty() || Hour.isEmpty() || Latitude.isEmpty() || Longitude.isEmpty() ||level.getText().toString().isEmpty()){
            Toast.makeText(this.getContext(), this.getString(R.string.empty_create_error), Toast.LENGTH_SHORT).show();
        }
        else if (Level > CurrentSession.getInstance().getCurrentUser().getLevel()) Toast.makeText(this.getContext(),R.string.level_too_high, Toast.LENGTH_SHORT).show();
        else if(Name.length()>=100) Toast.makeText(this.getContext(),this.getString(R.string.big_name_error), Toast.LENGTH_SHORT).show();
        else if(Description.length()>=500) Toast.makeText(this.getContext(), this.getString(R.string.big_description_error), Toast.LENGTH_SHORT).show();
        else{
            //DB stuff
            if (Public)  onCreateDialog(getActivity(), this.getString(R.string.public_friends), this.getString(R.string.public_yes_friends), this.getString(R.string.public_no_friends));
            else onCreateDialog(getActivity(), this.getString(R.string.private_friends), this.getString(R.string.private_yes_friends), this.getString(R.string.private_no_friends));
            //Toast.makeText(this.getContext(),"Meeting name: "+Name+", Date:"+Date+", Hour: "+Hour+", Level: "+Level+", Description: "+Description+", Kind of meeting: "+Public.toString(), Toast.LENGTH_SHORT).show();
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
            create();
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

        myMarker = map.addMarker(new MarkerOptions().position(myLocation).title("Meeting"));
        moveMapCameraAndMarker(myLocation);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(myLocation.latitude, myLocation.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null) {
            Address returnedAddress = addresses.get(0);
            StringBuilder strReturnedAddress = new StringBuilder("");

            for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
            }
            Log.e("CREATE MEETING", strReturnedAddress.toString());
            location.setText(strReturnedAddress.toString());
        }

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                sV.requestDisallowInterceptTouchEvent(true);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) { //Retrieve the result from the PlacePicker
                Place place = PlacePicker.getPlace(getActivity(), data);
                myLocation = place.getLatLng();
                moveMapCameraAndMarker(myLocation);
            }
        }
    }

    private void moveMapCameraAndMarker(LatLng location) {
        CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(location, 15);
        maps.moveCamera(camera);
        myMarker.remove();
        myMarker = maps.addMarker(new MarkerOptions().position(location).title("Meeting"));
    }

    public Dialog onCreateDialog(final FragmentActivity fa, String message, String pos, String neg) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setPositiveButton(pos, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        friends=true;
                        create_meeting();
                    }
                })
                .setNegativeButton(neg, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        create_meeting();
                        fa.finish();
                    }});
                    // Create the AlertDialog object and return it
        builder.show();
        return builder.create();

    }



    private void create_meeting(){
        new newMeeting().execute();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Public=b;
        if (Public) publicMeeting.setText(R.string.public_meeting);
        else publicMeeting.setText(R.string.private_meeting);
    }

    private class newMeeting extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings){
          /*  try {
                m= meetingAdapter.createMeeting(Name,Description,Public,Level,Date,Latitude,Longitude);
            } catch (ParamsException  e) {
                e.printStackTrace();
            } catch (AutorizationException e) {

                e.printStackTrace();
            }*/
            return null;
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            if (friends){
                Log.e("CREATE",String.valueOf(friends));
                Intent i=new Intent(getActivity(), MeetingFriendsActivity.class);
                //Integer MeetingId=m.getId();
                //i.putExtra("level", Level);
                //i.putExtra("meetingId",MeetingId);
                startActivity(i);
            }
            getActivity().finish();
        }
    }

}
