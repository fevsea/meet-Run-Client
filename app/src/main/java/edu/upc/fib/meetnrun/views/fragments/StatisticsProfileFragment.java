package edu.upc.fib.meetnrun.views.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Statistics;
import edu.upc.fib.meetnrun.models.User;


public class StatisticsProfileFragment extends Fragment {

    private String title;
    private int page;
    TextView level;
    TextView username;
    TextView meetings;
    TextView totalKm;
    TextView steps;
    TextView totalTime;
    TextView calories;
    TextView rhythm;
    TextView avgSpeed;
    TextView maxSpeed;
    TextView minSpeed;
    TextView maxLength;
    TextView minLength;
    TextView maxTime;
    TextView minTime;
    ProgressBar percentMeetings;
    ProgressBar percentKm;
    ProgressBar percentLevel;
    private FragmentActivity context;
    private int userLevel;
    private IUserAdapter iUserAdapter;
    Statistics s;
    int userId;
    User u;
    View view;

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
        //TODO: Hallar nivel usuario al que se le mira las estadísticas. Abajo es fake
       /* Bundle bundle = getActivity().getIntent().getExtras();
        userId=bundle.getInt("userId");*/
    }

    public void calcLevel (int meetings, float km){
        meetings--;
        for (int i=1; i<userLevel; i++){
            meetings -= 10*(i-1);
        }
        float  resMeetings;
        if (userLevel>0) resMeetings = (float) (meetings/(10*userLevel));
        else resMeetings = (float) meetings++;
        if (resMeetings>1.0) resMeetings=(float) 1.0;
        float resUser = (float) (km/(10+2.5*(userLevel*userLevel)));
        if (resUser>1.0) resUser=(float) 1.0;
        float res= (float) (0.5*resMeetings+0.5*resUser);
        while (res>=1.0) {
            meetings-=10*(userLevel);
            userLevel++;
            resMeetings= (float) (meetings/(10*userLevel));
            if (resMeetings>1.0) resMeetings=(float) 1.0;
            resUser = (float) (km /(10+2.5*(userLevel*userLevel)));
            if (resUser>1.0) resUser=(float) 1.0;
            res= (float) (0.5*resMeetings+0.5*resUser);
        }
        //TODO: Mirar que el nivel que se quiere poner no sea menor que el que ya tiene (data race)
        percentMeetings.setProgress ((int) (100*resMeetings));
        percentKm.setProgress((int) (100*resUser));
        percentLevel.setProgress((int) (100*res));
        level.setText (String.valueOf(userLevel));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistics_profile, container, false);
        context=this.getActivity();
        username  = view.findViewById (R.id.username);
        level     = view.findViewById (R.id.level);
        meetings  = view.findViewById (R.id.nMeetings);
        steps     = view.findViewById (R.id.nSteps);
        totalKm   = view.findViewById (R.id.totalKm);
        totalTime = view.findViewById (R.id.totalTime);
        calories  = view.findViewById (R.id.calories);
        rhythm    = view.findViewById (R.id.rhythm);
        avgSpeed  = view.findViewById (R.id.avgSpeed);
        maxSpeed  = view.findViewById (R.id.maxSpeed);
        minSpeed  = view.findViewById (R.id.minSpeed);
        maxTime   = view.findViewById (R.id.maxTime);
        minTime   = view.findViewById (R.id.minTime);
        maxLength = view.findViewById (R.id.maxLength);
        minLength = view.findViewById (R.id.minLength);
        percentMeetings = view.findViewById (R.id.percentMeetings);
        percentKm = view.findViewById (R.id.percentKm);
        percentLevel=view.findViewById(R.id.percentLevel);
        getStats();
        return view;
    }

    private void getStats(){
        new userStats().execute();
    }
    private class userStats extends AsyncTask<String,String,String> {
        private void setValues(){
            username.setText (u.getUsername());
            level.setText (String.valueOf(u.getLevel()));
            meetings.setText(String.valueOf(s.getNumberMeetings()));
            steps.setText(String.valueOf(s.getTotalSteps()));
            totalKm.setText(String.valueOf(s.getTotalKm()));
            totalTime.setText(s.getTotalTimeInString());
            calories.setText(String.valueOf( s.getTotalCalories()));
            rhythm.setText(s.getRhythmInString());
            avgSpeed.setText(s.getSpeedInString(s.getAvgSpeed()));
            maxSpeed.setText(s.getSpeedInString(s.getMaxSpeed()));
            minSpeed.setText(s.getSpeedInString(s.getMinSpeed()));
            maxTime.setText(s.getMaxTimeInString());
            maxLength.setText(String.valueOf(s.getMaxLength())+" km");
            minLength.setText(String.valueOf(s.getMinLength())+" km");
            minTime.setText(s.getMinTimeInString());
        }

        @Override
        protected String doInBackground(String... strings){
            try {
                //TODO: Que tot no sigui de current user
                u = CurrentSession.getInstance().getCurrentUser();
                s = iUserAdapter.getUserStatisticsByID(u.getId());

                //TODO: Hacerlo bien, sin hardcoded
                setValues();
                calcLevel(s.getNumberMeetings(), s.getTotalKm());
                        //u=iUserAdapter.getUser(userId);
                    } catch (AutorizationException e) {
                        e.printStackTrace();
                    }/* catch (NotFoundException e) {
                        e.printStackTrace();
                    }*/
            return null;
        }
            }
}