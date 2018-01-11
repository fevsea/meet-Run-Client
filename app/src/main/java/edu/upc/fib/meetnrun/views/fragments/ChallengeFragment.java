package edu.upc.fib.meetnrun.views.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IChallengeAdapter;
import edu.upc.fib.meetnrun.asynctasks.AcceptOrRejectChallenge;
import edu.upc.fib.meetnrun.asynctasks.AcceptOrRejectFriend;
import edu.upc.fib.meetnrun.asynctasks.GetChallenge;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.Challenge;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.utils.UtilsGlobal;

import static edu.upc.fib.meetnrun.utils.UtilsViews.getExpirationText;

public class ChallengeFragment extends BaseFragment implements View.OnClickListener {

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

        callGetChallenge(challengeId);


        FloatingActionButton fab = view.findViewById(R.id.activity_fab);

        if (fab != null) {
            fab.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    private void updateViews() {

        User currentUser = CurrentSession.getInstance().getCurrentUser();
        User opponent;
        float userProgress;
        float opponentProgress;
        float totalDistance = challenge.getDistance() / 1000.0f;
        if (currentUser.getId().equals(challenge.getCreator().getId())) {
            opponent = challenge.getChallenged();
            userProgress = challenge.getCreatorDistance() / 1000.0f;
            opponentProgress = challenge.getChallengedDistance() / 1000.0f;
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
            userProgressBar.setMax((int) totalDistance);
            userProgressBar.setProgress((int) userProgress);
            String userProgressString = String.format(Locale.forLanguageTag("es"),
                    progressTextResource, userProgress, totalDistance,
                    ((float) userProgress) / ((float) totalDistance) * 100, "%");
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
            opponentProgressBar.setMax((int) totalDistance);
            opponentProgressBar.setProgress((int) opponentProgress);
            String opponentProgressString = String.format(Locale.forLanguageTag("es"),
                    progressTextResource, opponentProgress, totalDistance,
                    ((float) opponentProgress) / ((float) totalDistance) * 100, "%");
            opponentProgressText.setText(opponentProgressString);
        }
        else {
            opponentProgressBar.setVisibility(View.GONE);
            opponentProgressText.setVisibility(View.GONE);
        }


        try {
            endsIn.setText(getExpirationText(challenge.getDeadline(), expirationTextResourceDays, expirationTextResourceNoDays, expirationPastTextResourceDays, expirationPastTextResourceNoDays));
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
        callAcceptOrRejectChallenge(accept);
    }


    private void callGetChallenge(final int challengeId) {
        new GetChallenge() {
            @Override
            public void onExceptionReceived(GenericException e) {
                if (e instanceof AuthorizationException) {
                    Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
                    dismissProgressBarsOnError();
                }
                else if (e instanceof NotFoundException) {
                    Toast.makeText(getActivity(), R.string.not_found_error, Toast.LENGTH_LONG).show();
                    dismissProgressBarsOnError();
                }
            }

            @Override
            public void onResponseReceived(Challenge challengeResponse) {
                challenge = challengeResponse;
                updateViews();
            }
        }.execute(challengeId);
    }

    private void dismissProgressBarsOnError() {
        userProgressBar.setVisibility(View.INVISIBLE);
        opponentProgressBar.setVisibility(View.INVISIBLE);
    }

    private void callAcceptOrRejectChallenge(Boolean accept) {
        new AcceptOrRejectChallenge(challenge.getId()) {
            @Override
            public void onExceptionReceived(GenericException e) {
                if (e instanceof AuthorizationException ) {
                    Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
                    dismissProgressBarsOnError();
                }
                else if ( e instanceof NotFoundException) {
                    Toast.makeText(getActivity(), R.string.not_found_error, Toast.LENGTH_LONG).show();
                    dismissProgressBarsOnError();
                }
            }

            @Override
            public void onResponseReceived() {
                getActivity().finish();
            }
        }.execute(accept);
    }

    public int getTitle() {
        return R.string.challenge;
    }

}
