package edu.upc.fib.meetnrun.views.fragments;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.asynctasks.AddFriend;
import edu.upc.fib.meetnrun.asynctasks.ReportUser;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.ForbiddenException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;

/**
 * Created by eric on 2/11/17.
 */

public class UserProfileFragment extends ProfileFragmentTemplate {

    public static UserProfileFragment newInstance(String id, String userName, String name, String postalCode) {
        UserProfileFragment fragmentFirst = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("userName", userName);
        args.putString("name", name);
        args.putString("postalCode", postalCode);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

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
        return getResources().getString(R.string.friend_request_dialog_message) + " " + currentFriend.getUsername() + "?";
    }

    @Override
    protected void getMethod(String s) {
        callAddFriend(s);
    }

    private void callAddFriend(String s) {
        new AddFriend() {
            @Override
            public void onExceptionReceived(GenericException e) {
                if (e instanceof AuthorizationException) {
                    Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
                } else if (e instanceof ParamsException) {
                    Toast.makeText(getActivity(), R.string.params_error, Toast.LENGTH_LONG).show();
                }
            }

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

    public int getTitle() {
        return R.string.user_profile_label;
    }
}
