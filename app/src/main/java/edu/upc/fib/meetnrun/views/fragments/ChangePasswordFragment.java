package edu.upc.fib.meetnrun.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.ILoginAdapter;
import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.ForbiddenException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.CreateMeetingActivity;
import edu.upc.fib.meetnrun.views.EditProfileActivity;
import edu.upc.fib.meetnrun.views.ProfileActivity;

public class ChangePasswordFragment extends Fragment {

    private ILoginAdapter controller = CurrentSession.getInstance().getLoginAdapter();
    EditText currentPassText;
    EditText newPassText;
    EditText newPass2Text;

    String currentPass;
    String newPass;
    String newPass2;

    View v;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_change_password, container, false);

        checkParameters();

        Button button = v.findViewById(R.id.save_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveNewPass();
            }
        });

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        return v;
    }

    private void checkParameters() {
        currentPassText = (EditText) v.findViewById(R.id.currentPass_enter);
        newPassText = (EditText) v.findViewById(R.id.newPass_enter);
        newPass2Text = (EditText) v.findViewById(R.id.repeatNewPass_enter);

        currentPass = currentPassText.getText().toString();
        newPass = newPassText.getText().toString();
        newPass2 = newPass2Text.getText().toString();

        /*if (currentPass.equals("")) {
            Toast.makeText(getApplicationContext(), "Current password field is empty", Toast.LENGTH_SHORT).show();
        }
        else if (newPass.equals("")) {
            Toast.makeText(getActivity(), "Password field is empty", Toast.LENGTH_SHORT).show();
        }
        else if (newPass2.equals("")) {
            Toast.makeText(getActivity(), "Repeat password field is empty", Toast.LENGTH_SHORT).show();
        }
        else if (newPass.length() < 5) {
            Toast.makeText(getActivity(), "Password field is too short", Toast.LENGTH_SHORT).show();
        }
        else if (!newPass.equals(newPass2)) {
            Toast.makeText(getActivity(), "Passwords don't match", Toast.LENGTH_SHORT).show();
        }
        else {

        }*/
    }

    private void changeToUserProfile() {
        Intent intent;
        intent = new Intent(getActivity(), ProfileActivity.class);
        startActivity(intent);
    }

    private void saveNewPass() {
        new updatePass().execute(currentPass, newPass);
    }


    private class updatePass extends AsyncTask<String, String, Boolean> {

        Exception exception = null;
        Boolean actualitzat_correctament;

        @Override
        protected Boolean doInBackground(String... params) {

                try {
                    actualitzat_correctament = controller.changePassword(params[0], params[1]);
                } catch (AutorizationException e) {
                    e.printStackTrace();
                } catch (ForbiddenException e) {
                    e.printStackTrace();
                }

            return actualitzat_correctament;
        }

        @Override
        protected void onPostExecute(Boolean b) {

            if(b) {
                changeToUserProfile();
            }
            else {
                Toast.makeText(getActivity(), getResources().getString(R.string.error_change_pass), Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(b);
        }
    }
}
