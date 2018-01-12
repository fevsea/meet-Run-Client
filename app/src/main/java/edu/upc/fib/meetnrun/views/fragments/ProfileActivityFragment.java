package edu.upc.fib.meetnrun.views.fragments;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.asynctasks.GetCity;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class ProfileActivityFragment extends BaseFragment {

    private User u;
    private View view;
    private TextView userPostCodeTextView;

    private String title;
    private int page;


    // newInstance constructor for creating fragment with arguments
    public static ProfileActivityFragment newInstance(int page, String title) {
        ProfileActivityFragment fragmentFirst = new ProfileActivityFragment();
        Bundle args = new Bundle();
        args.putInt("0", page);
        args.putString("Info", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("0", 0);
        title = getArguments().getString("Info");
        setHasOptionsMenu(true);
    }

    private GradientDrawable getColoredCircularShape(char letter) {

        int[] colors = view.getResources().getIntArray(R.array.colors);
        GradientDrawable circularShape = (GradientDrawable) ContextCompat.getDrawable(view.getContext(),R.drawable.user_profile_circular_text_view);
        int position = letter%colors.length;
        circularShape.setColor(colors[position]);
        return circularShape;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        u = CurrentSession.getInstance().getCurrentUser();
        String userName = u.getUsername();
        String name = u.getFirstName() + ' ' + u.getLastName();
        String postCode = u.getPostalCode();


        TextView userNameTextView = view.findViewById(R.id.userName);
        TextView nameTextView = view.findViewById(R.id.completeName);
        userPostCodeTextView = view.findViewById(R.id.userPostCode);

        TextView userPhoto = view.findViewById(R.id.userProfileImage);
        char letter = u.getUsername().charAt(0);
        String firstLetter = String.valueOf(letter);
        userPhoto.setBackground(getColoredCircularShape(letter));
        userPhoto.setText(firstLetter);


        userNameTextView.setText(userName);
        nameTextView.setText(name);
        userPostCodeTextView.setText(postCode);
        getCityFromPostcode(postCode);


        Button button = view.findViewById(R.id.editProfile_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BaseActivity.startWithFragment(getActivity(), new EditProfileFragment());
            }
        });

        Button button2 = view.findViewById(R.id.changePass_button);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BaseActivity.startWithFragment(getActivity(), new ChangePasswordFragment());
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.empty_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                getActivity().finish();
                break;
        }
        return false;
    }


    private void getCityFromPostcode(String p) {
        callGetCity(p);
    }

    private void callGetCity(String p) {
        new GetCity() {
            @Override
            public void onResponseReceived(String s) {
                userPostCodeTextView.setText(s);
            }
        }.execute(p);
    }

}
