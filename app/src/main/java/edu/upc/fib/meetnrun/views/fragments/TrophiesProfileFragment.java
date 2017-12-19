package edu.upc.fib.meetnrun.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.upc.fib.meetnrun.R;


public class TrophiesProfileFragment extends Fragment {

    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static TrophiesProfileFragment newInstance(int page, String title) {
        TrophiesProfileFragment fragmentFirst = new TrophiesProfileFragment();
        Bundle args = new Bundle();
        args.putInt("3", page);
        args.putString("trophies", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("3", 3);
        title = getArguments().getString("trophies");
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trophies_profile, container, false);
        return view;
    }
}
