package edu.upc.fib.gps.meetnrun.views.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.upc.fib.gps.meetnrun.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProfileActivityFragment extends Fragment {

    public ProfileActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}
