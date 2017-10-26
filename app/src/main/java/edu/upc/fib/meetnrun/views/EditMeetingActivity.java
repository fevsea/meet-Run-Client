package edu.upc.fib.meetnrun.views;

import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.persistence.GenericController;
import edu.upc.fib.meetnrun.persistence.IGenericController;
import edu.upc.fib.meetnrun.views.fragments.TimePickerFragment;
import edu.upc.fib.meetnrun.views.fragments.DatePickerFragment;
import edu.upc.fib.meetnrun.models.Meeting;

public class EditMeetingActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, CompoundButton.OnCheckedChangeListener {

    private GoogleMap map;
    private Marker marker;
    private Meeting meeting;
    private IGenericController controller;
    private boolean thereWasAnAttemptToSave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_meeting);
        this.controller = GenericController.getInstance();
        try {
            this.meeting = controller.getMeeting(getIntent().getIntExtra("id", -1));
        }
        catch (NotFoundException e) {
            Toast.makeText(EditMeetingActivity.this, getResources().getString(R.string.error_loading_meeting), Toast.LENGTH_SHORT).show();
        }
        if (this.meeting == null ) {
            Toast.makeText(EditMeetingActivity.this, getResources().getString(R.string.error_loading_meeting), Toast.LENGTH_SHORT).show();
            return;
        }
        EditText titleText = (EditText) findViewById(R.id.meeting_title);
        titleText.setText(meeting.getTitle());
        EditText descriptionText = (EditText) findViewById(R.id.meeting_description);
        descriptionText.setText(meeting.getDescription());
        EditText dateText = (EditText) findViewById(R.id.meeting_date);
        Calendar date = new GregorianCalendar();
        //date.setTime(meeting.getDateTime());
            date.setTime(new Date(meeting.getDate()));
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);
        dateText.setText(((day<10)?"0"+day:day) + "/" + (((month+1)<10)?"0"+(month+1):(month+1)) + "/" + year);
        EditText timeText = (EditText) findViewById(R.id.meeting_time);
        int hour = date.get(Calendar.HOUR_OF_DAY);
        int minute = date.get(Calendar.MINUTE);
        timeText.setText(((hour<10)?"0"+hour:hour) + ":" + ((minute<10)?"0"+minute:minute));
        Switch isPublic = (Switch) findViewById(R.id.isPublic);
        isPublic.setChecked(meeting.getPublic());

        Button changeDateButton = (Button) findViewById(R.id.change_date_button);
        changeDateButton.setOnClickListener(this);
        Button changeTimeButton = (Button) findViewById(R.id.change_time_button);
        changeTimeButton.setOnClickListener(this);
        Button changeLocationButton = (Button) findViewById(R.id.change_location_button);
        changeLocationButton.setOnClickListener(this);
        isPublic.setOnCheckedChangeListener(this);

        MapFragment mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.setTitle(getResources().getString(R.string.edit_meeting) + " " + meeting.getTitle());
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
        final EditText dateText = (EditText) findViewById(R.id.meeting_date);
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yearSet, int monthSet, int daySet) {
                Date dateTime = new Date(meeting.getDate());
                //Date dateTime = meeting.getDateTime();
                Calendar date = new GregorianCalendar();
                date.setTime(dateTime);
                date.set(Calendar.YEAR, yearSet + 1900);
                date.set(Calendar.MONTH, monthSet);
                date.set(Calendar.DAY_OF_MONTH, daySet);
                meeting.setDate(date.getTime().toString());
                //meeting.setDateTime(date.getTime());
                final String selectedDate = ((daySet<10)?"0"+daySet:daySet) + "/" + (((monthSet+1)<10)?"0"+(monthSet+1):(monthSet+1)) + "/" + yearSet;
                dateText.setText(selectedDate);
            }
        });
        //Date dateTime = meeting.getDateTime();
        Date dateTime = new Date(meeting.getDate());
        if (dateTime != null) {
            Calendar date = new GregorianCalendar();
            date.setTime(dateTime);
            datePickerFragment.setValues(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
        }
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void showTimePickerDialog() {
        final EditText timeText = (EditText) findViewById(R.id.meeting_time);
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourSet, int minuteSet) {
                Date dateTime = new Date(meeting.getDate());
                Calendar date = new GregorianCalendar();
                date.setTime(dateTime);
                date.set(Calendar.HOUR_OF_DAY, hourSet);
                date.set(Calendar.MINUTE, minuteSet);
                //meeting.setDateTime(date.getTime());
                meeting.setDate(date.getTime().toString());
                final String selectedTime = ((hourSet<10)?"0"+hourSet:hourSet) + ":" + ((minuteSet<10)?"0"+minuteSet:minuteSet);
                timeText.setText(selectedTime);
            }
        });
        //Date dateTime = meeting.getDateTime();
        Date dateTime = new Date(meeting.getDate());
        Calendar date = new GregorianCalendar();
        date.setTime(dateTime);
        timePickerFragment.setValues(date.get(Calendar.HOUR), date.get(Calendar.MINUTE));
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void showLocationPicker() {
        int PLACE_PICKER_REQUEST = 1;
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        }
        catch (GooglePlayServicesRepairableException|GooglePlayServicesNotAvailableException e) {
            GooglePlayServicesUtil.getErrorDialog(GooglePlayServicesUtil.isGooglePlayServicesAvailable(this), this, PLACE_PICKER_REQUEST);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        LatLng location = new LatLng(Double.valueOf(meeting.getLatitude()), Double.valueOf(meeting.getLongitude()));
        marker = map.addMarker(new MarkerOptions().position(location).title("Meeting"));
        moveMapCameraAndMarker(location);
    }

    private void moveMapCameraAndMarker(LatLng location) {
        CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(location, 15);
        map.moveCamera(camera);
        marker.remove();
        marker = map.addMarker(new MarkerOptions().position(location).title("Meeting"));
    }

    @Override
    public void onBackPressed() {
        if (!thereWasAnAttemptToSave) {
            String title = getResources().getString(R.string.edit_meeting_close_dialog_title);
            String message = getResources().getString(R.string.edit_meeting_close_dialog_message);
            String ok = getResources().getString(R.string.ok);
            String cancel = getResources().getString(R.string.cancel);
            showDialog(title, message, ok, cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditMeetingActivity.super.onBackPressed();
                        }
                    },
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }
            );
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showDialog(String title, String message, String okButtonText, String negativeButtonText, DialogInterface.OnClickListener ok, DialogInterface.OnClickListener cancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(okButtonText, ok);
        if (negativeButtonText != null && cancel != null)
            builder.setNegativeButton(negativeButtonText, cancel);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_meeting_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.done_button) {
            thereWasAnAttemptToSave = true;
            SaveMeeting saveMeeting = new SaveMeeting();
            saveMeeting.execute(this.meeting);
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) { //Retrieve the result from the PlacePicker
                Place place = PlacePicker.getPlace(this, data);
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
            mProgressDialog = new ProgressDialog(EditMeetingActivity.this);
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
            }
            catch (NotFoundException | ParamsException e) {
                exception = e;
            }
            return res;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mProgressDialog.dismiss();
            if (exception != null || !result) {
                Toast.makeText(EditMeetingActivity.this, getResources().getString(R.string.edit_meeting_error_dialog_message), Toast.LENGTH_SHORT).show();
            }
            else {
                finish();
            }
        }

    }

}
