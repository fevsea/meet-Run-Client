package edu.upc.fib.meetnrun.views;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.persistence.GenericController;
import edu.upc.fib.meetnrun.views.fragments.CreateMeetingFragment;
import edu.upc.fib.meetnrun.views.fragments.DatePickerFragment;
import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.views.fragments.TimePickerFragment;

/**
 * Created by Javier on 14/10/2017.
 */



public class CreateMeetingActivity extends BaseReturnActivity{


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected Fragment createFragment() {
        return new CreateMeetingFragment();
    }

}