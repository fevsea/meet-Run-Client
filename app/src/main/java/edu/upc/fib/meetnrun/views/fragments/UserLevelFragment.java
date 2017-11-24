package edu.upc.fib.meetnrun.views.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by Javier on 23/11/2017.
 */

public class UserLevelFragment extends Fragment {
    private View view;
    private User u;
    ProgressBar userLevel;
    ProgressBar km;
    ProgressBar speed;
    ProgressBar meetings;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_level, container, false);
        this.view = view;
        u = CurrentSession.getInstance().getCurrentUser();
        int level= u.getLevel();
        userLevel= (ProgressBar) view.findViewById(R.id.totalProgressBar);
        km=        (ProgressBar) view.findViewById(R.id.kmProgressBar);
        speed=     (ProgressBar) view.findViewById(R.id.speedProgressBar);
        meetings=  (ProgressBar) view.findViewById(R.id.meetingsProgressBar);
        return view;
    }


}