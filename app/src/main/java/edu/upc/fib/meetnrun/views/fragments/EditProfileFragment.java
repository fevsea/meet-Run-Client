package edu.upc.fib.meetnrun.views.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.EditText;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;


public class EditProfileFragment extends Fragment {

    User u;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        u = CurrentSession.getInstance().getCurrentUser();


        String userName = u.getUsername();
        String firstName = u.getFirstName();
        String lastName = u.getLastName();
        String postCode = u.getPostalCode();

        EditText userNameText = (EditText) view.findViewById(R.id.userName_edit);
        EditText firstNameText = (EditText) view.findViewById(R.id.firstName_edit);
        EditText lastNameText = (EditText) view.findViewById(R.id.lastName_edit);
        EditText userPostCodeText = (EditText) view.findViewById(R.id.postCode_edit);


        userNameText.setText(userName);
        firstNameText.setText(firstName);
        lastNameText.setText(lastName);
        userPostCodeText.setText(postCode);

        return view;
    }
}
