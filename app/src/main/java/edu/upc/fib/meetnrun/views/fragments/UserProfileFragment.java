package edu.upc.fib.meetnrun.views.fragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.asynctasks.AddFriend;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
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
        args.putString("name",name);
        args.putString("postalCode",postalCode);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        return getResources().getString(R.string.friend_request_dialog_message)+" "+currentFriend.getUsername()+"?";
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
                }
                else if (e instanceof ParamsException) {
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_report, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                getActivity().finish();
                break;
            case R.id.report_button:
                showDialog(getString(R.string.report),getString(R.string.ok),getString(R.string.cancel));
                break;
        }
        return false;
    }

    public void showDialog(String title, String okButtonText, String negativeButtonText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setPositiveButton(okButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //TODO crida al servidor per a reportar
            }
        });
        builder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
