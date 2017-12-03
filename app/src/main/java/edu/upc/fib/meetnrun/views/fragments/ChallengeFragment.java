package edu.upc.fib.meetnrun.views.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Challenge;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;

public class ChallengeFragment extends Fragment {

    private Challenge challenge;

    private TextView userUsername;
    private TextView userName;
    private TextView userLevel;
    private TextView userPhoto;
    private TextView userProgressText;
    private ProgressBar userProgressBar;

    private TextView opponentUsername;
    private TextView opponentName;
    private TextView opponentLevel;
    private TextView opponentPhoto;
    private TextView opponentProgressText;
    private ProgressBar opponentProgressBar;
    private String expirationTextResourceDays;
    private String expirationTextResourceNoDays;
    private String progressTextResource;

    private TextView endsIn;

    public ChallengeFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge, container, false);

        int challengeId = getActivity().getIntent().getIntExtra("id", -1);

        if (challengeId == -1) {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }

        userUsername = view.findViewById(R.id.user_username);
        userName = view.findViewById(R.id.user_name);
        userLevel = view.findViewById(R.id.user_level);
        userPhoto = view.findViewById(R.id.user_photo);
        userProgressText = view.findViewById(R.id.user_progress);
        userProgressBar = view.findViewById(R.id.user_progress_bar);

        opponentUsername = view.findViewById(R.id.opponent_username);
        opponentName = view.findViewById(R.id.opponent_name);
        opponentLevel = view.findViewById(R.id.opponent_level);
        opponentPhoto = view.findViewById(R.id.opponent_photo);
        opponentProgressText = view.findViewById(R.id.opponent_progress);
        opponentProgressBar = view.findViewById(R.id.opponent_progress_bar);

        endsIn = view.findViewById(R.id.ends_in);
        expirationTextResourceDays = view.getResources().getString(R.string.ends_in_days_hours_minutes);
        expirationTextResourceNoDays = view.getResources().getString(R.string.ends_in_hours_minutes);
        progressTextResource = view.getResources().getString(R.string.challenge_progress_km);

        GetChallenge getChallenge = new GetChallenge();
        getChallenge.execute(challengeId);
        return view;
    }

    private void updateViews() {

        User currentUser = CurrentSession.getInstance().getCurrentUser();
        User opponent;
        float userProgress;
        float opponentProgress;
        if (currentUser.getId().equals(challenge.getCreator().getId())) {
            opponent = challenge.getChallenged();
            userProgress = challenge.getCreatorDistance();
            opponentProgress = challenge.getChallengedDistance();
        }
        else {
            opponent = challenge.getCreator();
            userProgress = challenge.getChallengedDistance();
            opponentProgress = challenge.getCreatorDistance();
        }

        userUsername.setText(currentUser.getUsername());
        userName.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        userLevel.setText(String.valueOf(currentUser.getLevel()));
        userPhoto.setText(String.valueOf(currentUser.getUsername().charAt(0)));
        userProgressBar.setMax((int)challenge.getDistance());
        userProgressBar.setProgress((int)userProgress);
        String userProgressString = String.format(Locale.forLanguageTag("es"),
                progressTextResource, userProgress, challenge.getDistance(),
                ((float)userProgress)/((float)challenge.getDistance())*100, "%");
        userProgressText.setText(userProgressString);

        opponentUsername.setText(opponent.getUsername());
        opponentName.setText(opponent.getFirstName() + " " + opponent.getLastName());
        opponentLevel.setText(String.valueOf(opponent.getLevel()));
        opponentPhoto.setText(String.valueOf(opponent.getUsername().charAt(0)));
        opponentProgressBar.setMax((int)challenge.getDistance());
        opponentProgressBar.setProgress((int)opponentProgress);
        String opponentProgressString = String.format(Locale.forLanguageTag("es"),
                progressTextResource, opponentProgress, challenge.getDistance(),
                ((float)opponentProgress)/((float)challenge.getDistance())*100, "%");
        opponentProgressText.setText(opponentProgressString);

        endsIn.setText(getExpirationText(challenge.getDeadline()));

    }

    private String getExpirationText(String deadline) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.forLanguageTag("es"));
        Date dateTime;
        try {
            dateTime = inputFormat.parse(deadline);
        } catch (ParseException e) {
            dateTime = new Date(deadline);
        }
        final long millis = dateTime.getTime() - System.currentTimeMillis();
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        long hours = TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(days);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.DAYS.toMinutes(days) - TimeUnit.HOURS.toMinutes(hours);
        String expirationText;
        if (days > 0) {
            expirationText = String.format(Locale.forLanguageTag("es"), expirationTextResourceDays, days, hours, minutes);
        }
        else {
            expirationText = String.format(Locale.forLanguageTag("es"), expirationTextResourceNoDays, hours, minutes);
        }
        return expirationText;
    }

    private class GetChallenge extends AsyncTask<Integer, String, Boolean> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Loading challenge");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            User current = CurrentSession.getInstance().getCurrentUser();
            User other = new User(56, "aUsername", "Name", "Surname", "08001", "A?", 0);
            Date deadline = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(deadline);
            c.add(Calendar.DATE, 6);
            deadline = c.getTime();
            challenge = new Challenge(0, current, other, 51, deadline.toString(), new Date().toString(), 25, 33);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            updateViews();
        }

    }

}