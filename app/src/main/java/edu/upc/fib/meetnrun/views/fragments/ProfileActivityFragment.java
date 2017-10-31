package edu.upc.fib.meetnrun.views.fragments;

import android.app.ActionBar;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.EditProfileActivity;


public class ProfileActivityFragment extends Fragment {

    User u;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        u = CurrentSession.getInstance().getCurrentUser();


        String userName = u.getUsername();
        String name = u.getFirstName() + ' ' + u.getLastName();
        String postCode = u.getPostalCode();


        TextView userNameTextView = (TextView) view.findViewById(R.id.userName);
        TextView nameTextView = (TextView) view.findViewById(R.id.completeName);
        TextView userPostCodeTextView = (TextView) view.findViewById(R.id.userPostCode);


        userNameTextView.setText(userName);
        nameTextView.setText(name);
        userPostCodeTextView.setText(postCode);

        final Button button = view.findViewById(R.id.editProfile_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

}
