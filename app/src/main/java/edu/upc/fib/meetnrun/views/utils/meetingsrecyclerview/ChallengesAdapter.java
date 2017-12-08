package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Challenge;

public class ChallengesAdapter extends RecyclerView.Adapter<ChallengesViewHolder>  {

    private List<Challenge> challenges;
    protected RecyclerViewOnClickListener listener;

    public ChallengesAdapter(List<Challenge> challenges, RecyclerViewOnClickListener listener) {
        this.challenges = challenges;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @Override
    public ChallengesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.challenge_item, parent, false);
        return new ChallengesViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ChallengesViewHolder holder, int position) {
        Challenge challenge = challenges.get(position);
        holder.bindChallenge(challenge);
    }

    @Override
    public int getItemCount() {
        return challenges.size();
    }

    public void updateChallengeList(List<Challenge> challenges) {
        this.challenges = challenges;
        Collections.sort(this.challenges, new ChallengesComparator());
        notifyDataSetChanged();
    }

    public Challenge getChallengeAt(int position) {
        return challenges.get(position);
    }

    private class ChallengesComparator implements Comparator<Challenge> {

        @Override
        public int compare(Challenge o1, Challenge o2) {
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.forLanguageTag("es"));
            Date o1Date;
            Date o2Date;
            try {
                o1Date = inputFormat.parse(o1.getDeadline());
                o2Date = inputFormat.parse(o2.getDeadline());
            }
            catch (ParseException e) {
                o1Date = new Date(o1.getDeadline());
                o2Date = new Date(o2.getDeadline());
            }
            long currentdateMillis = new Date().getTime();
            long o1Millis = o1Date.getTime();
            long o2Millis = o2Date.getTime();
            if ((o1Millis > currentdateMillis)  && (o2Millis > currentdateMillis)) {
                return o1Date.compareTo(o2Date);
            }
            else if ((o1Millis < currentdateMillis)  && (o2Millis < currentdateMillis)) {
                return o2Date.compareTo(o1Date);
            }
            else if (o1Millis < currentdateMillis) {
                return 1;
            }
            else if (o2Millis < currentdateMillis) {
                return -1;
            }
            return 0;
        }

    }
}
