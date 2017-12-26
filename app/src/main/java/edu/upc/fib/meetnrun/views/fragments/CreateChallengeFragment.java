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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import edu.upc.fib.meetnrun.R;
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
            new GetUser().execute(userID);
            challenge.setChallenged(challenged);
            CreateChallenge createChallenge= new CreateChallenge();
            createChallenge.execute(this.challenge);
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


    private class CreateChallenge extends AsyncTask<Challenge, String ,Boolean> {

        Exception exception = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Challenge[] params) {
            try {
                User current = CurrentSession.getInstance().getCurrentUser();
                CurrentSession.getInstance().getChallengeAdapter().createNewChallenge(current, challenged, (int)challenge.getDistance(), challenge.getDateDeadline());
            } catch (AuthorizationException | ParamsException e) {
                this.exception = e;
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressBar.setVisibility(View.INVISIBLE);
            if (result && exception == null) {
                getActivity().finish();
            }
            else if (exception instanceof AuthorizationException) {
                Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
            }
            else if (exception instanceof ParamsException) {
                Toast.makeText(getActivity(), R.string.params_error, Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getActivity(), getResources().getString(R.string.error_saving_challenge), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class GetUser extends AsyncTask<Integer,String,User> {

        Exception exception = null;

        @Override
        protected User doInBackground(Integer[] params) {
            User ret = null;
            try {
                ret = CurrentSession.getInstance().getUserAdapter().getUser(userID);
            } catch (NotFoundException e) {
                this.exception = e;
            }
            return ret;
        }

        @Override
        protected void onPostExecute(User result) {
            if (exception != null || result == null) {
                Toast.makeText(getActivity(), getResources().getString(R.string.edit_meeting_error_dialog_message), Toast.LENGTH_SHORT).show();
            }
            challenged = result;
        }
    }

}
