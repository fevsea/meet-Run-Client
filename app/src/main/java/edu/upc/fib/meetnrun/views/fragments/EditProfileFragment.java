package edu.upc.fib.meetnrun.views.fragments;

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
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.persistence.WebDBController;
import edu.upc.fib.meetnrun.views.CreateMeetingActivity;
import edu.upc.fib.meetnrun.views.EditProfileActivity;
import edu.upc.fib.meetnrun.persistence.IGenericController;
import edu.upc.fib.meetnrun.views.ProfileActivity;


public class EditProfileFragment extends Fragment {

    User u;
    private View view;
    private IGenericController controller;
    EditText userNameText;
    EditText firstNameText;
    EditText lastNameText;
    EditText userPostCodeText;

    String userName;
    String firstName;
    String lastName;
    String postCode;

    boolean actualitzat_correctament = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        View newView = updateView(view);

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        final Button button = newView.findViewById(R.id.save_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeProfile();
                if(actualitzat_correctament) {
                    Intent intent;
                    intent = new Intent(getActivity(), ProfileActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.error_edit_profile), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return newView;
    }

    public View updateView(View v) {

        u = CurrentSession.getInstance().getCurrentUser();

        userName = u.getUsername();
        firstName = u.getFirstName();
        lastName = u.getLastName();
        postCode = u.getPostalCode();

        userNameText = (EditText) v.findViewById(R.id.userName_edit);
        firstNameText = (EditText) v.findViewById(R.id.firstName_edit);
        lastNameText = (EditText) v.findViewById(R.id.lastName_edit);
        userPostCodeText = (EditText) v.findViewById(R.id.postCode_edit);


        userNameText.setText(userName, TextView.BufferType.EDITABLE);
        firstNameText.setText(firstName, TextView.BufferType.EDITABLE);
        lastNameText.setText(lastName, TextView.BufferType.EDITABLE);
        userPostCodeText.setText(postCode, TextView.BufferType.EDITABLE);

        return v;
    }

    public void changeProfile() {

        userName = String.valueOf(userNameText.getText());
        firstName = String.valueOf(firstNameText.getText());
        lastName = String.valueOf(lastNameText.getText());
        postCode = String.valueOf(userPostCodeText.getText());

        u = CurrentSession.getInstance().getCurrentUser();

        u.setUsername(userName);
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setPostalCode(postCode);

        CurrentSession.getInstance().setCurrentUser(u);

        //updateUserServer(u);

    }

    /*private void updateUserServer(User u) {
        new EditProfileFragment.updateUser().execute(u);
    }


    private class updateUser extends AsyncTask<User,String,String> {

        @Override
        protected String doInBackground(User... users) {
            try {
                actualitzat_correctament = controller.updateUser(users[0]);
            } catch (ParamsException e) {
                e.printStackTrace();
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }*/
}
