package edu.upc.fib.meetnrun.views.fragments;

import android.app.ActionBar;
import android.content.Intent;
import android.location.Geocoder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.*;
import com.google.android.gms.location.places.Places;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.EditProfileActivity;

import com.google.android.gms.maps.*;


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

        final Button button2 = view.findViewById(R.id.changePass_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                //intent = new Intent(getActivity(), ChangePasswordActivity.class);
                //startActivity(intent);
            }
        });

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
        fab.setImageResource(android.R.drawable.ic_menu_edit);
        fab.setVisibility(View.GONE);


        return view;
    }

    /*public void getCityFromPostCode() {
        Geocoder geocoder = new google.maps.Geocoder();
        geocoder.geocode({ 'address': '110021'}, function(results, status) {
            console.log(results);
        });
    }*/

    /*protected void getCityFromPostCode(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }*/

}
