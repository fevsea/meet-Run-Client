package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
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

public class ChallengesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    final private View view;
    final private WeakReference<RecyclerViewOnClickListener> listener;
    private String expirationTextResourceDays;
    private String expirationTextResourceNoDays;

    public ChallengesViewHolder(View itemView, RecyclerViewOnClickListener listener) {
        super(itemView);
        view = itemView;
        this.listener = new WeakReference<>(listener);
        expirationTextResourceDays = view.getResources().getString(R.string.ends_in_days_hours_minutes);
        expirationTextResourceNoDays = view.getResources().getString(R.string.ends_in_hours_minutes);
    }

    public void bindChallenge(Challenge challenge) {
        User currentUser = CurrentSession.getInstance().getCurrentUser();
        User opponent;
        float currentUserDistance;
        float opponentDistance;
        if (challenge.getCreator().getId().equals(currentUser.getId())) {
            currentUserDistance = challenge.getCreatorDistance();
            opponentDistance = challenge.getChallengedDistance();
            opponent = challenge.getChallenged();
        }
        else {
            currentUserDistance = challenge.getChallengedDistance();
            opponentDistance = challenge.getCreatorDistance();
            opponent = challenge.getCreator();
        }
        TextView totalView = view.findViewById(R.id.total);
        ProgressBar opponentBar = view.findViewById(R.id.opponent_progress);
        ProgressBar youBar = view.findViewById(R.id.my_progress);
        TextView expirationView = view.findViewById(R.id.expires_in);
        String totalText = String.format(Locale.forLanguageTag("es"), "%.0f km", challenge.getDistance());
        totalView.setText(totalText);
        TextView opponentName = view.findViewById(R.id.opponent);
        TextView youName = view.findViewById(R.id.you);

        youName.setText(R.string.you);
        opponentName.setText(opponent.getUsername());

        opponentBar.setMax((int)challenge.getDistance());
        opponentBar.setProgress((int)opponentDistance);

        youBar.setMax((int)challenge.getDistance());
        youBar.setProgress((int)currentUserDistance);

        expirationView.setText(getExpirationText(challenge.getDeadline()));
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.get().onItemClicked(getAdapterPosition());
    }

    private String getExpirationText(String deadline) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.forLanguageTag("es"));
        Date dateTime;
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
            expirationText = String.format(Locale.forLanguageTag("es"), expirationTextResourceDays, days, hours, minutes);
        }
        else {
            expirationText = String.format(Locale.forLanguageTag("es"), expirationTextResourceNoDays, hours, minutes);
        }
        return expirationText;
    }
}
