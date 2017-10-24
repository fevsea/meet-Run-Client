package edu.upc.fib.meetnrun.views.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.persistence.UserPersistenceController;


public class ProfileActivityFragment extends Fragment {

    /*public ProfileActivityFragment() {
    }*/

    private View view;
    //CurrentSession cs;
    //User u;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        //User u = cs.getCurrentUser();

        TextView userNameTextView = view.findViewById(R.id.userName);
        TextView nameTextView = view.findViewById(R.id.name);
        TextView userEmailTextView = view.findViewById(R.id.userEmail);
        TextView userPostCodeTextView = view.findViewById(R.id.userPostCode);


        String userName = "MeetnRun"; //u.getUserName();
        String name = "Meet and Run"; //u.getFirstName() + ' ' + u.getLastName();
        String email = "meetnrun@meetnrun.com"; //u.getEmail();
        String postCode = "080XX"; //u.getPostCode();

        userNameTextView.setText(userName);
        nameTextView.setText(name);
        userEmailTextView.setText(email);
        userPostCodeTextView.setText(postCode);


        return view;
    }
}
