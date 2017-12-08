package edu.upc.fib.meetnrun.views.fragments;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;

/**
 * Created by eric on 2/11/17.
 */

public class UserProfileFragment extends ProfileFragmentTemplate {

    @Override
    protected void configureChallengeButton() {
        challengeButton.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void setImage() {
        img.setImageResource(R.drawable.send);
        chatImage.setVisibility(View.GONE);
    }

    @Override
    protected String setDialogTitle() {
        return getResources().getString(R.string.friend_request_dialog_title);
    }

    @Override
    protected String setDialogMessage() {
        return getResources().getString(R.string.friend_request_dialog_message)+" "+currentFriend.getUsername()+"?";
    }

    @Override
    protected void getMethod(String s) {
        new addFriend().execute(s);
    }

    private class addFriend extends AsyncTask<String,String,String> {

        boolean ok = false;

        @Override
        protected String doInBackground(String... s) {
            try {
                ok = friendsDBAdapter.addFriend(Integer.parseInt(s[0]));
            } catch (AutorizationException | ParamsException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (ok) {
                Toast.makeText(getContext(), "Friend request sent", Toast.LENGTH_SHORT).show();
                //currentFriend.setFriend(true);
                getActivity().finish();
            }
            super.onPostExecute(s);
        }
    }
}
