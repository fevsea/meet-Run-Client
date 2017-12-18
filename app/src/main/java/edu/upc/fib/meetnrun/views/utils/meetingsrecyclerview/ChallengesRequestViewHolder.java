package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Locale;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Challenge;

import static edu.upc.fib.meetnrun.utils.UtilsViews.getExpirationText;

/**
 * Created by guillemcastro on 08/12/2017.
 */

public class ChallengesRequestViewHolder extends ChallengesViewHolder {

    public ChallengesRequestViewHolder(View itemView, TwoButtonsRecyclerViewOnClickListener listener) {
        super(itemView, listener);
    }

    @Override
    protected void bindChallenge(Challenge challenge) {
        TextView totalView = view.findViewById(R.id.total);
        TextView opponentView = view.findViewById(R.id.opponent);
        ImageButton accept = view.findViewById(R.id.accept);
        ImageButton reject = view.findViewById(R.id.reject);
        TextView expiresIn = view.findViewById(R.id.expires_in);

        String totalText = String.format(Locale.forLanguageTag("es"), "%.0f km", challenge.getDistance());
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TwoButtonsRecyclerViewOnClickListener)listener.get()).onButtonAcceptClicked(getAdapterPosition());
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TwoButtonsRecyclerViewOnClickListener)listener.get()).onButtonRejectClicked(getAdapterPosition());
            }
        });

        totalView.setText(totalText);
        opponentView.setText(challenge.getCreator().getUsername());
        try {
            expiresIn.setText(getExpirationText(challenge.getDeadline(), expirationTextResourceDays, expirationTextResourceNoDays, expirationPastTextResourceDays, expirationPastTextResourceNoDays));
        }
        catch (ParseException e) {
            expiresIn.setText("");
            Log.e("PARSEEXCEPTION", e.getMessage());
        }

        view.setOnClickListener(this);
    }



}
