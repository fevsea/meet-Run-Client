package edu.upc.fib.meetnrun.views.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Date;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Challenge;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;

public class ChallengeFragment extends Fragment {

    private Challenge challenge;

    public ChallengeFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge, container, false);
        int challengeId = savedInstanceState.getInt("id");
        return view;
    }

    private void updateViews() {

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
            challenge = new Challenge(0, current, other, 50, deadline.toString(), new Date().toString(), 25, 32);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            updateViews();
        }

    }

}
