package edu.upc.fib.meetnrun.views.fragments;

import android.content.DialogInterface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.persistence.IGenericController;

/**
 * Created by eric on 2/11/17.
 */

public class FriendProfileFragment extends Fragment {

    private View view;
    private IGenericController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_friends_profile, container, false);

        controller = CurrentSession.getInstance().getController();

        final Bundle profileInfo = getActivity().getIntent().getExtras();

        final TextView userName = view.findViewById(R.id.userName2);
        TextView name = view.findViewById(R.id.completeName2);
        TextView postCode = view.findViewById(R.id.userPostCode2);
        ImageView img = view.findViewById(R.id.delete_friend);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = getResources().getString(R.string.delete_friend_dialog_title);
                String message = getResources().getString(R.string.delete_friend_dialog_message)+" "+profileInfo.getString("userName")+"?";
                String ok = getResources().getString(R.string.ok);
                String cancel = getResources().getString(R.string.cancel);
                showDialog(title, message, ok, cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("delete friend", userName.getText().toString());
                                new addFriend().execute(profileInfo.getString("id"));
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );

            }
        });
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        userName.setText(profileInfo.getString("userName"));
        name.setText(profileInfo.getString("name"));
        postCode.setText(profileInfo.getString("postCode"));

        return this.view;
    }

    private void showDialog(String title, String message, String okButtonText, String negativeButtonText, DialogInterface.OnClickListener ok, DialogInterface.OnClickListener cancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(okButtonText, ok);
        if (negativeButtonText != null && cancel != null)
            builder.setNegativeButton(negativeButtonText, cancel);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class addFriend extends AsyncTask<String,String,String> {

        boolean ok = false;

        @Override
        protected String doInBackground(String... s) {
            try {
                ok = controller.removeFriend(Integer.parseInt(s[0]));
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
            }
            super.onPostExecute(s);
        }
    }

}
