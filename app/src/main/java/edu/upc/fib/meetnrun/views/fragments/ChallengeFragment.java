package edu.upc.fib.meetnrun.views.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IChallengeAdapter;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.Challenge;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;

public class ChallengeFragment extends Fragment implements View.OnClickListener {

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
    private String expirationPastTextResourceDays;
    private String expirationPastTextResourceNoDays;
    private String progressTextResource;

    private Button accept;
    private Button reject;

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
        expirationPastTextResourceDays = view.getResources().getString(R.string.ended_in_days_hours_minutes);
        expirationPastTextResourceNoDays = view.getResources().getString(R.string.ended_in_hours_minutes);
        progressTextResource = view.getResources().getString(R.string.challenge_progress_km);

        accept = view.findViewById(R.id.accept);
        reject = view.findViewById(R.id.reject);

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
        if (challenge.isAccepted()) {
            userProgressBar.setMax((int) challenge.getDistance());
            userProgressBar.setProgress((int) userProgress);
            String userProgressString = String.format(Locale.forLanguageTag("es"),
                    progressTextResource, userProgress, challenge.getDistance(),
                    ((float) userProgress) / ((float) challenge.getDistance()) * 100, "%");
            userProgressText.setText(userProgressString);
        }
        else {
            userProgressBar.setVisibility(View.GONE);
            userProgressText.setVisibility(View.GONE);
        }

        opponentUsername.setText(opponent.getUsername());
        opponentName.setText(opponent.getFirstName() + " " + opponent.getLastName());
        opponentLevel.setText(String.valueOf(opponent.getLevel()));
        opponentPhoto.setText(String.valueOf(opponent.getUsername().charAt(0)));
        if (challenge.isAccepted()) {
            opponentProgressBar.setMax((int) challenge.getDistance());
            opponentProgressBar.setProgress((int) opponentProgress);
            String opponentProgressString = String.format(Locale.forLanguageTag("es"),
                    progressTextResource, opponentProgress, challenge.getDistance(),
                    ((float) opponentProgress) / ((float) challenge.getDistance()) * 100, "%");
            opponentProgressText.setText(opponentProgressString);
        }
        else {
            opponentProgressBar.setVisibility(View.GONE);
            opponentProgressText.setVisibility(View.GONE);
        }


        try {
            endsIn.setText(getExpirationText(challenge.getDeadline()));
        }
        catch (ParseException e) {
            endsIn.setText("");
            Log.e("PARSEEXCEPTION", e.getMessage());
        }

        if (challenge.isAccepted()) {
            accept.setVisibility(View.GONE);
            reject.setVisibility(View.GONE);
        }
        else {
            accept.setOnClickListener(this);
            reject.setOnClickListener(this);
        }


    }

    private String getExpirationText(String deadline) throws ParseException {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.forLanguageTag("es"));
        Date dateTime;
        String expirationText;
        try {
            dateTime = inputFormat.parse(deadline);
        } catch (ParseException e) {
            inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.forLanguageTag("es"));
            dateTime = inputFormat.parse(deadline);
        }
        if (dateTime.getTime() > System.currentTimeMillis()) {
            final long millis = dateTime.getTime() - System.currentTimeMillis();
            long days = TimeUnit.MILLISECONDS.toDays(millis);
            long hours = TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(days);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.DAYS.toMinutes(days) - TimeUnit.HOURS.toMinutes(hours);
            if (days > 0) {
                expirationText = String.format(Locale.forLanguageTag("es"), expirationTextResourceDays, days, hours, minutes);
            } else {
                expirationText = String.format(Locale.forLanguageTag("es"), expirationTextResourceNoDays, hours, minutes);
            }
        }
        else {
            final long millis = System.currentTimeMillis() - dateTime.getTime();
            long days = TimeUnit.MILLISECONDS.toDays(millis);
            long hours = TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(days);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.DAYS.toMinutes(days) - TimeUnit.HOURS.toMinutes(hours);
            if (days > 0) {
                expirationText = String.format(Locale.forLanguageTag("es"), expirationPastTextResourceDays, days, hours, minutes);
            } else {
                expirationText = String.format(Locale.forLanguageTag("es"), expirationPastTextResourceNoDays, hours, minutes);
            }
        }
        return expirationText;
    }

    @Override
    public void onClick(View v) {
        boolean accept = false;
        switch (v.getId()) {
            case R.id.accept:
                accept = true;
                break;
            case R.id.reject:
                accept = false;
                break;
        }
        AcceptOrRejectChallenge acceptOrRejectChallenge = new AcceptOrRejectChallenge();
        acceptOrRejectChallenge.execute(accept);
    }

    private class GetChallenge extends AsyncTask<Integer, String, Boolean> {

        private ProgressDialog progressDialog;
        private Exception ex;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(getResources().getString(R.string.loading));
            progressDialog.setMessage(getResources().getString(R.string.loading_challenge));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            try {
                challenge = CurrentSession.getInstance().getChallengeAdapter().getChallenge(params[0]);
            }
            catch(AuthorizationException | NotFoundException e) {
                ex = e;
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            if (result && ex == null) {
                updateViews();
            }
            else if (ex instanceof AuthorizationException) {
                Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
            }
            else if (ex instanceof NotFoundException) {
                Toast.makeText(getActivity(), R.string.not_found_error, Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getActivity(), R.string.error_loading, Toast.LENGTH_LONG).show();
            }
        }

    }

    private class AcceptOrRejectChallenge extends AsyncTask<Boolean, String, Boolean> {

        Exception exception;

        @Override
        protected Boolean doInBackground(Boolean... params) {
            IChallengeAdapter challengeAdapter = CurrentSession.getInstance().getChallengeAdapter();
            try {
                if (params[0]) {
                    challengeAdapter.acceptChallenge(challenge.getId());
                }
                else {
                    challengeAdapter.deleteRejectChallenge(challenge.getId());
                }
            }
            catch (NotFoundException | AuthorizationException e) {
                exception = e;
            }
            return true;
        }

        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            if (s && exception == null) {
                getActivity().finish();
            }
            else if (exception instanceof AuthorizationException){
                Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
            }
            else if (exception instanceof NotFoundException) {
                Toast.makeText(getActivity(), R.string.not_found_error, Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getActivity(), R.string.error_loading, Toast.LENGTH_LONG).show();
            }
        }

    }

}
