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
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Statistics;
import edu.upc.fib.meetnrun.models.User;


public class StatisticsProfileFragment extends BaseFragment {

    private String title;
    private int page;
    TextView level;
    TextView error;
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
    private IUserAdapter iUserAdapter;
    Statistics s;
    User u;
    View view;
    String user;
    String name, userlevel, usermeetings, usersteps, userkm, usertime, usercalories, userrhythm, userspeed, usermaxspeed, userminspeed, usermaxtime, usermintime, usermaxlength, userminlength;

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
       /* Bundle bundle = getActivity().getIntent().getExtras();
        userId=bundle.getInt("userId");*/
    }

    public int getActualLevel (int meetings, float km, int level){
        float  resMeetings;
        float resUser;
        if (level==0){
            resMeetings=(float) meetings;
        }
        else {
            meetings--;
            for (int i = 1; i < level; i++) {
                meetings -= 10 * (i - 1);
            }
            resMeetings = (float) (meetings / (10 * level));
        }
        if (resMeetings>1.0) resMeetings=(float) 1.0;
        resUser = (float) (km/(2+2.5*(level*level)));
        if (resUser>1.0) resUser=(float) 1.0;
        float res= (float) (0.5*resMeetings+0.5*resUser);
        while (res>=1.0) {
            meetings-=10*(level);
            level++;
            resMeetings= (float) (meetings/(10*level));
            if (resMeetings>1.0) resMeetings=(float) 1.0;
            resUser = (float) (km /(10+2.5*(level*level)));
            if (resUser>1.0) resUser=(float) 1.0;
            res= (float) (0.5*resMeetings+0.5*resUser);
        }
        //TODO: Mirar que el nivel que se quiere poner no sea menor que el que ya tiene (data race)
        percentMeetings.setProgress ((int) (100*resMeetings));
        percentKm.setProgress((int) (100*resUser));
        percentLevel.setProgress((int) (100*res));
        return level;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistics_profile, container, false);
        context=this.getActivity();
        error     = view.findViewById (R.id.error);
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
        u = CurrentSession.getInstance().getCurrentUser();

        getStats();

        return view;
    }

    private void getStats(){
        new userStats().execute();
    }
    private class userStats extends AsyncTask<String,String,String> {
        private void setValues(){
            userlevel=String.valueOf(u.getLevel());
            usercalories=String.valueOf(s.getTotalCalories());
            userrhythm=s.getRhythmInString();
            usersteps=String.valueOf(s.getTotalSteps());
            userspeed=s.getSpeedInString(s.getAvgSpeed());
            usermaxspeed=s.getSpeedInString(s.getMaxSpeed());
            userminspeed=s.getSpeedInString(s.getMinSpeed());
            usermaxtime=s.getTimeInString(s.getMaxTime());
            usermintime=s.getTimeInString(s.getMinTime());
            usermaxlength=String.valueOf(s.getMaxLength())+" km";
            userminlength=String.valueOf(s.getMinLength())+" km";
            usertime=s.getTimeInString(s.getTotalTimeMillis());
            int l=getActualLevel(s.getNumberMeetings(), s.getTotalKm(), (int) u.getLevel());
            userlevel=String.valueOf(l);
            if (u.getLevel()<l) u.setLevel(l);
        }

        @Override
        protected String doInBackground(String... strings){
            try {
                //TODO: Que tot no sigui de current user
                int id=u.getId();
                iUserAdapter=CurrentSession.getInstance().getUserAdapter();
                s = iUserAdapter.getUserStatisticsByID(id);

                //TODO: Hacerlo bien, sin hardcoded
                setValues();

                        //u=iUserAdapter.getUser(userId);
                    } catch (AuthorizationException e) {
                        e.printStackTrace();
                    }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            error.setText(" ");
            level.setText(userlevel);
            meetings.setText(usermeetings);
            steps.setText(usersteps);
            totalKm.setText(userkm);
            totalTime.setText(usertime);
            calories.setText(usercalories);
            rhythm.setText(userrhythm);
            avgSpeed.setText(userspeed);
            maxSpeed.setText(usermaxspeed);
            minSpeed.setText(userminspeed);
            maxTime.setText(usermaxtime);
            maxLength.setText(usermaxlength);
            minLength.setText(userminlength);
            minTime.setText(usermintime);

        }
            }
}
