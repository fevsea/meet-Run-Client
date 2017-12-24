package edu.upc.fib.meetnrun.views.fragments;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.asynctasks.CreateChallenge;
import edu.upc.fib.meetnrun.asynctasks.GetUser;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Challenge;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.utils.UtilsGlobal;

/**
 * Created by guillemcastro on 23/12/2017.
 */

public class CreateChallengeFragment extends BaseFragment implements View.OnClickListener {

    private NumberPicker distancePicker;
    private EditText deadlineText;

    final private Challenge challenge = new Challenge();

    private int userID;
    private User challenged;
    ProgressBar progressBar;

    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        this.view = inflater.inflate(R.layout.activity_createchallenge, container, false);



        userID = getActivity().getIntent().getIntExtra("id", -1);
        if (userID == -1) {
            Toast.makeText(getActivity(), R.string.error_loading, Toast.LENGTH_LONG).show();
            getActivity().finish();
        }

        progressBar = view.findViewById(R.id.pb_loading);
        distancePicker = view.findViewById(R.id.distance_picker);
        distancePicker.setMinValue(0);
        distancePicker.setMaxValue(1000);
        distancePicker.setWrapSelectorWheel(false);
        deadlineText = view.findViewById(R.id.deadline_picker);
        deadlineText.setFocusable(false);
        deadlineText.setClickable(true);
        deadlineText.setOnClickListener(this);

        FloatingActionButton fab =
                getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        return this.view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.deadline_picker) {
            showDatePickerDialog();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_meeting_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.done_button) {
            challenge.setDistance(distancePicker.getValue()*1000);
            challenge.setCreator(CurrentSession.getInstance().getCurrentUser());
            callGetUser(userID);
            challenge.setChallenged(challenged);
            callCreateChallenge();
        }
        return false;
    }

    private void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yearSet, int monthSet, int daySet) {
                Date dateTime;
                if(challenge.getDeadline() != null) {
                    dateTime = UtilsGlobal.parseDate(challenge.getDeadline());
                } else {
                    dateTime = new Date();
                }
                Calendar date = new GregorianCalendar();
                date.setTime(dateTime);
                date.set(Calendar.YEAR, yearSet/* + 1900*/);
                date.set(Calendar.MONTH, monthSet);
                date.set(Calendar.DAY_OF_MONTH, daySet);
                challenge.setDeadline(UtilsGlobal.formatDate(date.getTime()));
                final String selectedDate = ((daySet<10)?"0"+daySet:daySet) + "/" + (((monthSet+1)<10)?"0"+(monthSet+1):(monthSet+1)) + "/" + yearSet;
                deadlineText.setText(selectedDate);
            }
        });
        Date dateTime;
        if(challenge.getDeadline() != null) {
            dateTime = UtilsGlobal.parseDate(challenge.getDeadline());
        }
        else {
            dateTime = new Date();
        }
        if (dateTime != null) {
            Calendar date = new GregorianCalendar();
            date.setTime(dateTime);
            datePickerFragment.setValues(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
        }
        datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private void callCreateChallenge() {
        progressBar.setVisibility(View.VISIBLE);
        CreateChallenge createChallenge = new CreateChallenge(challenged,challenge) {
            @Override
            public void onResponseReceived() {
                progressBar.setVisibility(View.INVISIBLE);
                getActivity().finish();
            }
        };
        try {
            createChallenge.execute();
        }
        catch (AuthorizationException e) {
            Toast.makeText(getContext(), R.string.authorization_error, Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
        catch (ParamsException e) {
            Toast.makeText(getContext(), R.string.params_error, Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }


    private void callGetUser(int userID) {
        GetUser getUser = new GetUser(userID) {
            @Override
            public void onResponseReceied(User u) {
                challenged = u;
            }
        };
        try {
            getUser.execute();
        }
        catch (NotFoundException e) {
            Toast.makeText(getContext(), getResources().getString(R.string.edit_meeting_error_dialog_message), Toast.LENGTH_SHORT).show();
        }
    }

}
