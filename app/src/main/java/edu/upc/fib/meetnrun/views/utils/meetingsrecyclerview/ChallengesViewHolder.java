package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Challenge;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.utils.UtilsGlobal;

import static edu.upc.fib.meetnrun.utils.UtilsViews.getExpirationText;

public class ChallengesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    final protected View view;
    final protected WeakReference<RecyclerViewOnClickListener> listener;
    protected String expirationTextResourceDays;
    protected String expirationTextResourceNoDays;
    protected String expirationPastTextResourceDays;
    protected String expirationPastTextResourceNoDays;

    public ChallengesViewHolder(View itemView, RecyclerViewOnClickListener listener) {
        super(itemView);
        view = itemView;
        this.listener = new WeakReference<>(listener);
        expirationTextResourceDays = view.getResources().getString(R.string.ends_in_days_hours_minutes);
        expirationTextResourceNoDays = view.getResources().getString(R.string.ends_in_hours_minutes);
        expirationPastTextResourceDays = view.getResources().getString(R.string.ended_in_days_hours_minutes);
        expirationPastTextResourceNoDays = view.getResources().getString(R.string.ended_in_hours_minutes);
    }

    protected void bindChallenge(Challenge challenge) {
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

        try {
            expirationView.setText(getExpirationText(challenge.getDeadline(), expirationTextResourceDays, expirationTextResourceNoDays, expirationPastTextResourceDays, expirationPastTextResourceNoDays));
        }
        catch (ParseException ex) {
            expirationView.setText("");
            Log.e("PARSEEXCEPTION", ex.getMessage());
        }
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.get().onItemClicked(getAdapterPosition());
    }

}
