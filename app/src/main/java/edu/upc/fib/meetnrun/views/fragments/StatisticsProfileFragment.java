package edu.upc.fib.meetnrun.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Statistics;
import edu.upc.fib.meetnrun.models.User;

public class StatisticsProfileFragment extends Fragment {

    private String title;
    private int page;
    private Statistics stats;
    TextView meetings;
    TextView totalKm;
    TextView steps;
    TextView calories;
    TextView rhythm;
    TextView avgSpeed;
    TextView maxSpeed;
    TextView minSpeed;
    TextView maxLength;
    TextView minLength;
    TextView maxTime;
    TextView minTime;
    private FragmentActivity context;

    // newInstance constructor for creating fragment with arguments
    public static StatisticsProfileFragment newInstance(int page, String title) {
        StatisticsProfileFragment fragmentFirst = new StatisticsProfileFragment();
        Bundle args = new Bundle();
        args.putInt("2", page);
        args.putString("Statistics", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("2", 2);
        title = getArguments().getString("Statistics");


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics_profile, container, false);
        context=this.getActivity();

        meetings = (TextView) view.findViewById (R.id.nMeetings);
        steps = (TextView) view.findViewById (R.id.nSteps);
        totalKm = (TextView) view.findViewById(R.id.totalKm);
        calories = (TextView) view.findViewById (R.id.calories);
        rhythm = (TextView) view.findViewById (R.id.rhythm);
        avgSpeed = (TextView) view.findViewById (R.id.avgSpeed);
        maxSpeed = (TextView) view.findViewById (R.id.maxSpeed);
        minSpeed = (TextView) view.findViewById (R.id.minSpeed);
        maxTime = (TextView) view.findViewById (R.id.maxTime);
        minTime = (TextView) view.findViewById (R.id.minTime);
        maxLength = (TextView) view.findViewById (R.id.maxLength);
        minLength = (TextView) view.findViewById (R.id.minLength);

        return view;
    }
}