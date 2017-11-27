package edu.upc.fib.meetnrun.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.User;

public class StatisticsProfileFragment extends Fragment {

    private String title;
    private int page;


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
        return view;
    }
}