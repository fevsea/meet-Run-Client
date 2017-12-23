package edu.upc.fib.meetnrun.views.fragments;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;


public class EditProfileFragment extends BaseFragment {

    private User u;
    private View view;
    private final IUserAdapter controller = CurrentSession.getInstance().getUserAdapter();
    private EditText userNameText;
    private EditText firstNameText;
    private EditText lastNameText;
    private EditText userPostCodeText;

    private String userName;
    private String firstName;
    private String lastName;
    private String postCode;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        View newView = updateView(view);

        FloatingActionButton fab =
                getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        final Button button = newView.findViewById(R.id.save_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeProfile();
            }
        });

        return newView;
    }

    private View updateView(View v) {

        u = CurrentSession.getInstance().getCurrentUser();

        userName = u.getUsername();
        firstName = u.getFirstName();
        lastName = u.getLastName();
        postCode = u.getPostalCode();

        userNameText = v.findViewById(R.id.userName_edit);
        firstNameText = v.findViewById(R.id.firstName_edit);
        lastNameText = v.findViewById(R.id.lastName_edit);
        userPostCodeText = v.findViewById(R.id.postCode_edit);


        userNameText.setText(userName, TextView.BufferType.EDITABLE);
        firstNameText.setText(firstName, TextView.BufferType.EDITABLE);
        lastNameText.setText(lastName, TextView.BufferType.EDITABLE);
        userPostCodeText.setText(postCode, TextView.BufferType.EDITABLE);

        return v;
    }

    private void changeProfile() {

        userName = String.valueOf(userNameText.getText());
        firstName = String.valueOf(firstNameText.getText());
        lastName = String.valueOf(lastNameText.getText());
        postCode = String.valueOf(userPostCodeText.getText());

        User newUser = CurrentSession.getInstance().getCurrentUser();

        newUser.setUsername(userName);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setPostalCode(postCode);

        updateUserServer();

    }

    private void changeToNewUserProfile() {
        getActivity().finish();
    }

    private void updateUserServer() {
        new updateUser().execute(u);
    }


    private class updateUser extends AsyncTask<User, String, Boolean> {

        Exception exception = null;
        Boolean actualitzat_correctament;

        @Override
        protected Boolean doInBackground(User... params) {

            try {
                try {
                    actualitzat_correctament = controller.updateUser(params[0]);
                } catch (AuthorizationException e) {
                    e.printStackTrace();
                }
            } catch (NotFoundException e) {
                exception = e;
            } catch (ParamsException e) {
                e.printStackTrace();
            }
            return actualitzat_correctament;
        }

        @Override
        protected void onPostExecute(Boolean b) {

            if(b) {
                CurrentSession.getInstance().setCurrentUser(u);
                changeToNewUserProfile();
                }
                else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.error_edit_profile), Toast.LENGTH_SHORT).show();
                }
            super.onPostExecute(b);
        }
    }

    public int getTitle() {
        return R.string.title_edit_profile;
    }
}
