package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.Shape;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Challenge;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by guillemcastro on 26/11/2017.
 */

public class ChallengesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private View view;
    private WeakReference<RecyclerViewOnClickListener> listener;

    public ChallengesViewHolder(View itemView, RecyclerViewOnClickListener listener) {
        super(itemView);
        view = itemView;
        this.listener = new WeakReference<>(listener);
    }

    public void bindChallenge(Challenge challenge) {
        User currentUser = CurrentSession.getInstance().getCurrentUser();
        User opponent = null;
        int currentUserDistance;
        if (challenge.getCreator().getId().equals(currentUser.getId())) {
            opponent = challenge.getChallenged();
            currentUserDistance = challenge.getCreatorDistance();
        }
        else {
            opponent = challenge.getCreator();
            currentUserDistance = challenge.getChallengedDistance();
        }

        float progressF = (float)((float)currentUserDistance/(float)challenge.getDistance());
        int progress = (int)(progressF * 100.0f);
        Log.d("VIEW HOLDER", String.valueOf(progressF));
        Log.d("VIEW HOLDER", String.valueOf(progress));
        TextView progressView = view.findViewById(R.id.progress);
        TextView opponentView = view.findViewById(R.id.opponent);
        TextView expirationView = view.findViewById(R.id.expiration);
        String progressText = String.format(Locale.forLanguageTag("es"), "%d %s", progress, "%");
        progressView.setText(progressText);
        String opponentText = String.format("Opponent: %s %s", opponent.getFirstName(), opponent.getLastName());
        opponentView.setText(opponentText);
        expirationView.setText(getExpirationText(challenge.getDeadline()));
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.get().onMeetingClicked(getAdapterPosition());
    }

    private String getExpirationText(String deadline) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date dateTime = null;
        try {
            dateTime = inputFormat.parse(deadline);
        } catch (ParseException e) {
            dateTime = new Date(deadline);
        }
        final long millis = dateTime.getTime() - System.currentTimeMillis();
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        long hours = TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(days);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.DAYS.toMinutes(days) - TimeUnit.HOURS.toMinutes(hours);
        String expirationText;
        if (days > 0) {
            expirationText = String.format(Locale.forLanguageTag("es"), "Expires in %d days %d hours %d minutes", days, hours, minutes);
        }
        else {
            expirationText = String.format(Locale.forLanguageTag("es"), "Expires in %d hours %d minutes", hours, minutes);
        }
        return expirationText;
    }
}
