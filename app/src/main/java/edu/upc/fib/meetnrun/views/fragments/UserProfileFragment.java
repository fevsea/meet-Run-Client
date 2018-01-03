package edu.upc.fib.meetnrun.views.fragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
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
        new addFriend().execute(s);
    }

    private class addFriend extends AsyncTask<String,String,String> {

        boolean ok = false;

        @Override
        protected String doInBackground(String... s) {
            try {
                ok = friendsDBAdapter.addFriend(Integer.parseInt(s[0]));
            } catch (AuthorizationException | ParamsException e) {
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
