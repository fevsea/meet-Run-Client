package edu.upc.fib.meetnrun.views.fragments;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;

import static android.app.Activity.RESULT_OK;

public class EditMeetingFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback, CompoundButton.OnCheckedChangeListener{

    private View view;

    private GoogleMap map;
    private Marker marker;
    private Meeting meeting; //= new Meeting(1, "HOLA", "Descr \n ipcion \n rand \n om", false, 5, new Date().toString(), "41", "2");
    private IMeetingAdapter controller;
    private boolean thereWasAnAttemptToSave = false;
    private EditText titleText;
    private EditText descriptionText;
    private EditText levelText;
    private ScrollView scrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_meeting, container, false);
        this.view = view;

        this.controller = CurrentSession.getInstance().getMeetingAdapter();
        Log.i("GET Meeting with ID: ", String.valueOf(getActivity().getIntent().getIntExtra("id", -1)));
        GetMeeting getMeeting = new GetMeeting();
        getMeeting.execute(getActivity().getIntent().getIntExtra("id", -1));
        return view;
    }

    private void populateViews(){

        FloatingActionButton fab =
                getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        titleText = view.findViewById(R.id.meeting_title);
        titleText.setText(meeting.getTitle());
        descriptionText = view.findViewById(R.id.meeting_description);
        descriptionText.setText(meeting.getDescription());
        EditText dateText = view.findViewById(R.id.meeting_date);
        Calendar date = new GregorianCalendar();
        //date.setTime(meeting.getDateTime());
        Log.i("DATE", meeting.getDate());
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date dateTime = null;
        try {
            dateTime = inputFormat.parse(meeting.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
            dateTime = new Date();
        }
        date.setTime(dateTime);
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);
        dateText.setText(((day<10)?"0"+day:day) + "/" + (((month+1)<10)?"0"+(month+1):(month+1)) + "/" + year);
        EditText timeText = view.findViewById(R.id.meeting_time);
        int hour = date.get(Calendar.HOUR_OF_DAY);
        int minute = date.get(Calendar.MINUTE);
        timeText.setText(((hour<10)?"0"+hour:hour) + ":" + ((minute<10)?"0"+minute:minute));
        Switch isPublic = view.findViewById(R.id.isPublic);
        isPublic.setChecked(meeting.getPublic());
        levelText = view.findViewById(R.id.meeting_level);
        levelText.setText(String.valueOf(meeting.getLevel()));
        scrollView = view.findViewById(R.id.scrollView);

        Button changeDateButton = view.findViewById(R.id.change_date_button);
        changeDateButton.setOnClickListener(this);
        Button changeTimeButton = view.findViewById(R.id.change_time_button);
        changeTimeButton.setOnClickListener(this);
        Button changeLocationButton = view.findViewById(R.id.change_location_button);
        changeLocationButton.setOnClickListener(this);
        isPublic.setOnCheckedChangeListener(this);

        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.map, mMapFragment).commit();
        mMapFragment.getMapAsync(this);

        getActivity().setTitle(getResources().getString(R.string.edit_meeting) + " " + meeting.getTitle());
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
            case R.id.change_location_button:
                showLocationPicker();
                break;
        }

    }

    private void showDatePickerDialog() {
        final EditText dateText = view.findViewById(R.id.meeting_date);
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yearSet, int monthSet, int daySet) {
                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                Date dateTime = null;
                try {
                    dateTime = inputFormat.parse(meeting.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                    dateTime = new Date(meeting.getDate());
                }                //Date dateTime = meeting.getDateTime();
                Calendar date = new GregorianCalendar();
                date.setTime(dateTime);
                date.set(Calendar.YEAR, yearSet/* + 1900*/);
                date.set(Calendar.MONTH, monthSet);
                date.set(Calendar.DAY_OF_MONTH, daySet);
                meeting.setDate(inputFormat.format(date.getTime()));
                //meeting.setDateTime(date.getTime());
                final String selectedDate = ((daySet<10)?"0"+daySet:daySet) + "/" + (((monthSet+1)<10)?"0"+(monthSet+1):(monthSet+1)) + "/" + yearSet;
                dateText.setText(selectedDate);
            }
        });
        //Date dateTime = meeting.getDateTime();
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date dateTime = null;
        try {
            dateTime = inputFormat.parse(meeting.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
            dateTime = new Date(meeting.getDate());
        }
        if (dateTime != null) {
            Calendar date = new GregorianCalendar();
            date.setTime(dateTime);
            datePickerFragment.setValues(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
        }
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    private void showTimePickerDialog() {
        final EditText timeText = view.findViewById(R.id.meeting_time);
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourSet, int minuteSet) {
                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                Date dateTime = null;
                try {
                    dateTime = inputFormat.parse(meeting.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                    dateTime = new Date(meeting.getDate());
                }
                Calendar date = new GregorianCalendar();
                date.setTime(dateTime);
                date.set(Calendar.HOUR_OF_DAY, hourSet);
                date.set(Calendar.MINUTE, minuteSet);
                //meeting.setDateTime(date.getTime());
                meeting.setDate(inputFormat.format(date.getTime()));
                final String selectedTime = ((hourSet<10)?"0"+hourSet:hourSet) + ":" + ((minuteSet<10)?"0"+minuteSet:minuteSet);
                timeText.setText(selectedTime);
            }
        });
        //Date dateTime = meeting.getDateTime();
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date dateTime = null;
        try {
            dateTime = inputFormat.parse(meeting.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
            dateTime = new Date(meeting.getDate());
        }
        Calendar date = new GregorianCalendar();
        date.setTime(dateTime);
        timePickerFragment.setValues(date.get(Calendar.HOUR), date.get(Calendar.MINUTE));
        timePickerFragment.show(getFragmentManager(), "timePicker");
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
        this.map = map;
        LatLng location = new LatLng(Double.valueOf(meeting.getLatitude()), Double.valueOf(meeting.getLongitude()));
        marker = map.addMarker(new MarkerOptions().position(location).title("Meeting"));
        moveMapCameraAndMarker(location);
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                scrollView.requestDisallowInterceptTouchEvent(true);
            }
        });
    }

    private void moveMapCameraAndMarker(LatLng location) {
        CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(location, 15);
        map.moveCamera(camera);
        marker.remove();
        marker = map.addMarker(new MarkerOptions().position(location).title("Meeting"));
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
            thereWasAnAttemptToSave = true;
            meeting.setTitle(titleText.getText().toString());
            meeting.setDescription(descriptionText.getText().toString());
            meeting.setLevel(Integer.valueOf(levelText.getText().toString()));
            SaveMeeting saveMeeting = new SaveMeeting();
            saveMeeting.execute(this.meeting);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) { //Retrieve the result from the PlacePicker
                Place place = PlacePicker.getPlace(getActivity(), data);
                LatLng location = place.getLatLng();
                meeting.setLatitude(String.valueOf(location.latitude));
                meeting.setLongitude(String.valueOf(location.longitude));
                moveMapCameraAndMarker(location);
            }
        }
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.meeting.setPublic(isChecked);
    }

    private class SaveMeeting extends AsyncTask<Meeting, String, Boolean> {

        Exception exception = null;
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setTitle(R.string.saving);
            mProgressDialog.setMessage(getResources().getString(R.string.saving_meeting));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Meeting... params) {
            Boolean res = false;
            try {
                res = controller.updateMeeting(params[0]);
                //TODO Pending to catch correctly
            }
            catch (NotFoundException | ParamsException e) {
                exception = e;
            } catch (AuthorizationException e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mProgressDialog.dismiss();
            if (exception != null || !result) {
                Toast.makeText(getActivity(), getResources().getString(R.string.edit_meeting_error_dialog_message), Toast.LENGTH_SHORT).show();
            }
            else {
                getActivity().finish();
            }
        }

    }

    private class GetMeeting extends AsyncTask<Integer, String, Meeting> {

        Exception exception = null;

        @Override
        protected Meeting doInBackground(Integer... params) {
            Meeting res = null;
            try {
                res = controller.getMeeting(params[0]);
            } catch (NotFoundException e) {
                exception = e;
            }
            return res;
        }

        @Override
        protected void onPostExecute(Meeting result) {
            meeting = result;
            if (meeting == null ) {
                meeting = new Meeting(1, "HOLA", "Descr \n ipcion \n rand \n om", false, 5, new Date().toString(), "41", "2", null);
                Toast.makeText(getActivity(), getResources().getString(R.string.error_loading_meeting), Toast.LENGTH_SHORT).show();
            }
            populateViews();
        }


    }
}
