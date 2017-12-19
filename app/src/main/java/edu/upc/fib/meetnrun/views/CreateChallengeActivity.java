package edu.upc.fib.meetnrun.views;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
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
import edu.upc.fib.meetnrun.views.fragments.DatePickerFragment;

public class CreateChallengeActivity extends AppCompatActivity implements View.OnClickListener{

    private NumberPicker distancePicker;
    private EditText deadlineText;

    final private Challenge challenge = new Challenge();

    private int userID;
    private User challenged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createchallenge);

        setTitle(R.string.create_challenge);

        userID = getIntent().getIntExtra("id", -1);
        if (userID == -1) {
            Toast.makeText(this, R.string.error_loading, Toast.LENGTH_LONG).show();
            finish();
        }

        distancePicker = findViewById(R.id.distance_picker);
        distancePicker.setMinValue(0);
        distancePicker.setMaxValue(1000);
        distancePicker.setWrapSelectorWheel(false);
        deadlineText = findViewById(R.id.deadline_picker);
        deadlineText.setFocusable(false);
        deadlineText.setClickable(true);
        deadlineText.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.deadline_picker) {
            showDatePickerDialog();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.edit_meeting_menu, menu);
        return true;
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }


    private class CreateChallenge extends AsyncTask<Challenge, String ,Boolean> {

        Exception exception = null;
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(CreateChallengeActivity.this);
            mProgressDialog.setTitle(R.string.saving);
            mProgressDialog.setMessage(getResources().getString(R.string.saving_challenge));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
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
            mProgressDialog.dismiss();
            if (result && exception == null) {
                finish();
            }
            else if (exception instanceof AuthorizationException) {
                Toast.makeText(CreateChallengeActivity.this, R.string.authorization_error, Toast.LENGTH_LONG).show();
            }
            else if (exception instanceof ParamsException) {
                Toast.makeText(CreateChallengeActivity.this, R.string.params_error, Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(CreateChallengeActivity.this, getResources().getString(R.string.error_saving_challenge), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CreateChallengeActivity.this, getResources().getString(R.string.edit_meeting_error_dialog_message), Toast.LENGTH_SHORT).show();
            }
            challenged = result;
        }
    }

}
