package edu.upc.fib.meetnrun.views.fragments;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.asynctasks.AddFriend;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
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
        callAddFriend(s);
    }

    private void callAddFriend(String s) {
        new AddFriend() {
            @Override
            public void onResponseReceived(boolean b) {
                if (b) {
                    Toast.makeText(getContext(), "Friend request sent", Toast.LENGTH_SHORT).show();
                    //currentFriend.setFriend(true);
                    getActivity().finish();
                }
            }
        }.execute(s);
    }
}
