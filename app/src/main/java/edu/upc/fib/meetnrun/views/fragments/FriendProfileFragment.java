package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.views.CreateChallengeActivity;

/**
 * Created by eric on 2/11/17.
 */

public class FriendProfileFragment extends ProfileFragmentTemplate implements View.OnClickListener {

    @Override
    protected void setImage() {}

    @Override
    protected String setDialogTitle() {
        return getResources().getString(R.string.delete_friend_dialog_title);
    }

    @Override
    protected String setDialogMessage() {
        return getResources().getString(R.string.delete_friend_dialog_message)+" "+profileInfo.getString("userName")+"?";
    }

    @Override
    protected void getMethod(String s) {
        new removeFriend().execute(s);
    }

    @Override
    protected void configureChallengeButton() {
        challengeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(getActivity(), CreateChallengeActivity.class);
        i.putExtra("id",Integer.parseInt(this.profileInfo.getString("id")));
        startActivity(i);
    }

    private class removeFriend extends AsyncTask<String,String,String> {

        boolean ok = false;

        @Override
        protected String doInBackground(String... s) {
            try {
                ok = friendsDBAdapter.removeFriend(Integer.parseInt(s[0]));
            } catch (AutorizationException e) {
                e.printStackTrace();
            } catch (ParamsException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (ok) {
                Toast.makeText(getContext(), "Friend removed", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
            super.onPostExecute(s);
        }
    }

}