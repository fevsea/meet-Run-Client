package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.EditProfileActivity;


public class EditProfileFragment extends Fragment {

    User u;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);


       /* u = CurrentSession.getInstance().getCurrentUser();


        String userName = u.getUsername();
        String firstName = u.getFirstName();
        String lastName = u.getLastName();
        String postCode = u.getPostalCode();

        EditText userNameText = (EditText) view.findViewById(R.id.userName_edit);
        EditText firstNameText = (EditText) view.findViewById(R.id.firstName_edit);
        EditText lastNameText = (EditText) view.findViewById(R.id.lastName_edit);
        EditText userPostCodeText = (EditText) view.findViewById(R.id.postCode_edit);


        userNameText.setText(userName, TextView.BufferType.EDITABLE);
        firstNameText.setText(firstName, TextView.BufferType.EDITABLE);
        lastNameText.setText(lastName, TextView.BufferType.EDITABLE);
        userPostCodeText.setText(postCode, TextView.BufferType.EDITABLE); */


        final Button button = view.findViewById(R.id.save_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeProfile();
                Intent intent;
                intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void changeProfile() {

    }
}
